package tut.ac.za.bookingapps2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import tut.ac.za.bookingapps2.Service.LabService;
import tut.ac.za.bookingapps2.Service.TimeSlotService;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;

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
    public String showAddTimeSlotForm(Model model) {
        model.addAttribute("timeSlot", new TimeSlot());
        model.addAttribute("labs", labService.getAllLabs());
        return "add-timeslot";
    }

    @PostMapping("/timeslots")
    public String addTimeSlot(TimeSlot timeSlot) {
        timeSlotService.saveTimeSlot(timeSlot);
        return "redirect:/timeslots/add";
    }
    @GetMapping("/timeslots/view")
    public String viewTimeSlots(Model model) {
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