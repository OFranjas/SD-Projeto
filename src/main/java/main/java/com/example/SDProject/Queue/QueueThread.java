package main.java.com.example.SDProject.Queue;

import java.util.*;
import java.net.*;
import java.io.*;

public class QueueThread extends Thread {

    private int Port;

    private Queue queue;

    private ServerSocket serverSocket;

    private boolean debug;

    public QueueThread(Queue queue, int port, boolean debug) throws IOException {
        this.queue = queue;
        this.Port = port;
        this.debug = debug;
        serverSocket = new ServerSocket(Port);

        // System.out.println("QueueServer created with port " + Port);
    }

    private void recebe_urls() throws IOException {

        try {

            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String command = reader.readLine();
            if (command.startsWith("ADD_URL")) {

                String url = command.substring(8);

                synchronized (queue) {

                    if (queue.exists(url)) {
                        System.out.println("URL já existe no histórico: " + url);
                        return;
                    }

                    queue.add(url);
                    if (debug)
                        System.out.println("URL adicionada à queue: " + url);

                }

            }

            socket.close();
        } catch (IOException e) {
            System.out.println("Exception in QueueServer.recebe_urls: " + e);
        }

    }

    public void envia_urls() throws IOException {

        try {

            synchronized (queue) {

                // Send the url in the queue to a Downloader
                if (!queue.isEmpty()) {

                    Socket socket = serverSocket.accept();
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    writer.println("GET_URL " + queue.get(0));
                    writer.flush();

                    if (debug)
                        System.out.println("QueueServer Thread: Enviando url " + queue.get(0));

                    // If the Downloader has downloaded the page, remove the url from the queue

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String command = reader.readLine();
                    if (command.startsWith("REMOVE_URL")) {

                        String url = command.substring(11);
                        queue.remove(url);

                        if (debug)
                            System.out.println("URL removida da queue: " + url);
                    }

                    socket.close();

                }

            }

        } catch (IOException e) {
            System.out.println("Exception in QueueServer.envia_urls: " + e);
        }

    }

    // Print the queue
    public void printQueue() {

        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.get(i));
        }

    }

    public void run() {

        if (debug)
            System.out.println("QueueServer running on port " + Port);

        // printQueue();

        if (this.Port % 2 == 0) {

            while (true) {
                try {

                    // System.out.println("QueueServer running");
                    recebe_urls();

                    // printQueue();

                } catch (IOException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                }
            }
        } else if (this.Port % 2 != 0) {

            while (true) {

                try {

                    // System.out.println("QueueServer2 running");

                    envia_urls();

                    // printQueue();

                } catch (IOException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                }

            }
        } else if (this.Port == 7000) {

            while (true) {

                try {

                    // System.out.println("QueueServer2 running");

                    recebe_urls();

                    // printQueue();

                } catch (IOException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                }

            }

        }

        else {
            System.out.println("Porta inválida");
        }

    }

}
