package org.example.cd;

import java.sql.*;
import java.util.Scanner;

public class Add {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionnotif";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; 

    public static void ajouterAbonnee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Ajouer un abonnée ===");
        System.out.print("Entrez le nom : ");
        String nom = scanner.nextLine();
        System.out.print("Entrez le prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Entrez l'email : ");
        String email = scanner.nextLine();
        System.out.print("Entrez le numéro de téléphone : ");
        String numero = scanner.nextLine();
        System.out.print("Entrez le mot de passe : ");
        String mot_de_passe = scanner.nextLine();
        System.out.print("Etat : ");
        String etat = scanner.nextLine();

        if (addAbonnee(nom, prenom, email, numero, mot_de_passe, etat)) {
            System.out.println("Abonnée ajouter avec succès.");
        } else {
            System.out.println("Échec lors de l'ajout de l'abonnée. Le numéro de téléphone est peut-être déjà utilisé.");
        }
    }

    private static boolean addAbonnee(String nom, String prenom, String email, String numero, String mot_de_passe, String etat) {
        String insertQuery = "INSERT INTO abonnee (nom, prenom, email, numero, mot_de_passe, etat) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, numero);
            preparedStatement.setString(5, mot_de_passe);
            preparedStatement.setString(6, etat);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
