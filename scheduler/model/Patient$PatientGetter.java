package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import scheduler.db.ConnectionManager;
import scheduler.util.Util;

public class Patient$PatientGetter {
    private final String username;
    private final String password;
    private byte[] salt;
    private byte[] hash;

    public Patient$PatientGetter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Patient get() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String getPatient = "SELECT Salt, Hash FROM Patients WHERE Username = ?";

        Patient var9;
        try {
            PreparedStatement statement = con.prepareStatement(getPatient);
            statement.setString(1, this.username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                Object var15 = null;
                return (Patient)var15;
            }

            byte[] salt = resultSet.getBytes("Salt");
            byte[] hash = Util.trim(resultSet.getBytes("Hash"));
            byte[] calculatedHash = Util.generateHash(this.password, salt);
            if (!Arrays.equals(hash, calculatedHash)) {
                var9 = null;
                return var9;
            }

            this.salt = salt;
            this.hash = hash;
            var9 = new Patient(this);
        } catch (SQLException var13) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }

        return var9;
    }
}
