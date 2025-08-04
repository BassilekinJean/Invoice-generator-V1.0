package com.bassilekin.report_generator.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping("/login.html")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register.html")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping({"/index.html", "/"})
    public String showDashboard(Model model) {
        // Vous pouvez ajouter des données au modèle ici si besoin
        model.addAttribute("message", "Bienvenue sur votre tableau de bord !");
        return "index";
    }
}