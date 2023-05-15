package main.java.com.example.SDProject;

import java.util.ArrayList;
import java.util.Collections;
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

@Controller
public class App_Controller {

    private ServerInterface server;

    @Autowired
    public App_Controller(ServerInterface server) {
        this.server = server;
    }

    @GetMapping("/menu")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        model.addAttribute("othername", "SD");
        return "menu";
    }

    @GetMapping("/login")
    public String login(Model model) {
        // model.addAttribute("user", new User());
        return "login";
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

        System.out.println("Input Text: " + inputText);

        if (inputText != null) {

            int pagina = 1;

            int tentativas = 1;

            try {
                ArrayList<String> lista = server.opcaoDois(inputText, pagina);

                while (lista == null) {

                    Thread.sleep(1000);

                    if (tentativas == 10) {
                        System.out.println(
                                "Erro na pesquisa, tentativas esgotadas");

                        return "error";
                    }

                    tentativas++;

                    System.out.println(
                            "Erro na pesquisa, tentando novamente (tentativa "
                                    + tentativas + ")");

                    lista = server.opcaoDois(inputText, pagina);

                }

                if (lista.size() == 0) {
                    System.out.println(
                            "Erro na pesquisa, lista vazia");

                    return "error";

                }

                ArrayList<String> printed = new ArrayList<String>();

                while (true) {

                    for (int i = 0; i < 10; i++) {

                        if (i >= lista.size()) {

                            break;

                        }

                        String element = lista.get(i);

                        printed.add(element);

                        // Separate the string by the |
                        String[] res = element.split("\\|");

                        String url = res[0];
                        String title = res[1];
                        String description = res[2];

                        model.addAttribute("element" + i, element);

                    }

                    lista.removeAll(printed);

                    // Check if there are more elements to print
                    if (lista.size() == 0) {
                        break;
                    }

                    // If next button was pressed
                    

                }

                model.addAttribute("element1", lista);

            } catch (Exception e) {
                System.out.println("Exception in App_Controller.word: " + e);
            }

        }

        return "word";
    }

    @GetMapping("/url")
    public String url(Model model) {

        return "url";
    }

    @GetMapping("/error")
    public String error(Model model) {

        return "error";
    }

}
