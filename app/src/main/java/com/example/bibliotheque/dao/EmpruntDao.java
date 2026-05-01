package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bibliotheque.entities.Emprunt;

import java.util.List;

@Dao
public interface EmpruntDao {

    @Insert
    void insert(Emprunt emprunt);

    @Update
    void update(Emprunt emprunt);

    @Delete
    void delete(Emprunt emprunt);

    @Query("SELECT * FROM emprunts ORDER BY date_emprunt DESC")
    LiveData<List<Emprunt>> getAllEmprunts();

    @Query("SELECT * FROM emprunts WHERE id = :id")
    LiveData<Emprunt> getEmpruntById(int id);

    @Query("SELECT * FROM emprunts WHERE membre_id = :membreId")
    LiveData<List<Emprunt>> getEmpruntsByMembre(int membreId);

    @Query("SELECT * FROM emprunts WHERE livre_id = :livreId")
    LiveData<List<Emprunt>> getEmpruntsByLivre(int livreId);

    @Query("SELECT COUNT(*) FROM emprunts")
    LiveData<Integer> getTotalEmprunts();

    @Query("SELECT COUNT(*) FROM emprunts WHERE statut = 'EN_COURS'")
    LiveData<Integer> getEmpruntsEnCours();

    @Query("SELECT COUNT(*) FROM emprunts WHERE statut = 'EN_RETARD'")
    LiveData<Integer> getEmpruntsEnRetard();

    @Query("UPDATE emprunts SET statut = 'EN_RETARD' " +
            "WHERE date_retour_prevue < :currentTime " +
            "AND date_retour_reelle = 0 " +
            "AND statut = 'EN_COURS'")
    void updateRetardsAutomatiques(long currentTime);
}