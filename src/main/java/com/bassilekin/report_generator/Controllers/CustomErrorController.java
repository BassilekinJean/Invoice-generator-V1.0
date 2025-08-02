package com.bassilekin.report_generator.Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping(value = "/error", method = {
        RequestMethod.GET,
        RequestMethod.POST
    })
    public String handleError(HttpServletRequest request, Model model) {
        
        // Récupère le code d'erreur du serveur
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String statusCode = "500";
        String errorMessage = "Une erreur inconnue s'est produite.";
        String errorTitle = "Erreur serveur";
        
        if (status != null) {
            int httpStatusCode = Integer.parseInt(status.toString());
            statusCode = String.valueOf(httpStatusCode);
            
            switch (httpStatusCode) {
                case 404:
                    errorTitle = "Page introuvable";
                    errorMessage = "La page que vous recherchez n'existe pas.";
                    break;
                case 403:
                    errorTitle = "Accès interdit";
                    errorMessage = "Vous n'avez pas la permission d'accéder à cette ressource.";
                    break;
                case 500:
                    errorTitle = "Erreur interne du serveur";
                    errorMessage = "Une erreur interne s'est produite. Veuillez réessayer plus tard.";
                    break;
                default:
                    errorTitle = "Erreur " + statusCode;
                    errorMessage = "Une erreur serveur s'est produite.";
                    break;
            }
        }
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        
        // Renvoie le nom de la vue, c'est-à-dire le fichier error.html
        return "error";
    }
}