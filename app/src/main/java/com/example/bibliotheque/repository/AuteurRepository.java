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
    private final ExecutorService executorService;

    public AuteurRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        auteurDao = database.auteurDao();
        allAuteurs = auteurDao.getAllAuteurs();
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Auteur>> getAllAuteurs() {
        return allAuteurs;
    }

    public void insert(Auteur auteur) {
        executorService.execute(() -> auteurDao.insert(auteur));
    }

    public void update(Auteur auteur) {
        executorService.execute(() -> auteurDao.update(auteur));
    }

    public void delete(Auteur auteur) {
        executorService.execute(() -> auteurDao.delete(auteur));
    }

    public LiveData<List<Auteur>> searchAuteurs(String query) {
        return auteurDao.searchAuteurs(query);
    }

    public LiveData<Integer> countAuteurs() {
        return auteurDao.countAuteurs();
    }

    public void clear() {
        executorService.shutdown();
    }
}