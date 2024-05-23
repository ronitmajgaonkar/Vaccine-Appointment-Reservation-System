//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scheduler.db.ConnectionManager;
import scheduler.model.Caregiver;
import scheduler.model.Patient;
import scheduler.model.Vaccine;
import scheduler.util.Util;

public class Scheduler {
    private static Caregiver currentCaregiver = null;
    private static Patient currentPatient = null;

    public Scheduler() {
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println("Welcome to the COVID-19 Vaccine Reservation Scheduling Application!");
        System.out.println("*** Please enter one of the following commands ***");
        System.out.println("> create_patient <username> <password>");
        System.out.println("> create_caregiver <username> <password>");
        System.out.println("> login_patient <username> <password>");
        System.out.println("> login_caregiver <username> <password>");
        System.out.println("> search_caregiver_schedule <date>");
        System.out.println("> reserve <date> <vaccine>");
        System.out.println("> upload_availability <date>");
        System.out.println("> cancel <appointment_id>");
        System.out.println("> add_doses <vaccine> <number>");
        System.out.println("> show_appointments");
        System.out.println("> logout");
        System.out.println("> quit");
        System.out.println();
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.print("> ");
            String response = "";

            try {
                response = r.readLine();
            } catch (IOException var5) {
                System.out.println("Please try again!");
            }

            String[] tokens = response.split(" ");
            if (tokens.length == 0) {
                System.out.println("Please try again!");
            } else {
                String operation = tokens[0];
                if (operation.equals("create_patient")) {
                    createPatient(tokens);
                } else if (operation.equals("create_caregiver")) {
                    createCaregiver(tokens);
                } else if (operation.equals("login_patient")) {
                    loginPatient(tokens);
                } else if (operation.equals("login_caregiver")) {
                    loginCaregiver(tokens);
                } else if (operation.equals("search_caregiver_schedule")) {
                    searchCaregiverSchedule(tokens);
                } else if (operation.equals("reserve")) {
                    reserve(tokens);
                } else if (operation.equals("upload_availability")) {
                    uploadAvailability(tokens);
                } else if (operation.equals("cancel")) {
                    cancel(tokens);
                } else if (operation.equals("add_doses")) {
                    addDoses(tokens);
                } else if (operation.equals("show_appointments")) {
                    showAppointments(tokens);
                } else if (operation.equals("logout")) {
                    logout(tokens);
                } else {
                    if (operation.equals("quit")) {
                        System.out.println("Bye!");
                        return;
                    }

                    System.out.println("Invalid operation name!");
                }
            }
        }
    }

    private static void createPatient(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Failed to create user.");
        } else {
            String username = tokens[1];
            String password = tokens[2];
            if (usernameExistsPatient(username)) {
                System.out.println("Username taken, try again!");
            } else if (!strongPassword(password)) {
                System.out.println("Please create a stronger password!");
            } else {
                byte[] salt = Util.generateSalt();
                byte[] hash = Util.generateHash(password, salt);

                try {
                    Patient patient = (new Patient.PatientBuilder(username, salt, hash)).build();
                    patient.saveToDB();
                    System.out.println("Created user " + username);
                } catch (SQLException var6) {
                    System.out.println("Failed to create user.");
                    var6.printStackTrace();
                }

            }
        }
    }

    private static boolean usernameExistsPatient(String username) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String selectUsername = "SELECT * FROM Patients WHERE Username = ?";

        try {
            PreparedStatement statement = con.prepareStatement(selectUsername);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            boolean var6 = resultSet.isBeforeFirst();
            return var6;
        } catch (SQLException var10) {
            System.out.println("Error occurred when checking username");
            var10.printStackTrace();
        } finally {
            cm.closeConnection();
        }

         return true;
    }

    private static void createCaregiver(String[] tokens) {
        if (tokens.length != 3) {
            System.out.println("Failed to create user.");
        } else {
            String username = tokens[1];
            String password = tokens[2];
            if (usernameExistsCaregiver(username)) {
                System.out.println("Username taken, try again!");
            } else if (!strongPassword(password)) {
                System.out.println("Please create a stronger password!");
            } else {
                byte[] salt = Util.generateSalt();
                byte[] hash = Util.generateHash(password, salt);

                try {
                    Caregiver caregiver = (new Caregiver.CaregiverBuilder(username, salt, hash)).build();
                    caregiver.saveToDB();
                    System.out.println("Created user " + username);
                } catch (SQLException var6) {
                    System.out.println("Failed to create user.");
                    var6.printStackTrace();
                }

            }
        }
    }

    private static boolean usernameExistsCaregiver(String username) {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String selectUsername = "SELECT * FROM Caregivers WHERE Username = ?";

        try {
            PreparedStatement statement = con.prepareStatement(selectUsername);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            boolean var6 = resultSet.isBeforeFirst();
            return var6;
        } catch (SQLException var10) {
            System.out.println("Error occurred when checking username");
            var10.printStackTrace();
        } finally {
            cm.closeConnection();
        }

        return true;
    }

    private static boolean strongPassword(String password) {
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters!");
            return false;
        } else if (password != password.toUpperCase() && password != password.toLowerCase()) {
            if (!password.matches(".*[a-zA-Z].*") && !password.matches(".*\\d.*")) {
                System.out.println("Password must be a mixture of letters and numbers!");
                return false;
            } else if (!password.matches(".*[!@#?].*")) {
                System.out.println("Password must include at least one special character, from “!”, “@”, “#”, “?”");
                return false;
            } else {
                return true;
            }
        } else {
            System.out.println("Password must be a mixture of both uppercase and lowercase letters!");
            return false;
        }
    }

    private static void loginPatient(String[] tokens) {
        if (currentPatient == null && currentCaregiver == null) {
            if (tokens.length != 3) {
                System.out.println("Login failed.");
            } else {
                String username = tokens[1];
                String password = tokens[2];
                Patient patient = null;

                try {
                    patient = (new Patient.PatientGetter(username, password)).get();
                } catch (SQLException var5) {
                    System.out.println("Login failed.");
                    var5.printStackTrace();
                }

                if (patient == null) {
                    System.out.println("Login failed.");
                } else {
                    System.out.println("Logged in as: " + username);
                    currentPatient = patient;
                }

            }
        } else {
            System.out.println("User already logged in");
        }
    }

    private static void loginCaregiver(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            if (tokens.length != 3) {
                System.out.println("Login failed.");
            } else {
                String username = tokens[1];
                String password = tokens[2];
                Caregiver caregiver = null;

                try {
                    caregiver = (new Caregiver.CaregiverGetter(username, password)).get();
                } catch (SQLException var5) {
                    System.out.println("Login failed.");
                    var5.printStackTrace();
                }

                if (caregiver == null) {
                    System.out.println("Login failed.");
                } else {
                    System.out.println("Logged in as: " + username);
                    currentCaregiver = caregiver;
                }

            }
        } else {
            System.out.println("User already logged in.");
        }
    }

    private static void searchCaregiverSchedule(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first!");
        } else if (tokens.length != 2) {
            System.out.println("Please try again!");
        } else {
            String date = tokens[1];
            ConnectionManager cm = new ConnectionManager();
            Connection con = cm.createConnection();
            String username = " ";

            try {
                Date d = Date.valueOf(date);
                String caregiverSchedule = "SELECT C.Username, V.name, V.doses FROM Caregivers C, Availabilities A, Vaccines V WHERE A.Username = C.Username AND A.Time = ? ORDER BY C.Username";
                PreparedStatement statement = con.prepareStatement(caregiverSchedule);
                statement.setDate(1, d);
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    username = resultSet.getString("Username");
                    String vaccineName = resultSet.getString("Name");
                    int availableDoses = resultSet.getInt("Doses");
                    System.out.println("Caregiver: " + username + " Vaccine: " + vaccineName + " Doses: " + availableDoses);
                }

                if (username.equals(" ")) {
                    System.out.println("No availability in the schedule on this date");
                    return;
                }
            } catch (IllegalArgumentException var11) {
                System.out.println("Please enter a valid date!");
            } catch (SQLException var12) {
                System.out.println("Error when searching for schedule");
                var12.printStackTrace();
            }

        }
    }

    private static void reserve(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first!");
        } else if (currentPatient == null) {
            System.out.println("Please login as a patient!");
        } else if (tokens.length != 3) {
            System.out.println("Please try again!");
        } else {
            String date = tokens[1];
            ConnectionManager cm = new ConnectionManager();
            Connection con = cm.createConnection();
            String availableCaregiver = "SELECT Username FROM Availabilities WHERE Time = ? ORDER BY Username";
            String assignedCaregiver = " ";

            try {
                Date d = Date.valueOf(date);
                PreparedStatement statement = con.prepareStatement(availableCaregiver);
                statement.setDate(1, d);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    availableCaregiver = resultSet.getString("Username");
                    assignedCaregiver = availableCaregiver;
                }

                if (assignedCaregiver.equals(" ")) {
                    System.out.println("No Caregiver is available!");
                    return;
                }
            } catch (SQLException var30) {
                System.out.println("Error occurred while checking available caregivers");
                var30.printStackTrace();
            } catch (IllegalArgumentException var31) {
                System.out.println("Please enter a valid date!");
            }

            String vaccineName = tokens[2];
            int availableDoses = 0;
            String availableVaccine = "SELECT Name, Doses FROM Vaccines WHERE Name = ?";

            try {
                PreparedStatement statement = con.prepareStatement(availableVaccine);
                statement.setString(1, vaccineName);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    availableDoses = resultSet.getInt("Doses");
                }

                if (availableDoses == 0) {
                    System.out.println("Not enough available doses!");
                    return;
                }
            } catch (SQLException var29) {
                System.out.println("Error occured while checking vaccine dose availability");
                var29.printStackTrace();
            }

            String appointment_id = "SELECT MAX(appointment_id) FROM Appointments";
            int current_id = 0;

            try {
                PreparedStatement statement = con.prepareStatement(appointment_id);
                ResultSet resultSet = statement.executeQuery();
                current_id = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            } catch (SQLException var28) {
                System.out.println("Error occured while getting the appointment_id");
                var28.printStackTrace();
            }

            String insertAppointment = "INSERT INTO Appointments VALUES (?, ?, ?, ?, ?)";

            try {
                Date d = Date.valueOf(date);
                PreparedStatement statement = con.prepareStatement(insertAppointment);
                statement.setInt(1, current_id);
                statement.setDate(2, d);
                statement.setString(3, availableCaregiver);
                statement.setString(4, currentPatient.getUsername());
                statement.setString(5, vaccineName);
                statement.executeUpdate();
            } catch (SQLException var27) {
                System.out.println("Error occured while inserting appointment");
                var27.printStackTrace();
            }

            String deleteAvailability = "DELETE FROM Availabilities WHERE Time = ? AND Username = ?";

            PreparedStatement statement;
            try {
                Date d = Date.valueOf(date);
                statement = con.prepareStatement(deleteAvailability);
                statement.setDate(1, d);
                statement.setString(2, availableCaregiver);
                statement.executeUpdate();
            } catch (SQLException var26) {
                System.out.println("Error occured while updating caregiver availability");
                var26.printStackTrace();
            }

            String decreaseDoses = "UPDATE Vaccines SET Doses = Doses - 1 WHERE Name = ?";

            try {
                statement = con.prepareStatement(decreaseDoses);
                statement.setString(1, vaccineName);
                statement.executeUpdate();
            } catch (SQLException var24) {
                System.out.println("Error occured while updating caregiver availability");
                var24.printStackTrace();
            } finally {
                cm.closeConnection();
            }

            System.out.println("Appointment ID: " + current_id + ", Caregiver username: " + availableCaregiver);
        }
    }

    private static void uploadAvailability(String[] tokens) {
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
        } else if (tokens.length != 2) {
            System.out.println("Please try again!");
        } else {
            String date = tokens[1];

            try {
                Date d = Date.valueOf(date);
                currentCaregiver.uploadAvailability(d);
                System.out.println("Availability uploaded!");
            } catch (IllegalArgumentException var3) {
                System.out.println("Please enter a valid date!");
            } catch (SQLException var4) {
                System.out.println("Error occurred when uploading availability");
                var4.printStackTrace();
            }

        }
    }

    private static void cancel(String[] tokens) {
    }

    private static void addDoses(String[] tokens) {
        if (currentCaregiver == null) {
            System.out.println("Please login as a caregiver first!");
        } else if (tokens.length != 3) {
            System.out.println("Please try again!");
        } else {
            String vaccineName = tokens[1];
            int doses = Integer.parseInt(tokens[2]);
            Vaccine vaccine = null;

            try {
                vaccine = (new Vaccine.VaccineGetter(vaccineName)).get();
            } catch (SQLException var7) {
                System.out.println("Error occurred when adding doses");
                var7.printStackTrace();
            }

            if (vaccine == null) {
                try {
                    vaccine = (new Vaccine.VaccineBuilder(vaccineName, doses)).build();
                    vaccine.saveToDB();
                } catch (SQLException var6) {
                    System.out.println("Error occurred when adding doses");
                    var6.printStackTrace();
                }
            } else {
                try {
                    vaccine.increaseAvailableDoses(doses);
                } catch (SQLException var5) {
                    System.out.println("Error occurred when adding doses");
                    var5.printStackTrace();
                }
            }

            System.out.println("Doses updated!");
        }
    }

    private static void showAppointments(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first!");
        } else if (tokens.length != 1) {
            System.out.println("Please try again!");
        } else {
            try {
                String query;
                if (currentCaregiver != null) {
                    query = "SELECT appointment_id, vaccine_name, Time, patient_name FROM Appointments WHERE caregiver_name = ? ORDER BY appointment_id";
                } else {
                    query = "SELECT appointment_id, vaccine_name, Time, caregiver_name FROM Appointments WHERE patient_name = ? ORDER BY appointment_id";
                }

                ConnectionManager cm = new ConnectionManager();
                Connection con = cm.createConnection();
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, currentCaregiver != null ? currentCaregiver.getUsername() : currentPatient.getUsername());
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointment_id");
                    String vaccineName = resultSet.getString("vaccine_name");
                    Date date = resultSet.getDate("Time");
                    String patientUsername;
                    if (currentCaregiver != null) {
                        patientUsername = resultSet.getString("patient_name");
                        System.out.println("Appointment ID: " + appointmentId + ", Vaccine Name: " + vaccineName + ", Date: " + date + ", Patient Name: " + patientUsername);
                    } else {
                        patientUsername = resultSet.getString("caregiver_name");
                        System.out.println("Appointment ID: " + appointmentId + ", Vaccine Name: " + vaccineName + ", Date: " + date + ", Caregiver Name: " + patientUsername);
                    }
                }
            } catch (SQLException var10) {
                System.out.println("Please try again!");
                var10.printStackTrace();
            }

        }
    }

    private static void logout(String[] tokens) {
        if (currentCaregiver == null && currentPatient == null) {
            System.out.println("Please login first!");
        } else if (tokens.length != 1) {
            System.out.println("Please try again!");
        } else {
            currentPatient = null;
            currentCaregiver = null;
            System.out.println("Successfully logged out!");
        }
    }
}
