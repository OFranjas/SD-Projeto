import java.util.ArrayList;

import javax.management.Query;

public class Queue {

    private ArrayList<String> queue = new ArrayList<String>();

    public Queue() {
    }

    public void add(String url) {
        queue.add(url);
    }

    public String get() {
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void recebe_urls() {

        // 1. Criar thread para receber do Downloader de uma Classe para receber os urls
        // do
        // Downloader
        // Para prevenir o Caso em que haja mais do que um a querer aceder a queue

        // 2. Adicionar o url à queue
        // Supostamente as threads criadas antes vão retornar os urls para adicionar
    }

    public void envia_urls() {

    }

    public static int main(String[] args) {
        Queue queue = new Queue();
        queue.add("ola");

        // 1. Aceitar um Downloader

        return 0;
    }
}
