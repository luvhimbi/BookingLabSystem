package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tut.ac.za.bookingapps2.entities.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query("SELECT b FROM Booking b WHERE b.user.user_id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM Lab l JOIN l.timeSlots ts WHERE ts.id IS NOT NULL")
    List<Booking> findByLabId(Long labId);

}
