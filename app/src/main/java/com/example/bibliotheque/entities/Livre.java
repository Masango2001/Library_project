package com.example.bibliotheque.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "livres",
        foreignKeys = {
                @ForeignKey(
                        entity = Categorie.class,
                        parentColumns = "id",
                        childColumns = "categorie_id",
                        onDelete = ForeignKey.RESTRICT
                ),
                @ForeignKey(
                        entity = Auteur.class,
                        parentColumns = "id",
                        childColumns = "auteur_id",
                        onDelete = ForeignKey.RESTRICT
                )
        },
        indices = {
                @Index(value = "isbn", unique = true),
                @Index("categorie_id"),
                @Index("auteur_id")
        }
)
public class Livre {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titre;

    @NonNull
    private String isbn = "";

    @ColumnInfo(name = "annee_publication")
    private int anneePublication;

    private String editeur;

    @ColumnInfo(name = "categorie_id")
    private int categorieId;

    @ColumnInfo(name = "auteur_id")
    private int auteurId;

    @ColumnInfo(name = "quantite_totale")
    private int quantiteTotale;

    @ColumnInfo(name = "quantite_disponible")
    private int quantiteDisponible;

    public Livre() {}

    @Ignore
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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    @NonNull
    public String getIsbn() { return isbn; }
    public void setIsbn(@NonNull String isbn) { this.isbn = isbn; }
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
