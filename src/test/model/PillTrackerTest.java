package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PillTrackerTest {

    private PatientAccount testPatientAccount;
    private Prescription escitalopram;
    private Prescription buproprion;
    private Prescription ibuprofen;
    private Prescription vyvanse;
    private List<Prescription> testPrescriptions;

    @BeforeEach
    void setUp() {
        escitalopram = new Prescription("escitalopram", "P20",
                30, "500 mg", "take with water", 24, 1,
                LocalDate.of(2023, Month.FEBRUARY, 8));

        buproprion = new Prescription("buproprion", "142", 31,
                "250 mg", "take after eating", 12, 2,
                LocalDate.of(2023, Month.JANUARY, 20));

        ibuprofen = new Prescription("ibuprofen", "V1", 250, "50 mg",
                "take with solid food", 6, 3,
                LocalDate.of(2022, Month.DECEMBER, 20));

        vyvanse = new Prescription("vyvanse", "S489", 2, "100 mg",
                "take with solid food", 2, 2,
                LocalDate.of(2023, Month.FEBRUARY, 26));

        testPrescriptions = new ArrayList<>();
        testPrescriptions.add(escitalopram);
        testPrescriptions.add(buproprion);
        testPrescriptions.add(ibuprofen);
        testPatientAccount = new PatientAccount("Aurora Lee", 7582610, "anxiety, neck pain");
    }

    @Test
    void testPatientConstructor() {
        assertEquals("Aurora Lee", testPatientAccount.getName());
        assertEquals(7582610, testPatientAccount.getId());
        assertEquals("anxiety, neck pain", testPatientAccount.getSymptoms());
    }


    @Test
    void testAddPrescription() {
        //testing that prescriptions were added without duplicates
        testPatientAccount.addPrescription(escitalopram);
        testPatientAccount.addPrescription(buproprion);
        testPatientAccount.addPrescription(ibuprofen);
        testPatientAccount.addPrescription(escitalopram);
        assertTrue(testPatientAccount.alreadyPrescribed(escitalopram));
        assertEquals(escitalopram, testPatientAccount.getPrescriptions().get(0));
        assertEquals(buproprion, testPatientAccount.getPrescriptions().get(1));
        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(escitalopram);
        prescriptions.add(buproprion);
        prescriptions.add(ibuprofen);
        assertEquals(prescriptions, testPatientAccount.getPrescriptions());
    }

    @Test
    void testRemovePrescription() {
        //adding prescriptions
        testPatientAccount.addPrescription(escitalopram);
        testPatientAccount.addPrescription(buproprion);
        testPatientAccount.addPrescription(ibuprofen);

        //removing prescriptions
        testPatientAccount.removePrescription(escitalopram);

        //making list to compare with prescriptions
        List<Prescription> prescriptions = new ArrayList<>();
        prescriptions.add(buproprion);
        prescriptions.add(ibuprofen);
        assertEquals(prescriptions, testPatientAccount.getPrescriptions());

        //testing with removal of ibuprofen
        prescriptions.remove(ibuprofen);
        prescriptions.add(escitalopram);
        testPatientAccount.addPrescription(escitalopram);
        testPatientAccount.removePrescription(ibuprofen);
        assertEquals(prescriptions, testPatientAccount.getPrescriptions());

        //testing with removal of prescription that is not there
        testPatientAccount.removePrescription(ibuprofen);
        assertEquals(prescriptions, testPatientAccount.getPrescriptions());
        assertEquals(prescriptions, testPatientAccount.getPrescriptions());
    }

    @Test
    void takePrescription() {
        testPatientAccount.addPrescription(escitalopram);
        testPatientAccount.addPrescription(vyvanse);
        testPatientAccount.takePrescription("escitalopram");
        testPatientAccount.takePrescription("void prescription");
        assertEquals(29, escitalopram.getPillNumber());
        assertEquals(1, escitalopram.getDailyCounter());

        //test with number that becomes zero
        testPatientAccount.takePrescription("vyvanse");
        assertEquals(1, vyvanse.getPillNumber());
        assertEquals(1, vyvanse.getDailyCounter());
        testPatientAccount.takePrescription("vyvanse");
        assertEquals(0, vyvanse.getPillNumber());
        assertEquals(2, vyvanse.getDailyCounter());
        testPatientAccount.takePrescription("vyvanse");
        assertEquals(0, vyvanse.getPillNumber());
        assertEquals(2, vyvanse.getDailyCounter());
    }


    @Test
    void testPrescriptionConstructor() {
        assertEquals("escitalopram", escitalopram.getName());
        assertEquals("P20", escitalopram.getImprint());
        assertEquals("500 mg", escitalopram.getDosage());
        assertEquals(30, escitalopram.getPillNumber());
        assertEquals(LocalDate.of(2023, Month.FEBRUARY, 8), escitalopram.getLastRefill());
        assertEquals("take with water", escitalopram.getDirections());
        assertEquals(24, escitalopram.getTimeBuffer());
        assertEquals(0, escitalopram.getDailyCounter());
        assertEquals(1, escitalopram.getFrequency());
        assertEquals(LocalDate.of(2023, Month.FEBRUARY, 8), escitalopram.getLastRefill());
        assertEquals(LocalDate.of(2023, Month.MARCH, 10), escitalopram.getRefillDate());
    }

    @Test
    void testSetRefillDate() {
        assertEquals(LocalDate.of(2023, Month.MARCH, 10),
                escitalopram.createRefillDate(escitalopram.getLastRefill()));
        assertEquals(LocalDate.of(2023, Month.FEBRUARY, 4),
                buproprion.createRefillDate(buproprion.getLastRefill()));
        assertEquals(LocalDate.of(2023, Month.MARCH, 13),
                ibuprofen.createRefillDate(ibuprofen.getLastRefill()));
    }

    @Test
    void testAlreadyPrescribed() {
        testPatientAccount.addPrescription(escitalopram);
        testPatientAccount.addPrescription(ibuprofen);
        assertTrue(testPatientAccount.alreadyPrescribed(ibuprofen));
        assertTrue(testPatientAccount.alreadyPrescribed(escitalopram));
        assertFalse(testPatientAccount.alreadyPrescribed(vyvanse));
    }

    @Test
    void testTimeFormatter() {
        assertEquals(LocalTime.of(8, 30), escitalopram.timeFormatter("08:30"));
        assertEquals(LocalTime.of(23, 40), buproprion.timeFormatter("23:40"));
    }

    @Test
    void testDefaultSchedule() {
        //test for times of 3 doses a day ; 6 hrs apart
        assertEquals(LocalTime.of(8, 0), ibuprofen.getDoseSchedule().get(0));
        assertEquals(LocalTime.of(14, 0), ibuprofen.getDoseSchedule().get(1));
        assertEquals(LocalTime.of(20, 0), ibuprofen.getDoseSchedule().get(2));

        //test for time of 1 dose a day
        assertEquals(LocalTime.of(8, 0), escitalopram.getDoseSchedule().get(0));

        //test for time of 2nd dose of the day
        assertEquals(LocalTime.of(20, 0), buproprion.getDoseSchedule().get(1));
    }

    @Test
    void testEditSchedule() {
        //test with shift up 3.5 hours for escitalopram (single dose)
        escitalopram.editSchedule("11:30");
        assertEquals(LocalTime.of(11, 30), escitalopram.getDoseSchedule().get(0));

        //test with shift down 2 hours for buproprion (single dose)
        buproprion.editSchedule("06:30");
        assertEquals(LocalTime.of(6, 30), buproprion.getDoseSchedule().get(0));

        //test with shift to afternoon (2:00 pm) starting time for ibuprofen;
        //following doses are shifted to next day
        ibuprofen.editSchedule("14:00");
        assertEquals(LocalTime.of(14, 0), ibuprofen.getDoseSchedule().get(0));
        assertEquals(LocalTime.of(20, 0), ibuprofen.getDoseSchedule().get(1));
        assertEquals(LocalTime.of(2, 0), ibuprofen.getDoseSchedule().get(2));

    }

    @Test
    void takeDose() {
        escitalopram.takeDose();
        assertEquals(29, escitalopram.getPillNumber());
        escitalopram.takeDose();
        escitalopram.takeDose();
        assertEquals(27, escitalopram.getPillNumber());
    }
}