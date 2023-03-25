import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;

public class DownloaderThread extends Thread {

    private String MULTICAST_ADRESS = "224.3.2.1";
    private int MULTICAST_PORT = 4321;

    private String url;

    private int id;

    private int SendPort;

    private int ReceivePort;

    private static HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();

    private static ArrayList<String> found = new ArrayList<String>();

    public DownloaderThread(int Port, int id) {

        this.SendPort = Port;
        this.id = id;
        this.ReceivePort = Port + 1;

        // System.out.println("Downloader Thread " + id + " created with port " + Port);

    }

    // Download the page and read its words
    public static void download(String url) {

        try {

            Document doc = Jsoup.connect(url).get();

            // Get page title
            String title = doc.title();

            // Read all words and associate them with the page, without duplicates
            Elements words = doc.select("body");

            for (Element word : words) {
                String[] wordsArray = word.text().split(" ");

                for (String wordArray : wordsArray) {
                    if (index.containsKey(wordArray)) {
                        index.get(wordArray).add(url);
                    } else {
                        HashSet<String> urls = new HashSet<String>();
                        urls.add(url);
                        index.put(wordArray, urls);
                    }
                }
            }

            // Read any existing links and read their words as well recursively
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String linkHref = link.attr("href");
                // String linkText = link.text();

                // Recursively read the link
                // download(linkHref);

                // Add links to the link array
                found.add(linkHref);
            }
        } catch (Exception e) {
            System.out.println("Exception in Downloader.download: " + e);
        }

    }

    public static void printIndex() {

        try {

            // Print all words and associated pages
            for (String key : index.keySet()) {

                System.out.println(key + ": " + index.get(key));
            }
        } catch (Exception e) {
            System.out.println("Exception in Downloader.printIndex: " + e);
        }

    }

    // Get a URL from the queue
    public void getURL() {

        try {

            System.out.println("Downloader Thread " + id + " waiting for command");

            // Get the ServerSocket from the port in queue

            // Receive a url from the queue in the server from the port in queue
            Socket socket = new Socket("localhost", ReceivePort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = reader.readLine();

            // System.out.println("Downloader Thread " + id + " got command " + command);

            if (command.startsWith("GET_URL")) {

                String url = command.substring(8);

                // Remove the url from the queue
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output);
                writer.println("REMOVE_URL " + url);
                writer.flush();

                this.url = url;

                System.out.println("Downloader Thread " + id + " got url " + url);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Exception in Downloader.getURL: " + e);
        }

    }

    // Adds a URL to the queue
    public void adicionaURL(String url) {

        try {

            // Adicionar à queue
            Socket socket = new Socket("localhost", SendPort);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output);

            /*
             * TODO
             * Adicionar todos os urls encontrados, não apenas um
             * Função Recebe um array de urls
             * Ciclo for a enviar todos os urls
             * Funciona pois a thread da Queue está sempre a correr e a receber os comandos
             */

            writer.println("ADD_URL " + url);
            writer.flush();
            socket.close();
        } catch (IOException e) {
            System.out.println("Exception in Downloader.adicionaURL: " + e);
        }

    }

    public String transformLineHashMap(HashMap<String, HashSet<String>> pack) {
        // percorrer o hashmap e transformar em String do tipo "palavra: url1, url2,
        // url3"
        String string = "";
        // go trough the hashmap

        for (String key : pack.keySet()) {
            string += key + " ";
            for (String url : pack.get(key)) {
                string += url + " ";
            }
            // separete keys/url with a new line
            string += "\n";
        }
        return string;
    }

    public void enviaIndex(String index) {

        try {

            MulticastSocket socket = new MulticastSocket();

            // Enviar o index para o IndexStorageBarrel
            while (true) {
                InetAddress group = InetAddress.getByName(MULTICAST_ADRESS);
                // transform hasmap to bytes to send

                byte[] buf = index.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);
                socket.send(packet);

                // Confirm that the index was sent
                System.out.println("Downloader Thread " + id + " sent index to IndexStorageBarrel");

                // Confirm that the index was received
                // DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                // socket.receive(response);
                // if (response.getData() != null) {
                // System.out.println("Index was received, exiting");
                // break;
                // }
                break;
            }

            // socket.close();
        } catch (IOException e) {
            System.out.println("Exception in Downloader.enviaIndex: " + e);
        }

    }

    // Thread running method
    public void run() {

        // System.out.println("Downloader Thread " + id + " running with port " +
        // SendPort + " and " + ReceivePort + "");

        while (true) {

            // Ir buscar o url à queue
            getURL();

            if (url != null) {

                System.out.println("Downloader Thread " + id + " downloading " + url);

                // Fazer o download do url
                download(url);

                // Convert the hashmap to a string
                String string = transformLineHashMap(index);

                // Print the index
                System.out.println("Downloader Thread " + id + " index: " + string);

                // Enviar o index para o IndexStorageBarrel - Multicast
                enviaIndex(string);

                // Se houver urls encontrados, adicionar à queue
                if (found.size() > 0) {
                    for (String url : found) {

                        // Adicionar à queue
                        System.out.println("Downloader Thread " + id + " adding url " + url);
                        adicionaURL(url);

                    }
                }

            } else {

                System.out.println("Downloader Thread " + id + " got no url");
            }

            this.url = null;
            found.clear();
            index.clear(); // Depois de confirmar que o index foi enviado para o IndexStorageBarrel

        }

    }

}
