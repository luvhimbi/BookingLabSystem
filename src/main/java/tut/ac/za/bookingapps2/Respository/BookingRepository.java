package tut.ac.za.bookingapps2.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import tut.ac.za.bookingapps2.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
