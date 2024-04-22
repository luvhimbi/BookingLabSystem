package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;

import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findByLab(Lab lab);
}