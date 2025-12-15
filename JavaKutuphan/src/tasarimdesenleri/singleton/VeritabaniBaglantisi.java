package tasarimdesenleri.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class VeritabaniBaglantisi {
    private static VeritabaniBaglantisi instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/kutuphane";
    private final String USER = "root";
    private final String PASSWORD = "";

    private VeritabaniBaglantisi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized VeritabaniBaglantisi getInstance() {
        if (instance == null) {
            instance = new VeritabaniBaglantisi();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}