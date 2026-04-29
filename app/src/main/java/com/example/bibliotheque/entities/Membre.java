package com.example.bibliotheque.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "membres")
public class Membre {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String dateInscription;
    private String statut;

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
}