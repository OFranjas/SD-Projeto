import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;

// A comunicação com o IndexStorageBarrel é feita usando RMI
// Visível para o utilizador através do RMIClient
// Não armazena dados, logo é dependente dos IndexStorageBarrels
// Escolhe aleatoriamente um IndexStorageBarrel para responder a cada pesquisa

public class RMISearchModule {

    public static void main(String[] args) {

        // Receive the URL from the Client with multicast

        // Add the URL to the queue of URLs to be downloaded

        try {

            RMIImpl s = new RMIImpl();

            LocateRegistry.createRegistry(1099).rebind("search", s);
            System.out.println("Search Module ready.");

        } catch (RemoteException re) {
            System.out.println("Exception in RMISearchModule.main: " + re);
        }

    }

}
