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
@Override
    public Users authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(Users user) {
        userRepository.save(user);
    }


    @Override
    public long getTutorCount() {
        return userRepository.countByRole(UserRole.TUTOR);
    }
    @Override
    public long getMentorCount() {
        return userRepository.countByRole(UserRole.MENTOR);
    }
    @Override
    public long getStudentCount() {
        return userRepository.countByRole(UserRole.STUDENT);
    }


}