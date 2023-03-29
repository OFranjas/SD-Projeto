import java.rmi.*;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {
    public void opcaoUm(String s) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoDois(String s, int pagina) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoTres(String s) throws java.rmi.RemoteException;

    public void opcaoQuatro(String s) throws java.rmi.RemoteException;

    public void opcaoCinco(String s) throws java.rmi.RemoteException;

    public void opcaoSeis(String s) throws java.rmi.RemoteException;

    public void opcaoSete(String s) throws java.rmi.RemoteException;

    public ArrayList<String> recebe(ArrayList<String> s) throws java.rmi.RemoteException;

}