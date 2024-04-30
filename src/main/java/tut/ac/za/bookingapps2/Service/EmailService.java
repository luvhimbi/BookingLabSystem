package tut.ac.za.bookingapps2.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tut.ac.za.bookingapps2.entities.Booking;
import tut.ac.za.bookingapps2.entities.TimeSlot;
import tut.ac.za.bookingapps2.entities.Users;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmationEmail(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getUser().getEmail());
        message.setSubject("Booking Confirmation");

        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(booking.getUser().getFirstname()).append(" ").append(booking.getUser().getLastname()).append(",\n\n");
        body.append("Your booking has been submitted . Here are the details:\n\n");
        body.append("Booking Number: ").append(booking.getBookingNo()).append("\n");
        body.append("Lab Name: ").append(booking.getLab().getLabName()).append("\n");
        body.append("Lab Location: ").append(booking.getLab().getLocation()).append("\n");
        body.append("Booking Date: ").append(formatDate(booking.getBookingDate())).append("\n");
        body.append("Time Slot(s):\n");

        List<TimeSlot> timeSlots = booking.getAvailableTimeSlots();
        for (int i = 0; i < timeSlots.size(); i++) {
            TimeSlot timeSlot = timeSlots.get(i);
            body.append((i + 1)).append(". ").append(formatTime(timeSlot.getStartTime())).append(" - ").append(formatTime(timeSlot.getEndTime())).append("\n");
        }

        body.append("\nYour booking will be reviewed as soon as possible !\n\nRegards,\nTut Lab Booking Team Team");

        message.setText(body.toString());

        mailSender.send(message);
    }
    public void sendUserRegistrationEmail(Users user) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Profile created");

            StringBuilder htmlBody = new StringBuilder();
            htmlBody.append("<!DOCTYPE html>");
            htmlBody.append("<html>");
            htmlBody.append("<head>");
            htmlBody.append("<style>");
            htmlBody.append("body { font-family: Arial, sans-serif; }");
            htmlBody.append("h2 { color: #007bff; }");
            htmlBody.append("ul { list-style-type: none; padding: 0; }");
            htmlBody.append("li { margin-bottom: 10px; }");
            htmlBody.append(".label { font-weight: bold; }");
            htmlBody.append("</style>");
            htmlBody.append("</head>");
            htmlBody.append("<body>");
            htmlBody.append("<h2>Dear ").append(user.getFirstname()).append(" ").append(user.getLastname()).append(",</h2>");
            htmlBody.append("<p>Welcome to Tut  Booking System!</p>");
            htmlBody.append("<p> Here are your Profile details:</p>");
            htmlBody.append("<ul>");
            htmlBody.append("<li><span class='label'>Username:</span> ").append(user.getUsername()).append("</li>");
            htmlBody.append("<li><span class='label'>Email:</span> ").append(user.getEmail()).append("</li>");
            htmlBody.append("<li><span class='label'>FirstName:</span> ").append(user.getFirstname()).append("</li>");
            htmlBody.append("<li><span class='label'>LastName:</span> ").append(user.getLastname()).append("</li>");
            htmlBody.append("<li><span class='label'>Password:</span> ").append(user.getPassword()).append("</li>");
            htmlBody.append("</ul>");
            htmlBody.append("<p>Regards,<br>Tut Lab Booking Team</p>");
            htmlBody.append("</body>");
            htmlBody.append("</html>");

            helper.setText(htmlBody.toString(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    private String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private String formatTime(LocalTime time) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(time);
    }
}