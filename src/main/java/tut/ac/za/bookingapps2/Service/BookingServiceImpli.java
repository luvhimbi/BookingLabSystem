package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.BookingRepository;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.Respository.TimeSlotRepository;
import tut.ac.za.bookingapps2.entities.*;

import java.util.List;
@Service
public class BookingServiceImpli implements BookingService {
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;
    private TimeSlotRepository timeSlotRepository;

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
        System.out.println(booking.getStatus());
        existingBooking.setStatus(booking.getStatus());
        System.out.println(existingBooking.getStatus() );
        if (existingBooking.getStatus() == BookingStatus.REJECTED) {


            TimeSlot rejectedTimeSlot = timeSlotRepository.findByStartTimeAndEndTime(
                    existingBooking.getStartTime(), existingBooking.getEndTime());

            if (rejectedTimeSlot != null) {
                // Make the time slot available
                rejectedTimeSlot.setTimeSlotStatus(TimeSlotStatus.AVAILABLE);
                // Save the updated time slot
                timeSlotRepository.save(rejectedTimeSlot);
            }

            // Send email notification about the booking rejection
            emailService.sendBookingStatusUpdateEmail(existingBooking);

            return null;
        }

        // Send email notification about the booking status update (if approved)
        emailService.sendBookingStatusUpdateEmail(existingBooking);

        // Save the updated booking
        return bookingRepository.save(existingBooking);
    }


}
