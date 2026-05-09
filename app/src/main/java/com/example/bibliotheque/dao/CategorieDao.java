package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.bibliotheque.entities.Categorie;

import java.util.List;

@Dao
public interface CategorieDao {

    // ================= INSERT =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Categorie categorie);

    // ================= UPDATE =================
    @Update
    void update(Categorie categorie);

    // ================= DELETE =================
    @Delete
    void delete(Categorie categorie);

    // ================= LISTE =================
    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<Categorie>> getAllCategories();

    // ================= GET BY ID =================
    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    LiveData<Categorie> getCategorieById(int id);

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Categorie getCategorieByIdSync(int id);

    // ================= SEARCH =================
    @Query("SELECT * FROM categories " +
            "WHERE nom LIKE :keyword OR description LIKE :keyword " +
            "ORDER BY nom ASC")
    LiveData<List<Categorie>> searchCategories(String keyword);

    // ================= COUNT (CORRIGÉ) =================
    @Query("SELECT COUNT(*) FROM categories")
    LiveData<Integer> countCategories();

    // ================= EXISTS (CORRIGÉ SAFE) =================
    @Query("SELECT COUNT(*) FROM categories WHERE nom = :nom")
    int existsByName(String nom);

    @Query("SELECT COUNT(*) FROM categories WHERE nom = :nom AND id != :excludeId")
    int existsByNameExcludingId(String nom, int excludeId);

    // ================= DELETE BY ID =================
    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(int id);
}