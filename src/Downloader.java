import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Trabalha em paralelo (para aumentar o desempenho)
// Um URL -> Um Downloader
// Envia informação processada para o IndexStorageBarrel usando Multicast
// Segue uma fila de URL para escalonar futuras páginas a visitar
// Atualizam o index através de Multi

public class Downloader implements Runnable {

    private static int num_threads = 1;

    private Thread thread;

    private String url;

    private static HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();

    private static ArrayList<String> found = new ArrayList<String>();

    public Downloader() {

    }

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

    public String getURL() throws IOException {

        // Ir buscar o url à queue

        return null;

    }

    public void adicionaURL(String url) {

        // Adicionar à queue

    }

    // Arranque da thread
    public void run() {

        // System.out.println("Downloader running");

        while (true) {

            // Ir buscar o url à queue
            // try {
            // url = getURL();
            // } catch (IOException e) {
            // System.out.println("Exception in Downloader.getURL: " + e);
            // }

            url = "http://127.0.0.1:5500/Test_Site/site.html";

            // Fazer o download do url
            try {
                download(url);
            } catch (IOException e) {
                System.out.println("Exception in Downloader.download: " + e);
            }

            // Show index
            try {
                printIndex();
            } catch (IOException e) {
                System.out.println("Exception in Downloader.printIndex: " + e);
            }

            // Enviar o index para o IndexStorageBarrel

            // Se houver urls encontrados, adicionar à queue
            if (found.size() > 0) {
                for (String url : found) {
                    // Adicionar à queue
                    adicionaURL(url);

                }
            }

            break;
        }

    }

    public void start() {

        // Criar threads e inicia-las
        for (int i = 0; i < num_threads; i++) {
            thread = new Thread(this);
            thread.start();
            // System.out.println("Downloader " + i + " started");
        }

    }

}