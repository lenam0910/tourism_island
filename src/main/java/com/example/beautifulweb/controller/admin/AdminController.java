package com.example.beautifulweb.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.model.Tourism;
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

    @GetMapping("bookings")
    public String listBookings(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10; // Number of bookings per page
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<TourBooking> bookingPage = bookingService.findAll(pageable);

        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingPage.getTotalPages());

        return "admin/bookings";
    }

    @GetMapping("/tourism-manage")
    public String showTourismManage(Model model, @RequestParam(defaultValue = "0") int page) {

        int pageSize = 10;
        // Giảm page đi 1 vì PageRequest sử dụng index từ 0
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<Tourism> userPage = tourismService.findAll(pageable);

        // Thêm dữ liệu vào Model
        model.addAttribute("tourismList", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

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

    @GetMapping("/users")
    public String showUsers(Model model, @RequestParam(defaultValue = "0") int page) {
        // Số lượng mục trên mỗi trang
        int pageSize = 10;
        // Giảm page đi 1 vì PageRequest sử dụng index từ 0
        PageRequest pageable = PageRequest.of(page, pageSize);
        Page<User> userPage = userService.findAll(pageable);

        // Thêm dữ liệu vào Model
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());

        return "admin/users";
    }

}