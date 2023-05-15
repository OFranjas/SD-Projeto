package main.java.com.example.SDProject;

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;

import main.java.com.example.SDProject.Barrel.IndexStorageBarrels;
import main.java.com.example.SDProject.Downloader.Downloaders;
import main.java.com.example.SDProject.Global.Global;
import main.java.com.example.SDProject.Queue.Queue;

public class Main {

    private static int num_threads = Global.num_threads;

    private static boolean debug = false;

    public static void main(String[] args) {

        try {

            if (num_threads < 2) {

                System.out.println("Number of threads must be greater than 1");
                System.exit(0);

            }

            // Start the Queue
            Queue queue = new Queue(num_threads, debug);
            queue.start();

            // Start the IndexStorageBarrels Threads
            IndexStorageBarrels indexStorageBarrel = new IndexStorageBarrels(num_threads, debug);
            indexStorageBarrel.start();

            // Start the Downloaders Threads
            Downloaders downloader = new Downloaders(num_threads, debug);
            downloader.start();

            System.out.println("PROGRAM STARTED");

            // Start the Search Module Thread
            // RMISearchModule searchModule = new RMISearchModule();

        } catch (Exception e) {
            System.out.println("Exception in Main.main: " + e);
        }

    }

}
