package main.java.com.example.SDProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import main.java.com.example.SDProject.SearchModule.ServerInterface;
import main.java.com.example.SDProject.SearchModule.RMISearchModule;

import java.rmi.server.UnicastRemoteObject;

import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import main.java.com.example.SDProject.Global.Global;

@SpringBootApplication
public class SdProjectApplication {

	@Bean
	public ServerInterface Connect() {

		ServerInterface server = null;

		try {
			Registry registry = LocateRegistry.getRegistry(Global.RMI_IP, Global.RMI_PORT);

			server = (ServerInterface) registry.lookup(Global.RMI_NAME);

			// server = (ServerInterface) Naming
			// .lookup("searchmodule");

		} catch (Exception e) {

			e.printStackTrace();

		}

		return server;

	}

	public static void main(String[] args) {

		SpringApplication.run(SdProjectApplication.class, args);

	}

}
