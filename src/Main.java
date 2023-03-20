import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

public class Main {

    private final static int NUM_DOWNLOADERS = 5;
    private final static int NUM_INDEXSTORAGEBARRELS = 5;

    public static void main(String[] args) {

        try {

            // Start the Queue
            Queue queue = new Queue();

            // Start the Downloaders Threads
            ArrayList<Downloader> downloaders = new ArrayList<Downloader>();
            for (int i = 0; i < NUM_DOWNLOADERS; i++) {
                Downloader downloader = new Downloader();
                downloaders.add(downloader);
                downloader.start();
            }

            // Start the IndexStorageBarrels Threads

            // Start the Search Module Thread
            // RMISearchModule searchModule = new RMISearchModule();

        } catch (Exception e) {
            System.out.println("Exception in Main.main: " + e);
        }

    }

}
