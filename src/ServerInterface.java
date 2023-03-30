import java.rmi.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ServerInterface extends Remote {
    public boolean opcaoUm(String s) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoDois(String s, int tentativas) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoTres(String s) throws java.rmi.RemoteException;

    public HashMap<String, Integer> opcaoQuatro() throws java.rmi.RemoteException;

    public String opcaoQuatroAgain() throws RemoteException;

    public ArrayList<String> recebe(ArrayList<String> s) throws java.rmi.RemoteException;

}