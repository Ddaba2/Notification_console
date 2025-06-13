package org.example.cd;

import java.time.LocalDateTime;

public class Notification {
    private final int id;
    private final String titre;
    private final String message;
    private final LocalDateTime date;
    private final String expediteur;
    private boolean lu;  // Ajout d'un champ pour suivre l'état de lecture

    // Constructeur principal
    public Notification(int id, String titre, String message, LocalDateTime date, String expediteur) {
        this.id = id;
        this.titre = titre;
        this.message = message;
        this.date = date;
        this.expediteur = expediteur;
        this.lu = false;  // Par défaut, une notification n'est pas lue
    }

     public void envoyerNotification(int id, String titre, String message, LocalDateTime date, String expediteur) {
        
    }

    // Getters pour accéder aux propriétés
    public int getId() { return id; }
    public String getTitre() { return titre; }
    public String getMessage() { return message; }
    public LocalDateTime getDate() { return date; }
    public String getExpediteur() { return expediteur; }
    public boolean isLu() { return lu; }  // Implémentation réelle

    // Setter pour le statut de lecture
    public void setLu(boolean lu) {
        this.lu = lu;
    }
}