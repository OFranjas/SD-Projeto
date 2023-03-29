import java.util.ArrayList;
import java.util.Scanner;

import javax.lang.model.element.Element;

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

    public static void senderRmi(String string) {

        try {
            // create a RMIClient

            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost/searchmodule");

            // System.out.println("String: " + string);

            // split the string if has 2 words

            if (string.length() != 1) {
                String[] words = string.split(" ");

                // System.out.println("word0: " + words[0]);
                // System.out.println("word1: " + words[1]);

                if (words[0].equals("1")) {
                    server.opcaoUm(words[1]);
                }

                if (words[0].equals("2")) {
                    ArrayList<String> lista = new ArrayList<String>();

                    lista = server.opcaoDois(words[1]);

                    // System.out.println(" OPCAO 2 ");

                    for (int i = 0; i < lista.size(); i++) {

                        String element = lista.get(i);

                        // Separate the string by the |
                        String[] res = element.split("\\|");

                        String url = res[0];
                        String title = res[1];
                        String description = res[2];

                        System.out
                                .println(
                                        " _____________________________________________________________________________________");
                        System.out
                                .println(
                                        "|                                                                                     |");
                        System.out
                                .println(
                                        "|                                                                                     |");
                        System.out.println(
                                "|                                        " + title
                                        + "                                                         ");
                        System.out
                                .println(
                                        "|                                                                                     |");
                        System.out.println(
                                "|                     " + url
                                        + "                                                         ");
                        System.out
                                .println(
                                        "|                                                                                     |");
                        System.out.println("|                   " + description
                                + "                                                         ");
                        System.out
                                .println(
                                        "|                                                                                     |");
                        System.out
                                .println(
                                        "|____________________________________________________________________________________|");
                    }

                    // Thread.sleep(5000);
                }

                if (words[0].equals("3")) {
                    // create arraylist of strings´

                    ArrayList<String> lista = new ArrayList<String>();

                    lista = server.opcaoTres(words[1]);
                    // System.out.println(" OPCAO 3 ");

                    System.out
                            .println(
                                    " _____________________________________________________________________________________");
                    System.out
                            .println(
                                    "|                                  LINKS                                              |");
                    System.out
                            .println(
                                    "|_____________________________________________________________________________________|");
                    System.out
                            .println(
                                    "|                                                                                     |");
                    System.out
                            .println(
                                    "|                                                                                     |");

                    for (int i = 0; i < lista.size(); i++) {

                        String element = lista.get(i);

                        System.out
                                .println("|              " + element
                                        + "                                                                 ");
                    }

                    System.out.println(
                            "|                                                                                     |");
                    System.out.println(
                            "|_____________________________________________________________________________________|");

                    // Thread.sleep(5000);

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
        boolean login = false;
        boolean aux = false;
        String string;

        while (true) {
            String a = "";
            // create a RMIClient
            if (login == false) {
                a = "(Login necessário)";
            }
            if (aux == false) {
                System.out.println(
                        " ___________________________________________________________________________________________________________________________________");
                System.out.println(
                        "|        Googol         |   _|_                                                                                          --  |_|  X |");
                System.out.println(
                        "| ______________________|____|______________________________________________________________________________________________________|");
                System.out.println(
                        "|                                         _____    ____   ____   _____   ____                                                       |");
                System.out.println(
                        "|                                        |        |    | |    | |       |    | |                                                    |");
                System.out.println(
                        "|                                        |   __   |    | |    | |   __  |    | |                                                    |");
                System.out.println(
                        "|                                        |_____|  |____| |____| |_____| |____| |____                                                |");
                System.out.println(
                        "|                               _______________________________________________________________                                     |");
                System.out.println(
                        "|                              /  _                                                        __  |                                    |");
                System.out.println(
                        "|                              | |_|                                                      |__| |                                    |");
                System.out.println(
                        "|                              |__|_______________________________________________________/___/                                     |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      Escolha uma das seguntes opções:                                                                                             |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      1. Indexar novo URL                                                                                                          |");
                System.out.println(
                        "|      2. Pesquisar por uma palavra aou conjunto de palavras                                                                        |");
                System.out.println(
                          "|      3. Pesquisar por URL  " + a + "                                                                                                    ");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      8. Registar  ( Indisponivel )                                                                                                |");
                System.out.println(
                        "|      9. Login                                                                                                                     |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      0. Sair                                                                                                                      |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|___________________________________________________________________________________________________________________________________|");

            } else if (aux == true) {
                System.out.println(
                        " ___________________________________________________________________________________________________________________________________");
                System.out.println(
                        "|        Googol         |   _|_                                                                                          --  |_|  X |");
                System.out.println(
                        "| ______________________|____|______________________________________________________________________________________________________|");
                System.out.println(
                        "|                                         _____    ____   ____   _____   ____                                                       |");
                System.out.println(
                        "|                                        |        |    | |    | |       |    | |                                                    |");
                System.out.println(
                        "|                                        |   __   |    | |    | |   __  |    | |                                                    |");
                System.out.println(
                        "|                                        |_____|  |____| |____| |_____| |____| |____                                                |");
                System.out.println(
                        "|                               _______________________________________________________________                                     |");
                System.out.println(
                        "|                              /  _                                                        __  |                                    |");
                System.out.println(
                        "|                              | |_|                                                      |__| |                                    |");
                System.out.println(
                        "|                              |__|_______________________________________________________/___/                                     |");
                System.out.println(
                        "|                                                     INVALID OPTION                                                                |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      Escolha uma das seguintes opções                                                                                             |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      1. Indexar novo URL                                                                                                          |");
                System.out.println(
                        "|      2. Pesquisar por uma palavar aou conjunto de palavras                                                                        |");
                System.out.println(
                        "|      3. Pesquisar por URL  " + a
                                + "                                                                                                    ");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      8. Registar  ( Indisponivel )                                                                                                |");
                System.out.println(
                        "|      9. Login                                                                                                                     |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|      0. Sair                                                                                                                      |");
                System.out.println(
                        "|                                                                                                                                   |");
                System.out.println(
                        "|___________________________________________________________________________________________________________________________________|");
                aux = false;
            }

            System.out.print("\nOpçao : ");

            option = sc.nextInt();

            switch (option) {

                case 1:
                    // cliente can write the url

                    // Send url by rmi to rmisearchmodule
                    sc.nextLine();
                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                               _______________________________________________________________                                     |");
                    System.out.println(
                            "|                              /  _                                                        __  |                                    |");
                    System.out.println(
                            "|                              | |_|                                                      |__| |                                    |");
                    System.out.println(
                            "|                              |__|_______________________________________________________/___/                                     |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    string = sc.nextLine();

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                               _______________________________________________________________                                     |");
                    System.out.println(
                            "|                              /  _                                                        __  |                                    |");
                    System.out.println(
                            "|                              | |_|                                                      |__| |                                    |");
                    System.out.println(
                            "|                              |  |                                                       /   /                                     |");
                    System.out.println("|                                         " + string
                            + "                                                                                     ");
                    System.out.println(
                            "|                              |_______________________________________________________________|                                    |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    string = option + " " + string;
                    senderRmi(string);

                    System.out.println(" Insira qualquer coisa para continuar...");
                    sc.nextLine();

                    break;

                case 2:
                    // Pesquisar páginas que contenham um conjunto de termos

                    sc.nextLine();

                    System.out.print("Escreva o palavra: ");

                    string = sc.nextLine();

                    // make a string like " option + string"

                    string = option + " " + string;

                    senderRmi(string);

                    System.out.println(" Insira qualquer coisa para continuar...");
                    sc.nextLine();

                    break;
                case 3:
                    // Consultar lista de páginas com ligação para uma página específica
                    if (login == false) {
                        System.out.println(
                                " ___________________________________________________________________________________________________________________________________");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                                            Login necessário para aceder a esta opçâo                                              |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                                              Insira qualquer coisa para continuar...                                              |");
                        System.out.println(
                                "|___________________________________________________________________________________________________________________________________|");
                        sc.nextLine();
                        sc.nextLine();
                        break;
                    }

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                               _______________________________________________________________                                     |");
                    System.out.println(
                            "|                              /  _                                                        __  |                                    |");
                    System.out.println(
                            "|                              | |_|                                                      |__| |                                    |");
                    System.out.println(
                            "|                              |__|_______________________________________________________/___/                                     |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    sc.nextLine();
                    string = sc.nextLine();

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                               _______________________________________________________________                                     |");
                    System.out.println(
                            "|                              /  _                                                        __  |                                    |");
                    System.out.println(
                            "|                              | |_|                                                      |__| |                                    |");
                    System.out.println(
                            "|                              |  |                                                       /   /                                     |");
                    System.out.println("|                                         " + string
                            + "                                                                                     ");
                    System.out.println(
                            "|                              |_______________________________________________________________|                                    |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    string = option + " " + string;

                    senderRmi(string);

                    System.out.println(" Insira qualquer coisa para continuar...");
                    sc.nextLine();

                    // send option by rmi to rmisearchmodule
                    break;
                case 8:
                    // Registar
                    System.out.println("Nao sabes ler?");
                    break;

                case 9:
                    // Login

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                      L O G I N                                                                    |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println(
                            "|             Username: |____________________________________|                                                                      |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println(
                            "|             Password: |____________________________________|                                                                      |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    sc.nextLine();

                    String user = sc.nextLine();

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                      L O G I N                                                                    |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println("|             Username: |_______________" + user
                            + "________________|                                                                      |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println(
                            "|             Password: |____________________________________|                                                                      |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    String pass = sc.nextLine();

                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                      L O G I N                                                                    |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println("|             Username: |_______________" + user
                            + "________________|                                                                      |");
                    System.out.println(
                            "|                        ____________________________________                                                                       |");
                    System.out.println(
                            "|             Password: |______________........______________|                                                                      |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|___________________________________________________________________________________________________________________________________|");

                    if (user.equals("aluno") && pass.equals("password")) {
                        System.out.println(
                                " ___________________________________________________________________________________________________________________________________");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                                                      L O G I N                                                                    |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                        ____________________________________                                                                       |");
                        System.out.println("|             Username: |_______________" + user
                                + "________________|                                                                      |");
                        System.out.println(
                                "|                        ____________________________________                                                                       |");
                        System.out.println(
                                "|             Password: |______________........______________|                                                                      |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|             Login Successful!!!                                                                                                   |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|             Insira qualquer coisa para continuar...                                                                               |");
                        System.out.println(
                                "|___________________________________________________________________________________________________________________________________|");
                        sc.nextLine();
                        login = true;
                    } else {

                        System.out.println(
                                " ___________________________________________________________________________________________________________________________________");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                                                      L O G I N                                                                    |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|                        ____________________________________                                                                       |");
                        System.out.println("|             Username: |_______________" + user
                                + "________________|                                                                      |");
                        System.out.println(
                                "|                        ____________________________________                                                                       |");
                        System.out.println(
                                "|             Password: |______________........______________|                                                                      |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|             Invalid Credentials!!!                                                                                                |");
                        System.out.println(
                                "|                                                                                                                                   |");
                        System.out.println(
                                "|             Insira qualquer coisa para continuar...                                                                               |");
                        System.out.println(
                                "|___________________________________________________________________________________________________________________________________|");
                        sc.nextLine();
                    }
                    break;
                case 0:
                    // Sair
                    System.out.println(
                            " ___________________________________________________________________________________________________________________________________");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                               _______   ______   ______   ____     _____           ______                                       |");
                    System.out.println(
                            "|                              |         |      | |      | |     \\  |     |   \\   / |                                             |");
                    System.out.println(
                            "|                              |   ____  |      | |      | |     |  |____/     \\ /  |____                                         |");
                    System.out.println(
                            "|                              |      |  |      | |      | |     /  |     \\     |   |                                             |");
                    System.out.println(
                            "|                              |______|  |______| |______| |____/   |_____|     |   |______                                       |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "|                                                                                                                                   |");
                    System.out.println(
                            "____________________________________________________________________________________________________________________________________|");

                    System.exit(0);
                    break;
                default:
                    aux = true;
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
