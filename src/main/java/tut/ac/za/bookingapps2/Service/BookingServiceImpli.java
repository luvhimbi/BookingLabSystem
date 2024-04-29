package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.BookingRepository;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.entities.Lab;

import java.util.List;
@Service
public class BookingServiceImpli implements  BoookingService{
    @Autowired
    private LabRepository labRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Lab> getAllLabs() {
        return labRepository.findAll();
    }

}
