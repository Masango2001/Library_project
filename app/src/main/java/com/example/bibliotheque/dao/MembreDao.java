package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bibliotheque.entities.Membre;

import java.util.List;

@Dao
public interface MembreDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Membre membre);

    @Update
    void update(Membre membre);

    @Delete
    void delete(Membre membre);

    @Query("DELETE FROM membres WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM membres ORDER BY nom ASC, prenom ASC")
    LiveData<List<Membre>> getAllMembres();

    @Query("SELECT * FROM membres WHERE statut = 'actif' ORDER BY nom ASC")
    LiveData<List<Membre>> getMembresActifs();

    @Query("SELECT * FROM membres WHERE statut = 'suspendu' ORDER BY nom ASC")
    LiveData<List<Membre>> getMembresSuspendus();

    @Query("SELECT * FROM membres WHERE nom LIKE '%' || :recherche || '%' " +
            "OR prenom LIKE '%' || :recherche || '%' ORDER BY nom ASC")
    LiveData<List<Membre>> searchMembres(String recherche);

    @Query("SELECT * FROM membres WHERE id = :id LIMIT 1")
    LiveData<Membre> getMembreById(int id);

    @Query("SELECT * FROM membres WHERE id = :id LIMIT 1")
    Membre getMembreByIdSync(int id);

    @Query("SELECT * FROM membres WHERE email = :email LIMIT 1")
    LiveData<Membre> getMembreByEmail(String email);

    @Query("SELECT COUNT(*) FROM membres WHERE email = :email AND id != :excludeId")
    int countByEmailExcludingId(String email, int excludeId);

    @Query("SELECT COUNT(*) FROM membres")
    LiveData<Integer> countAll();

    @Query("SELECT COUNT(*) FROM membres WHERE statut = 'actif'")
    LiveData<Integer> countMembresActifs();

    @Query("SELECT COUNT(*) FROM membres WHERE statut = 'suspendu'")
    LiveData<Integer> countMembresSuspendus();

    @Query("UPDATE membres SET statut = :statut WHERE id = :id")
    void updateStatut(int id, String statut);

    @Query("UPDATE membres SET statut = 'suspendu' WHERE id = :id")
    void suspendMembre(int id);

    @Query("UPDATE membres SET statut = 'actif' WHERE id = :id")
    void activateMembre(int id);
}
