package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.LivreDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Livre;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LivreRepository {

    private final LivreDao livreDao;
    private final LiveData<List<Livre>> allLivres;
    private final ExecutorService executor;

    public LivreRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        livreDao = db.livreDao();
        allLivres = livreDao.getAllLivres();
        executor = Executors.newSingleThreadExecutor();
    }

    // ================= CRUD =================

    public LiveData<List<Livre>> getAllLivres() {
        return allLivres;
    }

    public void insert(Livre l) {
        executor.execute(() -> livreDao.insert(l));
    }

    // ================= UPDATE =================
    public void update(Livre l) {
        executor.execute(() -> livreDao.update(l));
    }

    // ================= DELETE =================
    public void delete(Livre l) {
        executor.execute(() -> livreDao.delete(l));
    }

    // ================= SEARCH =================
    public LiveData<List<Livre>> searchLivres(String query) {
        return livreDao.searchLivres(query);
    }

    // ================= FILTRES =================
    public LiveData<List<Livre>> getLivresByCategorie(int id) {
        return livreDao.getLivresByCategorie(id);
    }

    public LiveData<List<Livre>> getLivresByAuteur(int id) {
        return livreDao.getLivresByAuteur(id);
    }

    // ================= STATISTIQUES =================
    public LiveData<Integer> countLivres() {
        return livreDao.countLivres();
    }

    public LiveData<Integer> totalStockDisponible() {
        return livreDao.totalStockDisponible();
    }

    public LiveData<Integer> totalStockTotal() {
        return livreDao.totalStockTotal();
    }

    // ================= STOCK =================
    public void decrementStock(int id) {
        executor.execute(() -> livreDao.decrementStock(id));
    }

    public void incrementStock(int id) {
        executor.execute(() -> livreDao.incrementStock(id));
    }
}