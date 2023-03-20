import java.util.Scanner;
import java.rmi.*;

// Utilizado pelo utilizador para pesquisar palavras
// Serve para invocar métodos remotos no servidor RMI
// Apenas comunica com o RMISearchModule (porta de entrada)

public class RMIClient {

    public static void main(String[] args) {

        try {

            // Get the URL from the user input
            Scanner sc = new Scanner(System.in);

            System.out.println("Enter the URL: ");

            String url = sc.nextLine();

            // Send the URL to the RMISearchModule

            RMIInterface s = (RMIInterface) Naming.lookup("rmi://localhost:1099/search");

            String a = s.recebe(url);

            System.out.println(a);

            sc.close();

        } catch (Exception e) {
            System.out.println("Exception in RMIClient.main: " + e);
        }

    }
}
