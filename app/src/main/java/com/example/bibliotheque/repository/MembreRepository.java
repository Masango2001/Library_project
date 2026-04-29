package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.MembreDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Membre;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MembreRepository {

    private final MembreDao membreDao;
    private final LiveData<List<Membre>> allMembres;
    private final ExecutorService executor;

    public MembreRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        membreDao = db.membreDao();
        allMembres = membreDao.getAllMembres();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Membre>> getAllMembres() {
        return allMembres;
    }

    public void insert(Membre membre) {
        executor.execute(() -> membreDao.insert(membre));
    }
    public void update(Membre membre) {
        executor.execute(() -> membreDao.update(membre));
    }

    public void delete(Membre membre) {
        executor.execute(() -> membreDao.delete(membre));
    }

    public void deleteById(int id) {
        executor.execute(() -> membreDao.deleteById(id));
    }
}