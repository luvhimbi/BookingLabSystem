package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.BookingRepository;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.entities.Booking;
import tut.ac.za.bookingapps2.entities.Lab;

import java.util.List;
@Service
public class BookingServiceImpli implements BookingService {
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking id: " + id));
    }

    @Override
    public Booking updateBookingStatus(Booking booking) {
        Booking existingBooking = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking id: " + booking.getId()));
        existingBooking.setStatus(booking.getStatus());
        emailService.sendBookingStatusUpdateEmail(existingBooking);
        return bookingRepository.save(existingBooking);
    }
}
