package org.example.cd;

import java.sql.*;

public class AfficherAbonnee {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void afficherAbonnes() {
        String sql = "SELECT id, nom, prenom, numero, email, etat FROM abonnee";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("=== Liste des abonnés ===");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String numero = rs.getString("numero");
                String email = rs.getString("email");
                String etat = rs.getString("etat");

                System.out.printf("- [%d] %s %s | %s | %s | État: %s%n",
                        id, prenom, nom, numero, email, etat);
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}
