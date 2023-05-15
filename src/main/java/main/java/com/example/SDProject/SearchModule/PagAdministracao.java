package main.java.com.example.SDProject.SearchModule;

import Global.Global;
import java.util.*;
import java.net.*;
import java.io.*;

public class PagAdministracao {

    private ArrayList<String> barrel;
    private ArrayList<String> donwloader;

    private String status;

    private MulticastSocket socket;
    private InetSocketAddress group;
    private NetworkInterface netIf;

    private HashMap<String, Integer> words;

    public PagAdministracao(HashMap<String, Integer> words) {
        this.words = new HashMap<String, Integer>();
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

    public String finalStatus() {
        // put barrel and downloader status "0"

        for (int i = 0; i < Global.MAX_THREADS; i++) {
            this.barrel.add(i, "0");
            this.donwloader.add(i, "0");
        }

        receiveMulticast();

        return this.status;
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
                    this.barrel.add(i, parts[2]);
                } else if (parts[0].equals("DOWNLOADER")) {
                    this.donwloader.add(i, parts[2]);
                }
            }
        }
    }

    public String printStatus() {

        // create a string with all this prints

        String s = "";

        // System.out.println("Barrel Status: ");
        for (int i = 0; i < Global.num_threads; i++) {
            // System.out.println("Barrel " + i + " " + this.barrel.get(i));
            if (this.barrel.get(i).equals("1")) {
                s += "| Barrel " + i + " is active\n";
            } else if (this.barrel.get(i).equals("0")) {
                s += "| Barrel " + i + " is not active\n";
            }
        }

        // System.out.println("Downloader Status: ");
        for (int i = 0; i < Global.num_threads; i++) {
            // System.out.println("Downloader " + i + " " + this.donwloader.get(i));
            if (this.donwloader.get(i).equals("1")) {
                s += "| Downloader " + i + " is active\n";
            } else if (this.donwloader.get(i).equals("0")) {
                s += "| Downloader " + i + " is not active\n";
            }
        }

        return s;
    }

    public void receiveMulticast() {

        try {
            System.out.println("Receiving Multicast");
            this.socket = new MulticastSocket(6500);
            this.group = new InetSocketAddress(Global.MULTICAST_ADRESS, 6500);
            this.netIf = NetworkInterface.getByName("bge0");

            socket.joinGroup(group, netIf);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                // System.out.println("Received: " + msg);
                checkStatus(msg);

                this.status = printStatus();
            }

        } catch (Exception e) {
            System.out.println("Exception in PagAdministracao.receiveMulticast: " + e);
        } finally {
            socket.close();
        }

    }

    // get status

    public String getStatus() {
        return this.status;
    }

}