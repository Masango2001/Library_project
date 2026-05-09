package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.AuteurDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Auteur;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuteurRepository {

    private final AuteurDao auteurDao;
    private final LiveData<List<Auteur>> allAuteurs;

    // Thread pool partagé (bon choix 👍)
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(4);

    public AuteurRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        auteurDao = database.auteurDao();
        allAuteurs = auteurDao.getAllAuteurs();
    }

    // ================= READ =================
    public LiveData<List<Auteur>> getAllAuteurs() {
        return allAuteurs;
    }

    public LiveData<List<Auteur>> searchAuteurs(String query) {

        if (query == null || query.trim().isEmpty()) {
            return allAuteurs;
        }

        String safeQuery = "%" + query.trim() + "%";
        return auteurDao.searchAuteurs(safeQuery);
    }

    public LiveData<Integer> countAuteurs() {
        return auteurDao.countAuteurs();
    }

    // ================= WRITE =================
    public void insert(Auteur auteur) {
        if (auteur == null) return;

        executorService.execute(() -> {
            try {
                auteurDao.insert(auteur);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void update(Auteur auteur) {
        if (auteur == null) return;

        executorService.execute(() -> {
            try {
                auteurDao.update(auteur);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(Auteur auteur) {
        if (auteur == null) return;

        executorService.execute(() -> {
            try {
                auteurDao.delete(auteur);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // 🔥 OPTION PRO (BONNE PRATIQUE)
    public void clear() {
        executorService.shutdown();
    }
}