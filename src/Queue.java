import java.io.BufferedReader;
import java.net.ServerSocket;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.management.Query;

public class Queue {

    private ArrayList<String> queue;

    // TODO Criar histórioco de URLs

    private Thread thread;

    public Queue() {

        queue = new ArrayList<String>();

    }

    public void add(String url) {
        queue.add(url);
    }

    public String get(int i) {
        return queue.remove(i);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public void start() {

        try {
            QueueServer recebeServer = new QueueServer(this, 8080);
            thread = new Thread(recebeServer);
            thread.start();

            QueueServer mandaServer = new QueueServer(this, 8081);
            thread = new Thread(mandaServer);
            thread.start();
        } catch (IOException e) {
            System.out.println("Exception in Queue.start: " + e);
        }

    }
}
