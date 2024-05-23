package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import scheduler.db.ConnectionManager;

public class Vaccine {
    private final String vaccineName;
    private int availableDoses;

    private Vaccine(VaccineBuilder builder) {
        this.vaccineName = builder.vaccineName;
        this.availableDoses = builder.availableDoses;
    }

    private Vaccine(VaccineGetter getter) {
        this.vaccineName = getter.vaccineName;
        this.availableDoses = getter.availableDoses;
    }

    public String getVaccineName() {
        return this.vaccineName;
    }

    public int getAvailableDoses() {
        return this.availableDoses;
    }

    public void saveToDB() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String addDoses = "INSERT INTO vaccines VALUES (?, ?)";

        try {
            PreparedStatement statement = con.prepareStatement(addDoses);
            statement.setString(1, this.vaccineName);
            statement.setInt(2, this.availableDoses);
            statement.executeUpdate();
        } catch (SQLException var8) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }

    }

    public void increaseAvailableDoses(int num) throws SQLException {
        if (num <= 0) {
            throw new IllegalArgumentException("Argument cannot be negative!");
        } else {
            this.availableDoses += num;
            ConnectionManager cm = new ConnectionManager();
            Connection con = cm.createConnection();
            String removeAvailability = "UPDATE vaccines SET Doses = ? WHERE name = ?;";

            try {
                PreparedStatement statement = con.prepareStatement(removeAvailability);
                statement.setInt(1, this.availableDoses);
                statement.setString(2, this.vaccineName);
                statement.executeUpdate();
            } catch (SQLException var9) {
                throw new SQLException();
            } finally {
                cm.closeConnection();
            }

        }
    }

    public void decreaseAvailableDoses(int num) throws SQLException {
        if (this.availableDoses - num < 0) {
            throw new IllegalArgumentException("Not enough available doses!");
        } else {
            this.availableDoses -= num;
            ConnectionManager cm = new ConnectionManager();
            Connection con = cm.createConnection();
            String removeAvailability = "UPDATE vaccines SET Doses = ? WHERE name = ?;";

            try {
                PreparedStatement statement = con.prepareStatement(removeAvailability);
                statement.setInt(1, this.availableDoses);
                statement.setString(2, this.vaccineName);
                statement.executeUpdate();
            } catch (SQLException var9) {
                throw new SQLException();
            } finally {
                cm.closeConnection();
            }

        }
    }

    public String toString() {
        return "Vaccine{vaccineName='" + this.vaccineName + "', availableDoses=" + this.availableDoses + "}";
    }
}
