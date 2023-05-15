package main.java.com.example.SDProject.Downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;
import main.java.com.example.SDProject.Global.Global;

public class DownloaderThread extends Thread {

    private String MULTICAST_ADRESS = "224.3.2.1";
    private int MULTICAST_PORT = 4321;

    private int MAX_LINKS = 10;

    private String status;

    private boolean debug;

    private String url;

    private int id;

    private int SendPort;

    private int ReceivePort;

    private HashMap<String, HashSet<String>> index;

    private ArrayList<String> found;

    private HashMap<String, HashSet<String>> linksReferences;

    private String title;

    private ArrayList<String> content;

    private int word_limit = 10;

    public DownloaderThread(int Port, int id, boolean debug) {

        this.SendPort = Port;
        this.id = id;
        this.status = "0";
        this.ReceivePort = Port + 1;
        this.debug = debug;
        this.index = new HashMap<String, HashSet<String>>();
        this.found = new ArrayList<String>();
        this.linksReferences = new HashMap<String, HashSet<String>>();
        this.content = new ArrayList<String>();

        // System.out.println("Downloader Thread " + id + " created with port " + Port);

    }

    // Download the page and read its words
    public void download(String url) {

        try {

            Document doc = Jsoup.connect(url).get();

            this.title = doc.title();

            // If page has no title, don't add it to the index
            if (this.title.equals("")) {

                System.out.println("Page " + url + " has no title");
                return;
            }

            // Get page title
            this.title = title;

            // Read all words and associate them with the page, without duplicates
            Elements words = doc.select("body");

            int i = 0;

            for (Element word : words) {
                String[] wordsArray = word.text().split(" ");

                for (String wordArray : wordsArray) {

                    if (this.index.containsKey(wordArray)) {
                        this.index.get(wordArray).add(url);
                    } else {
                        HashSet<String> urls = new HashSet<String>();
                        urls.add(url);
                        this.index.put(wordArray, urls);

                        // Add the word to the content
                        if (i < this.word_limit) {
                            this.content.add(wordArray);
                            i++;
                        }
                    }
                }

            }

            // Add page to linksReferences but without a reference to it (if it isn't
            // already there)
            if (!this.linksReferences.containsKey(url)) {
                HashSet<String> urls = new HashSet<String>();
                this.linksReferences.put(url, urls);
            }

            int linksFound = 0;

            // Read any existing links and read their words as well recursively
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                String linkHref = link.attr("href");

                // Check if link is a valid url
                if (!linkHref.startsWith("http") && !linkHref.startsWith("https")) {
                    continue;
                }

                // Only get the first MAX_LINKS links
                if (linksFound >= this.MAX_LINKS) {
                    break;
                }

                linksFound++;

                // Add link to the refereces
                if (this.linksReferences.containsKey(linkHref)) {
                    this.linksReferences.get(linkHref).add(url);
                } else {
                    HashSet<String> urls = new HashSet<String>();
                    urls.add(url);
                    this.linksReferences.put(linkHref, urls);
                }

                // Check if link was already found
                if (!this.found.contains(linkHref)) {
                    this.found.add(linkHref);
                }

            }
        } catch (Exception e) {
            System.out.println("Couldn't dowload  " + url);
            return;
        }

    }

    public void printIndex() {

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

            if (debug)
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

                if (debug)
                    System.out.println("Downloader Thread " + id + " got url " + url);
            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Exception in Downloader.getURL: " + e);
        }

    }

    // Adds a URL to the queue
    public void adicionaURL(ArrayList<String> urls) {

        for (String url : urls) {

            try {

                // Adicionar à queue
                Socket socket = new Socket("localhost", SendPort);
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output);

                writer.println("ADD_URL " + url);
                writer.flush();
                socket.close();
            } catch (ConnectException e) {
                System.out.println("Couldn't connect to server");
            } catch (IOException e) {
                System.out.println("Exception in Downloader.adicionaURL: " + e);
            }

        }

    }

    public String transformLineHashMap() {
        // percorrer o hashmap e transformar em String do tipo "palavra: url1, url2,
        // url3"
        String string = "";

        // Add the index to the string
        for (String key : this.index.keySet()) {
            string += key + " ";
            for (String url : this.index.get(key)) {
                string += url + " ";
            }
            // separete keys/url with a new line
            string += "\n";
        }

        // Add a separator between the index and the links references
        string += "LINKS\n";

        string += this.url + " ";

        // Add the found urls to the string
        for (String url : this.found) {
            string += url + " ";
        }

        // Add a separator and add the title
        string += "\nTITLE\n" + this.title + "\n";

        // Add a separator and add the content
        string += "CONTENT\n";
        for (String content : this.content) {
            string += content + " ";
        }

        return string;
    }

    public void enviaIndex(String index) {

        try {

            MulticastSocket socket = new MulticastSocket();

            // Enviar o index para o IndexStorageBarrel

            InetAddress group = InetAddress.getByName(MULTICAST_ADRESS);
            // transform hasmap to bytes to send

            byte[] buf = index.getBytes();

            // If the buffer is bigger than 65534
            if (buf.length > 65534) {
                // System.out.println("Buffer is bigger than 65534");
                return;
            }

            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, MULTICAST_PORT);
            socket.send(packet);

            // Confirm that the index was sent
            if (debug)
                System.out.println("Downloader Thread " + id + " sent index to IndexStorageBarrel");

            // Confirm that the index was received
            // DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
            // socket.receive(response);
            // if (response.getData() != null) {
            // System.out.println("Index was received, exiting");
            // break;
            // }

            // socket.close();
        } catch (IOException e) {
            System.out.println("Exception in Downloader.enviaIndex: " + e);
        }

    }

    public void status() {

        try {
            MulticastSocket socket = new MulticastSocket();

            InetAddress group = InetAddress.getByName(Global.MULTICAST_ADRESS);

            String msg = "DOWNLOADER " + this.id + " " + status;

            byte[] buffer = msg.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 6500);

            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Exception in Downloader.statusDownloader: " + e);
        }
    }

    // Thread running method
    public void run() {

        // System.out.println("Downloader Thread " + id + " running with port " +
        // SendPort + " and " + ReceivePort + "");

        try {

            while (true) {

                // Ir buscar o url à queue
                getURL();

                this.status = "1";
                status();

                if (url != null) {

                    if (debug) {
                    }
                    System.out.println("Downloader Thread " + id + " downloading " + url);

                    // Fazer o download do url
                    download(this.url);

                    if (this.title == null || this.title.equals("")) {

                        System.out.println("Downloader Thread " + id + " -> " + url + " has no title, skipping");

                        continue;
                    }

                    if (debug) {
                        System.out.println("Downloader Thread " + id + " Content " + this.content);
                        System.out.println("Downloader Thread " + id + " Title " + this.title);
                    }

                    // Convert the index hashmap and the linksReferences hashmap to a string
                    String string = transformLineHashMap();

                    // Print the index
                    if (debug)
                        System.out.println("Downloader Thread " + id + " index e linksReferences:\n " + string);

                    // Enviar o index para o IndexStorageBarrel - Multicast
                    enviaIndex(string);

                    // Se houver urls encontrados, adicionar à queue
                    if (found.size() > 0) {
                        // for (String url : found) {

                        // // Adicionar à queue
                        // if (debug)
                        // System.out.println("Downloader Thread " + id + " adding url " + url);

                        // }

                        adicionaURL(found);
                    }

                    if (debug)
                        System.out.println("=================================================================");

                } else {

                    System.out.println("Downloader Thread " + id + " got no url");
                }

                this.url = null;
                this.found.clear();
                this.index.clear(); // Depois de confirmar que o index foi enviado para o IndexStorageBarrel
                this.linksReferences.clear();
                this.content.clear();
                this.title = null;
            }

        } catch (Exception e) {
            System.out.println("Exception in Downloader.run: " + e);
            this.status = "0";
            status();
        }

        this.status = "0";
        status();

    }

}
