package tut.ac.za.bookingapps2.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tut.ac.za.bookingapps2.Service.PasswordGenerator;
import tut.ac.za.bookingapps2.Service.UserService;
import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/Login")
    public String showLoginForm(Model model) {

        return "Login";
    }

    @GetMapping("/Dashboard")
    public String redirectToDashboard(HttpSession session) {

        Users currentUser = (Users) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:Login";
        }

        return "AdminDashboard";
    }
    @GetMapping("/Profile")
    public String redirectToProfile(HttpSession session,Model model) {

        Users currentUser = (Users) session.getAttribute("loggedInUser");
        if (currentUser == null) {
            return "redirect:Login";
        }
        model.addAttribute("user", currentUser);
        return "Profile";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }

        // Check if the current user has the required role to add a new user
        if (!currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/access-denied";
        }

        return "add-user";
    }


    @GetMapping("/manage-users")
    public String redirectToManageUsers(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");
        if (currentUser == null || !currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:Login";
        }

        // Retrieve all users from the service or database
        List<Users> allUsers = userService.getAllUsers();

        // Filter out the logged-in user
        List<Users> usersExceptLoggedInUser = allUsers.stream()
                .filter(user -> !user.equals(currentUser.getUsername()))
                .collect(Collectors.toList());

        // Add the filtered user list and user roles to the model
        model.addAttribute("users", usersExceptLoggedInUser);

        return "manage-users";
    }

    @GetMapping("/user-counts")
    public String getUserCounts(Model model,HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null || !currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/access-denied";
        }

        // Retrieve user counts from the service
        long studentCount = userService.getStudentCount();
        System.out.println(studentCount);
        long tutorCount = userService.getTutorCount();
        long mentorCount = userService.getMentorCount();

        // Add user counts to the model
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("tutorCount", tutorCount);
        model.addAttribute("mentorCount", mentorCount);

        return "AdminDashboard"; // Or the appropriate view name
    }
    @PostMapping("/add-user")
    public String addUser(@RequestParam("firstname") String firstname,
                          @RequestParam("lastname") String lastname,
                          @RequestParam("username") String username,
                          @RequestParam("email") String email,
                          @RequestParam("role") String role,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null || !currentUser.getRole().equals(UserRole.ADMIN)) {
            return "redirect:/access-denied";
        }

        // Create a new Users object
        Users newUser = new Users();
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setRole(UserRole.valueOf(role.toUpperCase()));

        // Generate a random password
        String generatedPassword = PasswordGenerator.generateRandomPassword();
        newUser.setPassword(generatedPassword);

        userService.saveUser(newUser);

        // Add success message to the redirect attributes
        redirectAttributes.addFlashAttribute("successMessage", "User added successfully.");
        redirectAttributes.addFlashAttribute("generatedPassword", generatedPassword);

        // Construct the URL to the manage users page with a query parameter
        String manageUsersUrl = "/manage-users?userAdded=true";

        return "redirect:" + manageUsersUrl;
    }


    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("role") String role,
                               Model model,
                               HttpSession session) {
        Users user = userService.authenticate(username, password);

        if (user != null) {
            // If authentication is successful, store the entire user object in the session
            session.setAttribute("loggedInUser", user);

            // Redirect to the appropriate page based on the user's role
            switch (role) {
                case "STUDENT":
                    return "StudentDashboard";
                case "TUTOR":
                    return "TutorDashboard";
                case "MENTOR":
                    return "MentorDashboard";
                case "ADMIN":
                    return "AdminDashboard";
                default:
                    model.addAttribute("error", "Invalid role selected");
                    return "Login";
            }
        } else {
            // If authentication fails, add an error message to the model
            model.addAttribute("error", "Invalid username or password");
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
return "redirect:Login";
    }

}
