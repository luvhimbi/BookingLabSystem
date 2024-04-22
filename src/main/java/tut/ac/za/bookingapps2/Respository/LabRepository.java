package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tut.ac.za.bookingapps2.entities.Lab;

public interface LabRepository extends JpaRepository<Lab,Long> {
}
