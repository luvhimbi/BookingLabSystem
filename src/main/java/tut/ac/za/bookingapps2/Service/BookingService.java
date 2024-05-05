package tut.ac.za.bookingapps2.Service;

import tut.ac.za.bookingapps2.entities.Booking;
import tut.ac.za.bookingapps2.entities.Lab;

import java.util.List;

public interface BookingService {
    public List<Lab> getAllLabs();
    public List<Booking> getUserBookings(Long userId);

    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    Booking updateBookingStatus(Booking booking);
}
