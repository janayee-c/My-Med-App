package persistence;

// CITATION: JSONReader from workroom example cs210

//A reader that reads PatientAccount data from JSON data stored in file

import model.PatientAccount;
import model.Prescription;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JsonReader {
    String source;

    // EFFECTS: constructs a reader that reads from the specified source
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: returns a Patient Account from file after loading data from source,
    // as a string, reading and storing in JSON Object, parsing it as a JSON Object; an IO Exception
    // is thrown if error in reading the file
    public PatientAccount read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePatientAccount(jsonObject);
    }

    // EFFECTS: reads the data in the source file and returns it as a string;
    // an IO exception is thrown if there is an error in reading the file and converting to string
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses patient account data from JSON Object and returns PatientAccount
    private PatientAccount parsePatientAccount(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        String symptoms = jsonObject.getString("symptoms");
        PatientAccount pa = new PatientAccount(name, id, symptoms);
        addPrescriptions(pa, jsonObject);
        return pa;
    }

    // MODIFIES: pa
    // EFFECTS: parses prescriptions in JSON Array from JSON object and adds them to a PatientAccount (iterates)
    private void addPrescriptions(PatientAccount pa, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("prescriptions");
        for (Object json : jsonArray) {
            JSONObject nextPrescription = (JSONObject) json; //casts as JSONObject
            addPrescription(pa, nextPrescription); //nextPrescription is next jsonObject represnting next prescription
        }
    }

    // MODIFIES: pa
    // EFFECTS: parses prescription from JSON Object and adds it to a PatientAccount
    private void addPrescription(PatientAccount pa, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String imprint = jsonObject.getString("imprint");
        int number = jsonObject.getInt("number");
        String dosage = jsonObject.getString("dosage");
        String directions = jsonObject.getString("directions");
        int frequency = jsonObject.getInt("frequency");
        int timeBuffer = jsonObject.getInt("buffer");
        LocalDate lastRefill = LocalDate.parse(jsonObject.getString("lastRefill"));
        LocalDate nextRefill = LocalDate.parse(jsonObject.getString("refillDate"));

        Prescription prescription = new Prescription(name, imprint, number,
                dosage, directions, timeBuffer, frequency, lastRefill);

        prescription.setRefillDate(nextRefill);
        ArrayList<LocalTime> loadSchedule = getSchedule(jsonObject.getJSONArray("schedule"));
        prescription.setDoseSchedule(loadSchedule);
        prescription.setDailyCounter(jsonObject.getInt("counter"));

        pa.addPrescription(prescription);
    }

    //EFFECTS: returns a list of LocalTimes from a jsonArray representing a schedule
    private ArrayList<LocalTime> getSchedule(JSONArray jsonArray) {
        ArrayList<LocalTime> schedule = new ArrayList<>();
        for (Object json : jsonArray) {
            String stringTime = json.toString();
            LocalTime time = LocalTime.parse(stringTime);
            schedule.add(time);
        }
        return schedule;
    }
}





