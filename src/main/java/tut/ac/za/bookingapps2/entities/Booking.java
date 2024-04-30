package tut.ac.za.bookingapps2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date bookingDate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_timeslot",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "timeslot_id")
    )
    private List<TimeSlot> availableTimeSlots;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;
    private String bookingNo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;


    @Enumerated(EnumType.STRING)
    private BookingStatus status=BookingStatus.CONFIRMED;
}