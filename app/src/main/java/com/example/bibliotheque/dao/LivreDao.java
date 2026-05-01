package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bibliotheque.entities.Livre;

import java.util.List;

@Dao
public interface LivreDao {

    @Insert
    void insert(Livre livre);

    @Update
    void update(Livre livre);

    @Delete
    void delete(Livre livre);

    // ================= LISTE =================
    @Query("SELECT * FROM livres ORDER BY titre ASC")
    LiveData<List<Livre>> getAllLivres();

    @Query("SELECT * FROM livres WHERE id = :id LIMIT 1")
    LiveData<Livre> getLivreById(int id);

    // ================= SEARCH =================
    @Query("SELECT * FROM livres WHERE titre LIKE '%' || :query || '%' " +
            "OR isbn LIKE '%' || :query || '%' ORDER BY titre ASC")
    LiveData<List<Livre>> searchLivres(String query);

    @Query("SELECT * FROM livres WHERE categorieId = :categorieId")
    LiveData<List<Livre>> getLivresByCategorie(int categorieId);

    @Query("SELECT * FROM livres WHERE auteurId = :auteurId")
    LiveData<List<Livre>> getLivresByAuteur(int auteurId);

    // ================= STATISTIQUES =================
    @Query("SELECT COUNT(*) FROM livres")
    LiveData<Integer> countLivres();

    @Query("SELECT SUM(quantiteDisponible) FROM livres")
    LiveData<Integer> totalStockDisponible();

    @Query("SELECT SUM(quantiteTotale) FROM livres")
    LiveData<Integer> totalStockTotal();

    // ================= STOCK =================
    @Query("UPDATE livres SET quantiteDisponible = quantiteDisponible - 1 " +
            "WHERE id = :id AND quantiteDisponible > 0")
    void decrementStock(int id);

    @Query("UPDATE livres SET quantiteDisponible = quantiteDisponible + 1 WHERE id = :id")
    void incrementStock(int id);
}