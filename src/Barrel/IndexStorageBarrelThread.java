package Barrel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.net.*;
import java.io.*;
import Global.Global;
import java.rmi.ConnectException;

public class IndexStorageBarrelThread extends Thread implements BarrelInterface, Serializable {

    private String MULTICAST_ADRESS = "224.3.2.1";
    private int MULTICAST_PORT = 4321;
    private MulticastSocket socket;
    private InetSocketAddress group;
    private NetworkInterface netIf;

    private boolean debug;

    private int id;

    private String status;

    private HashMap<String, HashSet<String>> index; // Index Normal

    private HashMap<String, HashSet<String>> invertedIndex; // Index Invertido

    private HashMap<String, HashSet<String>> linksReferences; // Referencias de links

    private HashMap<String, HashSet<String>> downloaderIndex; // Index do Downloader

    private HashMap<String, HashSet<String>> downloaderLinksReferences;

    private HashMap<String, String> titles;

    private HashMap<String, String> contents;

    private String url;

    private boolean ligado;

    private boolean parteSo;

    public IndexStorageBarrelThread(int id, boolean debug) {

        this.id = id;
        this.debug = debug;
        this.status = "0";
        this.ligado = false;

        if (id <= (Global.num_threads / 2)) {

            this.parteSo = true;

        } else {

            this.parteSo = false;
        }

        this.index = new HashMap<String, HashSet<String>>();
        this.invertedIndex = new HashMap<String, HashSet<String>>();
        this.linksReferences = new HashMap<String, HashSet<String>>();
        this.titles = new HashMap<String, String>();
        this.contents = new HashMap<String, String>();

        // System.out.println("IndexStorageBarrel Thread " + id + " created");
        try {
            UnicastRemoteObject.exportObject(this, 0);

            while (this.ligado == false) {

                try {
                    // Try to bind the object to the registry
                    Naming.rebind("IndexStorageBarrel" + id, this);

                    this.ligado = true;

                } catch (ConnectException e) {

                    System.out.println("Waiting to start");
                    sleep(1000);

                } catch (Exception e) {
                    System.out.println("IndexStorageBarrelThread: " + e.getMessage());
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            System.out.println("IndexStorageBarrelThread: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void CreateTxtFile() { // funcao para criar ficheiro de texto com o id do storage barrel

        // Create a file named "IndexStorageBarrelid.txt"

        try {
            File file = new File("src/Barrel/IndexStorageBarrel" + id + ".txt"); // bate ???

            // check if file exist

            if (file.createNewFile()) {

                if (debug)
                    System.out.println("File is created!");

            } else {

                if (debug)
                    System.out.println("File already exists.");

                // Read the file and add the index to the index and links references to the
                // links references

                // Open the file for reading
                FileReader fr = new FileReader("src/Barrel/IndexStorageBarrel" + id + ".txt");
                BufferedReader br = new BufferedReader(fr);

                // Read the file line by line
                String line;

                while ((line = br.readLine()) != null) {

                    String words[] = line.split(" ");

                    // If the first element is LINKREFERENCE, add the link reference to the links

                    if (words[0].equals("LINKSREFERENCES")) {

                        // Separate the elements with |
                        String splitado[] = line.split("\\|");

                        words = splitado[0].split(" ");

                        this.contents.put(words[1], splitado[2]);

                        this.titles.put(words[1], splitado[1]);

                        // System.out.println("WORDS -> " + words[0] + " " + words[1] + " " + words[2]);

                        if (words.length == 2) {

                            this.linksReferences.put(words[1], new HashSet<String>());

                        } else {

                            for (int i = 2; i < words.length; i++) {

                                // If it is already in the linksReferences, add the urls to the hashset

                                if (this.linksReferences.containsKey(words[1])) {

                                    this.linksReferences.get(words[1]).add(words[i]);

                                } else {

                                    HashSet<String> urls = new HashSet<String>();
                                    urls.add(words[i]);
                                    this.linksReferences.put(words[1], urls);

                                }

                            }

                        }

                    } else {

                        // If it is already in the index, add the urls to the hashset
                        for (int i = 1; i < words.length; i++) {

                            if (this.index.containsKey(words[0])) {

                                this.index.get(words[0]).add(words[i]);

                            } else {

                                HashSet<String> urls = new HashSet<String>();
                                urls.add(words[i]);
                                this.index.put(words[0], urls);

                            }

                        }
                    }

                }

                // Close the file
                br.close();
                fr.close();

            }

        } catch (IOException e) {
            System.out.println("IndexBarrel -> Error in CreateTxtFile");
            e.printStackTrace();
        }

    }

    private void WriteTxtFile() { // funcao para escrever no ficheiro de texto
        // Read the file

        try {
            FileWriter myWriter = new FileWriter("src/Barrel/IndexStorageBarrel" + id + ".txt");

            // Go trough the index and write it to the file word url line by line
            for (String key : this.index.keySet()) {

                // Strip the brackets and the commas
                String res = this.index.get(key).toString().replace("[", "");
                res = res.replace("]", "");
                res = res.replace(",", "");

                myWriter.write(key + " " + res + "\n");
            }

            // Write the links references
            for (String key : this.linksReferences.keySet()) {

                // Strip the brackets
                String res = this.linksReferences.get(key).toString().replace("[", "");
                res = res.replace("]", "");

                // If the key has a title and contend write it to the file
                if (this.titles.containsKey(key) && this.contents.containsKey(key)) {

                    // If it has no res
                    if (res.equals("")) {
                        myWriter.write("LINKSREFERENCES " + key + "|" + this.titles.get(key) + "|"
                                + this.contents.get(key) + "\n");
                    } else {
                        myWriter.write("LINKSREFERENCES " + key + " " + res + "|" + this.titles.get(key) + "|"
                                + this.contents.get(key) + "\n");
                    }

                } else {
                    myWriter.write("LINKSREFERENCES " + key + " " + res + "\n");
                }

            }

            myWriter.close();

            if (debug)
                System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("IndexBarrel -> Error in WriteTxtFile");
            e.printStackTrace();
        }
    }

    private void CompareIndexWithDownloader() { // function to compare the index with downloader

        // go trough the index and compare it with the downloader

        try {

            // If the index is empty, add the downloader to the index
            if (this.index.isEmpty()) {
                index = downloaderIndex;
            } else {

                // Go through the downloader, if the word is in the index, add the url to the
                // index, if not, add the word to the index
                for (String key : this.downloaderIndex.keySet()) {

                    if (this.index.containsKey(key)) {

                        // if the word is in the downloader, and the url is not in the index, add the
                        // url to the index
                        for (String url : downloaderIndex.get(key)) {
                            if (!this.index.get(key).contains(url)) {
                                this.index.get(key).add(url);
                            }
                        }

                    } else {

                        // if the word is not in the downloader, add the word to the index
                        this.index.put(key, this.downloaderIndex.get(key));
                    }
                }
            }

            if (this.linksReferences.isEmpty()) {
                linksReferences = downloaderLinksReferences;

                // System.out.println("Empty");

            } else {

                for (String key : this.downloaderLinksReferences.keySet()) {

                    if (this.linksReferences.containsKey(key)) {

                        for (String url : this.downloaderLinksReferences.get(key)) {
                            if (!this.linksReferences.get(key).contains(url)) {

                                // System.out.println("IndexBarrel -> CompareIndexWithDownloader -> " + key + "
                                // " + url);
                                this.linksReferences.get(key).add(url);
                            }
                        }

                    } else {

                        // if the word is not in the downloader, add the word to the index
                        this.linksReferences.put(key, downloaderLinksReferences.get(key));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("IndexBarrel -> Error in CompareIndexWithDownloader");
            System.out.println(e);
        }

    }

    private void messageToHashmap(String message) {

        try {

            // percorrer a string recebida linhh a linha e separar a 1ª palavra de cada
            // linha como key e as restantes como urls
            downloaderIndex = new HashMap<String, HashSet<String>>();
            downloaderLinksReferences = new HashMap<String, HashSet<String>>();
            String linhas[] = message.split("\n");

            int res = 9999;

            for (int i = 0; i < linhas.length; i++) {
                String[] palavras = linhas[i].split(" ");

                if (palavras[0].equals("LINKS")) {
                    res = i;
                    break;
                }

                if (this.parteSo) {
                    if (palavras[0].toLowerCase().charAt(0) <= 'm') {
                        for (int j = 1; j < palavras.length; j++) {

                            if (j == 1) {

                                // System.out.println(palavras[0]);
                                this.downloaderIndex.put(palavras[0], new HashSet<String>());
                                this.downloaderIndex.get(palavras[0]).add(palavras[j]);
                            } else {
                                this.downloaderIndex.get(palavras[0]).add(palavras[j]);
                            }

                        }
                    }
                } else {
                    if (palavras[0].toLowerCase().charAt(0) > 'm') {
                        for (int j = 1; j < palavras.length; j++) {

                            if (j == 1) {

                                // System.out.println(palavras[0]);
                                this.downloaderIndex.put(palavras[0], new HashSet<String>());
                                this.downloaderIndex.get(palavras[0]).add(palavras[j]);
                            } else {
                                this.downloaderIndex.get(palavras[0]).add(palavras[j]);
                            }

                        }
                    }
                }

            }

            // For
            for (int i = res + 1; i < linhas.length; i++) {
                String[] palavras = linhas[i].split(" ");

                // If it finds the TITLE separator, stop
                if (palavras[0].equals("TITLE")) {
                    res = i;
                    break;
                }

                this.url = palavras[0];

                // If there is no links references, only add the url
                if (palavras.length == 1) {
                    this.downloaderLinksReferences.put(palavras[0], new HashSet<String>());
                    continue;
                }

                for (int j = 1; j < palavras.length; j++) {
                    if (j == 1) {
                        // System.out.println(palavras[0]);
                        this.downloaderLinksReferences.put(palavras[0], new HashSet<String>());
                        this.downloaderLinksReferences.get(palavras[0]).add(palavras[j]);
                    } else {
                        this.downloaderLinksReferences.get(palavras[0]).add(palavras[j]);
                    }
                }
            }

            // Add the title and content to the hashmap if it isnt already there
            if (!this.titles.containsKey(this.url))
                this.titles.put(this.url, linhas[res + 1]);
            if (!this.contents.containsKey(this.url))
                this.contents.put(this.url, linhas[res + 3]);

        } catch (StringIndexOutOfBoundsException e) {

            System.out.println("IndexBarrel -> Message received is empty");
        } catch (Exception e) {
            System.out.println("IndexBarrel -> Error in messageToHashmap");
            System.out.println(e);
        }

    }

    private void printHashMap(HashMap<String, HashSet<String>> map) {

        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
        }

    }

    private void receiveMessage() {

        try {

            if (debug)
                System.out.println("IndexStorageBarrel " + id + " is waiting for the index from Downloader");

            this.socket = new MulticastSocket(MULTICAST_PORT);
            this.group = new InetSocketAddress(MULTICAST_ADRESS, MULTICAST_PORT);
            this.netIf = NetworkInterface.getByName("bge0");

            socket.joinGroup(group, netIf);

            byte[] buffer = new byte[65534];

            // Wait for a message to be received, when it is received, the loop will break
            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength()); // receber a string com muita

                if (debug) {
                    System.out.println("IndexStorageBarrel " + id + " received the following message from Downloader:");
                    System.out.println(message);
                }

                this.status = "1";
                status();

                messageToHashmap(message);

                if (debug) {
                    System.out.println("IndexStorageBarrel " + id + " received the following index from Downloader:");
                    printHashMap(this.downloaderIndex);
                    System.out.println("IndexStorageBarrel " + id + " received the following links from Downloader:");
                    printHashMap(this.downloaderLinksReferences);

                }

                // Editar index com as novas cenas
                CompareIndexWithDownloader();

                if (debug) {
                    System.out.println("IndexStorageBarrel " + id + " has the following index:");
                    printHashMap(this.index);
                    System.out.println("IndexStorageBarrel " + id + " has the following links:");
                    printHashMap(this.linksReferences);

                }

                // Guarda o index num ficheiro de texto
                WriteTxtFile();

                // Transforma o index normal em index invertido
                convertIndex();

                // Se o Search Module pedir, envia o index -> RMI

                // Kill the barrel
                // break;

                if (debug)
                    System.out.println("=======================================================================");

            }

        } catch (IOException e) {
            System.out.println("IndexBarrel -> Error in receiveMessage");
            e.printStackTrace();
            this.socket.close();
            this.status = "0";
            status();
        }

        this.status = "0";
        status();

    }

    private void convertIndex() {

        // Transform the normal index into the inverted index -> To each URL is
        // associated the words in it

        for (String key : this.index.keySet()) {

            for (String url : this.index.get(key)) {

                if (this.invertedIndex.containsKey(url)) {

                    this.invertedIndex.get(url).add(key);

                } else {

                    this.invertedIndex.put(url, new HashSet<String>());
                    this.invertedIndex.get(url).add(key);

                }

            }

        }

        if (debug) {
            System.out.println("IndexStorageBarrel " + id + " has the following inverted index:");
            printHashMap(invertedIndex);
        }

    }

    private ArrayList<String> sortUrls(ArrayList<String> urls) {

        // Store the number of times each url appears in the index
        HashMap<String, Integer> urlsCount = new HashMap<String, Integer>();

        // Go throught the linksReferences hashmap and count the number of links that
        // contain the url
        for (String url : urls) {

            int count = 0;

            for (String key : this.linksReferences.keySet()) {

                if (this.linksReferences.get(key).contains(url)) {
                    count++;
                }

            }

            urlsCount.put(url, count);

        }

        // Sort the urls by the number of times they appear in the index, from the most
        // to the least
        Collections.sort(urls, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return urlsCount.get(o2) - urlsCount.get(o1);
            }

        });

        return urls;

    }

    public void status() {

        try {
            MulticastSocket socket = new MulticastSocket();

            InetAddress group = InetAddress.getByName(Global.MULTICAST_ADRESS);

            String msg = "BARREL " + this.id + " " + status;

            byte[] buffer = msg.getBytes();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 6500);

            socket.send(packet);
        } catch (Exception e) {
            System.out.println("Exception in IndexStorageBarrelThread.status: " + e);
        }
    }

    public void run() {

        this.status = "0";
        status();

        // Create text file if it doesn't exist
        CreateTxtFile();

        if (debug) {

            System.out.println("IndexStorageBarrel " + id + " has the following starting index:");
            printHashMap(index);
            System.out.println("IndexStorageBarrel " + id + " has the following starting LINKS:");
            printHashMap(linksReferences);
            System.out.println("IndexStorageBarrel " + id + " has the following starting TITLES:");
            // Print the titles hashmap
            for (String key : this.titles.keySet()) {
                System.out.println(key + " " + this.titles.get(key));
            }
            System.out.println("IndexStorageBarrel " + id + " has the following starting CONTENTS:");
            // Print the contents hashmap
            for (String key : this.contents.keySet()) {
                System.out.println(key + " " + this.contents.get(key));
            }

        }

        if (debug)
            System.out.println("IndexStorageBarrel " + id + " is running");

        // Recebe o index do Downloader -> Multicast
        receiveMessage();

    }

    @Override
    public ArrayList<String> procuraConteudo(String conteudo) {

        try {

            ArrayList<String> urls = new ArrayList<String>();

            // String low = conteudo.toLowerCase();

            // Check if the word is not in the index
            if (!this.index.containsKey(conteudo)) {
                return urls;
            }

            // Run through the words in the index
            for (String word : this.index.keySet()) {

                // word = word.toLowerCase();

                // If the word is the one we are looking for
                if (word.equals(conteudo)) {

                    // Run through the urls in the index of the word thats equal
                    for (String url : this.index.get(conteudo)) {

                        urls.add(url);

                    }

                }

            }

            // Sort the urls by the number of times they appear in the index
            urls = sortUrls(urls);

            // Get the title and content of the urls
            for (int i = 0; i < urls.size(); i++) {

                String url = urls.get(i);

                String title = this.titles.get(url);
                String content = this.contents.get(url);

                urls.set(i, url + "|" + title + "|" + content);

            }

            // System.out.println("IndexStorageBarrel " + id + " has the following urls for
            // the word " + conteudo + ":");

            // for (String url : urls) {
            // System.out.println(url);
            // }

            return urls;

        } catch (Exception e) {
            System.out.println("IndexStorageBarrel -> Error in procuraConteudo");
            e.printStackTrace();

            return null;
        }

    }

    @Override
    public ArrayList<String> ligacoesURL(String url) {

        try {

            ArrayList<String> links = new ArrayList<String>();

            // Go through the linksReferences hashmap and get the links that contain the url
            for (String key : this.linksReferences.keySet()) {

                if (this.linksReferences.get(key).contains(url)) {
                    links.add(key);
                }

            }

            links = sortUrls(links);

            // System.out.println("IndexStorageBarrel " + id + " has the following links for
            // the url " + url + ":");

            // for (String link : links) {
            // System.out.println(link);
            // }

            return links;

        } catch (Exception e) {
            System.out.println("IndexStorageBarrel -> Error in ligacoesURL");
            e.printStackTrace();
            return null;
        }

    }
}
