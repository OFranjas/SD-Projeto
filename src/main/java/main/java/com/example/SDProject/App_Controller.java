package main.java.com.example.SDProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

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

@Controller
public class App_Controller {

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

        return "index";
    }

    @GetMapping("/word")
    public String word(Model model) {

        return "word";
    }

    @GetMapping("/url")
    public String url(Model model) {

        return "url";
    }

}
