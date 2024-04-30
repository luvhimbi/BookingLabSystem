package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tut.ac.za.bookingapps2.entities.Lab;

import java.util.List;

public interface LabRepository extends JpaRepository<Lab,Long> {

    @Query("SELECT l FROM Lab l JOIN l.timeSlots ts WHERE ts.id IS NOT NULL")
    List<Lab> findAllByAvailableTimeSlots_IdIsNotNull();
}
