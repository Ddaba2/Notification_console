package org.example.cd;

import java.sql.*;
import java.text.SimpleDateFormat;

public class HistoriqueEmail {
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
    public static void afficherHistoriqueEmails() {
        System.out.println(ANSI_GREEN+"\n=== Historique des emails envoyées ===" + ANSI_RESET);

        String sql = "SELECT id, numero_abonne, email, sujet, contenu, date_envoi " +
                    "FROM emails_envoyes ORDER BY date_envoi DESC";

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // En-tête du tableau
            System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");
            System.out.println("| ID   | Sujet                | Message                        | Date envoi          |Numéro        | Email du destinataires|");
            System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (rs.next()) {
                // Formatage des données
                String titre = formatField(rs.getString("sujet"), 20);
                String message = formatField(rs.getString("contenu"), 30);
                String date = dateFormat.format(rs.getTimestamp("date_envoi"));
                int nbDest = rs.getInt("numero_abonne");
                String dest = formatField(rs.getString("email"), 15);

                System.out.printf("| %-2d | %-20s | %-30s | %-19s | %-4d | %-15s |\n",
                        rs.getInt("id"), titre, message, date, nbDest, dest);
            }

            System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Formate un champ pour l'affichage en le tronquant si nécessaire
     *
     * @param field     Le champ à formater
     * @param maxLength La longueur maximale
     * @param abonneId
     * @return Le champ formaté
     */
   


//Méthode pour afficher les emails reçus par un abonnée
public static void afficherEmails(int abonneId) {
    System.out.println(ANSI_GREEN+"\n=== Emails non lues ===" + ANSI_RESET);

    String sql = "SELECT id, numero_abonne, email, sujet, contenu, date_envoi " +
            "FROM emails_envoyes ORDER BY date_envoi DESC";

    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        // En-tête du tableau
        System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");
        System.out.println("| ID   | Sujet                | Message                        | Date envoi          |Numéro        | Email du destinataires|");
        System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        while (rs.next()) {
            // Formatage des données
            String titre = formatField(rs.getString("sujet"), 20);
            String message = formatField(rs.getString("contenu"), 30);
            String date = dateFormat.format(rs.getTimestamp("date_envoi"));
            int nbDest = rs.getInt("numero_abonne");
            String dest = formatField(rs.getString("email"), 15);

            System.out.printf("| %-2d | %-20s | %-30s | %-19s | %-4d | %-15s |\n",
                    rs.getInt("id"), titre, message, date, nbDest, dest);
        }

        System.out.println("+------+----------------------+--------------------------------+---------------------+--------------+-----------------------+");

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