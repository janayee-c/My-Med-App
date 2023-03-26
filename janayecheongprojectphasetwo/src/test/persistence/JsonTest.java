package persistence;

import model.Prescription;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkPrescription(String name,
                                     String imprint,
                                     String dosage,
                                     String directions,
                                     int frequency,
                                     ArrayList<LocalTime> schedule,
                                     int buffer,
                                     int number,
                                     int counter,
                                     LocalDate lastRefill,
                                     LocalDate nextRefill,
                                     Prescription prescription) {
        assertEquals(name, prescription.getName());
        assertEquals(imprint, prescription.getImprint());
        assertEquals(dosage, prescription.getDosage());
        assertEquals(directions, prescription.getDirections());
        assertEquals(frequency, prescription.getFrequency());
        assertEquals(schedule, prescription.getDoseSchedule());
        assertEquals(buffer, prescription.getTimeBuffer());
        assertEquals(number, prescription.getPillNumber());
        assertEquals(counter, prescription.getDailyCounter());
        assertEquals(lastRefill, prescription.getLastRefill());
        assertEquals(nextRefill, prescription.getRefillDate());
    }

}
