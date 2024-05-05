package tut.ac.za.bookingapps2.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // Convert Date to String with desired format
            String dateString = dateFormat.format(date);

            // Parse the formatted string back to a Date object
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null; // Or throw an exception if necessary
        }
    }
}