package com.example.bibliotheque.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "membres",
        indices = {@Index(value = "email", unique = true)}
)
public class Membre {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;

    @ColumnInfo(name = "date_inscription")
    private String dateInscription;

    private String statut; // actif ou suspendu

    // ================= CONSTRUCTEUR ROOM =================
    public Membre() {}

    // ================= CONSTRUCTEUR UTILISATEUR =================
    @Ignore
    public Membre(String nom, String prenom, String email,
                  String telephone, String adresse,
                  String dateInscription, String statut) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    // ================= GETTERS =================
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getTelephone() { return telephone; }
    public String getAdresse() { return adresse; }
    public String getDateInscription() { return dateInscription; }
    public String getStatut() { return statut; }

    // ================= SETTERS =================
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setDateInscription(String dateInscription) { this.dateInscription = dateInscription; }
    public void setStatut(String statut) { this.statut = statut; }
}
