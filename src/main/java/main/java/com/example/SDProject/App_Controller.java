package main.java.com.example.SDProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import main.java.com.example.SDProject.SearchModule.ServerInterface;

import java.io.FileWriter;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;

import org.springframework.web.util.HtmlUtils;

import main.java.com.example.SDProject.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class App_Controller {

    private ServerInterface server;

    private boolean logged_in = false;
    private String username;

    @Autowired
    public App_Controller(ServerInterface server) {
        this.server = server;

    }

    @GetMapping("/menu")
    public String menu(Model model) {

        if (this.logged_in) {
            String aux = "Logged in as " + this.username;

            // System.out.println(aux);

            model.addAttribute("user", aux);
        } else {

            model.addAttribute("user", "Not logged in");
        }

        return "menu";
    }

    @GetMapping("/register")
    public String register(Model model) {
        // model.addAttribute("user", new User());

        try {
            String username = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("name");
            String password = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("password");

            // print the username and password
            // System.out.println("Username: " + username);
            // System.out.println("Password: " + password);

            if (username == null || password == null) {
                return "register";
            }

            File file = new File("src\\main\\java\\main\\java\\com\\example\\SDProject\\login.txt");

            file.createNewFile();

            // read from login file and check if username exists
            StringBuilder sb = new StringBuilder(); // create a StringBuilder to store the contents of the file
            BufferedReader reader = new BufferedReader(
                    new FileReader("src\\main\\java\\main\\java\\com\\example\\SDProject\\login.txt")); // create a
                                                                                                        // BufferedReader
                                                                                                        // to read
                                                                                                        // the
            // file

            String line; // create a String to store each line of the file

            while ((line = reader.readLine()) != null) { // read each line of the file
                // System.out.println("line: " + line);
                sb.append(line); // add the line to the StringBuilder
                sb.append("\n"); // add a newline character
            }

            reader.close(); // close the BufferedReader

            String fileContents = sb.toString(); // convert the StringBuilder to a String

            // System.out.println("fileContents: " + fileContents);

            String[] lines = fileContents.split("\n"); // split the String into an array of Strings, one for each
                                                       // line

            // go through the array of lines and check if the username exists

            boolean userExists = false;

            for (int i = 0; i < lines.length; i++) {
                String[] user = lines[i].split(";"); // split the line into an array of Strings, one for each word

                if (user[0].equals(username)) { // check if the first word is the username

                    // System.out.println("User already exists!");
                    model.addAttribute("User already exists!", "User already exists!");
                    userExists = true;
                    break;
                }
            }

            // if the username doesn't exist, add it to the file like this:
            // username;password

            if (!userExists) {
                FileWriter writer = new FileWriter(
                        "src\\main\\java\\main\\java\\com\\example\\SDProject\\login.txt",
                        true); // create a FileWriter to write to the file

                writer.write(username + ";" + password + "\n"); // write the username and password to the file

                model.addAttribute("user", "Not logged in");

                writer.close(); // close the FileWriter

                return "menu";
            }

            return "register";

        } catch (Exception e) {
            return "register";
        }

    }

    @GetMapping("/login")
    public String login(Model model) {

        try {
            // model.addAttribute("user", new User());
            String username = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("username");
            String password = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("password");

            if (username == null || password == null) {
                return "login";
            }

            // go through the file and check if the username exists and if the password is
            // correct

            File file = new File("src\\main\\java\\main\\java\\com\\example\\SDProject\\login.txt");

            file.createNewFile();

            StringBuilder sb = new StringBuilder(); // create a StringBuilder to store the contents of the file

            BufferedReader reader = new BufferedReader(
                    new FileReader("src\\main\\java\\main\\java\\com\\example\\SDProject\\login.txt")); // create a
                                                                                                        // BufferedReader
                                                                                                        // to
                                                                                                        // read the file

            String line; // create a String to store each line of the file

            while ((line = reader.readLine()) != null) { // read each line of the file
                // System.out.println("line: " + line);
                sb.append(line); // add the line to the StringBuilder
                sb.append("\n"); // add a newline character
            }

            reader.close(); // close the BufferedReader

            String fileContents = sb.toString(); // convert the StringBuilder to a String

            // System.out.println("fileContents: " + fileContents);

            String[] lines = fileContents.split("\n"); // split the String into an array of Strings, one for each line

            boolean userExists = false;

            for (int i = 0; i < lines.length; i++) {
                String[] user = lines[i].split(";"); // split the line into an array of Strings, one for each word

                if (user[0].equals(username)) { // check if the first word is the username
                    // System.out.println("User exists!");
                    userExists = true;

                    if (user[1].equals(password)) { // check if the second word is the password
                        // System.out.println("Password is correct!");
                        this.logged_in = true;
                        this.username = username;

                        String aux = "Logged in as " + this.username;

                        // System.out.println(aux);

                        model.addAttribute("user", aux);
                        return "menu";
                    } else {
                        // System.out.println("Password is incorrect!");
                        return "login";
                    }
                }
            }

            if (!userExists) {
                // System.out.println("User doesn't exist!");
                model.addAttribute("error", "User doesn't exist!");
            }

            return "login";
        } catch (Exception e) {
            return "login";
        }

    }

    @GetMapping("/index")
    public String index(Model model) {

        // Get the URL from the link "?link=..."
        String link = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                .getParameter("link");

        if (link != null) {

            try {
                boolean success = server.opcaoUm(link);

                String result;

                if (success) {
                    result = link + " was indexed successfully!";

                    model.addAttribute("success", result);
                } else {
                    result = link + " couldn't be indexed!";

                    model.addAttribute("success", result);

                }

            } catch (Exception e) {
                System.out.println("Exception in App_Controller.index: " + e);
            }

        }

        return "index";
    }

    @GetMapping("/word")
    public String word(Model model) {

        // Get the word or phrase from the link "?inputText=...+..."
        String inputText = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                .getParameter("inputText");

        // System.out.println("Input Text: " + inputText);

        if (inputText != null) {

            int pagina = 1;

            int tentativas = 1;

            try {
                ArrayList<String> lista = server.opcaoDois(inputText, pagina);

                while (lista == null) {

                    Thread.sleep(1000);

                    if (tentativas == 10) {

                        model.addAttribute("error", "Erro na pesquisa, tentativas esgotadas");

                        return "error";
                    }

                    tentativas++;

                    // System.out.println(
                    // "Erro na pesquisa, tentando novamente (tentativa "
                    // + tentativas + ")");

                    lista = server.opcaoDois(inputText, pagina);

                }

                if (lista.size() == 0) {

                    model.addAttribute("error", "Erro na pesquisa, lista vazia");

                    return "error";

                }

                model.addAttribute("lista", lista);

            } catch (Exception e) {
                System.out.println("Exception in App_Controller.word: " + e);
            }

        }

        return "word";
    }

    @GetMapping("/url")
    public String url(Model model) {

        if (!this.logged_in) {

            model.addAttribute("error", "You must be logged in to access this page!");

            return "error";
        }

        try {

            String link = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("link");

            // System.out.println("Link: " + link);

            if (link != null) {

                int tentativas = 1;

                ArrayList<String> lista = server.opcaoTres(link);

                while (lista == null) {

                    Thread.sleep(1000);

                    tentativas++;

                    if (tentativas > 10) {

                        model.addAttribute("error", "Erro na pesquisa, tentativas esgotadas");

                        return "error";
                    }

                    // System.out.println(
                    // "Erro na pesquisa, tentando novamente (tentativa "
                    // + tentativas + ")");

                    lista = server.opcaoTres(link);

                }

                if (lista.size() == 0) {

                    model.addAttribute("error", "Não foram encontrados resultados");

                    return "error";
                }

                // for (int i = 0; i < lista.size(); i++) {

                // String element = lista.get(i);

                // // System.out.println(element);
                // }

                model.addAttribute("lista", lista);

            }

        } catch (Exception e) {
            System.out.println("Exception in App_Controller.url: " + e);
        }

        return "url";
    }

    @GetMapping("/error")
    public String error(Model model) {

        return "error";
    }

    @GetMapping("/logout")
    public String logout(Model model) {

        this.logged_in = false;

        model.addAttribute("user", "Not logged in");

        return "menu";
    }

    @GetMapping("/userstories")
    public String UserStories(Model model) {

        if (!this.logged_in) {

            model.addAttribute("error", "You must be logged in to access this page!");

            return "error";
        }

        try {

            String id = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getParameter("id");

            if (id != null) {

                // System.out.println("ID: " + id);

                String[] urls = apirest.userStories(id);

                if (urls == null) {

                    model.addAttribute("error", "User doesn't exist!");
                    return "error";
                }

                for (int i = 0; i < urls.length; i++) {

                    if (urls[i] == null) {
                        continue;
                    }

                    boolean success = server.opcaoUm(urls[i]);

                    if (!success) {

                        model.addAttribute("error", urls[i] + " couldn't be indexed!");

                        return "error";
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("Exception in App_Controller.userstories: " + e);
        }

        return "userstories";
    }

    @GetMapping("/adminpage")
    public String Adminpage(Model model) {

        return "adminpage";
    }

    @GetMapping("/topstories")
    public String TopStories(Model model) {

        if (!this.logged_in) {

            model.addAttribute("error", "You must be logged in to access this page!");

            return "error";
        }

        try {

            String[] urls = apirest.topStories();

            if (urls == null) {

                model.addAttribute("error", "Error getting top stories!");

                return "error";
            }

            for (int i = 0; i < urls.length; i++) {

                if (urls[i] == null) {
                    continue;
                }

                boolean success = server.opcaoUm(urls[i]);

                if (!success) {

                    model.addAttribute("error", urls[i] + " couldn't be indexed!");

                    return "error";
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in App_Controller.topstories: " + e);
        }

        return "menu";

    }

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public Message onMessage(Message message) {

        try {

            Thread.sleep(1000);

            Message res = new Message(server.opcaoQuatroAgain());

            HashMap<String, Integer> words = server.opcaoQuatro();

            // Add the words in res text

            String text = res.getText();

            for (String word : words.keySet()) {

                text += word + " " + words.get(word) + "\n";
            }

            res.setText(text);

            // System.out.println("words: " + words);

            // model.addAttribute("words", words);

            // System.out.println(res.getText());

            return res;

        } catch (Exception e) {
            System.out.println("Exception in App_Controller.onMessage: " + e);
            return null;
        }

    }

}
