package org.example.cd;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Classe pour gérer les notifications
public class NotificationService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String USER = "root";
    private static final String PASS = "";
// Code pour la classe Notification
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    // Méthode pour envoyer une notification à un abonné spécifique
    public static void envoyerNotification(int abonneId, String titre, String message, String expediteur) throws SQLException {
        String sql = "INSERT INTO notification (titre, message, date, lu, abonnee_id, expediteur) VALUES (?, ?, NOW(), FALSE, ?, ?)";
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, titre);
            stmt.setString(2, message);
            stmt.setInt(3, abonneId);
            stmt.setString(4, expediteur);
            stmt.executeUpdate();
            System.out.println("Notification envoyée !");
        }
    }

    // Méthode pour voir les notifications non lues d'un abonné avec le nouveau format d'affichage
    public static void voirNotifications(int abonneId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT id, titre, message, date, expediteur FROM notification WHERE abonnee_id = ? AND lu = FALSE ORDER BY date DESC";
        
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, abonneId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Notification(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("message"),
                        rs.getTimestamp("date").toLocalDateTime(),
                        rs.getString("expediteur")
                    ));
                }
            }
        }

        if (list.isEmpty()) {
            System.out.println(" Aucune nouvelle notification.");
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            // Affichage avec le format tableau
            System.out.println(ANSI_GREEN+"\n=== Notifications non lues ==="+ANSI_RESET);
            System.out.println("+----+----------------------+--------------------------------+---------------------+-----------------+");
            System.out.println("| ID | Titre                | Message                        | Date                | Expéditeur      |");
            System.out.println("+----+----------------------+--------------------------------+---------------------+-----------------+");
            
            for (Notification n : list) {
                String titre = formatField(n.getTitre(), 20);
                String msg = formatField(n.getMessage(), 30);
                String date = n.getDate().format(dtf);
                String expediteur = formatField(n.getExpediteur(), 15);
                
                System.out.printf("| %-2d | %-20s | %-30s | %-19s | %-15s |\n",
                    n.getId(), titre, msg, date, expediteur);
            }
            
            System.out.println("+----+----------------------+--------------------------------+---------------------+-----------------+");
            
            markAsRead(list);
        }
    }

    // Méthode pour formater les champs
    private static String formatField(String field, int maxLength) {
        if (field == null) return "";
        if (field.length() > maxLength) {
            return field.substring(0, maxLength - 3) + "...";
        }
        return field;
    }

    // Méthode pour marquer les notifications comme lues
    private static void markAsRead(List<Notification> list) throws SQLException {
        String sql = "UPDATE notification SET lu = TRUE WHERE id = ?";
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (Notification n : list) {
                ps.setInt(1, n.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    public static void envoyeNotif(int abonneId, String titre, String message) {
        throw new UnsupportedOperationException("Unimplemented method 'envoyeNotif'");
    }
}