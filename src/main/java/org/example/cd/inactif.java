package org.example.cd;

import java.sql.*;
import java.util.Scanner;

public class inactif {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

private static void inactif(Scanner scanner) {
    System.out.print("Entrez le numéro à désactiver : ");
    String numero = scanner.nextLine();
    String sql = "UPDATE abonnee SET etat = ? WHERE numero = ?"; // attention à l'orthographe

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, "inactif");
        pstmt.setString(2, numero);

        int rows = pstmt.executeUpdate();
        System.out.println(rows + " abonné(s) désactivé(s).");
    } catch (SQLException e) {
        System.err.println("Erreur SQL : " + e.getMessage());
    }
}
}
