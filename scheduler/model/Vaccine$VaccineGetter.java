package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scheduler.db.ConnectionManager;

public class Vaccine$VaccineGetter {
    private final String vaccineName;
    private int availableDoses;

    public Vaccine$VaccineGetter(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public Vaccine get() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String getVaccine = "SELECT Name, Doses FROM Vaccines WHERE Name = ?";

        Vaccine var6;
        try {
            PreparedStatement statement = con.prepareStatement(getVaccine);
            statement.setString(1, this.vaccineName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                this.availableDoses = resultSet.getInt("Doses");
                var6 = new Vaccine(this);
                return var6;
            }

            var6 = null;
        } catch (SQLException var10) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }

        return var6;
    }
}
