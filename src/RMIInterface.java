import java.rmi.*;

public interface RMIInterface extends Remote {
    public String recebe(String url) throws java.rmi.RemoteException;
}
