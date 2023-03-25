import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;

public class IndexStorageBarrelThread extends Thread {

    private String MULTICAST_ADRESS = "224.3.2.1";
    private int MULTICAST_PORT = 4321;

    private HashMap<String, HashSet<String>> index; // Tem o que ja tem no ficheiro de texto, receber o que o downloader
                                                    // tem e adicionar ao que ja tem
    private int id;
    private HashMap<String, HashSet<String>> downloader; // isto nunca pode existir, deve ser recebido por tcp/udp vindo
                                                         // do downloader

    public IndexStorageBarrelThread(int id) {

        this.id = id;

        this.index = new HashMap<String, HashSet<String>>();

        // System.out.println("IndexStorageBarrel Thread " + id + " created");

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
            System.out.println("IndexBarrel -> Error in CreateTxtFile");
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
            System.out.println("IndexBarrel -> Error in WriteTxtFile");
            e.printStackTrace();
        }
    }

    public void CompareIndexWithDownloader() { // function to compare the index with downloader

        // go trough the index and compare it with the downloader

        try {

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
        } catch (Exception e) {
            System.out.println("IndexBarrel -> Error in CompareIndexWithDownloader");
            System.out.println(e);
        }

    }

    public void messageToHashmap(String message) {

        // percorrer a string recebida linhh a linha e separar a 1ª palavra de cada
        // linha como key e as restantes como urls
        downloader = new HashMap<String, HashSet<String>>();
        String linhas[] = message.split("\n");

        for (int i = 0; i < linhas.length; i++) {
            String[] palavras = linhas[i].split(" ");
            for (int j = 1; j < palavras.length; j++) {
                if (j == 1) {
                    // System.out.println(palavras[0]);
                    downloader.put(palavras[0], new HashSet<String>());
                    downloader.get(palavras[0]).add(palavras[j]);
                } else {
                    downloader.get(palavras[0]).add(palavras[j]);
                }
            }
        }

    }

    public void printHashMap(HashMap<String, HashSet<String>> map) {

        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
        }

    }

    public void receiveMessage() {

        try {

            System.out.println("IndexStorageBarrel " + id + " is waiting for the index from Downloader");

            MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
            InetSocketAddress group = new InetSocketAddress(MULTICAST_ADRESS, MULTICAST_PORT);
            NetworkInterface netIf = NetworkInterface.getByName("bge0");

            socket.joinGroup(group, netIf);

            byte[] buffer = new byte[1024];

            // Wait for a message to be received, when it is received, the loop will break
            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength()); // receber a string com muita
                // merda
                // System.out.println(message);
                messageToHashmap(message);

                System.out.println("IndexStorageBarrel " + id + " received the following index from Downloader:");
                printHashMap(downloader);

                // Send response that the index was received
                String response = "Index received";
                byte[] buffer2 = response.getBytes();
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, packet.getAddress(),
                        packet.getPort());
                socket.send(packet2);

                if (message != null) {
                    break;
                }

            }
            socket.leaveGroup(group, netIf);
            socket.close();
        } catch (IOException e) {
            System.out.println("IndexBarrl -> Error in receiveMessage");
            e.printStackTrace();
        }

    }

    public void run() {

        // Create text file if it doesn't exist
        CreateTxtFile();

        while (true) {

            System.out.println("IndexStorageBarrel " + id + " is running");

            // Recebe o index do Downloader -> Multicast
            receiveMessage();

            // Editar index com as novas cenas
            CompareIndexWithDownloader();

            // Guarda o index num ficheiro de texto

            WriteTxtFile();

            // Se o Search Module pedir, envia o index -> RMI

        }

    }

}
