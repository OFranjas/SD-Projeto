import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

public class Main {

    private static int num_threads = 1;

    private static boolean debug = true;

    public static void main(String[] args) {

        try {

            // Start the Queue
            Queue queue = new Queue(num_threads, debug);
            queue.start();

            // Start the IndexStorageBarrels Threads
            IndexStorageBarrels indexStorageBarrel = new IndexStorageBarrels(num_threads, debug);
            indexStorageBarrel.start();

            // Start the Downloaders Threads
            Downloaders downloader = new Downloaders(num_threads, debug);
            downloader.start();

            // Start the Search Module Thread
            // RMISearchModule searchModule = new RMISearchModule();

        } catch (Exception e) {
            System.out.println("Exception in Main.main: " + e);
        }

    }

}
