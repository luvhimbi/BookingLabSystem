package tut.ac.za.bookingapps2.Service;

import tut.ac.za.bookingapps2.entities.Lab;
import tut.ac.za.bookingapps2.entities.LabType;

import java.util.List;

public interface LabService {
    public Lab saveLab(LabType type, String location);

    List<Lab> getAllLabs();
}
