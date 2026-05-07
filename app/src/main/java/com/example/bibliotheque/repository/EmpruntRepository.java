package com.example.bibliotheque.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.dao.EmpruntDao;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.model.EmpruntDisplayItem;
import com.example.bibliotheque.model.TopLivreStat;
import com.example.bibliotheque.model.TopMembreStat;
import com.example.bibliotheque.util.LibraryConstants;

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

    public LiveData<List<EmpruntDisplayItem>> getAllDetails() {
        return empruntDao.getAllEmpruntDetails();
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

    public void enregistrerRetour(int empruntId, long dateRetourReelle) {
        executorService.execute(() ->
                empruntDao.enregistrerRetour(
                        empruntId,
                        dateRetourReelle,
                        LibraryConstants.STATUT_EMPRUNT_TERMINE
                ));
    }

    public LiveData<List<TopLivreStat>> getTopLivres(int limit) {
        return empruntDao.getTopLivres(limit);
    }

    public LiveData<List<TopMembreStat>> getTopMembres(int limit) {
        return empruntDao.getTopMembres(limit);
    }
}
