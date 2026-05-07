package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.bibliotheque.entities.Auteur;

import java.util.List;

@Dao
public interface AuteurDao {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Auteur auteur);

    // UPDATE
    @Update
    void update(Auteur auteur);

    // DELETE
    @Delete
    void delete(Auteur auteur);

    // READ ALL
    @Query("SELECT * FROM auteurs ORDER BY nom ASC, prenom ASC")
    LiveData<List<Auteur>> getAllAuteurs();

    // READ BY ID
    @Query("SELECT * FROM auteurs WHERE id = :id LIMIT 1")
    LiveData<Auteur> getAuteurById(int id);

    @Query("SELECT * FROM auteurs WHERE id = :id LIMIT 1")
    Auteur getAuteurByIdSync(int id);

    // SEARCH
    @Query("SELECT * FROM auteurs WHERE nom LIKE '%' || :keyword || '%' " +
            "OR prenom LIKE '%' || :keyword || '%' ORDER BY nom ASC")
    LiveData<List<Auteur>> searchAuteurs(String keyword);

    // STATISTICS
    @Query("SELECT COUNT(*) FROM auteurs")
    LiveData<Integer> countAuteurs();

    @Query("SELECT COUNT(*) FROM auteurs WHERE nom = :nom AND prenom = :prenom AND id != :excludeId")
    int countByIdentityExcludingId(String nom, String prenom, int excludeId);
}
