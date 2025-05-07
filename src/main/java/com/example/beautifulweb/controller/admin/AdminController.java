package com.example.beautifulweb.controller.admin;

// import com.example.beautifulweb.model.Service;
// import com.example.beautifulweb.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // @Autowired
    // private ServiceService serviceService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // model.addAttribute("services", serviceService.getAllServices());
        return "admin/dashboard";
    }

    // @GetMapping("/add-service")
    // public String showAddServiceForm(Model model) {
    // model.addAttribute("service", new Service());
    // return "admin/add-service";
    // }

    // @PostMapping("/add-service")
    // public String addService(@ModelAttribute("service") Service service, Model
    // model) {
    // try {
    // serviceService.saveService(service);
    // model.addAttribute("success", "Service added successfully!");
    // } catch (Exception e) {
    // model.addAttribute("error", "Error adding service: " + e.getMessage());
    // }
    // return "admin/add-service";
    // }
}