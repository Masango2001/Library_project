package com.example.bibliotheque.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emprunts")
public class Emprunt {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "membre_id")
    private int membreId;

    @ColumnInfo(name = "livre_id")
    private int livreId;

    @ColumnInfo(name = "date_emprunt")
    private long dateEmprunt;

    @ColumnInfo(name = "date_retour_prevue")
    private long dateRetourPrevue;

    @ColumnInfo(name = "date_retour_reelle")
    private long dateRetourReelle;

    private String statut;

    public Emprunt() {}

    public Emprunt(int membreId, int livreId,
                   long dateEmprunt,
                   long dateRetourPrevue,
                   long dateRetourReelle,
                   String statut) {
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourReelle = dateRetourReelle;
        this.statut = statut;
    }

    // GETTERS & SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMembreId() { return membreId; }
    public void setMembreId(int membreId) { this.membreId = membreId; }

    public int getLivreId() { return livreId; }
    public void setLivreId(int livreId) { this.livreId = livreId; }

    public long getDateEmprunt() { return dateEmprunt; }
    public void setDateEmprunt(long dateEmprunt) { this.dateEmprunt = dateEmprunt; }

    public long getDateRetourPrevue() { return dateRetourPrevue; }
    public void setDateRetourPrevue(long dateRetourPrevue) { this.dateRetourPrevue = dateRetourPrevue; }

    public long getDateRetourReelle() { return dateRetourReelle; }
    public void setDateRetourReelle(long dateRetourReelle) { this.dateRetourReelle = dateRetourReelle; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}