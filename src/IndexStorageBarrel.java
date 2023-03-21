import java.util.HashMap;
import java.util.HashSet;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Trabalha em paralelo (para aumentar o desempenho)

// Recebe informação processada pelo Downloader usando Multicast

// É o servidor central (replicado) que armazena todos os dados da aplicação
// Recebe os elementos do índice (palavras e URLs) através de multicast envidado pelos Downloaders
// Protocolo de Multicast fiável para que todos os IndexStorageBarrels tenham informação idêntica (podendo haver omissões)
// saber qual quantos storage barrels existem e qual é este -> id tem de vir nas threads quando se criam no ciclo for

public class IndexStorageBarrel implements Runnable {

    private static int num_threads = 1;
    private Thread thread;
    private HashMap<String, HashSet<String>> index; // Tem o que ja tem no ficheiro de texto, receber o que o downloader
                                                    // tem e adicionar ao que ja tem
    private int id;
    private HashMap<String, HashSet<String>> downloader; // isto nunca pode existir, deve ser recebido por tcp/udp vindo
                                                         // do downloader

    public IndexStorageBarrel(int id) {
        index = new HashMap<String, HashSet<String>>();
        this.id = id;
    }

    public static int getNum_threads() {
        return num_threads;
    }

    public Thread getThread() {
        return thread;
    }

    public HashMap<String, HashSet<String>> getIndex() {
        return index;
    }

    public int getId() {
        return id;
    }

    public void CreateTxtFile() { // funcao para criar ficheiro de texto com o id do storage barrel

        // Create a file named "IndexStorageBarrelid.txt"

        try {
            File file = new File("IndexStorageBarrel" + id + ".txt"); // bate ???

            // check if file exist

            if (file.createNewFile()) {

                System.out.println("File is created!");

            } else {

                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }

    public void WriteTxtFile() { // funcao para escrever no ficheiro de texto
        // Read the file

        try {
            FileWriter myWriter = new FileWriter("IndexStorageBarrel" + id + ".txt");

            // go trough the index and write it to the file word url line by line

            for (String key : index.keySet()) {

                myWriter.write(key + " " + index.get(key) + "\n");
            }

            myWriter.close();

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public void CompareIndexWithDownloader() { // function to compare the index with downloader

        // go trough the index and compare it with the downloader

        for (String key : index.keySet()) {

            if (downloader.containsKey(key)) {

                // if the word is in the downloader, and the url is not in the index, add the
                // url to the index
                for (String url : downloader.get(key)) {
                    if (!index.get(key).contains(url)) {
                        index.get(key).add(url);
                    }
                }

            } else {

                // if the word is not in the downloader, add the word to the index

                index.put(key, downloader.get(key));
            }
        }
    }

    public void run() {

        // Recebe index de um Downloader
        CreateTxtFile();

        // Editar index com as novas cenas

        CompareIndexWithDownloader();

        // Guarda o index num ficheiro de texto

        WriteTxtFile();

        // Se o Search Module pedir, envia o index -> RMI

    }

    public void start() {

        for (int i = 0; i < num_threads; i++) {
            thread = new Thread(this);
            thread.start(); // send i to the thread for id of storage barrel
        }

    }

}
