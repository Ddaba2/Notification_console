package org.example.cd;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class EmailService {
    // Configuration corrigée de la base de données (nom exact)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif"; // "gestionnoiff" avec deux f
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Configuration SMTP
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = "dabadiallo694@gmail.com";
    private static final String SMTP_PASSWORD = "aimf vljh yjkn shiq";

    /**
     * Envoie un email à tous les abonnés actifs.
     * Cette méthode récupère les abonnés actifs depuis la base de données,
     * envoie un email à chacun d'eux et enregistre l'envoi dans la base de données.
     */
    public static void envoyerEmailsAbonnesActifs() {
        Scanner scanner = new Scanner(System.in);// Scanner passé en paramètre
        System.out.println("\n=== Envoi d'email aux abonnés actifs ===");
        System.out.print("Entrez le sujet de l'email : ");
        String sujet = scanner.nextLine();
        System.out.print("Entrez le contenu de l'email : ");
        String contenu = scanner.nextLine();

        String selectAbonnes = "SELECT numero, email FROM abonnee WHERE etat = 'actif'";
        
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(selectAbonnes)) {

            int emailsEnvoyes = 0;
            int echecs = 0;
            
            System.out.println("\nDébut de l'envoi des emails...");
            
            while (rs.next()) {
                String numeroAbonne = rs.getString("numero");
                String email = rs.getString("email");
                
                System.out.print("Envoi à " + email + " (n°" + numeroAbonne + ")... ");
                
                try {
                    envoyerEmail(email, sujet, contenu);
                    enregistrerEmailEnvoye(con, numeroAbonne, email, sujet, contenu);
                    emailsEnvoyes++;
                    System.out.println("[OK]");
                } catch (Exception e) {
                    echecs++;
                    System.out.println("[ÉCHEC] " + e.getMessage());
                    e.printStackTrace();
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

                Thread.sleep(1000); // Pause anti-rate limiting
            }
            
            System.out.printf("\nRésumé: %d email(s) envoyé(s), %d échec(s)\n", emailsEnvoyes, echecs);
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Envoi interrompu");
        }
    }
    /**
     * Enregistre les détails de l'email envoyé dans la base de données.
     * @param con La connexion à la base de données
     * @param numero Le numéro de l'abonné
     * @param email L'email de l'abonné
     * @param sujet Le sujet de l'email
     * @param contenu Le contenu de l'email
     * @throws SQLException Si une erreur SQL se produit
     */
    private static void enregistrerEmailEnvoye(Connection con, String numero, 
                                             String email, String sujet, 
                                             String contenu) throws SQLException {
        String sql = "INSERT INTO emails_envoyes (numero_abonne, email, sujet, contenu, date_envoi) " +
                    "VALUES (?, ?, ?, ?, NOW())";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, numero);
            pstmt.setString(2, email);
            pstmt.setString(3, sujet);
            pstmt.setString(4, contenu);
            pstmt.executeUpdate();
        }
    }
    /**
     * Envoie un email à un destinataire spécifique.
     * @param to L'adresse email du destinataire
     * @param sujet Le sujet de l'email
     * @param contenu Le contenu de l'email
     * @throws MessagingException Si une erreur se produit lors de l'envoi de l'email
     */
    private static void envoyerEmail(String to, String sujet, String contenu) throws MessagingException, jakarta.mail.MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);
        props.put("mail.smtp.ignorelistener", "true");
        props.put("mail.debug", "true"); // Active les logs SMTP
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(sujet);
            message.setText(contenu);
            Transport.send(message);
        } catch (jakarta.mail.MessagingException e) {
            System.err.println("Échec d'envoi à " + to);
            throw e;
        }
    }
}