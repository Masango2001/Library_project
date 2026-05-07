package com.example.bibliotheque.model;

import com.example.bibliotheque.entities.Livre;

public class LivreCatalogueItem {

    public int id;
    public String titre;
    public String isbn;
    public int anneePublication;
    public String editeur;
    public int categorieId;
    public int auteurId;
    public int quantiteTotale;
    public int quantiteDisponible;
    public String auteurNomComplet;
    public String categorieNom;

    public Livre toLivre() {
        Livre livre = new Livre();
        livre.setId(id);
        livre.setTitre(titre);
        livre.setIsbn(isbn);
        livre.setAnneePublication(anneePublication);
        livre.setEditeur(editeur);
        livre.setCategorieId(categorieId);
        livre.setAuteurId(auteurId);
        livre.setQuantiteTotale(quantiteTotale);
        livre.setQuantiteDisponible(quantiteDisponible);
        return livre;
    }
}
