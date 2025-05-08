package com.example.beautifulweb.controller.admin;

// import com.example.beautifulweb.model.Service;
// import com.example.beautifulweb.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.beautifulweb.model.User;
import com.example.beautifulweb.service.BookingService;
import com.example.beautifulweb.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // @Autowired
    // private ServiceService serviceService;

    @Autowired
    private UserService userService;

    private final BookingService bookingService;

    public AdminController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // model.addAttribute("services", serviceService.getAllServices());
        return "admin/dashboard";
    }

    @GetMapping("/map")
    public String showMap(Model model) {
        return "admin/map";
    }

    @GetMapping("/user")
    public String showUser(Model model) {
        // model.addAttribute("services", serviceService.getAllServices());
        return "admin/user";
    }

    @GetMapping("/bookings")
    public String showBooking(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin/bookings";
    }

    // Hiển thị danh sách người dùng
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
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