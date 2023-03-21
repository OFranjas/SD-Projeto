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
    public static void download(String url) throws IOException {

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

    }

    public static void printIndex() throws IOException {

        // Print all words and associated pages
        for (String key : index.keySet()) {

            System.out.println(key + ": " + index.get(key));
        }

    }

    // Get a URL from the queue
    public void getURL() throws IOException {

        try {

            System.out.println("Downloader Thread " + id + " waiting for command");

            // Get the ServerSocket from the port in queue

            // Receive a url from the queue in the server from the port in queue
            Socket socket = new Socket("localhost", ReceivePort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = reader.readLine();

            System.out.println("Downloader Thread " + id + " got command " + command);

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
    public void adicionaURL(String url) throws IOException {

        // Adicionar à queue
        Socket socket = new Socket("localhost", SendPort);
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output);
        writer.println("ADD_URL " + url);
        writer.flush();
        socket.close();
    }

    public void enviaIndex() throws IOException {

        // Enviar o index para o IndexStorageBarrel

    }

    // Thread running method
    public void run() {

        // System.out.println("Downloader Thread " + id + " running with port " +
        // SendPort + " and " + ReceivePort + "");

        while (true) {

            // Ir buscar o url à queue
            try {
                getURL();
            } catch (IOException e) {
                System.out.println("Exception in Downloader.getURL: " + e);
            }

            if (url != null) {

                System.out.println("Downloader Thread " + id + " downloading " + url);

                // Fazer o download do url
                try {
                    download(url);
                } catch (IOException e) {
                    System.out.println("Exception in Downloader.download: " + e);
                }

                // Show index
                // try {
                // printIndex();
                // } catch (IOException e) {
                // System.out.println("Exception in Downloader.printIndex: " + e);
                // }

                // Enviar o index para o IndexStorageBarrel - Multicast
                try {
                    enviaIndex();
                } catch (IOException e) {
                    System.out.println("Exception in Downloader.enviaIndex: " + e);
                }

                // Se houver urls encontrados, adicionar à queue
                if (found.size() > 0) {
                    for (String url : found) {
                        // Adicionar à queue
                        try {

                            System.out.println("Downloader Thread " + id + " adding url " + url);
                            adicionaURL(url);
                        } catch (IOException e) {
                            System.out.println("Exception in Downloader.adicionaURL with url - " + url + ": " + e);
                        }
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
