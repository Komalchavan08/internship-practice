package util;

import java.sql.Connection;
import java.sql.DriverManager;



public class DBConnection {

    private static final String url = "jdbc:mysql://localhost:3306/student_db";
    private static final String User = "root";
    private static final String Password = "root";

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(url, User, Password);

            return con;
        } catch (Exception e) {

            System.out.println("connection failed");
            e.printStackTrace();
            return null;
        }
    }

}
