package ui;

import model.PatientAccount;
import model.Prescription;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

// myPrescription application that enables display, tracking and editing of prescriptions
// in an account

//citation: cs210 TellerApp
public class PrescriptionApp {
    private static final String JSON_STORE_FILE = "./data/patientaccount.json";
    private PatientAccount myAccount;
    private Prescription p1;
    private Prescription p2;
    private Prescription p3;
    private Prescription p4;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // EFFECTS: runs the prescription application
    public PrescriptionApp() throws FileNotFoundException {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(JSON_STORE_FILE);
        jsonReader = new JsonReader(JSON_STORE_FILE);
        runPrescription();
    }

    //citation: cs210 TellerApp
    // MODIFIES: this
    // EFFECTS: processes the input from user
    private void runPrescription() {
        boolean continueProgram = true;
        String userInput = null;
        input.useDelimiter("\n");

        initializePatientAccount();

        while (continueProgram) {
            welcomePage();
            userInput = input.next();
            userInput = userInput.toLowerCase();

            if (userInput.equals("exit")) {
                continueProgram = false;
            } else {
                processInput(userInput);
            }
        }

        System.out.println("\nThank You!");
    }

    //citation: cs210 TellerApp
    // MODIFIES: this
    // EFFECTS: processes input from user
    private void processInput(String input) {
        if (input.equals("display")) {
            printPrescriptionNames(myAccount.getPrescriptions());
        } else if (input.equals("view")) {
            prescriptionViewMenu();
        } else if (input.equals("edit")) {
            prescriptionEditMenu();
        } else if (input.equals("add")) {
            addToPrescriptions();
        } else if (input.equals("remove")) {
            removeFromPrescriptions();
        } else if (input.equals("save")) {
            savePatientAccount();
        } else if (input.equals("load")) {
            loadPatientAccount();
        } else {
            System.out.println("Command not found");
        }

    }

    //citation: scanner usage by cs210 TellerApp
    // MODIFIES: this
    // EFFECTS: initializes patient account and four default prescriptions that have been added
    private void initializePatientAccount() {
        myAccount = new PatientAccount("my prescription account", 2004,
                "stomach ache, sore throat, frequent migraines, anxiety, insomnia");

        //rizatriptan  (built-in prescription)
        p1 = new Prescription("rizatriptan", "CL34", 30, "50 mg",
                "refrigerate bottle", 6, 2, LocalDate.of(2023, 2, 14));
        //escitalopram pill (built-in prescription)
        p2 = new Prescription("escitalopram", "P20", 60, "10 mg",
                "take with food", 24, 1, LocalDate.of(2023, 1, 20));
        //oxacillin pill (built-in prescription)
        p3 = new Prescription("oxacillin", "biocraft 14", 50, "50 mg",
                "take before bed", 7, 3, LocalDate.of(2023, 12, 1));
        p4 = new Prescription("ibuprofen", "I-2", 100, "10 mg",
                "take after meal", 6, 4, LocalDate.of(2022, 11, 29));


        myAccount.addPrescription(p1);
        myAccount.addPrescription(p2);
        myAccount.addPrescription(p3);
        myAccount.addPrescription(p4);

        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

    // MODIFIES: this
    // EFFECTS: displays a welcome page with options for user input
    private void welcomePage() {
        System.out.println("\nWelcome to myPrescriptionApp! Select an action from below:");
        System.out.println("\tdisplay -> display all prescriptions in account");
        System.out.println("\tview -> view prescription information");
        System.out.println("\tedit -> edit prescription dose schedule or take a dosage");
        System.out.println("\tadd -> add prescription");
        System.out.println("\tremove -> remove prescription");
        System.out.println("\texit -> exit program");
        System.out.println("\tsave -> save account details");
        System.out.println("\tload -> load account details");
        System.out.println("\n");
    }

    // EFFECTS: displays options for user to specify prescription data and prints information specified
    private void prescriptionViewMenu() {
        Prescription selected = selectPrescription();
        if (!(selected == null)) {
            System.out.println("\tschedule -> view dosage schedule");
            System.out.println("\tamount -> retrieve number of pills left");
            System.out.println("\tinstructions -> retrieve dosage instructions");
            System.out.print("\tdates -> retrieve next refill date and last refill date\n");

            String direction = input.next();
            if (direction.equals("schedule")) {
                System.out.println(selected.getDoseSchedule());
            } else if (direction.equals("amount")) {
                System.out.println(selected.getPillNumber());
            } else if (direction.equals("instructions")) {
                System.out.println("take " + selected.getDosage() + " and " + selected.getDirections());
            } else if (direction.equals("dates")) {
                System.out.println("next refill date: " + selected.getRefillDate());
                System.out.println("last refill date: " + selected.getLastRefill());
            } else {
                System.out.println("Command not found");
            }
        } else {
            System.out.println("Try again!");
            prescriptionViewMenu();
        }
    }

    // EFFECTS: displays options for user to edit prescription data
    private void prescriptionEditMenu() {
        Prescription selected = selectPrescription();
        if (!(selected == null)) {
            System.out.println("\treschedule -> reschedule time of first dose");
            System.out.println("\ttake -> take a dose of the prescription ");

            String direction = input.next();
            if (direction.equals("reschedule")) {
                System.out.print("Redirecting to schedule menu");
                editSchedule(selected);
            } else if (direction.equals("take")) {
                takeADose(selected);
            } else {
                System.out.println("Command not found");
            }
        } else {
            System.out.println("Try again!");
            prescriptionEditMenu();
        }
    }

    // EFFECTS: saves the PatientAccount to file (patientaccount.json)
    private void savePatientAccount() {
        try {
            jsonWriter.open();
            jsonWriter.write(myAccount);
            jsonWriter.close();
            System.out.println("Saved " + myAccount.getName() + " to " + JSON_STORE_FILE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE_FILE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads PatientAccount from file (patientaccount.json)
    private void loadPatientAccount() {
        try {
            myAccount = jsonReader.read();
            System.out.println("Loaded " + myAccount.getName() + " from " + JSON_STORE_FILE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE_FILE);
        }
    }

    // MODIFIES: this and Prescription
    // EFFECTS: edits the dosage schedule for a given prescription to match user's preferences
    private void editSchedule(Prescription pill) {
        System.out.print("\nEnter first dose time in format: HH:DD \n");
        String time = input.next();
        pill.editSchedule(time);
        System.out.print("Here is your new dosage schedule for " + pill.getName() + ": ");
        System.out.print(pill.getDoseSchedule());
    }

    // MODIFIES: this and Prescription
    // EFFECTS: takes a dose from the given prescription or does nothing
    private void takeADose(Prescription pill) {
        if (!(pill.getDoseSchedule().contains(LocalTime.now()))) {
            System.out.print("Time does not match dose schedule. Enter y to confirm dosage or n to exit. \n");
        } else {
            System.out.print("Time matches dosage schedule. Enter y to confirm dosage or n to exit. \n");
        }

        String confirmation = input.next();
        if (confirmation.equals("y")) {
            pill.takeDose();
            System.out.print("Dose was taken!");
            System.out.print("\n Doses remaining of " + pill.getName() + ": " + pill.getPillNumber());
            System.out.print("\n You have taken " + pill.getDailyCounter() + " dose of " + pill.getName() + " today.");
        } else {
            System.out.print("Dose not taken.");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a Prescription to a PatientAccount
    private void addToPrescriptions() {
        System.out.print("\nEnter prescription information:\n");

        System.out.print("\tEnter prescription name\n");
        String name = input.next();

        System.out.print("\tEnter prescription imprint/id code (String)\n");
        String id = input.next();
        id.toLowerCase();

        System.out.print("\tEnter number of pills/treatments (int)\n");
        Integer number = input.nextInt();

        System.out.print("\tEnter dose amount with units\n");
        String dose = input.next();

        System.out.print("\tEnter prescription directions/usage\n");
        String directions = input.next();

        System.out.print("\tEnter buffer time in hrs (int)\n");
        Integer buffer = input.nextInt();

        System.out.print("\tEnter frequency of doses per day (int)\n");
        Integer frequency = input.nextInt();

        System.out.print("\tEnter date of refill in format 'YYYY/MM/DD'\n");
        LocalDate refillDate = dateSetter(input.next());

        Prescription newPrescription = new Prescription(name, id, number, dose, directions,
                buffer, frequency, refillDate);
        myAccount.addPrescription(newPrescription);
    }

    // MODIFIES: this and PatientAccount
    // EFFECTS: removes a Prescription from a PatientAccount
    private void removeFromPrescriptions() {
        Prescription selected = selectPrescription();
        if (!(selected == null)) {
            myAccount.removePrescription(selected);
            System.out.println(selected.getName() + " was removed from your prescriptions");
        } else {
            removeFromPrescriptions();
        }
    }

    // EFFECTS: prompts user to specify a Prescription and returns it
    private Prescription selectPrescription() {
        String selection = "";
        while (selection.length() == 0) {
            System.out.println("Enter prescription name");
            selection = input.next();
        }

        for (Prescription p : myAccount.getPrescriptions()) {
            if (selection.equals(p.getName())) {
                return p;
            }
        }
        System.out.println("Cannot find in prescription list");
        return null;
    }

    // EFFECTS: prints out every Prescription listed in the PatientAccount on the screen
    private void printPrescriptionNames(List<Prescription> prescriptionMenu) {
        System.out.println("Your prescriptions are: \n");
        for (Prescription p : prescriptionMenu) {
            System.out.println(p.getName());
        }
    }

    //REQUIRES: string of length 10 in 24 hr time format (ex. "2024/12/24")
    //EFFECTS: returns LocalDate from specified string date
    public LocalDate dateSetter(String date) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 7));
        int day = Integer.valueOf(date.substring(8, date.length()));
        LocalDate formattedDate = LocalDate.of(year, month, day);
        return formattedDate;
    }
}
