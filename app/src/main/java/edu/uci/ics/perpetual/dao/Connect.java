package dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect implements AutoCloseable {

    Connection connection;

    public Connect(String type) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (type.equals("server")) {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://sensoria-mysql.ics.uci.edu:3306/tippersdb_restored?useSSL=false&serverTimezone=PST",
                        "tippersUser", "tippers2018");
            }
            if (type.equals("local")) {
                String user = null, pwd = null;
                try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("credential.txt")))) {
                    user = br.readLine();
                    pwd = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/localization?useSSL=false&serverTimezone=PST",
                        user, pwd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
