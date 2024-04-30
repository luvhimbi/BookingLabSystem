package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tut.ac.za.bookingapps2.entities.UserRole;
import tut.ac.za.bookingapps2.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    // Method to get the count of mentors

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = :role")
    long countByRole(@Param("role") UserRole role);
    Users findByUsername(String username);
    Users findByEmail(String email);

}
