package com.example.bibliotheque.ui.emprunts;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.LivreRepository;
import com.example.bibliotheque.repository.MembreRepository;
import com.example.bibliotheque.util.DateUtils;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEmpruntActivity extends AppCompatActivity {

    private Spinner spinnerMembre;
    private Spinner spinnerLivre;
    private TextView txtDateRetour;
    private Button btnSave;
    private MembreRepository membreRepository;
    private LivreRepository livreRepository;
    private AppDatabase db;
    private ExecutorService executorService;
    private List<Membre> membreList = new ArrayList<>();
    private List<Livre> livreList = new ArrayList<>();
    private int preselectedLivreId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emprunt);

        spinnerMembre = findViewById(R.id.spinnerMembre);
        spinnerLivre = findViewById(R.id.spinnerLivre);
        txtDateRetour = findViewById(R.id.txtDateRetour);
        btnSave = findViewById(R.id.btnSave);

        membreRepository = new MembreRepository(getApplication());
        livreRepository = new LivreRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        preselectedLivreId = getIntent().getIntExtra("LIVRE_ID", -1);

        long dateRetour = DateUtils.addDays(
                System.currentTimeMillis(),
                LibraryConstants.DUREE_EMPRUNT_JOURS
        );
        txtDateRetour.setText("Date de retour prevue : " + DateUtils.formatDate(dateRetour));

        loadSpinners();
        btnSave.setOnClickListener(v -> saveEmprunt());
    }

    private void loadSpinners() {
        membreRepository.getMembresActifs().observe(this, membres -> {
            if (membres == null) {
                return;
            }

            membreList = membres;
            List<String> names = new ArrayList<>();
            for (Membre membre : membres) {
                names.add(membre.getNom() + " " + membre.getPrenom());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    names
            );
            spinnerMembre.setAdapter(adapter);
        });

        livreRepository.getLivresDisponibles().observe(this, livres -> {
            if (livres == null) {
                return;
            }

            livreList = livres;
            List<String> titles = new ArrayList<>();
            for (Livre livre : livres) {
                titles.add(livre.getTitre() + " (" + livre.getQuantiteDisponible() + " dispo)");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    titles
            );
            spinnerLivre.setAdapter(adapter);

            if (preselectedLivreId != -1) {
                spinnerLivre.setSelection(findLivrePosition(preselectedLivreId));
            }
        });
    }

    private int findLivrePosition(int livreId) {
        for (int index = 0; index < livreList.size(); index++) {
            if (livreList.get(index).getId() == livreId) {
                return index;
            }
        }
        return 0;
    }

    private void saveEmprunt() {
        if (membreList.isEmpty() || livreList.isEmpty()) {
            Toast.makeText(this, "Selectionnez un membre et un livre", Toast.LENGTH_SHORT).show();
            return;
        }

        int membrePos = spinnerMembre.getSelectedItemPosition();
        int livrePos = spinnerLivre.getSelectedItemPosition();
        if (membrePos < 0 || livrePos < 0) {
            Toast.makeText(this, "Selection invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        Membre membreSelectionne = membreList.get(membrePos);
        Livre livreSelectionne = livreList.get(livrePos);

        executorService.execute(() -> {
            Membre membre = db.membreDao().getMembreByIdSync(membreSelectionne.getId());
            Livre livre = db.livreDao().getLivreByIdSync(livreSelectionne.getId());

            if (membre == null || !LibraryConstants.STATUT_MEMBRE_ACTIF.equalsIgnoreCase(membre.getStatut())) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Ce membre ne peut pas emprunter",
                        Toast.LENGTH_SHORT
                ).show());
                return;
            }

            if (livre == null || livre.getQuantiteDisponible() <= 0) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Ce livre n'est plus disponible",
                        Toast.LENGTH_SHORT
                ).show());
                return;
            }

            long now = System.currentTimeMillis();
            long retourPrevu = DateUtils.addDays(now, LibraryConstants.DUREE_EMPRUNT_JOURS);

            try {
                db.runInTransaction(() -> {
                    if (db.livreDao().decrementStock(livre.getId()) == 0) {
                        throw new IllegalStateException("Stock indisponible");
                    }

                    Emprunt emprunt = new Emprunt(
                            membre.getId(),
                            livre.getId(),
                            now,
                            retourPrevu,
                            0,
                            LibraryConstants.STATUT_EMPRUNT_EN_COURS
                    );
                    db.empruntDao().insert(emprunt);
                });

                runOnUiThread(() -> {
                    Toast.makeText(this, "Emprunt enregistre", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception exception) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Impossible d'enregistrer l'emprunt",
                        Toast.LENGTH_SHORT
                ).show());
            }
        });
    }
}
