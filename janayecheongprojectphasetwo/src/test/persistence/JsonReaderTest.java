package persistence;

import model.PatientAccount;
import model.Prescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {
    Prescription p1;
    Prescription p2;
    Prescription p3;
    ArrayList<LocalTime> scheduleOne;
    ArrayList<LocalTime> scheduleTwo;

    @BeforeEach
    void setUp() {
        //rizatriptan  (built-in prescription)
        p1 = new Prescription("rizatriptan", "CL34", 30, "50 mg",
                "refrigerate bottle", 6, 2, LocalDate.of(2023, 2, 14));
        //escitalopram pill (built-in prescription)
        p2 = new Prescription("escitalopram", "P20", 60, "10 mg",
                "take with food", 24, 1, LocalDate.of(2023, 1, 20));
        //oxacillin pill (built-in prescription)
        p3 = new Prescription("oxacillin", "biocraft 14", 50, "50 mg",
                "take before bed", 7, 3, LocalDate.of(2023, 12, 1));
        scheduleOne = new ArrayList<>();
        scheduleTwo = new ArrayList<>();
        LocalTime morningDose = LocalTime.of(8, 0);
        scheduleOne.add(morningDose);
        scheduleTwo.add(morningDose);
        scheduleTwo.add(LocalTime.of(14, 0));
    }

        @Test
        void testReaderNonExistentFile () {
            JsonReader reader = new JsonReader("./data/noSuchFile.json");
            try {
                PatientAccount pa = reader.read();
                fail("IOException expected");
            } catch (IOException e) {
                // tests passes if it catches exception (cannot read file)
            }
        }

        @Test
        void testReaderEmptyWorkRoom () {
            JsonReader reader = new JsonReader("./data/testReaderEmptyPatientAccount.json");
            try {
                PatientAccount pa = reader.read();
                assertEquals("empty patient account", pa.getName());
                assertEquals(0, pa.getPrescriptions().size());
            } catch (IOException e) {
                fail("File was unreadable");
            }
        }

        @Test
        void testReaderGeneralWorkRoom () {
            JsonReader reader = new JsonReader("./data/testReaderGeneralPatientAccount.json");
            try {
                PatientAccount pa = reader.read();
                assertEquals("my patient account", pa.getName());
                List<Prescription> prescriptions = pa.getPrescriptions();
                assertEquals(2, prescriptions.size());
                checkPrescription("escitalopram",
                        "P20",
                        "10 mg",
                        "take with food",
                        1,
                        scheduleOne,
                        24,
                        60, 0,
                        LocalDate.of(2023, 1, 20),
                        LocalDate.of(2023, 3, 21),
                        p2);
                checkPrescription("rizatriptan",
                        "CL34",
                        "50 mg",
                        "refrigerate bottle",
                        2,
                        scheduleTwo,
                        6,
                        30,
                        0,
                        LocalDate.of(2023, 2, 14),
                        LocalDate.of(2023, 3, 1),
                        p1);
            } catch (IOException e) {
                fail("Couldn't read from file");
            }
        }
    }
