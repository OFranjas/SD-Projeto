import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.HashSet;

// Trabalha em paralelo (para aumentar o desempenho)
// Um URL -> Um Downloader
// Envia informação processada para o IndexStorageBarrel usando Multicast
// Segue uma fila de URL para escalonar futuras páginas a visitar
// Atualizam o index através de Multi

public class Downloader extends Thread {

    public Downloader() {
    }

    public static HashMap<String, HashSet<String>> index = new HashMap<String, HashSet<String>>();

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
            download(linkHref);

            // Add links to the link queue, not repeating any links

        }

    }

    public static void printIndex() throws IOException {

        // Print all words and associated pages
        for (String key : index.keySet()) {

            System.out.println(key + ": " + index.get(key));
        }

    }

    // Arranque da thread
    public void run() {

        // Dentro de um while(true)

        // Ir buscar o url à queue

        // Fazer o download do url

        // Enviar o index para o IndexStorageBarrel

        // Se houver urls encontrados, adicionar à queue

    }

}