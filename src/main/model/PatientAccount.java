package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a patient account of the myPrescriptionApp with a name, 4-digit number id, symptoms,
// and list of prescriptions

// citations in README file
// additional citation: json files in workroom example (CS210)

public class PatientAccount implements Writable {
    private String name;                           // the full name of the patient user (first and last name)
    private int id;                                // the four-digit id of the patient account
    private String symptoms;                       // symptoms experienced by the patient
    private List<Prescription> prescriptions;      // a list of the patient's prescriptions

    //SPECIFIES: name is non-zero length, id is 4-digit integer
    //EFFECTS: a new patient with a name, medical record number and symptoms
    public PatientAccount(String name, int id, String symptoms) {
        this.name = name;
        this.id = id;
        this.symptoms = symptoms;
        prescriptions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    //MODIFIES: this
    //EFFECTS: adds a prescription to the list of patient prescriptions if not already added, otherwise does nothing
    public void addPrescription(Prescription p) {
        if (!(alreadyPrescribed(p))) {
            prescriptions.add(p);
        }
    }

    //EFFECTS: returns true if prescription is already in the patient prescription list, otherwise returns false
    public boolean alreadyPrescribed(Prescription p) {
        if (prescriptions.isEmpty()) {
            return false;
        } else {
            return prescriptions.contains(p);
        }
    }

    //SPECIFIES: the list of prescriptions is not empty
    //MODIFIES: this
    //EFFECTS: removes the prescription from the patient list of prescriptions if it is there, otherwise does nothing
    public void removePrescription(Prescription p) {
        if (prescriptions.contains(p)) {
            prescriptions.remove(p);
        }
    }

    //MODIFIES: this and prescription
    //EFFECTS: takes a dose of the prescription with the given name
    public void takePrescription(String name) {
        for (Prescription p : prescriptions) {
            if (name.equals(p.getName())) {
                p.takeDose();
            }
        }
    }

    //EFFECTS: returns PatientAccount as a new JSONObject containing a list of key strings
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("symptoms", symptoms);
        json.put("prescriptions", prescriptionsToJson());
        return json;
    }

    //EFFECTS: returns the account's list of prescriptions as a JSONArray
    public JSONArray prescriptionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Prescription p : prescriptions) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }
}

