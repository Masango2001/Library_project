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

        long retour = DateUtils.addDays(
                System.currentTimeMillis(),
                LibraryConstants.DUREE_EMPRUNT_JOURS
        );

        txtDateRetour.setText("Date de retour prévue : " + DateUtils.formatDate(retour));

        loadSpinners();

        btnSave.setOnClickListener(v -> saveEmprunt());
    }

    private void loadSpinners() {

        membreRepository.getMembresActifs().observe(this, membres -> {
            if (membres == null) return;

            membreList = membres;

            List<String> names = new ArrayList<>();
            for (Membre m : membres) {
                names.add(m.getNom() + " " + m.getPrenom());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    names
            );

            spinnerMembre.setAdapter(adapter);
        });

        livreRepository.getLivresDisponibles().observe(this, livres -> {
            if (livres == null) return;

            livreList = livres;

            List<String> titles = new ArrayList<>();
            for (Livre l : livres) {
                titles.add(l.getTitre() + " (" + l.getQuantiteDisponible() + " dispo)");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    titles
            );

            spinnerLivre.setAdapter(adapter);

            // ✔ sélection sécurisée du livre préselectionné
            if (preselectedLivreId != -1) {
                int pos = findLivrePosition(preselectedLivreId);
                if (pos >= 0) {
                    spinnerLivre.setSelection(pos);
                }
            }
        });
    }

    private int findLivrePosition(int livreId) {
        for (int i = 0; i < livreList.size(); i++) {
            if (livreList.get(i).getId() == livreId) {
                return i;
            }
        }
        return 0;
    }

    private void saveEmprunt() {

        if (membreList.isEmpty() || livreList.isEmpty()) {
            Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_SHORT).show();
            return;
        }

        int membrePos = spinnerMembre.getSelectedItemPosition();
        int livrePos = spinnerLivre.getSelectedItemPosition();

        if (membrePos < 0 || livrePos < 0) {
            Toast.makeText(this, "Sélection invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        Membre membre = membreList.get(membrePos);
        Livre livre = livreList.get(livrePos);

        if (!LibraryConstants.STATUT_MEMBRE_ACTIF.equalsIgnoreCase(membre.getStatut())) {
            Toast.makeText(this, "Membre non autorisé à emprunter", Toast.LENGTH_SHORT).show();
            return;
        }

        if (livre.getQuantiteDisponible() <= 0) {
            Toast.makeText(this, "Livre indisponible", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();
        long retour = DateUtils.addDays(now, LibraryConstants.DUREE_EMPRUNT_JOURS);

        executorService.execute(() -> {

            try {
                db.runInTransaction(() -> {

                    if (db.livreDao().decrementStock(livre.getId()) == 0) {
                        throw new IllegalStateException("Stock indisponible");
                    }

                    Emprunt emprunt = new Emprunt(
                            membre.getId(),
                            livre.getId(),
                            now,
                            retour,
                            0,
                            LibraryConstants.STATUT_EMPRUNT_EN_COURS
                    );

                    db.empruntDao().insert(emprunt);
                });

                runOnUiThread(() -> {
                    Toast.makeText(this, "Emprunt enregistré", Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Erreur lors de l'emprunt", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}