package com.example.beautifulweb.controller.admin;

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
import com.example.beautifulweb.service.TourismService;
import com.example.beautifulweb.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final BookingService bookingService;
    private final TourismService tourismService;

    public AdminController(BookingService bookingService, UserService userService, TourismService tourismService) {
        this.tourismService = tourismService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        return "admin/dashboard";
    }

    @GetMapping("/tourism-manage")
    public String showTourismManage(Model model) {
        model.addAttribute("tourismList", tourismService.getAll());
        return "admin/tourism-manage";
    }

    @GetMapping("/map")
    public String showMap(Model model) {
        return "admin/map";
    }

    @GetMapping("/user")
    public String showUser(Model model) {
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

}