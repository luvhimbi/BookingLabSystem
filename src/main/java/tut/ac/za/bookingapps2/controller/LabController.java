package tut.ac.za.bookingapps2.controller;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tut.ac.za.bookingapps2.Service.LabService;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;

@Controller
public class LabController {
    @Autowired
    private  LabService labService;
    @GetMapping("/createLab")
    public String ShowCreateLabForm(Model model, HttpSession session){
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }

        if (!currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/access-denied";
        }

        return "CreateLab";

    }
    @PostMapping("/createLab")
    public String saveLab( @RequestParam("location") String location, HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }

        if (!currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/access-denied";
        }


        labService.saveLab(location);

        return "redirect:/labs";
    }
    @GetMapping("/labs")
    public String getAllLabs(Model model) {
        List<Lab> labs = labService.getAllLabs();
        model.addAttribute("labs", labs);
        return "ViewLabs";
    }
}
