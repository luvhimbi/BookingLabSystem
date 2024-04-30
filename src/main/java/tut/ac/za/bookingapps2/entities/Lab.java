package tut.ac.za.bookingapps2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String LabName;
    @Enumerated(EnumType.STRING)
    private LabType type;
    private String location;

    @Enumerated(EnumType.STRING)
    private LabAvailability availability;

    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "lab", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<TimeSlot> timeSlots;
}