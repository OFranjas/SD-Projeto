import java.util.ArrayList;
import java.util.Scanner;
import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.io.File;

// Utilizado pelo utilizador para pesquisar palavras
// Serve para invocar métodos remotos no servidor RMI
// Apenas comunica com o RMISearchModule (porta de entrada)

public class RMIClient extends UnicastRemoteObject implements ClientInterface {
    // create constructor

    RMIClient() throws RemoteException {
        super();
    }

    // create a function to make a menu
    // made a function to make a menu
    // public static void login(){

    // //ask for username and password

    // String username;
    // String password;

    // Scanner sc = new Scanner(System.in);

    // System.out.println("Escreva o username: ");

    // username = sc.nextLine();

    // System.out.println("Escreva o password: ");

    // password = sc.nextLine();

    // //open a file.txt and check if the username and password are correct

    // //if correct, call the mainMenu function

    // //if not correct, call the login function again

    // File fp = new File("users.txt");

    // }

    public static void printList(ArrayList<String> lista) {
        System.out.println("printList");

        for (int i = 0; i < lista.size(); i++) {
            System.out.println(lista.get(i));
        }

    }

    public static void senderRmi(String string) {

        try {
            // create a RMIClient

            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost/searchmodule");

            System.out.println("String: " + string);

            // split the string if has 2 words

            if (string.length() != 1) {
                String[] words = string.split(" ");

                System.out.println("word0: " + words[0]);
                System.out.println("word1: " + words[1]);

                if (words[0].equals("1")) {
                    server.opcaoUm(words[1]);
                }

                if (words[0].equals("2")) {
                    ArrayList<String> lista = new ArrayList<String>();

                    lista = server.opcaoDois(words[1]);

                    printList(lista);

                    Thread.sleep(5000);
                }

                if (words[0].equals("3")) {
                    // create arraylist of strings´

                    ArrayList<String> lista = new ArrayList<String>();

                    lista = server.opcaoTres(words[1]);

                    printList(lista);

                    Thread.sleep(5000);

                }
            } else {

                if (string.equals("4")) {
                    server.opcaoQuatro(string);
                }

                if (string.equals("5")) {
                    server.opcaoCinco(string);
                }

                if (string.equals("6")) {
                    server.opcaoSeis(string);
                }

                if (string.equals("7")) {
                    server.opcaoSete(string);
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in RMIClient.main: " + e);
            e.printStackTrace();
        }

    }

    public static RMIClient mainMenu() {

        Scanner sc = new Scanner(System.in);
        int option = 0;
        String string;

        while (true) {

            // create a RMIClient

            System.out.println("1 - Indexar novo URL\n");
            System.out.println("2 - Indexar recursivamente todos os URLs encontrados\n");
            System.out.println("3 - Pesquisar páginas que contenham um conjunto de termos\n");
            System.out.println("4 - Resultados de pesquisa ordenados por importância\n");
            System.out.println("5 - Consultar lista de páginas com ligação para uma página específica\n");
            System.out.println("6 - Página de administração atualizada em tempo real\n");
            System.out.println("7 - Particionamento do índice\n");
            System.out.println("8 - Sair\n");

            System.out.println("Choose an option: ");

            option = sc.nextInt();

            switch (option) {

                case 1:
                    // cliente can write the url

                    // Send url by rmi to rmisearchmodule
                    sc.nextLine();

                    string = sc.nextLine();

                    System.out.println("Escreva o url: ");

                    string = option + " " + string;
                    senderRmi(string);

                    break;
                case 2:
                    // Indexar recursivamente todos os URLs encontrados

                    // create String name = "Indexar"

                    // string = option

                    sc.nextLine();

                    System.out.println("Escreva o palavra: ");

                    string = sc.nextLine();

                    string = option + " " + string;

                    senderRmi(string);

                    // send option by rmi to rmisearchmodule

                    break;
                case 3:
                    // Pesquisar páginas que contenham um conjunto de termos

                    sc.nextLine();

                    System.out.println("Escreva o url: ");

                    string = sc.nextLine();

                    // make a string like " option + string"

                    string = option + " " + string;

                    senderRmi(string);

                    break;
                case 4:
                    // Resultados de pesquisa ordenados por importância

                    string = String.valueOf(option);

                    // send option by rmi to rmisearchmodule

                    string = option + " " + string;

                    senderRmi(string);

                    break;
                case 5:
                    // Consultar lista de páginas com ligação para uma página específica

                    string = String.valueOf(option);

                    senderRmi(string);

                    // send option by rmi to rmisearchmodule
                    break;
                case 6:
                    // Página de administração atualizada em tempo real

                    string = String.valueOf(option);

                    senderRmi(string);

                    // send option by rmi to rmisearchmodule
                    break;
                case 7:
                    // Particionamento do índice

                    string = String.valueOf(option);

                    senderRmi(string);

                    // send option by rmi to rmisearchmodule
                    break;
                case 8:
                    // Sair
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

    }

    public static void main(String[] args) {

        // Scanner sc = new Scanner(System.in);
        // int option = 0;

        try {

            // System.out.println("1 - Login\n");
            // System.out.println("2 - Register\n");

            // System.out.println("Choose an option: ");

            // option = sc.nextInt();

            // switch(option){

            // case 1:
            // //login
            // break;
            // case 2:
            // //register
            // break;
            // default:
            // System.out.println("Invalid option");
            // break;
            // }

            // Get the URL from the user input
            while (true) {

                mainMenu();

            }

        } catch (Exception e) {
            System.out.println("Exception in RMIClient.main: " + e);
        }

    }
}
