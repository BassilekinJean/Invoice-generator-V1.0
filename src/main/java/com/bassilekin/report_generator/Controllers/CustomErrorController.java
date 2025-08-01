package com.bassilekin.report_generator.Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class CustomErrorController {
    
    @RequestMapping(value = "/error", method = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE
    })
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = 500; // Par défaut, erreur interne du serveur
        String message = "Une erreur inattendue s'est produite.";

        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
            HttpStatus httpStatus = HttpStatus.resolve(statusCode);
            if (httpStatus != null) {
                switch (httpStatus) {
                    case NOT_FOUND:
                        message = "La page que vous recherchez n'a pas été trouvée.";
                        break;
                    case FORBIDDEN:
                        message = "Accès refusé. Vous n'avez pas les permissions nécessaires.";
                        break;
                    case BAD_REQUEST:
                        message = "Votre requête est invalide.";
                        break;
                    default:
                        message = "Une erreur serveur s'est produite.";
                }
            }
        }
        
        Throwable exception = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String exceptionMessage = (exception != null) ? exception.getMessage() : "";

        String redirectUrl = String.format(
            "redirect:/error.html?code=%d&message=%s&exception=%s",
            statusCode,
            URLEncoder.encode(message, StandardCharsets.UTF_8),
            URLEncoder.encode(exceptionMessage, StandardCharsets.UTF_8)
        );
        
        return new ModelAndView(redirectUrl);
    }
}
