package scheduler.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import scheduler.db.ConnectionManager;
import scheduler.util.Util;

public class Caregiver$CaregiverGetter {
    private final String username;
    private final String password;
    private byte[] salt;
    private byte[] hash;

    public Caregiver$CaregiverGetter(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Caregiver get() throws SQLException {
        ConnectionManager cm = new ConnectionManager();
        Connection con = cm.createConnection();
        String getCaregiver = "SELECT Salt, Hash FROM Caregivers WHERE Username = ?";

        Caregiver var9;
        try {
            PreparedStatement statement = con.prepareStatement(getCaregiver);
            statement.setString(1, this.username);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                Object var15 = null;
                return (Caregiver)var15;
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
            var9 = new Caregiver(this);
        } catch (SQLException var13) {
            throw new SQLException();
        } finally {
            cm.closeConnection();
        }

        return var9;
    }
}
