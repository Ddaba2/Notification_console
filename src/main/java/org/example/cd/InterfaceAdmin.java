package org.example.cd;//import AbonneeService;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;




public class InterfaceAdmin {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

   

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static void demarrer(Scanner scanner) {
        // Texte d'accueil avec encadrement coloré
        String welcome = "=== Interface Admin ===";
        System.out.println(ANSI_GREEN + welcome + ANSI_RESET);


        System.out.print("Entrez le mot de passe admin : ");
        String mot_de_passeAdmin = scanner.nextLine();
        // Authentification de l'administrateur
        if (authentifierAdmin(mot_de_passeAdmin)) {
            System.out.println("Connexion admin réussie.");
            afficherMenuAdmin(scanner);
        } else {
            System.out.println("Mot de passe incorrect. Retour au menu abonné.");
        }
    }

    private static boolean authentifierAdmin(String mot_de_passeAdmin) {
 
        String query = "SELECT mot_de_passe  FROM admin WHERE mot_de_passe = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, mot_de_passeAdmin);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return false;
        }
    }

    private static void afficherMenuAdmin(Scanner scanner) {
        boolean quitter = false;

        while (!quitter) {
            System.out.println(ANSI_GREEN +"\n=== Menu Admin ===" + ANSI_RESET);
            System.out.println("1. Ajouter un abonné");
            System.out.println("2. Désactiver un abonné");
            System.out.println("3. Envoyer une notifications");
            System.out.println("4. Envoyer un email");
            System.out.println("5. Voire notifications envoyées");
            System.out.println("6. Voire emails envoyés");
            System.out.println("7. Afficher les abonnés");
            System.out.println("8. Se déconnecter");
            System.out.println("9. Quitter");
            System.out.print("Votre choix : ");
            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    System.out.println("Ajout d'un abonné...");
                    Add.ajouterAbonnee();
                    break;
                case "2":
                    System.out.println("Désactivation d'un abonné...");
                    inactif(scanner);
                    break;
                case "3":
                    System.out.println("Envoi d'une notification...");
                    envoyeNotif.envoyerNotification();
                    break;
                case "4":
                    EmailService.envoyerEmailsAbonnesActifs();
                    break;     
                case "5":
                  HistoriqueNotif.afficherHistoriqueNotifications();
                    break;
                case "6":
                  HistoriqueEmail.afficherHistoriqueEmails();
                    break;
                case "7":
                    System.out.println("Voici la liste des abonnés...");
                    AfficherAbonnee.afficherAbonnes();
                    break;

                case "8":
                    System.out.println("Déconnexion de l'interface admin...");
                    quitter = true;
                    break;

                case "9":
                    quitter = true;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }   
    }
    


 private static void envoyerNotificationsEmail(Scanner scanner) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'envoyerNotificationsEmail'");
    }

 private static void afficherHistoriqueAdmin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherHistoriqueAdmin'");
    }

 

 private static void voirNotif() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'voirNotif'");
    }

 private static void envoyerNotifications() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'envoyerNotifications'");
    }

 private static void voirNotifications(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'voirNotifications'");
    }

// Désactivé un abonné avec une mise à jour de son état dans la base de données
private static void inactif(Scanner scanner) {
    System.out.print("Entrez le numéro à désactiver : ");
    String numero = scanner.nextLine();

    String sql = "UPDATE abonnee SET etat = ? WHERE numero = ?";

    try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, "inactif");
        pstmt.setString(2, numero);

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            System.out.println(rows + " abonné désactivé(s).");
        } else {
            System.out.println("Aucun abonné trouvé avec ce numéro.");
        }
    } catch (SQLException e) {
        System.err.println("Erreur SQL : " + e.getMessage());
    }
}
   
    private static void emailEnvoye() {
     
        System.out.println("Envoi d'un email...");
        // Vous pouvez appeler la méthode de votre classe emailEnvoye ici
    }
}
