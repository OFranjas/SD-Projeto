import java.util.HashMap;
import java.util.HashSet;
// Trabalha em paralelo (para aumentar o desempenho)

// Recebe informação processada pelo Downloader usando Multicast

// É o servidor central (replicado) que armazena todos os dados da aplicação
// Recebe os elementos do índice (palavras e URLs) através de multicast envidado pelos Downloaders
// Protocolo de Multicast fiável para que todos os IndexStorageBarrels tenham informação idêntica (podendo haver omissões)

public class IndexStorageBarrel implements Runnable {

    private HashMap<String, HashSet<String>> index;

    private static int num_threads = 1;

    private Thread thread;

    public IndexStorageBarrel() {
        index = new HashMap<String, HashSet<String>>();
    }

    public void run() {

        // Recebe index de um Downloader

        // Adiciona ao index o que não existia no index

        // Guarda o index num ficheiro de texto

        // Se o Search Module pedir, envia o index

    }

    public void start() {

        for (int i = 0; i < num_threads; i++) {
            thread = new Thread(this);
            thread.start();
        }

    }

}
