package main.java.com.example.SDProject.Downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import java.io.*;

// Trabalha em paralelo (para aumentar o desempenho)
// Um URL -> Um Downloader
// Envia informação processada para o IndexStorageBarrel usando Multicast
// Segue uma fila de URL para escalonar futuras páginas a visitar
// Atualizam o index através de Multi

public class Downloaders {

    private int num_threads;

    private static int MAX_THREADS = 5;

    private boolean debug;

    private DownloaderThread thread;

    public Downloaders(int num_threads, boolean debug) {

        this.num_threads = num_threads;
        this.debug = debug;

        // System.out.println("Downloader created");

    }

    // Start the threads
    public void start() {

        if (num_threads > MAX_THREADS) {
            num_threads = MAX_THREADS;
        }

        // Criar threads e inicia-las
        for (int i = 0; i < num_threads; i++) {

            thread = new DownloaderThread(8000 + i * 2, i, debug);
            thread.start();
            // System.out.println("Downloader Thread " + i + " started...");
        }

    }

}