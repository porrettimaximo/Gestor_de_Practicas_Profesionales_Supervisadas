package ing.gpps.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @GetMapping
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        
        if (statusCode == null) {
            statusCode = 500;
        }
        
        if (errorMessage == null && exception != null) {
            errorMessage = exception.getMessage();
        }
        
        if (errorMessage == null) {
            errorMessage = "Ha ocurrido un error inesperado";
        }
        
        logger.error("Error {}: {}", statusCode, errorMessage);
        if (exception != null) {
            logger.error("Excepción: ", exception);
        }
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        return "error";
    }
} 