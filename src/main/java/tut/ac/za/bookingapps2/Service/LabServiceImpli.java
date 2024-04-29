package tut.ac.za.bookingapps2.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.Respository.LabRepository;
import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.LabAvailability;
import tut.ac.za.bookingapps2.entities.LabType;

import java.util.List;

@Service
public class LabServiceImpli implements LabService {
    @Autowired
    private LabRepository labRepository;

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
}