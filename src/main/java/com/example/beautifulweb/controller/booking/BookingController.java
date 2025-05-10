package com.example.beautifulweb.controller.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.beautifulweb.model.TourBooking;
import com.example.beautifulweb.repository.TourBookingRepository;
import com.example.beautifulweb.service.EmailService;
import com.example.beautifulweb.service.TourismService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;

@Controller
public class BookingController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
    private final EmailService emailService;
    private final TourismService tourismService;

    public BookingController(EmailService emailService, TourismService tourismService) {
        this.tourismService = tourismService;
        this.emailService = emailService;
    }

    @Autowired
    private TourBookingRepository tourBookingRepository;

    @PostMapping("/book-tour")
    public String bookTour(
            @Valid @ModelAttribute("tourBooking") TourBooking tourBooking,
            BindingResult result,
            Model model) {
        List<String> destinations = tourismService.getAllDestinations();

        if (result.hasErrors()) {
            model.addAttribute("tourBooking", tourBooking);
            model.addAttribute("destinations", destinations);

            return "redirect:/home?booking-fail";
        }

        try {
            // Parse datetime nếu cần (đề phòng)
            if (tourBooking.getDatetime() == null) {
                String datetimeStr = (String) model.getAttribute("datetime");
                if (datetimeStr != null) {
                    tourBooking.setDatetime(LocalDateTime.parse(datetimeStr, formatter));
                }
            }

            tourBooking.setConfirmed(false);
            tourBookingRepository.save(tourBooking);
            model.addAttribute("message", "Your tour has been booked! Please wait for admin confirmation.");
            return "redirect:/home?success-booking";
        } catch (Exception e) {
            model.addAttribute("tourBooking", tourBooking);
            model.addAttribute("destinations", destinations);

            return "redirect:/home?booking-fail";
        }
    }

    @PostMapping("/admin/confirm-booking")
    public String confirmBooking(@RequestParam Long id, Model model) {
        TourBooking booking = tourBookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + id));
        booking.setConfirmed(true);
        tourBookingRepository.save(booking);
        emailService.sendBookingConfirmationEmail(booking.getEmail(), booking.getName(), booking.getDestination(),
                booking.getDatetime().format(formatter));

        model.addAttribute("message", "Tour booking confirmed successfully!");
        return "redirect:/admin/bookings?confirm_booking";
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String email, Model model) {
        try {

            emailService.sendNewsletterConfirmationEmail(email);

            return "redirect:/home?subscribe_success";
        } catch (Exception e) {
            model.addAttribute("message", "Error processing subscription. Please try again.");
            model.addAttribute("messageType", "danger");
            return "redirect:/home?subscribe_fail";
        }
    }
}