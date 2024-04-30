package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Excption.ResourceNotFoundException;
import tut.ac.za.bookingapps2.Respository.BookingRepository;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.Respository.TimeSlotRepository;
import tut.ac.za.bookingapps2.Respository.UserRepository;
import tut.ac.za.bookingapps2.entities.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LabServiceImpli implements LabService {
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private  EmailService emailService;
    private UserRepository userRepository;
    @Override
    public Lab saveLab(String labName,LabType type, String location) {
        Lab lab = new Lab();
        lab.setLabName(labName);
        lab.setType(type);
        lab.setLocation(location);
        lab.setAvailability(LabAvailability.AVAILABLE);
        return labRepository.save(lab);
    }

    @Override
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

    public List<TimeSlot> getAvailableTimeSlotsForLab(Long labId) {
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new RuntimeException("Lab not found"));
        List<TimeSlot> allTimeSlots = lab.getTimeSlots();
        List<Booking> bookingsForLab = bookingRepository.findByLabId(labId);

        // Filter out the booked time slots
        List<TimeSlot> bookedTimeSlots = bookingsForLab.stream()
                .flatMap(booking -> booking.getAvailableTimeSlots().stream())
                .collect(Collectors.toList());

        return allTimeSlots.stream()
                .filter(timeSlot -> !bookedTimeSlots.contains(timeSlot))
                .collect(Collectors.toList());
    }

    public Booking createBooking(Long labId, Long timeSlotId, Date bookingDate, Users user) {
        Lab lab = labRepository.findById(labId).orElseThrow(() -> new ResourceNotFoundException("Lab not found"));
        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId).orElseThrow(() -> new ResourceNotFoundException("Time slot not found"));

        Booking booking = new Booking();
        booking.setLab(lab);
        List<TimeSlot> slots = new ArrayList<>();
        slots.add(timeSlot);
        booking.setAvailableTimeSlots(slots);
        booking.setBookingDate(bookingDate);
        booking.setUser(user);
        booking.setBookingNo(generateBookingNumber());
        emailService.sendBookingConfirmationEmail(booking);
        return bookingRepository.save(booking);
    }
    private String generateBookingNumber() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}