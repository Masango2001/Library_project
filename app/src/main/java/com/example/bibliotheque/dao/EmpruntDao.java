package com.example.bibliotheque.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.model.EmpruntDisplayItem;
import com.example.bibliotheque.model.TopLivreStat;
import com.example.bibliotheque.model.TopMembreStat;

import java.util.List;

@Dao
public interface EmpruntDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(Emprunt emprunt);

    @Update
    void update(Emprunt emprunt);

    @Delete
    void delete(Emprunt emprunt);

    @Query("SELECT * FROM emprunts ORDER BY date_emprunt DESC")
    LiveData<List<Emprunt>> getAllEmprunts();

    @Query("SELECT e.id, e.membre_id AS membreId, e.livre_id AS livreId, " +
            "e.date_emprunt AS dateEmprunt, e.date_retour_prevue AS dateRetourPrevue, " +
            "e.date_retour_reelle AS dateRetourReelle, e.statut, " +
            "m.nom || ' ' || m.prenom AS membreNomComplet, l.titre AS livreTitre " +
            "FROM emprunts e " +
            "INNER JOIN membres m ON m.id = e.membre_id " +
            "INNER JOIN livres l ON l.id = e.livre_id " +
            "ORDER BY e.date_emprunt DESC")
    LiveData<List<EmpruntDisplayItem>> getAllEmpruntDetails();

    @Query("SELECT * FROM emprunts WHERE id = :id")
    LiveData<Emprunt> getEmpruntById(int id);

    @Query("SELECT * FROM emprunts WHERE id = :id LIMIT 1")
    Emprunt getEmpruntByIdSync(int id);

    @Query("SELECT * FROM emprunts WHERE membre_id = :membreId")
    LiveData<List<Emprunt>> getEmpruntsByMembre(int membreId);

    @Query("SELECT * FROM emprunts WHERE livre_id = :livreId")
    LiveData<List<Emprunt>> getEmpruntsByLivre(int livreId);

    @Query("SELECT COUNT(*) FROM emprunts")
    LiveData<Integer> getTotalEmprunts();

    @Query("SELECT COUNT(*) FROM emprunts WHERE statut = 'en cours'")
    LiveData<Integer> getEmpruntsEnCours();

    @Query("SELECT COUNT(*) FROM emprunts WHERE statut = 'en retard'")
    LiveData<Integer> getEmpruntsEnRetard();

    @Query("SELECT COUNT(*) FROM emprunts WHERE membre_id = :membreId AND date_retour_reelle = 0")
    int countEmpruntsActifsByMembre(int membreId);

    @Query("UPDATE emprunts SET statut = 'en retard' " +
            "WHERE date_retour_prevue < :currentTime " +
            "AND date_retour_reelle = 0 " +
            "AND statut = 'en cours'")
    void updateRetardsAutomatiques(long currentTime);

    @Query("UPDATE emprunts SET date_retour_reelle = :dateRetourReelle, statut = :statut WHERE id = :id")
    void enregistrerRetour(int id, long dateRetourReelle, String statut);

    @Query("SELECT l.titre AS livreTitre, COUNT(e.id) AS totalEmprunts " +
            "FROM emprunts e " +
            "INNER JOIN livres l ON l.id = e.livre_id " +
            "GROUP BY e.livre_id " +
            "ORDER BY totalEmprunts DESC, l.titre ASC " +
            "LIMIT :limit")
    LiveData<List<TopLivreStat>> getTopLivres(int limit);

    @Query("SELECT m.nom || ' ' || m.prenom AS membreNomComplet, COUNT(e.id) AS totalEmprunts " +
            "FROM emprunts e " +
            "INNER JOIN membres m ON m.id = e.membre_id " +
            "GROUP BY e.membre_id " +
            "ORDER BY totalEmprunts DESC, m.nom ASC, m.prenom ASC " +
            "LIMIT :limit")
    LiveData<List<TopMembreStat>> getTopMembres(int limit);
}
