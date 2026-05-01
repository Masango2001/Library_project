package com.example.bibliotheque.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "auteurs")
public class Auteur {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom;
    private String prenom;
    private String nationalite;
    private String dateNaissance;

    // Constructeur vide (OBLIGATOIRE recommandé Room)
    public Auteur() {
    }

    // Constructeur principal
    public Auteur(String nom, String prenom, String nationalite, String dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.nationalite = nationalite;
        this.dateNaissance = dateNaissance;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
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

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}