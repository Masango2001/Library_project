package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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

    // ================= RECHERCHE =================
    @Query("SELECT * FROM categories " +
            "WHERE nom LIKE '%' || :keyword || '%' " +
            "OR description LIKE '%' || :keyword || '%' " +
            "ORDER BY nom ASC")
    LiveData<List<Categorie>> searchCategories(String keyword);

    // ================= STATISTIQUES =================
    @Query("SELECT COUNT(*) FROM categories")
    LiveData<Integer> countCategories();

    // ================= VERIFICATION =================
    @Query("SELECT COUNT(*) FROM categories WHERE nom = :nom LIMIT 1")
    int existsByName(String nom);

    // ================= SUPPRESSION PAR ID =================
    @Query("DELETE FROM categories WHERE id = :id")
    void deleteById(int id);
}