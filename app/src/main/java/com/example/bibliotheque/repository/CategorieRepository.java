package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.CategorieDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Categorie;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorieRepository {

    private final CategorieDao categorieDao;
    private final LiveData<List<Categorie>> allCategories;

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(4);

    public CategorieRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        categorieDao = database.categorieDao();
        allCategories = categorieDao.getAllCategories();
    }

    // ================= LISTE =================
    public LiveData<List<Categorie>> getAllCategories() {
        return allCategories;
    }

    // ================= INSERT =================
    public void insert(Categorie categorie) {
        if (categorie == null) return;

        executorService.execute(() -> {
            try {
                categorieDao.insert(categorie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ================= UPDATE =================
    public void update(Categorie categorie) {
        if (categorie == null) return;

        executorService.execute(() -> {
            try {
                categorieDao.update(categorie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ================= DELETE =================
    public void delete(Categorie categorie) {
        if (categorie == null) return;

        executorService.execute(() -> {
            try {
                categorieDao.delete(categorie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // ================= SEARCH =================
    public LiveData<List<Categorie>> searchCategories(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return allCategories;
        }

        String safeKeyword = "%" + keyword.trim() + "%";
        return categorieDao.searchCategories(safeKeyword);
    }

    // ================= GET BY ID =================
    public LiveData<Categorie> getCategorieById(int id) {
        return categorieDao.getCategorieById(id);
    }

    // ================= COUNT =================
    public LiveData<Integer> countCategories() {
        return categorieDao.countCategories();
    }

    // ================= CLEAN =================
    public void clear() {
        executorService.shutdown();
    }
}