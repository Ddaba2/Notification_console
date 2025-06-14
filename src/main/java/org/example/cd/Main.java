package org.example.cd;

import java.sql.*;
import java.util.Scanner;

import static org.example.cd.HistoriqueEmail.afficherEmails;

public class Main {
    // Informations de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Mettez votre mot de passe ici si nécessaire

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Authentification de l'utilisateur
        System.out.println(ANSI_GREEN + "=== Bienvenue dans le système de gestion des notifications ===" + ANSI_RESET);
        System.out.println("Veuillez vous connecter pour continuer.");
        System.out.print("Entrez votre numéro : ");
        String numero = scanner.nextLine();
        System.out.print("Entrez votre mot de passe : ");
        String mot_de_passe = scanner.nextLine();

        int abonneId = authenticateUser(numero, mot_de_passe); // retourne int, -1 si échec
        if (abonneId != -1) {
            System.out.println("Connecté avec succès !");
            afficherMenu(scanner, abonneId);
        } else {
            System.out.println("Identifiants incorrects.");
        }
        scanner.close();
    }

    // Méthode d'authentification de l'utilisateur
    private static int authenticateUser(String numero, String mot_de_passe) {
        String query = "SELECT id FROM abonnee WHERE numero = ? AND mot_de_passe = ? AND etat = 'actif'";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, numero);
            stmt.setString(2, mot_de_passe);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return -1;
        }
    }

    // Afficher le menu principal et gérer les choix de l'utilisateur
    private static void afficherMenu(Scanner scanner, int abonneId) {
        boolean quitter = false;

        while (!quitter) {
            System.out.println(ANSI_GREEN + "\n=== Menu Principal ===" + ANSI_RESET);
            System.out.println("1. Consulter mes notifications");
            System.out.println("2. Consulter mes emails");
            System.out.println("3. Se déconnecter");
            System.out.println("4. Se désabonner");
            System.out.println("5. Quitter");
            System.out.print("Votre choix : ");

            int choix;
            try {
                choix = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre entre 1 et 5.");
                continue;
            }

            switch (choix) {
                case 00:
                    InterfaceAdmin.demarrer(scanner);
                    break;

                case 1:
                    voirNotifications(abonneId);
                    break;

                case 2:
                    afficherEmails(abonneId);
                     break;

                case 3:
                    System.out.println("Déconnexion en cours...");
                    main(null);
                    return;

                case 4:
                    System.out.println(ANSI_RED + "=== Attention : vous allez vous désabonner !! ===" + ANSI_RESET);
                    inactif(scanner);
                    return;

                case 5:
                    System.out.println("Fermeture de l'application...");
                    quitter = true;
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre 1 et 5.");
                    break;
            }
        }
    }

   // Méthode pour voir les notifications de l'utilisateur
   private static void voirNotifications(int abonneId) {
    try {
        // Appel à la méthode de NotificationService
        NotificationService.voirNotifications(abonneId);
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des notifications: " + e.getMessage());
    }
}
    // Méthode pour désabonner un utilisateur
    private static void inactif(Scanner scanner) {
        System.out.print("Entrez le numéro à désabonner : ");
        String numero = scanner.nextLine();

        String sql = "UPDATE abonnee SET etat = ? WHERE numero = ?";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, "inactif");
            pstmt.setString(2, numero);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Vous venez de vous désabonner (" + rows + " ligne(s) mise(s) à jour).");
                System.exit(0);
            } else {
                System.out.println("Aucun abonné trouvé avec ce numéro.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}