package tut.ac.za.bookingapps2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tut.ac.za.bookingapps2.Service.BookingService;
import tut.ac.za.bookingapps2.Service.DateUtils;
import tut.ac.za.bookingapps2.Service.LabService;
import tut.ac.za.bookingapps2.entities.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private LabService labService;
    private DateUtils dateUtils;
    @Autowired
    private BookingService bookingService;


    @GetMapping("/AddBooking")
    public String showAddBooking(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }
        List<Lab> availableLabs = labService.getAllLabs();
        model.addAttribute("availableLabs", availableLabs);
        return "AddBooking";
    }
    @GetMapping("/AddTutorBooking")
    public String showTutorAddBooking(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }

        List<Lab> availableLabs = labService.getAllLabs();
        model.addAttribute("availableLabs", availableLabs);
        return "TutorAddBooking";
    }


    @GetMapping("/getAvailableTimeSlots/{labId}")
    @ResponseBody
    public List<TimeSlot> getAvailableTimeSlots(@PathVariable Long labId,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        return labService.getAvailableTimeSlotsForLab(labId);
    }
    @GetMapping("/ViewTutorBooking")
    public String viewTutorBookings(Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if (loggedInUser != null) {

            List<Booking> userBookings = bookingService.getUserBookings(loggedInUser.getUser_id());

            // Add the user's bookings to the model
            model.addAttribute("userBookings", userBookings);
        } else {
            // Handle the case where the user is not authenticated
            model.addAttribute("error", "You must be logged in to view your bookings.");
        }


        return "ViewTutorBookings";
    }
    @GetMapping("/AddMentorBooking")
    public String showMentorAddBooking(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }

        List<Lab> availableLabs = labService.getAllLabs();
        model.addAttribute("availableLabs", availableLabs);
        return "TutorAddBooking";
    }
    @GetMapping("/ViewMentorBooking")
    public String viewMentorBookings(Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            return "redirect:/Login";
        }

            List<Booking> userBookings = bookingService.getUserBookings(loggedInUser.getUser_id());
            model.addAttribute("userBookings", userBookings);



        return "ViewMentorBookings";
    }
    @GetMapping("/ViewBooking")
    public String viewBookings(Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/Login";
        }

        List<Booking> userBookings = bookingService.getUserBookings(loggedInUser.getUser_id());
        model.addAttribute("userBookings", userBookings);

        return "ViewBookings";
    }
    @PostMapping("/bookLab")
    public String bookLab(@RequestParam Long labId, @RequestParam Long timeSlotId, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date bookingDate, Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if(loggedInUser== null){
            return "redirect:/Login";
        }
       Date formattedDate = DateUtils.formatDate(DateUtils.formatDate(bookingDate));
        Booking booking = labService.createBooking(labId, timeSlotId,formattedDate , loggedInUser);
        model.addAttribute("booking", booking);
        return "bookingConfirmation";
    }
    @PostMapping("/Tutor_BookLab")
    public String Tutor_bookLab(@RequestParam Long labId, @RequestParam Long timeSlotId, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date bookingDate, Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");

        if(loggedInUser== null){
            return "redirect:/Login";
        }
        Date formattedDate = DateUtils.formatDate(DateUtils.formatDate(bookingDate));
        Booking booking = labService.createBooking(labId, timeSlotId,formattedDate , loggedInUser);
        model.addAttribute("booking", booking);
        return "TutorBookingConfirmation";
    }

    @PostMapping("/Mentor_BookLab")
    public String Mentor_bookLab(@RequestParam Long labId, @RequestParam Long timeSlotId, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date bookingDate, Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if(loggedInUser== null){
            return "redirect:/Login";
        }
        Date formattedDate = DateUtils.formatDate(DateUtils.formatDate(bookingDate));
        Booking booking = labService.createBooking(labId, timeSlotId,formattedDate , loggedInUser);
        model.addAttribute("booking", booking);
        return "MentorBookingConfirmation";
    }
    @GetMapping("/AllBookings")
    public String getAllBookings(Model model,HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }

        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "AdminViewBookings";
    }
    @GetMapping("/booking/details/{id}")
    public String getBookingDetails(@PathVariable Long id, Model model,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if(loggedInUser== null){
            return "redirect:/Login";
        }
        Booking booking = bookingService.getBookingById(id);
        System.out.println(booking.getStatus());
        model.addAttribute("booking", booking);
        return "Booking_Details";
    }

    @PostMapping("/booking/update-status")
    public String updateBookingStatus(Booking booking,HttpSession session) {
        Users loggedInUser = (Users) session.getAttribute("loggedInUser");
        if(loggedInUser== null){
            return "redirect:/Login";
        }
        System.out.println(booking.getStatus());
        bookingService.updateBookingStatus(booking);
        return "redirect:/AllBookings";
    }
}
