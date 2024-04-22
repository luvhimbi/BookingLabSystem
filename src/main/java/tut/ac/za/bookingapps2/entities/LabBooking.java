package tut.ac.za.bookingapps2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String labName;
    private String BookingNo;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    @OneToMany(mappedBy = "labBooking", cascade = CascadeType.ALL)
    private List<TimeSlot> availableTimeSlots;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;


}
