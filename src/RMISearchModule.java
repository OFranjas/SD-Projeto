import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

// A comunicação com o IndexStorageBarrel é feita usando RMI
// Visível para o utilizador através do RMIClient
// Não armazena dados, logo é dependente dos IndexStorageBarrels
// Escolhe aleatoriamente um IndexStorageBarrel para responder a cada pesquisa

public class RMISearchModule extends UnicastRemoteObject implements ServerInterface {

    static ClientInterface client;

    private int num_threads = 1;

    RMISearchModule() throws RemoteException {
        super();
    }

    public void opcaoUm(String s) throws RemoteException {
        System.out.println("Enviar string para a qeue: " + s);

        // check if the string is a URL

        if (s.contains("http://") || s.contains("https://")) {
            // send the string to the queue
            System.out.println("String is a URL");
        } else {
            System.out.println("String is not a URL, ERROR");
            return;
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
        } catch (IOException e) {
            System.out.println("Exception in Downloader.adicionaURL: " + e);
        }

    }

    public ArrayList<String> opcaoDois(String s) throws RemoteException {
        System.out.println("Opcao2, fazer coisinhas: " + s);

        int num = (int) (Math.random() * num_threads);

        System.out.println("NUM: " + num);

        try {

            BarrelInterface barril = (BarrelInterface) Naming
                    .lookup("IndexStorageBarrel" + num);

            ArrayList<String> res = barril.procuraConteudo(s, 1);

            System.out.println("RES: " + res);

            return res;

        } catch (Exception e) {
            System.out.println("Exception in RMISearchModule.opcaoDois: " + e);
            return null;
        }
    }

    public ArrayList<String> opcaoTres(String s) throws RemoteException {
        System.out.println("Opcao3, fazer coisinhas: " + s);

        int num = (int) (Math.random() * num_threads);

        System.out.println("NUM: " + num);

        try {

            BarrelInterface barril = (BarrelInterface) Naming
                    .lookup("IndexStorageBarrel" + num);

            ArrayList<String> res = barril.ligacoesURL(s);

            System.out.println("RES: " + res);

            return res;
        } catch (Exception e) {
            System.out.println("Exception in RMISearchModule.opcaoTres: " + e);
            return null;
        }

    }

    public void opcaoQuatro(String s) throws RemoteException {
        System.out.println("Opcao4, fazer coisinhas: " + s);
    }

    public void opcaoCinco(String s) throws RemoteException {
        System.out.println("Opcao5, fazer coisinhas: " + s);
    }

    public void opcaoSeis(String s) throws RemoteException {
        System.out.println("Opcao6, fazer coisinhas: " + s);
    }

    public void opcaoSete(String s) throws RemoteException {
        System.out.println("Opcao7, fazer coisinhas: " + s);
    }

    public void getBarrel() throws RemoteException {
        // Get the URL from the queue of URLs to be downloaded

        // Download the URL

        // Send the URL to the IndexStorageBarrel

        // Receive the response from the IndexStorageBarrel

        // Send the response to the Client

    }

    private void run(RMISearchModule searchModule) throws MalformedURLException {
        // Receive the URL from the Client with multicast

        // Add the URL to the queue of URLs to be downloaded

        try {

            LocateRegistry.createRegistry(1099);
            // get string form RMIClient and save in a string

            // Server is waitting for client to connect

            System.out.println("RMISearchModule is waiting for client to connect");

            Naming.rebind("rmi://localhost/searchmodule", searchModule);

            // receive the string from the client

        } catch (RemoteException re) {
            System.out.println("Exception in RMISearchModule.main: " + re);

        }

    }

    public static void main(String[] args) throws MalformedURLException {
        try {
            RMISearchModule searchModule = new RMISearchModule();
            searchModule.run(searchModule);
        } catch (RemoteException re) {
            System.out.println("Exception in RMISearchModule.main: " + re);
        }

    }

    @Override
    public ArrayList<String> recebe(ArrayList<String> s) {

        return s;
    }

}
