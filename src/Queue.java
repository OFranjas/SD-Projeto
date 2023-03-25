import java.io.BufferedReader;
import java.net.ServerSocket;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.management.Query;

public class Queue {

    private ArrayList<String> queue;

    private ArrayList<String> history;

    private int num_threads;

    private static int MAX_THREADS = 5;

    private boolean debug;

    // TODO Criar histórioco de URLs

    private QueueThread thread;

    public Queue(int num_threads, boolean debug) {

        queue = new ArrayList<String>();

        history = new ArrayList<String>();

        this.num_threads = num_threads;

        this.debug = debug;

        queue.add("http://127.0.0.1:5500/Test_Site/site.html");

    }

    public void add(String url) {
        queue.add(url);
        history.add(url);
    }

    public String get(int i) {
        return queue.get(i);
    }

    public void remove(String url) {
        queue.remove(url);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    // Verifica se a URL já existe no histórico
    public boolean exists(String url) {
        return history.contains(url);
    }

    public void start() {

        if (num_threads > MAX_THREADS) {
            num_threads = MAX_THREADS;
        }

        try {

            // TODO -> Mudar ports para 8000s e 9000s

            for (int i = 0; i < num_threads; i++) {

                thread = new QueueThread(this, 8000 + i * 2, debug);
                thread.start();

                thread = new QueueThread(this, 8001 + i * 2, debug);
                thread.start();
            }

        } catch (IOException e) {
            System.out.println("Exception in Queue.start: " + e);
        }

    }
}
