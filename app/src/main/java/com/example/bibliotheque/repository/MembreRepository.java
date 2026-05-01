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
    private final ExecutorService executorService;
    private final LiveData<List<Membre>> allMembres;

    public MembreRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        membreDao = db.membreDao();
        executorService = Executors.newSingleThreadExecutor();
        allMembres = membreDao.getAllMembres();
    }

    public LiveData<List<Membre>> getAllMembres() { return allMembres; }
    public LiveData<List<Membre>> getMembresActifs() { return membreDao.getMembresActifs(); }
    public LiveData<Membre> getMembreById(int id) { return membreDao.getMembreById(id); }

    public void insert(Membre membre) {
        executorService.execute(() -> membreDao.insert(membre));
    }
    public void update(Membre membre) {
        executorService.execute(() -> membreDao.update(membre));
    }
    public void delete(Membre membre) {
        executorService.execute(() -> membreDao.delete(membre));
    }

    public void activateMembre(int id) {
        executorService.execute(() -> membreDao.activateMembre(id));
    }
    public void suspendMembre(int id) {
        executorService.execute(() -> membreDao.suspendMembre(id));
    }

    public LiveData<Integer> countAll() { return membreDao.countAll(); }
    public LiveData<Integer> countMembresActifs() { return membreDao.countMembresActifs(); }
}