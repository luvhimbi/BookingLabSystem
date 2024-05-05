package tut.ac.za.bookingapps2.Service;

import tut.ac.za.bookingapps2.entities.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface LabService {
    public Lab saveLab(String location);

    List<Lab> getAllLabs();
    public List<TimeSlot> getAvailableTimeSlotsForLab(Long labId);
    public Booking createBooking(Long labId, Long timeSlotId, Date bookingDate, Users user);
}
