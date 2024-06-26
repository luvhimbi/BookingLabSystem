package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.UserRepository;
import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;

@Service
public class UserServiceImpli implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
@Override
    public Users authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username.trim());
        if (user != null && user.getPassword().equals(password.trim())) {
            return user;
        }
        return null;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(Users user) {

        userRepository.save(user);
        emailService.sendUserRegistrationEmail(user);
    }




    @Override
    public Users updateUser(Users user) {
        Users existingUser = userRepository.findById(user.getUser_id()).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));
        existingUser.setUsername(user.getUsername());
        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setEmail(user.getEmail());
       return  userRepository.save(existingUser);
    }


}