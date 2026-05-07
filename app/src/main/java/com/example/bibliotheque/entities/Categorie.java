package com.example.bibliotheque.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Categorie {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nom;

    @ColumnInfo(name = "description")
    private String description;

    // ✅ constructeur vide (IMPORTANT pour Room)
    public Categorie() {
    }

    // ✅ constructeur utilisé dans ton code
    @Ignore
    public Categorie(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // ================= GETTERS =================
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    // ================= SETTERS =================
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
