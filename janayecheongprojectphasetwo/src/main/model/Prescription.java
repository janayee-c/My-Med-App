package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

// Represents a Prescription with a name, imprint code, dosage amount, dosage directions, frequency of dosages
// per day, schedule of dosages, buffer period between doses, pill number, dosages in the current day and refill date

// citations for Prescription in README file
// additional citation: json files in workroom example (CS210)

public class Prescription implements Writable {

    //default first dosage time
    public static final LocalTime DEFAULT_TIME = LocalTime.of(8, 00);

    private String name;                               // the name of the prescription drug
    private String imprint;                            // the imprint id of prescription drug
    private String dosage;                             // the dose amount per dosage of the prescription drug
    private String directions;                         // instructions on dosage (eg. with water, with food)
    private int frequency;                             // number of dosages per day
    private ArrayList<LocalTime> doseSchedule;         // dosage schedule of the drug in a 24 hour period
    private int timeBuffer;                            // the period (in hrs) between dosages
    private int pillNumber;                            // number of pills left in current prescription
    private int dailyCounter;                          // number of pills taken within the current day
    private LocalDate lastRefill;                      // the date in YYYY/MM/DD of the last required refill date
    private LocalDate refillDate;                      // the date in YYYY/MM/DD of the next required refill date

    //SPECIFIES: prescription imprint number is a 4 digit positive integer, frequency is not more than 4 times a day
    //EFFECTS: creates a prescription for the user with a default dosage time of 8am
    public Prescription(String drugName, String id, int number, String doseAmount,
                        String usage, int buffer, int frequency, LocalDate lastRefill) {

        name = drugName;
        imprint = id;
        pillNumber = number;
        dosage = doseAmount;
        directions = usage;
        timeBuffer = buffer;
        this.frequency = frequency;
        this.lastRefill = lastRefill;
        refillDate = createRefillDate(lastRefill);
        doseSchedule = defaultSchedule();
        dailyCounter = 0;
    }

    public String getName() {
        return name;
    }

    public String getImprint() {
        return imprint;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDirections() {
        return directions;
    }

    public int getTimeBuffer() {
        return timeBuffer;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getPillNumber() {
        return pillNumber;
    }

    public int getDailyCounter() {
        return dailyCounter;
    }

    public void setRefillDate(LocalDate savedDate) {
        this.refillDate = savedDate;
    }

    public LocalDate getLastRefill() {
        return lastRefill;
    }

    public LocalDate getRefillDate() {
        return refillDate;
    }

    public List<LocalTime> getDoseSchedule() {
        return doseSchedule;
    }

    public void setDoseSchedule(ArrayList<LocalTime> schedule) {
        this.doseSchedule = schedule;
    }


    public void setDailyCounter(int num) {
        this.dailyCounter = num;
    }

    //EFFECTS: returns a default dosage schedule as a list of LocalTime for the prescription
    public ArrayList<LocalTime> defaultSchedule() {
        ArrayList<LocalTime> schedule = new ArrayList<>();
        schedule.add(0, DEFAULT_TIME);
        LocalTime previousTime = DEFAULT_TIME;
        for (int index = 1; index < frequency; index++) {
            schedule.add(index, previousTime.plusHours(timeBuffer));
            previousTime = schedule.get(index);
        }
        return schedule;
    }

    //SPECIFIES: LocalDate in format (YYYY, MM, DD) must not be before the current date
    //EFFECTS: returns a LocalDate that describes when the prescription must be refilled next
    public LocalDate createRefillDate(LocalDate lastRefill) {
        int remainingDays = floorDiv(this.pillNumber, this.frequency);
        return lastRefill.plusDays(remainingDays);
    }


    //MODIFIES: this
    //EFFECTS: edits the first scheduled dose and adjusts following times according to buffer period
    public void editSchedule(String startingTime) {
        doseSchedule.remove(0);
        doseSchedule.add(0, timeFormatter(startingTime));
        LocalTime previousTime = doseSchedule.get(0);
        for (int index = 1; index < frequency; index++) {
            this.doseSchedule.set(index, previousTime.plusHours(timeBuffer));
            previousTime = doseSchedule.get(index);
        }
    }

    //MODIFIES: this
    //EFFECTS: removes a single pill from the prescription and edits counter and number of pills
    public void takeDose() {
        if (pillNumber > 0) {
            pillNumber--;
            dailyCounter++;
        }
    }


    //REQUIRES: string of length 5 with format within 24 hr time including colon (ex. "08:24")
    //EFFECTS: returns a LocalTime described by the string
    public LocalTime timeFormatter(String time) {
        int hour;
        if (Integer.valueOf(time.substring(0, 1)) == 0) {
            hour = Integer.valueOf(time.substring(1, 2));
        } else {
            hour = Integer.valueOf(time.substring(0, 2));
        }
        int minute = Integer.valueOf(time.substring(3));
        LocalTime formattedTime = LocalTime.of(hour, minute);
        return formattedTime;
    }

    //returns a JSONObject that represents the contents of a prescription
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("imprint", imprint);
        json.put("dosage", dosage);
        json.put("directions", directions);
        json.put("frequency", frequency);
        json.put("schedule", doseScheduleToJson());
        json.put("buffer", timeBuffer);
        json.put("number", pillNumber);
        json.put("counter", dailyCounter);
        json.put("lastRefill", lastRefill);
        json.put("refillDate", refillDate);
        return json;
    }


    //EFFECTS: returns the scheduled times in doseSchedule as a JSONArray
    public JSONArray doseScheduleToJson() {
        JSONArray jsonArray = new JSONArray();
        for (LocalTime lt : doseSchedule) {
            jsonArray.put(lt);
        }

        return jsonArray;
    }
}


