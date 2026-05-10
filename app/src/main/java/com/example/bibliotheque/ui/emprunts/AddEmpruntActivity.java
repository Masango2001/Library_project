package com.example.bibliotheque.ui.emprunts;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEmpruntActivity extends AppCompatActivity {

    private Spinner spinnerMembre;
    private Spinner spinnerLivre;
    private TextView txtDateRetour;
    private Button btnSave;
    private long selectedReturnDate;

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

        selectedReturnDate = retour;
        setReturnDateText(selectedReturnDate);
        txtDateRetour.setClickable(true);
        txtDateRetour.setOnClickListener(v -> showDatePicker());

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
            Toast.makeText(this, "Loading data...", Toast.LENGTH_SHORT).show();
            return;
        }

        int membrePos = spinnerMembre.getSelectedItemPosition();
        int livrePos = spinnerLivre.getSelectedItemPosition();

        if (membrePos < 0 || livrePos < 0) {
            Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
            return;
        }

        Membre membre = membreList.get(membrePos);
        Livre livre = livreList.get(livrePos);

        if (!LibraryConstants.STATUT_MEMBRE_ACTIF.equalsIgnoreCase(membre.getStatut())) {
            Toast.makeText(this, "Member is not allowed to borrow", Toast.LENGTH_SHORT).show();
            return;
        }

        if (livre.getQuantiteDisponible() <= 0) {
            Toast.makeText(this, "Book is unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();
        if (selectedReturnDate <= now) {
            Toast.makeText(this, "Please choose a future return date", Toast.LENGTH_SHORT).show();
            return;
        }

        long retour = selectedReturnDate;

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
                    Toast.makeText(this, "Loan saved", Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error while saving loan", Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedReturnDate);

        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, dayOfMonth, 0, 0, 0);
                    selected.set(Calendar.MILLISECOND, 0);

                    if (selected.getTimeInMillis() <= System.currentTimeMillis()) {
                        Toast.makeText(this, "Please select a future return date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    selectedReturnDate = selected.getTimeInMillis();
                    setReturnDateText(selectedReturnDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void setReturnDateText(long dateMillis) {
        txtDateRetour.setText("Return date: " + DateUtils.formatDate(dateMillis));
    }
}