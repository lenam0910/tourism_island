package com.example.beautifulweb.controller.navigation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

@Controller
public class navigationController {
    
    // This class is used to handle navigation between pages in the web application.
    // It contains methods that map to different URLs and return the corresponding
    // HTML templates.
    // The methods are annotated with @GetMapping to specify the HTTP GET request
    // method.

    @GetMapping("/about")
    public String about() {
        return "about"; // Returns the about.html template
    }

    @GetMapping("/services")
    public String services() {
        return "service"; // Returns the services.html template
    }

    @GetMapping("/package")
    public String packages() {
        return "package"; // Returns the packages.html template
    }

    @GetMapping("/map")
    public String map() {
        return "map"; // Returns the map.html template
    }

    @GetMapping("/tour-details")
    public String tourDetails(Model model) {

        return "tour-user-detail"; // Returns the tour-details.html template
    }
}
