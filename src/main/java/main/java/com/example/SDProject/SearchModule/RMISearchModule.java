package main.java.com.example.SDProject.SearchModule;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.com.example.SDProject.Barrel.BarrelInterface;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.net.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import main.java.com.example.SDProject.Global.Global;

// A comunicação com o IndexStorageBarrel é feita usando RMI
// Visível para o utilizador através do RMIClient
// Não armazena dados, logo é dependente dos IndexStorageBarrels
// Escolhe aleatoriamente um IndexStorageBarrel para responder a cada pesquisa

public class RMISearchModule extends UnicastRemoteObject implements ServerInterface {

    private int num_threads = Global.num_threads;

    private PagAdministracao pagadmin;

    private HashMap<String, Integer> words;

    // constructor

    RMISearchModule() throws RemoteException {
        super();
        this.words = new HashMap<String, Integer>();
        pagadmin = new PagAdministracao(words);

    }

    public boolean opcaoUm(String s) throws RemoteException {
        System.out.println("Enviar string para a queue: " + s);

        // check if the string is a URL

        if (!s.contains("http://") && !s.contains("https://")) {
            // send the string to the queue
            System.out.println("String is not a url, don't do anything");
            return false;
        }

        // send the string to the queue

        try {
            Socket socket = new Socket("localhost", 7000);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output);
            writer.println("ADD_URL " + s);
            System.out.println("STRING SENT TO THE QUEUE");
            writer.flush();
            socket.close();

            return true;
        } catch (ConnectException e) {
            System.out.println("Could not connect to the server");
            return false;
        } catch (IOException e) {
            System.out.println("Exception in Downloader.adicionaURL: " + e);
            return false;
        }

    }

    public ArrayList<String> opcaoDois(String s, int tentativas, Boolean First) throws RemoteException {
        // System.out.println("Opcao2, fazer coisinhas: " + s);

        try {

            if (First)
                this.words = pagadmin.recebe_palavras(s);

            ArrayList<String> res1 = new ArrayList<String>();

            ArrayList<String> res2 = new ArrayList<String>();

            // Separar palavras
            String[] palavras = s.split(" ");

            // Percorrer palavras
            for (String palavra : palavras) {

                // Verificar se primeira letra é menor que 'm'
                if (palavra.toLowerCase().charAt(0) <= 'm') {

                    // Escolher um IndexStorageBarrel com id <= num_threads/2
                    int num = (int) (Math.random() * ((Global.num_threads - 1) / 2));

                    // System.out.println("Barril: " + num);

                    // Procurar palavra no IndexStorageBarrel

                    BarrelInterface barril = (BarrelInterface) Naming
                            .lookup("IndexStorageBarrel" + num);

                    // If the res1 is empty, then add the results of the first search
                    if (res1.isEmpty())
                        res1.addAll(barril.procuraConteudo(palavra));
                    else
                        res1.retainAll(barril.procuraConteudo(palavra));

                } else {

                    // Escolher um IndexStorageBarrel com id > num_threads/2
                    int num = (int) (Math.random() * ((Global.num_threads - 1) / 2)) + ((Global.num_threads - 1) / 2)
                            + 1;

                    // System.out.println("Barril: " + num);

                    // Procurar palavra no IndexStorageBarrel
                    BarrelInterface barril = (BarrelInterface) Naming
                            .lookup("IndexStorageBarrel" + num);

                    // If res2 is empty, then add the results of the first search
                    if (res2.isEmpty())
                        res2.addAll(barril.procuraConteudo(palavra));
                    else
                        res2.retainAll(barril.procuraConteudo(palavra));

                    // System.out.println("RES2 para " + palavra + ": " + res2);

                }

            }

            // System.out.println("RES1: " + res1);

            // System.out.println("RES2: " + res2);

            ArrayList<String> res = new ArrayList<String>();

            // Fazer interseção dos resultados se for preciso, ou seja, se res2 não estiver
            // vazio
            if (!res2.isEmpty() && !res1.isEmpty()) {

                // Make intersection of the two results res1 and res2 and store in res
                for (String s1 : res1) {
                    for (String s2 : res2) {
                        if (s1.equals(s2)) {
                            res.add(s1);
                        }
                    }
                }
            }

            // Se for só uma palavra, ou seja, se res2 estiver vazio, então res = res1
            else if (res2.isEmpty()) {
                res = res1;
            }

            // Se for só uma palavra, ou seja, se res1 estiver vazio, então res = res2
            else if (res1.isEmpty()) {
                res = res2;
            }

            // System.out.println("RES: " + res);

            return res;

        } catch (NotBoundException e) {
            System.out.println("Barrel not bound");
            return null;
        } catch (StringIndexOutOfBoundsException e) {

            System.out.println("Input inválido");

            ArrayList<String> res = new ArrayList<String>();

            return res;

        } catch (RemoteException e) {
            System.out.println("Could not connect to the server");
            return null;
        } catch (Exception e) {
            System.out.println("Exception in RMISearchModule.opcaoDois: " + e);
            return null;
        }
    }

    public ArrayList<String> opcaoTres(String s) throws RemoteException {
        // System.out.println("Opcao3, fazer coisinhas: " + s);

        int num = (int) (Math.random() * (Global.num_threads - 1));

        // System.out.println("NUM: " + num);

        if (!s.contains("http://") && !s.contains("https://")) {
            // send the string to the queue

            System.out.println("String is not a URL, don't do anything");
            return new ArrayList<String>();
        }

        try {

            BarrelInterface barril = (BarrelInterface) Naming
                    .lookup("IndexStorageBarrel" + num);

            ArrayList<String> res = barril.ligacoesURL(s);

            System.out.println("RES: " + res);

            return res;
        } catch (NotBoundException e) {
            System.out.println("Barrel not bound");
            return null;
        } catch (RemoteException e) {
            System.out.println("Could not connect to the server");
            return null;
        } catch (Exception e) {
            System.out.println("Exception in RMISearchModule.opcaoTres: ");
            return null;
        }

    }

    public HashMap<String, Integer> opcaoQuatro() throws RemoteException {

        // Create a string wiht all the words in the words HashMap

        return this.words;

    }

    @Override
    public String getStatus() throws RemoteException {

        return pagadmin.getStatus();

    }

    private void run(RMISearchModule searchModule) throws MalformedURLException {
        // Receive the URL from the Client with multicast

        // Add the URL to the queue of URLs to be downloaded

        try {

            System.setProperty("java.rmi.server.hostname", "192.168.1.92");
            Registry registry = LocateRegistry.createRegistry(1099);
            searchModule = new RMISearchModule();
            registry.rebind("searchmodule", searchModule);

            System.out.println("RMISearchModule is waiting for client to connect");

            this.pagadmin.finalStatus();

            // System.out.println("AQUIIII"+ this.status);

        } catch (RemoteException re) {
            System.out.println("Exception in RMISearchModule.main: " + re);

        }

    }

    public static void main(String[] args) throws MalformedURLException {
        try {
            RMISearchModule searchModule = new RMISearchModule();

            System.setProperty("java.rmi.server.hostname", "192.168.1.68");
            Registry registry = LocateRegistry.createRegistry(1099);
            searchModule = new RMISearchModule();
            registry.rebind("searchmodule", searchModule);

            // searchModule.run(searchModule);

            searchModule.pagadmin = new PagAdministracao(searchModule.words);
            searchModule.pagadmin.finalStatus();
        } catch (RemoteException re) {
            System.out.println("Exception in RMISearchModule.main: " + re);
        }

    }

    @Override
    public ArrayList<String> recebe(ArrayList<String> s) {

        return s;
    }

}
