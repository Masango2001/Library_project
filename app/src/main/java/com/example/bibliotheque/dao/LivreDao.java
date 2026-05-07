package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.model.LivreCatalogueItem;

import java.util.List;

@Dao
public interface LivreDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Livre livre);

    @Update
    void update(Livre livre);

    @Delete
    void delete(Livre livre);

    // ================= LISTE =================
    @Query("SELECT * FROM livres ORDER BY titre ASC")
    LiveData<List<Livre>> getAllLivres();

    @Query("SELECT * FROM livres WHERE id = :id LIMIT 1")
    LiveData<Livre> getLivreById(int id);

    @Query("SELECT * FROM livres WHERE id = :id LIMIT 1")
    Livre getLivreByIdSync(int id);

    @Query("SELECT * FROM livres WHERE quantite_disponible > 0 ORDER BY titre ASC")
    LiveData<List<Livre>> getLivresDisponibles();

    @Query("SELECT COUNT(*) FROM livres WHERE isbn = :isbn AND id != :excludeId")
    int countByIsbnExcludingId(String isbn, int excludeId);

    @Query("SELECT l.id, l.titre, l.isbn, l.annee_publication AS anneePublication, " +
            "l.editeur, l.categorie_id AS categorieId, l.auteur_id AS auteurId, " +
            "l.quantite_totale AS quantiteTotale, l.quantite_disponible AS quantiteDisponible, " +
            "a.nom || ' ' || a.prenom AS auteurNomComplet, c.nom AS categorieNom " +
            "FROM livres l " +
            "INNER JOIN auteurs a ON a.id = l.auteur_id " +
            "INNER JOIN categories c ON c.id = l.categorie_id " +
            "ORDER BY l.titre ASC")
    LiveData<List<LivreCatalogueItem>> getCatalogue();

    // ================= SEARCH =================
    @Query("SELECT * FROM livres WHERE titre LIKE '%' || :query || '%' " +
            "OR isbn LIKE '%' || :query || '%' ORDER BY titre ASC")
    LiveData<List<Livre>> searchLivres(String query);

    @Query("SELECT l.id, l.titre, l.isbn, l.annee_publication AS anneePublication, " +
            "l.editeur, l.categorie_id AS categorieId, l.auteur_id AS auteurId, " +
            "l.quantite_totale AS quantiteTotale, l.quantite_disponible AS quantiteDisponible, " +
            "a.nom || ' ' || a.prenom AS auteurNomComplet, c.nom AS categorieNom " +
            "FROM livres l " +
            "INNER JOIN auteurs a ON a.id = l.auteur_id " +
            "INNER JOIN categories c ON c.id = l.categorie_id " +
            "WHERE l.titre LIKE '%' || :query || '%' " +
            "OR l.isbn LIKE '%' || :query || '%' " +
            "OR a.nom LIKE '%' || :query || '%' " +
            "OR a.prenom LIKE '%' || :query || '%' " +
            "OR c.nom LIKE '%' || :query || '%' " +
            "ORDER BY l.titre ASC")
    LiveData<List<LivreCatalogueItem>> searchCatalogue(String query);

    @Query("SELECT * FROM livres WHERE categorie_id = :categorieId")
    LiveData<List<Livre>> getLivresByCategorie(int categorieId);

    @Query("SELECT * FROM livres WHERE auteur_id = :auteurId")
    LiveData<List<Livre>> getLivresByAuteur(int auteurId);

    // ================= STATISTIQUES =================
    @Query("SELECT COUNT(*) FROM livres")
    LiveData<Integer> countLivres();

    @Query("SELECT SUM(quantite_disponible) FROM livres")
    LiveData<Integer> totalStockDisponible();

    @Query("SELECT SUM(quantite_totale) FROM livres")
    LiveData<Integer> totalStockTotal();

    // ================= STOCK =================
    @Query("UPDATE livres SET quantite_disponible = quantite_disponible - 1 " +
            "WHERE id = :id AND quantite_disponible > 0")
    int decrementStock(int id);

    @Query("UPDATE livres SET quantite_disponible = quantite_disponible + 1 " +
            "WHERE id = :id AND quantite_disponible < quantite_totale")
    int incrementStock(int id);
}
