package tut.ac.za.bookingapps2.Service;

import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;

public interface  UserService {
    public Users authenticate(String username, String password);
    List<Users> getAllUsers();
    public Users findByUsername(String username);
    public Users findByEmail(String email);
    void saveUser(Users user);


    Users updateUser(Users user);

}
