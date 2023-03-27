import java.rmi.*;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    public void opcaoUm(String s) throws java.rmi.RemoteException;

    public void opcaoDois(String s) throws java.rmi.RemoteException;

    public void opcaoTres(String s) throws java.rmi.RemoteException;

    public void opcaoQuatro(String s) throws java.rmi.RemoteException;

    public void opcaoCinco(String s) throws java.rmi.RemoteException;

    public void opcaoSeis(String s) throws java.rmi.RemoteException;

    public void opcaoSete(String s) throws java.rmi.RemoteException;

}