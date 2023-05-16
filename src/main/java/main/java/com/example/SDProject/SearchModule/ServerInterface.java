package main.java.com.example.SDProject.SearchModule;

import java.rmi.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.rmi.ConnectException;

public interface ServerInterface extends Remote {
    public boolean opcaoUm(String s) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoDois(String s, int tentativas, Boolean First) throws java.rmi.RemoteException;

    public ArrayList<String> opcaoTres(String s) throws java.rmi.RemoteException;

    public HashMap<String, Integer> opcaoQuatro() throws java.rmi.RemoteException;

    public String getStatus() throws RemoteException;

    public ArrayList<String> recebe(ArrayList<String> s) throws java.rmi.RemoteException;

}