package org.example.cd;

import java.sql.*;
import java.util.Scanner;

public class InterfaceAdmin {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        demarrer();
    }

    public static void demarrer() {
        afficherEntete("Interface Admin");
        
        if (!authentifierAdmin()) {
            System.out.println("Accès refusé. Retour au menu principal.");
            return;
        }

        boolean continuer = true;
        while (continuer) {
            afficherMenu();
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1" -> AjouterAbonnee();
                case "2" -> desactiverAbonne();
                case "3" -> EnvoyerNotification();
                case "4" -> EmailService.envoyerEmailsAbonnesActifs();
                case "5" -> afficherHistorique();
                case "6" -> afficherHistorique();
                case "7" -> AfficherAbonnee.afficherAbonnes();
                case "8" -> {
                    System.out.println("Déconnexion...");
                    continuer = false;
                }
                case "9" -> {
                    System.out.println("Fermeture de l'application...");
                    System.exit(0);
                }
                default -> System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static Object afficherHistorique() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afficherHistorique'");
    }

    private static Object EnvoyerNotification() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'EnvoyerNotification'");
    }

    private static Object AjouterAbonnee() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'AjouterAbonnee'");
    }

    private static boolean authentifierAdmin() {
        System.out.print("Entrez le mot de passe admin : ");
        String motDePasse = scanner.nextLine();

        String query = "SELECT COUNT(*) FROM admin WHERE mot_de_passe = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, motDePasse);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Authentification réussie.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur d'authentification : " + e.getMessage());
        }
        return false;
    }

    private static void afficherEntete(String titre) {
        System.out.println("\n" + "=".repeat(30));
        System.out.println(titre);
        System.out.println("=".repeat(30) + "\n");
    }

    private static void afficherMenu() {
        afficherEntete("Menu Principal");
        System.out.println("1. Ajouter un abonné");
        System.out.println("2. Désactiver un abonné");
        System.out.println("3. Envoyer une notification");
        System.out.println("4. Envoyer un email");
        System.out.println("5. Voir historique notifications");
        System.out.println("6. Voir historique emails");
        System.out.println("7. Lister les abonnés");
        System.out.println("8. Se déconnecter");
        System.out.println("9. Quitter l'application");
        System.out.print("\nVotre choix : ");
    }

    private static void desactiverAbonne() {
        System.out.print("Entrez le numéro de l'abonné à désactiver : ");
        String numero = scanner.nextLine();

        String sql = "UPDATE abonnee SET etat = 'inactif' WHERE numero = ?";
        
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, numero);
            int rows = pstmt.executeUpdate();
            
            System.out.println(rows > 0 ? "Abonné désactivé avec succès." : "Aucun abonné trouvé.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la désactivation : " + e.getMessage());
        }
    }
}