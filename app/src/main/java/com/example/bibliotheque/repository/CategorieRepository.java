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
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        executorService.execute(() -> categorieDao.insert(categorie));
    }

    // ================= UPDATE =================
    public void update(Categorie categorie) {
        executorService.execute(() -> categorieDao.update(categorie));
    }

    // ================= DELETE =================
    public void delete(Categorie categorie) {
        executorService.execute(() -> categorieDao.delete(categorie));
    }

    // ================= SEARCH =================
    public LiveData<List<Categorie>> searchCategories(String keyword) {
        return categorieDao.searchCategories(keyword);
    }

    // ================= GET BY ID =================
    public LiveData<Categorie> getCategorieById(int id) {
        return categorieDao.getCategorieById(id);
    }

    // ================= COUNT =================
    public LiveData<Integer> countCategories() {
        return categorieDao.countCategories();
    }
}