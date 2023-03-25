import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.stylesheets.DocumentStyle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
// Trabalha em paralelo (para aumentar o desempenho)
import java.net.MulticastSocket;
import java.net.*;

// Recebe informação processada pelo Downloader usando Multicast

// É o servidor central (replicado) que armazena todos os dados da aplicação
// Recebe os elementos do índice (palavras e URLs) através de multicast envidado pelos Downloaders
// Protocolo de Multicast fiável para que todos os IndexStorageBarrels tenham informação idêntica (podendo haver omissões)
// saber qual quantos storage barrels existem e qual é este -> id tem de vir nas threads quando se criam no ciclo for

public class IndexStorageBarrels {

    private int num_threads;
    private static int MAX_THREADS = 5;
    private IndexStorageBarrelThread thread;

    private boolean debug;

    public IndexStorageBarrels(int num_threads, boolean debug) {

        this.num_threads = num_threads;
        this.debug = debug;

    }

    public void start() {

        if (this.num_threads > MAX_THREADS) {
            this.num_threads = MAX_THREADS;
        }

        for (int i = 0; i < this.num_threads; i++) {
            thread = new IndexStorageBarrelThread(i, debug);
            thread.start(); // send i to the thread for id of storage barrel
        }

    }

}
