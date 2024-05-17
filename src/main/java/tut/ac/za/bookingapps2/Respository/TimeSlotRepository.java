package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;

import java.time.LocalTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    @Query("SELECT t FROM TimeSlot t WHERE t.lab = :lab")
    List<TimeSlot> findByLab(Lab lab);
    TimeSlot findByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);


    @Query("SELECT t FROM TimeSlot t JOIN t.lab l WHERE l.id = :labId")
    List<TimeSlot> findByLabId(Long labId);

}