package com.bassilekin.report_generator.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }

    @GetMapping({"/dashboard"})
    public String showDashboard(Model model) {

        model.addAttribute("message", "Bienvenue sur votre tableau de bord !");
        return "index";
    }
}