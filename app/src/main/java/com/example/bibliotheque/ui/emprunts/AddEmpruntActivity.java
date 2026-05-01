package com.example.bibliotheque.ui.emprunts;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.EmpruntRepository;
import com.example.bibliotheque.repository.LivreRepository;
import com.example.bibliotheque.repository.MembreRepository;

import java.util.ArrayList;
import java.util.List;

public class AddEmpruntActivity extends AppCompatActivity {

    private Spinner spinnerMembre, spinnerLivre;
    private Button btnSave;

    private EmpruntRepository empruntRepository;
    private MembreRepository membreRepository;
    private LivreRepository livreRepository;

    private List<Membre> membreList = new ArrayList<>();
    private List<Livre> livreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emprunt);

        // Correction des IDs pour correspondre au layout XML (Spinners au lieu d'EditText)
        spinnerMembre = findViewById(R.id.spinnerMembre);
        spinnerLivre = findViewById(R.id.spinnerLivre);
        btnSave = findViewById(R.id.btnSave);

        empruntRepository = new EmpruntRepository(getApplication());
        membreRepository = new MembreRepository(getApplication());
        livreRepository = new LivreRepository(getApplication());

        loadSpinners();

        btnSave.setOnClickListener(v -> saveEmprunt());
    }

    private void loadSpinners() {
        // Charger les membres actifs
        membreRepository.getMembresActifs().observe(this, membres -> {
            if (membres != null) {
                membreList = membres;
                List<String> names = new ArrayList<>();
                for (Membre m : membres) {
                    names.add(m.getNom() + " " + m.getPrenom());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        android.R.layout.simple_spinner_dropdown_item, names);
                spinnerMembre.setAdapter(adapter);
            }
        });

        // Charger les livres
        livreRepository.getAllLivres().observe(this, livres -> {
            if (livres != null) {
                livreList = livres;
                List<String> titles = new ArrayList<>();
                for (Livre l : livres) {
                    titles.add(l.getTitre() + " (" + l.getQuantiteDisponible() + " dispo)");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                        android.R.layout.simple_spinner_dropdown_item, titles);
                spinnerLivre.setAdapter(adapter);
            }
        });
    }

    private void saveEmprunt() {
        if (membreList.isEmpty() || livreList.isEmpty()) {
            Toast.makeText(this, "Sélectionnez un membre et un livre", Toast.LENGTH_SHORT).show();
            return;
        }

        int membrePos = spinnerMembre.getSelectedItemPosition();
        int livrePos = spinnerLivre.getSelectedItemPosition();

        if (membrePos < 0 || livrePos < 0) {
            Toast.makeText(this, "Sélection invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        Membre selectedMembre = membreList.get(membrePos);
        Livre selectedLivre = livreList.get(livrePos);

        if (selectedLivre.getQuantiteDisponible() <= 0) {
            Toast.makeText(this, "Ce livre n'est plus disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();
        long retourPrevu = now + (7L * 24 * 60 * 60 * 1000); // +7 jours

        Emprunt e = new Emprunt(
                selectedMembre.getId(),
                selectedLivre.getId(),
                now,
                retourPrevu,
                0,
                "EN_COURS"
        );

        empruntRepository.insert(e);

        // Mettre à jour le stock du livre
        selectedLivre.setQuantiteDisponible(selectedLivre.getQuantiteDisponible() - 1);
        livreRepository.update(selectedLivre);

        Toast.makeText(this, "Emprunt enregistré", Toast.LENGTH_SHORT).show();
        finish();
    }
}