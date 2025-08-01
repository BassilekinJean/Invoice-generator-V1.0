package com.bassilekin.report_generator.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Une erreur inattendue s'est produite.";
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Default to 500

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());
            httpStatus = HttpStatus.resolve(statusCode); // Resolve HTTP status
            if (httpStatus == null) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR; // Default to 500 if unresolved
            }

            if (httpStatus == HttpStatus.NOT_FOUND) { // 404
                errorMessage = "La page que vous recherchez n'a pas été trouvée.";
            } else if (httpStatus == HttpStatus.FORBIDDEN) { // 403
                errorMessage = "Accès refusé. Vous n'avez pas les permissions nécessaires.";
            } else if (httpStatus == HttpStatus.BAD_REQUEST) { // 400
                errorMessage = "Votre requête est invalide.";
            }
        }

        model.addAttribute("statusCode", httpStatus.value());
        model.addAttribute("error", httpStatus.getReasonPhrase());
        model.addAttribute("message", errorMessage);
        model.addAttribute("timestamp", new java.util.Date());

        Throwable exception = (Throwable) request.getAttribute(jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION);
        if (exception != null) {
            model.addAttribute("exceptionMessage", exception.getMessage());
        }

        return "error.html";
    }

    public String getErrorPath() {
        return "/error"; 
    }
}
