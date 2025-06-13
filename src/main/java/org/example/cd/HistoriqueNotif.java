package org.example.cd;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class HistoriqueNotif {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String USER = "root";
    private static final String PASS = "";
    // Codes ANSI pour la coloration du texte dans la console
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Affiche l'historique des notifications envoyées.
     * Cette méthode se connecte à la base de données, récupère les notifications
     * envoyées et les affiche dans un format lisible.
     */
    public static void afficherHistoriqueNotifications() {
        System.out.println(ANSI_GREEN+"\n=== Historique des notifications envoyées ===" + ANSI_RESET);

        String sql = "SELECT id, titre, message, date_envoi, nombre_destinataires, destinataires " +
                    "FROM sendNotif ORDER BY date_envoi DESC";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // En-tête du tableau
            System.out.println("+----+----------------------+--------------------------------+---------------------+------+-----------------+");
            System.out.println("| ID | Titre                | Message                        | Date envoi          | Nb   | Destinataires   |");
            System.out.println("+----+----------------------+--------------------------------+---------------------+------+-----------------+");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                // Formatage des données
                String titre = formatField(rs.getString("titre"), 20);
                String message = formatField(rs.getString("message"), 30);
                String date = dateFormat.format(rs.getTimestamp("date_envoi"));
                int nbDest = rs.getInt("nombre_destinataires");
                String dest = formatField(rs.getString("destinataires"), 15);

                System.out.printf("| %-2d | %-20s | %-30s | %-19s | %-4d | %-15s |\n",
                        rs.getInt("id"), titre, message, date, nbDest, dest);
            }

            System.out.println("+----+----------------------+--------------------------------+---------------------+------+-----------------+");

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Formate un champ pour l'affichage en le tronquant si nécessaire
     * @param field Le champ à formater
     * @param maxLength La longueur maximale
     * @return Le champ formaté
     */
    private static String formatField(String field, int maxLength) {
        if (field == null) return "";
        if (field.length() > maxLength) {
            return field.substring(0, maxLength - 3) + "...";
        }
        return field;
    }
}