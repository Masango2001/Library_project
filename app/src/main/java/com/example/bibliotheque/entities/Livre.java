package com.example.bibliotheque.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "livres",
        foreignKeys = {
                @ForeignKey(entity = Auteur.class,
                        parentColumns = "id",
                        childColumns = "auteurId"),
                @ForeignKey(entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorieId")
        })
public class Livre {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titre;
    private String isbn;
    private int anneePublication;
    private String editeur;
    private int categorieId;
    private int auteurId;
    private int quantiteTotale;
    private int quantiteDisponible;

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
}