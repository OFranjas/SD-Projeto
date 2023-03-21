import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try {

            // Start the Queue
            Queue queue = new Queue();
            queue.start();

            // Start the Downloaders Threads
            Downloader downloader = new Downloader(1);
            downloader.start();

            // Start the IndexStorageBarrels Threads

            // Start the Search Module Thread
            // RMISearchModule searchModule = new RMISearchModule();

        } catch (Exception e) {
            System.out.println("Exception in Main.main: " + e);
        }

    }

}
