package io.vitasoft.dvorakbackend.config.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerRedirect {
    @GetMapping("/swagger-ui")
    public String api(){
        return "redirect:/swagger-ui/index.html";
    }
}
