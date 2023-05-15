package main.java.com.example.SDProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.java.com.example.SDProject.SearchModule.ServerInterface;

@SpringBootApplication
public class SdProjectApplication {

	private ServerInterface server;

	public static void main(String[] args) {

		SpringApplication.run(SdProjectApplication.class, args);
	}

}
