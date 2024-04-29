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
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingNo;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<TimeSlot> availableTimeSlots;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}