package tut.ac.za.bookingapps2.Service;

import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;

import java.util.List;

public interface TimeSlotService {
    void saveTimeSlot(TimeSlot timeSlot);
    List<TimeSlot> getTimeSlotsByLab(Lab lab);
}
