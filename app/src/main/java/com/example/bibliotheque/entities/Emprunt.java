package com.example.bibliotheque.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "emprunts")
public class Emprunt {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int membreId;
    private int livreId;
    private String dateEmprunt;
    private String dateRetourPrevue;
    private String dateRetourReelle;
    private String statut;
    public Emprunt(int membreId, int livreId,
                   String dateEmprunt,
                   String dateRetourPrevue,
                   String dateRetourReelle,
                   String statut) {
        this.membreId = membreId;
        this.livreId = livreId;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourReelle = dateRetourReelle;
        this.statut = statut;
    }
}