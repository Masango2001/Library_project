package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.EmpruntDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Emprunt;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmpruntRepository {

    private final EmpruntDao empruntDao;
    private final LiveData<List<Emprunt>> allEmprunts;
    private final ExecutorService executorService;

    public EmpruntRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        empruntDao = database.empruntDao();
        allEmprunts = empruntDao.getAllEmprunts();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Emprunt>> getAll() {
        return allEmprunts;
    }

    public void insert(Emprunt emprunt) {
        executorService.execute(() -> empruntDao.insert(emprunt));
    }

    public void update(Emprunt emprunt) {
        executorService.execute(() -> empruntDao.update(emprunt));
    }

    public void delete(Emprunt emprunt) {
        executorService.execute(() -> empruntDao.delete(emprunt));
    }

    public void updateRetards(long currentTime) {
        executorService.execute(() -> empruntDao.updateRetardsAutomatiques(currentTime));
    }
}