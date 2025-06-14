package org.example.cd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class envoyeNotif {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void envoyerNotification() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Envoi de notification personnalisée ===");
        System.out.print("Entrez le titre de la notification : ");
        String titre = scanner.nextLine();
        
        System.out.print("Entrez le message de la notification : ");
        String message = scanner.nextLine();
        
        try {
            // Récupérer tous les abonnés actifs avec leurs noms
            List<Abonne> abonnesActifs = getAbonnesActifs();
            
            if (abonnesActifs.isEmpty()) {
                System.out.println("Aucun abonné actif trouvé.");
                return;
            }
            
            // Enregistrer d'abord dans sendNotif (log admin)
            int notificationId = enregistrerDansSendNotif(titre, message);
            
            // Envoyer la notification à chaque abonné actif
            int count = 0;
            List<String> destinataires = new ArrayList<>();
            for (Abonne abonne : abonnesActifs) {
                NotificationService.envoyerNotification(abonne.getId(), titre, message, abonne.getNom());
                count++;
                destinataires.add(abonne.getNom());
            }
            
            System.out.println("Notification envoyée à " + count + " abonné(s) actif(s).");
            
            // Mettre à jour sendNotif avec le nombre de destinataires et leurs noms
            updateSendNotif(notificationId, count, String.join(", ", destinataires));
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'envoi des notifications : " + e.getMessage());
        }
    }

    private static List<Abonne> getAbonnesActifs() throws SQLException {
        List<Abonne> abonnesActifs = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, email FROM abonnee WHERE etat = 'actif'";
        
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                abonnesActifs.add(new Abonne(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("email")));
            }
        }
        
        return abonnesActifs;
    }
// Méthode pour enregistrer la notification dans la table sendNotif
    private static int enregistrerDansSendNotif(String titre, String message) throws SQLException {
    int id = -1;  
    String sql = "INSERT INTO sendNotif (titre, message, date_envoi, nombre_destinataires, destinataires) VALUES (?, ?, NOW(), 0, '')";
    
    try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        pstmt.setString(1, titre);
        pstmt.setString(2, message);
        pstmt.executeUpdate();
        
        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
        }
    }
    return id;
}
// Méthode pour mettre à jour la table sendNotif avec le nombre de destinataires et leurs noms
    private static void updateSendNotif(int notificationId, int count, String destinataires) throws SQLException {
        String sql = "UPDATE sendNotif SET nombre_destinataires = ?, destinataires = ? WHERE id = ?";
        
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setInt(1, count);
            pstmt.setString(2, destinataires);
            pstmt.setInt(3, notificationId);
            pstmt.executeUpdate();
        }
    }
}

// Classe Abonne pour représenter un abonné
class Abonne {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    public Abonne(int id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;

    }
    
    public String getNom() {
        return nom;
    }
}