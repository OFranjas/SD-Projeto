import java.rmi.*;
import java.rmi.server.*;

public class RMIImpl extends UnicastRemoteObject implements RMIInterface {

    public RMIImpl() throws RemoteException {
    };

    public String recebe(String url) throws RemoteException {

        // Send the URL to the FIFO queue

        // Get the index from the Barrel

        // Return the index to the Client

        System.out.println("Recebi: " + url);

        // Send the url to the Queue

        return "deu";
    }

}
