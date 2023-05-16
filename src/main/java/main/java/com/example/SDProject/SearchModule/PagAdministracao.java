package main.java.com.example.SDProject.SearchModule;

import main.java.com.example.SDProject.Global.Global;
import java.util.*;
import java.net.*;
import java.io.*;

public class PagAdministracao {

    private ArrayList<String> barrel;
    private ArrayList<String> donwloader;

    private String status;

    private HashMap<String, Integer> words;

    public PagAdministracao(HashMap<String, Integer> words) {
        this.words = words;
        this.barrel = new ArrayList<String>();
        this.donwloader = new ArrayList<String>();
    }

    public HashMap<String, Integer> recebe_palavras(String s) {
        // see if string in words, if not add with value = 1, if yes add 1 to value

        if (words.containsKey(s)) {
            words.put(s, words.get(s) + 1);
        } else {
            words.put(s, 1);
        }

        // System.out.println("Palavra: " + s + " Valor: " + words.get(s));

        return words;
    }

    public void finalStatus() {
        // put barrel and downloader status "0"

        for (int i = 0; i < Global.MAX_THREADS; i++) {
            this.barrel.add(i, "0");
            this.donwloader.add(i, "0");
        }

        status = printStatus();

        receiveMulticast();

    }

    public void checkStatus(String s) {

        // check if string is in active BARREL NUM ACTIVE 0/1

        // split string

        String[] parts = s.split(" ");

        // transform parts[1] to int

        int num = Integer.parseInt(parts[1]);

        for (int i = 0; i < Global.MAX_THREADS; i++) {

            if (num == i) {

                // add in position i parts[2]
                if (parts[0].equals("BARREL")) {
                    this.barrel.set(i, parts[2]);
                } else if (parts[0].equals("DOWNLOADER")) {
                    this.donwloader.set(i, parts[2]);
                }
            }
        }
    }

    public String printStatus() {

        // create a string with all this prints

        StringBuilder s = new StringBuilder();

        // System.out.println("Barrel Status: ");
        for (int i = 0; i < Global.num_threads; i++) {
            // System.out.println("Barrel " + i + " " + this.barrel.get(i));
            if (this.barrel.get(i).equals("1")) {
                s.append("| Barrel " + i + " is active\n");
            } else if (this.barrel.get(i).equals("0")) {
                s.append("| Barrel " + i + " is not active\n");
            }
        }

        // System.out.println("Downloader Status: ");
        for (int i = 0; i < Global.num_threads; i++) {
            // System.out.println("Downloader " + i + " " + this.donwloader.get(i));
            if (this.donwloader.get(i).equals("1")) {
                s.append("| Downloader " + i + " is active\n");
            } else if (this.donwloader.get(i).equals("0")) {
                s.append("| Downloader " + i + " is not active\n");
            }
        }

        return s.toString();
    }

    public void receiveMulticast() {

        MulticastSocket socket = null;

        try {
            System.out.println("Receiving Multicast");
            socket = new MulticastSocket(6500);
            InetSocketAddress group = new InetSocketAddress(Global.MULTICAST_ADRESS, 6500);
            NetworkInterface netIf = NetworkInterface.getByName("bge0");

            socket.joinGroup(group, netIf);

            while (true) {

                System.out.println("Waiting for multicast message - status" + this.getStatus());

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                // System.out.println("Received: " + msg);
                checkStatus(msg);

                status = printStatus();
                System.out.println("status: " + status);
            }

        } catch (Exception e) {
            System.out.println("Exception in PagAdministracao.receiveMulticast: " + e);
        } finally {
            socket.close();
        }

    }

    // get status

    public String getStatus() {

        System.out.println("Getting status" + this.status);
        System.out.println("Barrel Status: " + this.barrel);
        System.out.println("Downloader Status: " + this.donwloader);
        return this.status;
    }

}