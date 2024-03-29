package main.java.com.example.SDProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.lang.model.element.Element;

import org.jsoup.select.Selector.SelectorParseException;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.net.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.io.File;
import main.java.com.example.SDProject.Global.Global;
import main.java.com.example.SDProject.SearchModule.ServerInterface;

// Utilizado pelo utilizador para pesquisar palavras
// Serve para invocar métodos remotos no servidor RMI
// Apenas comunica com o RMISearchModule (porta de entrada)

public class RMIClient extends UnicastRemoteObject {

        private boolean ligado;

        private ServerInterface server;

        // create constructor

        RMIClient() throws RemoteException {
                super();
                this.ligado = false;
                this.server = null;
        }

        public void senderRmi(String string) {

                try {

                        int tries = 0;

                        while (ligado == false) {

                                tries++;

                                if (tries > 10) {
                                        System.out.println("Erro na ligação, tentativas esgotadas");
                                        return;
                                }

                                try {
                                        ServerInterface server = (ServerInterface) Naming
                                                        .lookup("rmi://localhost/searchmodule");

                                        if (server != null) {
                                                this.server = server;
                                                // System.out.println("Conectado ao servidor");
                                                ligado = true;
                                        }

                                } catch (Exception e) {
                                        System.out.println("Erro na ligação, tentando novamente (tentativa "
                                                        + tries + ")");
                                        Thread.sleep(1000);
                                }

                        }
                        // create a RMIClient

                        // System.out.println("String: " + string);

                        // split the string if has 2 words

                        if (string.length() != 1) {
                                String[] words = string.split(" ");

                                // System.out.println("word0: " + words[0]);
                                // System.out.println("word1: " + words[1]);

                                if (words[0].equals("1")) {

                                        try {
                                                boolean res = server.opcaoUm(words[1]);

                                                if (res == true) {
                                                        System.out.println("URL enviada com sucesso");
                                                } else {
                                                        System.out.println("Erro ao enviar URL");
                                                }

                                        } catch (Exception e) {

                                                ligado = false;
                                                senderRmi(string);
                                        }

                                }

                                if (words[0].equals("2")) {

                                        int pagina = 1;

                                        int tentativas = 1;

                                        // Store the words from index 1 to the end in a string
                                        String word = "";

                                        for (int i = 1; i < words.length; i++) {
                                                word += words[i] + " ";
                                        }

                                        try {
                                                ArrayList<String> lista = server.opcaoDois(word, pagina, true);

                                                while (lista == null) {

                                                        Thread.sleep(1000);

                                                        tentativas++;

                                                        if (tentativas > 10) {
                                                                System.out.println(
                                                                                "Erro na pesquisa, tentativas esgotadas");
                                                                return;
                                                        }

                                                        System.out.println(
                                                                        "Erro na pesquisa, tentando novamente (tentativa "
                                                                                        + tentativas + ")");

                                                        lista = server.opcaoDois(words[1], tentativas, false);

                                                }

                                                ArrayList<String> printed = new ArrayList<String>();

                                                if (lista.size() == 0) {
                                                        System.out.println("Não foram encontrados resultados");
                                                        return;
                                                }

                                                while (true) {

                                                        for (int i = 0; i < 10; i++) {

                                                                if (i >= lista.size()) {

                                                                        break;

                                                                }

                                                                String element = lista.get(i);

                                                                printed.add(element);

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
                                                                                "|                                        "
                                                                                                + title
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

                                                        System.out.println("Página: " + pagina);

                                                        // Remove the elements that were already printed
                                                        lista.removeAll(printed);

                                                        // Check if there are more elements to print
                                                        if (lista.size() == 0) {
                                                                break;
                                                        }

                                                        System.out.println("Próxima página? (s/n)");

                                                        Scanner sc = new Scanner(System.in);

                                                        String next = sc.nextLine();

                                                        if (next.equals("s")) {
                                                                pagina++;
                                                        } else {
                                                                break;
                                                        }

                                                }

                                        } catch (Exception e) {

                                                // Try to reconnect
                                                ligado = false;
                                                senderRmi(string);

                                        }

                                        // Thread.sleep(5000);
                                }

                                if (words[0].equals("3")) {
                                        // create arraylist of strings´

                                        ArrayList<String> lista = server.opcaoTres(words[1]);

                                        int tentativas = 1;

                                        while (lista == null) {

                                                Thread.sleep(1000);

                                                tentativas++;

                                                if (tentativas > 10) {
                                                        System.out.println(
                                                                        "Erro na pesquisa, tentativas esgotadas");
                                                        return;
                                                }

                                                System.out.println(
                                                                "Erro na pesquisa, tentando novamente (tentativa "
                                                                                + tentativas + ")");

                                                lista = server.opcaoTres(words[1]);

                                        }

                                        if (lista.size() == 0) {
                                                System.out.println("Não foram encontrados resultados");
                                                return;
                                        }

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

                                        String status = server.getStatus();

                                        if (status == null) {
                                                System.out.println("Erro ao obter status");
                                                return;
                                        }

                                        // System.out.println("OPCAO 4");
                                        System.out.println(
                                                        " ________________________________________________________________________________");
                                        System.out.println(
                                                        "|                             Status do sistema                                  |");
                                        System.out.println(
                                                        "|________________________________________________________________________________|");
                                        System.out.println(
                                                        "|                                                                                |");
                                        System.out.println(
                                                        "|                                                                                |");

                                        // GET BARRELS/DOWNLOADERS ONLINE AND HOW MANY
                                        System.out.println("| IP in use between Downloaders and Barrels: "
                                                        + Global.MULTICAST_ADRESS + "                           |");
                                        System.out.println("| Port in use between Downloaders and Barrels: "
                                                        + Global.MULTICAST_PORT + "                              |");

                                        System.out.println(
                                                        "|                                                                                |");
                                        System.out.println(
                                                        "|________________________________________________________________________________|");
                                        System.out.println(
                                                        "|                                                                                |");
                                        System.out.println(
                                                        "|                                                                                |");

                                        System.out.println(status);

                                        System.out.println(
                                                        "|                                                                                |");
                                        System.out.println(
                                                        "|________________________________________________________________________________|");
                                        System.out.println(
                                                        "|                                                                                |");
                                        HashMap<String, Integer> lista = new HashMap<String, Integer>();
                                        lista = server.opcaoQuatro();
                                        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
                                                        lista.entrySet());

                                        // If the list is empty, return
                                        if (list.size() == 0) {
                                                System.out.println("Não foram realizadas pesquisas");
                                                return;
                                        }
                                        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                                public int compare(Map.Entry<String, Integer> o1,
                                                                Map.Entry<String, Integer> o2) {
                                                        return (o2.getValue()).compareTo(o1.getValue());
                                                }
                                        });
                                        System.out.println("| Palavras mais pesquisadas: ");

                                        // for (Map.Entry<String, Integer> entry : list) {
                                        // System.out.println(entry.getKey() + " = " + entry.getValue());
                                        // }
                                        for (int i = 0; i < 10; i++) {

                                                if (i >= list.size()) {

                                                        break;

                                                }

                                                System.out.println(
                                                                "| |" + (i + 1) + "º| -> " + list.get(i).getKey() + ": "
                                                                                + list.get(i).getValue());
                                        }
                                        System.out.println(
                                                        "|________________________________________________________________________________|");

                                }

                        }

                } catch (Exception e) {
                        System.out.println("Exception in RMIClient.main: " + e);
                        e.printStackTrace();
                }

        }

        public void mainMenu() {

                try {

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
                                                        "|      3. Pesquisar por URL  " + a
                                                                        + "                                                                                                    ");
                                        System.out.println(
                                                        "|      4. Página de administração                                                                                                   |");
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
                                                        "|      4. Página de administração                                                                                                   |");
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
                                                this.senderRmi(string);

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

                                        case 4:

                                                // transform option to string
                                                string = Integer.toString(option);
                                                senderRmi(string);

                                                System.out.println(" Insira qualquer coisa para continuar...");
                                                sc.nextLine();
                                                sc.nextLine();
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

                                                if ((user.equals("aluno") && pass.equals("password"))
                                                                || (user.equals("rafa") && pass.equals("tacho"))
                                                                || (user.equals("jose") && pass.equals("fialho"))) {
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
                                                        System.out.println("|             Username: |_______________"
                                                                        + user
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
                                                        System.out.println("|             Username: |_______________"
                                                                        + user
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

                } catch (InputMismatchException e) {
                        System.out.println("Invalid option");

                        this.mainMenu();
                }

        }

        private void run() {

                // Get the URL from the user input
                while (true) {

                        this.mainMenu();

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

                        RMIClient client = new RMIClient();
                        client.run();

                } catch (Exception e) {
                        System.out.println("Exception in RMIClient.main: " + e);
                }

        }
}
