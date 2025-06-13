package org.example.cd;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Établir la connexion
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie à la base de données.");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur : Driver JDBC non trouvé.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Erreur : Échec de la connexion à la base de données.");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        // Tester la connexion
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

