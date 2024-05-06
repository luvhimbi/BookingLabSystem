package tut.ac.za.bookingapps2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tut.ac.za.bookingapps2.Service.LabService;
import tut.ac.za.bookingapps2.Service.TimeSlotService;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;
import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final LabService labService;

    @Autowired
    public TimeSlotController(TimeSlotService timeSlotService, LabService labService) {
        this.timeSlotService = timeSlotService;
        this.labService = labService;
    }

    @GetMapping("/timeslots/add")
    public String showAddTimeSlotForm(Model model,HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");
        String message = (String) session.getAttribute("message");
        if (currentUser == null) {
            return "redirect:Login";
        }
        session.removeAttribute("message");
        model.addAttribute("timeSlot", new TimeSlot());
        model.addAttribute("labs", labService.getAllLabs());
        model.addAttribute("message", message);
        return "add-timeslot";
    }

    @PostMapping("/timeslots")
    public String addTimeSlot(TimeSlot timeSlot, HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");
        String message;

        if (currentUser == null) {
            return "Login";
        }
        try {
            timeSlotService.saveTimeSlot(timeSlot);
            message = "Time slot added successfully.";
        } catch (Exception e) {
            message = "Error occurred while adding time slot: " + e.getMessage();
            e.printStackTrace();
        }
        session.setAttribute("message", message);
        return "redirect:/timeslots/add";
    }

    @GetMapping("/timeslots/view")
    public String viewTimeSlots(Model model, HttpSession session) {

        //check the current user who is in the session
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "Login";
        }
        List<Lab> labs = labService.getAllLabs();
        List<Lab> labsWithTimeSlots = labs.stream()
                .map(lab -> {
                    List<TimeSlot> timeSlots = timeSlotService.getTimeSlotsByLab(lab);
                    lab.setTimeSlots(timeSlots);
                    return lab;
                })
                .collect(Collectors.toList());

        model.addAttribute("labs", labsWithTimeSlots);
        return "view-timeslots";
    }
}