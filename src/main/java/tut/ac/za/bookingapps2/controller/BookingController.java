package tut.ac.za.bookingapps2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tut.ac.za.bookingapps2.Service.BookingService;
import tut.ac.za.bookingapps2.Service.LabService;
import tut.ac.za.bookingapps2.entities.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
private LabService labService;
    @Autowired
private BookingService bookingService;
    @GetMapping("/AddBooking")
    public String showAddBooking(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }


        if (!(currentUser.getRole().equals(UserRole.STUDENT))) {
            return "redirect:/access-denied";
        }

        List<Lab> availableLabs = labService.getAllLabs();
        model.addAttribute("availableLabs", availableLabs);
        return "AddBooking";
    }
    @GetMapping("/AddTutorBooking")
    public String showTutorAddBooking(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }


        if (!(currentUser.getRole().equals(UserRole.TUTOR))) {
            return "redirect:/access-denied";
        }

        List<Lab> availableLabs = labService.getAllLabs();
        model.addAttribute("availableLabs", availableLabs);
        return "TutorAddBooking";
    }


    @GetMapping("/getAvailableTimeSlots/{labId}")
    @ResponseBody
    public List<TimeSlot> getAvailableTimeSlots(@PathVariable Long labId) {
        return labService.getAvailableTimeSlotsForLab(labId);
    }

    @GetMapping("/ViewBooking")
    public String viewBookings(Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {

            List<Booking> userBookings = bookingService.getUserBookings(loggedInUser.getUser_id());

            // Add the user's bookings to the model
            model.addAttribute("userBookings", userBookings);
        } else {
            // Handle the case where the user is not authenticated
            model.addAttribute("error", "You must be logged in to view your bookings.");
        }


        return "ViewBookings";
    }
    @PostMapping("/bookLab")
    public String bookLab(@RequestParam Long labId, @RequestParam Long timeSlotId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date bookingDate, Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        Booking booking = labService.createBooking(labId, timeSlotId, bookingDate, loggedInUser);
        model.addAttribute("booking", booking);
        return "bookingConfirmation";
    }
    @GetMapping("/AllBookings")
    public String getAllBookings(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "ViewBookings";
    }
}
