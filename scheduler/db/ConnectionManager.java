package scheduler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private final String connectionUrl;
    private final String userName;
    private final String userPass;
    private Connection con;

    public ConnectionManager() {
        String var10001 = System.getenv("Server");
        this.connectionUrl = "jdbc:sqlserver://" + var10001 + ".database.windows.net:1433;database=" + System.getenv("DBName");
        this.userName = System.getenv("UserID");
        this.userPass = System.getenv("Password");
        this.con = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException var2) {
            System.out.println(var2.toString());
        }

    }

    public Connection createConnection() {
        try {
            this.con = DriverManager.getConnection(this.connectionUrl, this.userName, this.userPass);
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

        return this.con;
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }
}
