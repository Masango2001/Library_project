package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.bibliotheque.entities.Categorie;

import java.util.List;

@Dao
public interface CategorieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Categorie categorie);

    @Update
    void update(Categorie categorie);
    @Delete
    void delete(Categorie categorie);

    @Query("SELECT * FROM categories ORDER BY nom ASC")
    LiveData<List<Categorie>> getAllCategories();

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    Categorie getCategorieById(int id);

    @Query("SELECT COUNT(*) FROM categories")
    int countAll();
}