package com.example.bibliotheque.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "livres")
public class Livre {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titre;

    @NonNull
    private String isbn;

    private int anneePublication;
    private String editeur;

    private int categorieId;
    private int auteurId;

    private int quantiteTotale;
    private int quantiteDisponible;

    public Livre() {}

    public Livre(String titre, String isbn, int anneePublication,
                 String editeur, int categorieId, int auteurId,
                 int quantiteTotale, int quantiteDisponible) {

        this.titre = titre;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
        this.editeur = editeur;
        this.categorieId = categorieId;
        this.auteurId = auteurId;
        this.quantiteTotale = quantiteTotale;
        this.quantiteDisponible = quantiteDisponible;
    }

    // GETTERS & SETTERS

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAnneePublication() { return anneePublication; }
    public void setAnneePublication(int anneePublication) { this.anneePublication = anneePublication; }

    public String getEditeur() { return editeur; }
    public void setEditeur(String editeur) { this.editeur = editeur; }

    public int getCategorieId() { return categorieId; }
    public void setCategorieId(int categorieId) { this.categorieId = categorieId; }

    public int getAuteurId() { return auteurId; }
    public void setAuteurId(int auteurId) { this.auteurId = auteurId; }

    public int getQuantiteTotale() { return quantiteTotale; }
    public void setQuantiteTotale(int quantiteTotale) { this.quantiteTotale = quantiteTotale; }

    public int getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(int quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }
}