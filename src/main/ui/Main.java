package ui;

import java.io.FileNotFoundException;

//Creates a new PrescriptionApp to be run
public class Main {
    public static void main(String[] args) {
        try {
            new PrescriptionApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
