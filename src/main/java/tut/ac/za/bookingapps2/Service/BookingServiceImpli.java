package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.BookingRepository;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.Respository.TimeSlotRepository;
import tut.ac.za.bookingapps2.entities.*;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpli implements BookingService {
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
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
       //status of booking = submitted
        Booking existingBooking = bookingRepository.findById(booking.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking id: " + booking.getId()));
       //exists
        //
        System.out.println(booking.getStatus());
        existingBooking.setStatus(booking.getStatus());
        System.out.println(existingBooking.getStatus());

        if (existingBooking.getStatus() == BookingStatus.REJECTED) {
          long labId =   existingBooking.getLab().getId();
          List<TimeSlot> timeSlots = timeSlotRepository.findByLabId(labId);
        // TimeSlot rejectedTimeSlot = timeSlotRepository.findByStartTimeAndEndTime(                 existingBooking.getStartTime(), existingBooking.getEndTime());
          for (TimeSlot time : timeSlots){
              if (time.getStartTime().equals(existingBooking.getStartTime())){
                  // Make the time slot available
                  time.setTimeSlotStatus(TimeSlotStatus.AVAILABLE);
                  // Save the updated time slot
                  timeSlotRepository.save(time);
                  return null;
              }
          }
        }
        emailService.sendBookingStatusUpdateEmail(existingBooking);

      //   Save the updated booking
        return bookingRepository.save(existingBooking);
    }


}
