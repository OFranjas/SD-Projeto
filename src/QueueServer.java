import java.util.*;
import java.net.*;
import java.io.*;

public class QueueServer extends Thread {

    private int Port;

    private Queue queue;

    private ServerSocket recebeServerSocket;

    public QueueServer(Queue queue, int port) throws IOException {
        this.queue = queue;
        this.Port = port;
        recebeServerSocket = new ServerSocket(Port);
    }

    private void recebe_urls() throws IOException {

        // Return if the

        Socket socket = recebeServerSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String command = reader.readLine();
        if (command.startsWith("ADD_URL")) {

            String url = command.substring(8);

            // TODO Verificar se a URL já existiu no histórico

            queue.add(url);
            System.out.println("URL adicionada à queue: " + url);
        }

        socket.close();
    }

    public void envia_urls() throws IOException {

    }

    // Print the queue
    public void printQueue() {

        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.get(i));
        }

    }

    public void run() {

        while (true) {

            if (this.Port == 8080) {
                try {

                    // System.out.println("QueueServer running");
                    recebe_urls();

                    printQueue();

                    sleep(1000);

                } catch (IOException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                } catch (InterruptedException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                }
            } else if (this.Port == 8081) {

                try {

                    // System.out.println("QueueServer2 running");
                    sleep(1000);
                    envia_urls();

                    printQueue();

                } catch (IOException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                } catch (InterruptedException e) {
                    System.out.println("Exception in QueueServer.run: " + e);
                }
            } else {
                System.out.println("Porta inválida");
            }

        }

    }

}
