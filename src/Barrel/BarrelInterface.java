package Barrel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BarrelInterface extends Remote {

    public ArrayList<String> procuraConteudo(String conteudo)
            throws FileNotFoundException, IOException, RemoteException;

    public ArrayList<String> ligacoesURL(String url) throws FileNotFoundException, IOException, RemoteException;

}
