package tut.ac.za.bookingapps2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showLoginForm(Model model, HttpSession session) {
        String message = (String) session.getAttribute("successMessage");
        if (message != null) {
            model.addAttribute("successMessage", message);
            // Clear the attribute from session to avoid showing it again on subsequent requests
            session.removeAttribute("successMessage");
        }
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
    @GetMapping("/MentorDashboard")
    public String redirectToMentorDashboard(HttpSession session) {

        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }

        return "MentorDashboard";
    }
    @GetMapping("/TutorDashboard")
    public String redirectToTutorDashboard(HttpSession session) {

        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/Login";
        }

        return "TutorDashboard";
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


    //Student related methods
    @GetMapping("/studentProfile")
    public String redirectToStudentProfile(HttpSession session,Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }


        if (!currentUser.getRole().equals(UserRole.STUDENT)) {
            return "redirect:/access-denied";
        }
        model.addAttribute("user", currentUser);
        return "StudentProfile";
    }
    @GetMapping("/access-denied")
    public String showAccessDeniedPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Invalidate the current session
        }
        return "access-denied";
    }
    @GetMapping("/tutorProfile")
    public String redirectToTutorProfile(HttpSession session,Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }



        model.addAttribute("user", currentUser);
        return "TutorProfile";
    }
    @GetMapping("/StudentDash")
    public String showStudentDashboard(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
        }



        return "StudentDashboard";
    }

    @GetMapping("/add-user")
    public String showAddUserForm(HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:Login";
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


    @PostMapping("/add-user")
    public String addUser(@RequestParam("firstname") String firstname,
                          @RequestParam("lastname") String lastname,
                          @RequestParam("username") String username,
                          @RequestParam("email") String email,
                          @RequestParam("role") String role,
                          RedirectAttributes redirectAttributes,
                          HttpSession session) {
        Users currentUser = (Users) session.getAttribute("loggedInUser");

        if (currentUser == null ) {
            return "redirect:/Login";
        }

        // Check if a user with the same username or email already exists
        Users existingUserByUsername = userService.findByUsername(username);
        Users existingUserByEmail = userService.findByEmail(email);

        if (existingUserByUsername != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "A user with the same username already exists.");
            return "redirect:/add-user";
        }

        if (existingUserByEmail != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "A user with the same email already exists.");
            return "redirect:/add-user";
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
                               Model model,
                               HttpSession session) {

        // Check if the user is already logged in
        if (session.getAttribute("loggedInUser") != null) {
            UserRole userRole = (UserRole) session.getAttribute("userRole");
            switch (userRole) {
                case STUDENT:
                    return "StudentDashboard";
                case TUTOR:
                    return "TutorDashboard";
                case MENTOR:
                    return "MentorDashboard";
                case ADMIN:
                    return "AdminDashboard";
                default:
                    // Handle invalid role
                    return "ErrorPage";
            }
        }

        Users user = userService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userRole", user.getRole());

            switch (user.getRole()) {
                case STUDENT:
                    return "StudentDashboard";
                case TUTOR:
                    return "TutorDashboard";
                case MENTOR:
                    return "MentorDashboard";
                case ADMIN:
                    return "AdminDashboard";
                default:
                    model.addAttribute("error", "Invalid role");
                    return "Login";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "Login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
return "redirect:Login";
    }

    @PostMapping("/update")
    public String updateUserDetails(Users updatedUser, BindingResult bindingResult,Model model,HttpSession session) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            return "user-details";
        }

        // Retrieve the currently logged-in user from the session
        Users currentUser = (Users) session.getAttribute("loggedInUser");
        if (currentUser == null) {

            // Handle the case when the user is not logged in
            return "redirect:/login";
        }

        // Update the user details

        currentUser.setUsername(updatedUser.getUsername());
        currentUser.setFirstname(updatedUser.getFirstname());
        currentUser.setLastname(updatedUser.getLastname());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setPassword(updatedUser.getPassword());

        // Save the updated user details

        Users updatedCurrentUser = userService.updateUser(currentUser);

        // Update the session with the updated user details
        session.setAttribute("loggedInUser", updatedCurrentUser);
        session.setAttribute("successMessage", "User details updated successfully.please login again");
        return "redirect:/Login";
    }

}
