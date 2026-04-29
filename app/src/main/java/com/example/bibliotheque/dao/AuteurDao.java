package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.bibliotheque.entities.Auteur;

import java.util.List;

@Dao
public interface AuteurDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Auteur auteur);

    @Update
    void update(Auteur auteur);
    @Delete
    void delete(Auteur auteur);

    @Query("DELETE FROM auteurs WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM auteurs ORDER BY nom ASC, prenom ASC")
    LiveData<List<Auteur>> getAllAuteurs();

    @Query("SELECT * FROM auteurs WHERE id = :id LIMIT 1")
    Auteur getAuteurById(int id);

    @Query("SELECT * FROM auteurs WHERE nom LIKE '%' || :keyword || '%' OR prenom LIKE '%' || :keyword || '%' ORDER BY nom ASC")
    LiveData<List<Auteur>> searchAuteurs(String keyword);

    @Query("SELECT COUNT(*) FROM auteurs")
    int countAll();
}