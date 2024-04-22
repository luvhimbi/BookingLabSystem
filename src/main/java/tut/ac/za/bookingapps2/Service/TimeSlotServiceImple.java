package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.TimeSlotRepository;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.TimeSlot;

import java.util.List;

@Service
public class TimeSlotServiceImple implements TimeSlotService{
    @Autowired
    private  TimeSlotRepository timeSlotRepository;


    @Override
    public void saveTimeSlot(TimeSlot timeSlot) {
        timeSlotRepository.save(timeSlot);
    }

    @Override
    public List<TimeSlot> getTimeSlotsByLab(Lab lab) {
        return timeSlotRepository.findByLab(lab);
    }
}
