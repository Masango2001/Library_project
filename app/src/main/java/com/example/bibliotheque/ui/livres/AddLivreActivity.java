package com.example.bibliotheque.ui.livres;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.repository.AuteurRepository;
import com.example.bibliotheque.repository.CategorieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddLivreActivity extends AppCompatActivity {

    private EditText titre;
    private EditText isbn;
    private EditText annee;
    private EditText editeur;
    private EditText stock;
    private Spinner spinnerAuteur;
    private Spinner spinnerCategorie;
    private Button btnSave;
    private AuteurRepository auteurRepository;
    private CategorieRepository categorieRepository;
    private AppDatabase db;
    private ExecutorService executorService;
    private List<Auteur> auteurList = new ArrayList<>();
    private List<Categorie> categorieList = new ArrayList<>();
    private int livreId = -1;
    private int auteurIdSelection = -1;
    private int categorieIdSelection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_livre);

        titre = findViewById(R.id.edtTitre);
        isbn = findViewById(R.id.edtIsbn);
        annee = findViewById(R.id.edtAnnee);
        editeur = findViewById(R.id.edtEditeur);
        stock = findViewById(R.id.edtStock);
        spinnerAuteur = findViewById(R.id.spinnerAuteur);
        spinnerCategorie = findViewById(R.id.spinnerCategorie);
        btnSave = findViewById(R.id.btnSave);

        auteurRepository = new AuteurRepository(getApplication());
        categorieRepository = new CategorieRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        loadEditMode();
        loadSpinners();

        btnSave.setOnClickListener(v -> saveLivre());
    }

    private void loadEditMode() {
        if (!getIntent().hasExtra("id")) {
            return;
        }

        livreId = getIntent().getIntExtra("id", -1);
        auteurIdSelection = getIntent().getIntExtra("auteur_id", -1);
        categorieIdSelection = getIntent().getIntExtra("categorie_id", -1);

        titre.setText(getIntent().getStringExtra("titre"));
        isbn.setText(getIntent().getStringExtra("isbn"));
        annee.setText(String.valueOf(getIntent().getIntExtra("annee_publication", 0)));
        editeur.setText(getIntent().getStringExtra("editeur"));
        stock.setText(String.valueOf(getIntent().getIntExtra("quantite_totale", 0)));
        setTitle("Modifier livre");
    }

    private void loadSpinners() {
        auteurRepository.getAllAuteurs().observe(this, auteurs -> {
            if (auteurs == null) {
                return;
            }

            auteurList = auteurs;
            List<String> names = new ArrayList<>();
            for (Auteur auteur : auteurs) {
                names.add(auteur.getNom() + " " + auteur.getPrenom());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    names
            );
            spinnerAuteur.setAdapter(adapter);
            if (auteurIdSelection != -1) {
                spinnerAuteur.setSelection(findAuteurPosition(auteurIdSelection));
            }
        });

        categorieRepository.getAllCategories().observe(this, categories -> {
            if (categories == null) {
                return;
            }

            categorieList = categories;
            List<String> names = new ArrayList<>();
            for (Categorie categorie : categories) {
                names.add(categorie.getNom());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    names
            );
            spinnerCategorie.setAdapter(adapter);
            if (categorieIdSelection != -1) {
                spinnerCategorie.setSelection(findCategoriePosition(categorieIdSelection));
            }
        });
    }

    private int findAuteurPosition(int auteurId) {
        for (int index = 0; index < auteurList.size(); index++) {
            if (auteurList.get(index).getId() == auteurId) {
                return index;
            }
        }
        return 0;
    }

    private int findCategoriePosition(int categorieId) {
        for (int index = 0; index < categorieList.size(); index++) {
            if (categorieList.get(index).getId() == categorieId) {
                return index;
            }
        }
        return 0;
    }

    private void saveLivre() {
        String titreValue = titre.getText().toString().trim();
        String isbnValue = isbn.getText().toString().trim();
        String anneeValue = annee.getText().toString().trim();
        String editeurValue = editeur.getText().toString().trim();
        String stockValue = stock.getText().toString().trim();

        if (TextUtils.isEmpty(titreValue)
                || TextUtils.isEmpty(isbnValue)
                || TextUtils.isEmpty(editeurValue)
                || TextUtils.isEmpty(stockValue)) {
            Toast.makeText(this, "Titre, ISBN, editeur et stock sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auteurList.isEmpty() || categorieList.isEmpty()) {
            Toast.makeText(this, "Ajoutez d'abord un auteur et une categorie", Toast.LENGTH_SHORT).show();
            return;
        }

        int anneeInt;
        int stockInt;
        try {
            anneeInt = TextUtils.isEmpty(anneeValue) ? 2026 : Integer.parseInt(anneeValue);
            stockInt = Integer.parseInt(stockValue);
        } catch (NumberFormatException exception) {
            Toast.makeText(this, "Annee ou stock invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (stockInt <= 0) {
            Toast.makeText(this, "Le stock doit etre superieur a zero", Toast.LENGTH_SHORT).show();
            return;
        }

        int auteurId = auteurList.get(spinnerAuteur.getSelectedItemPosition()).getId();
        int categorieId = categorieList.get(spinnerCategorie.getSelectedItemPosition()).getId();

        executorService.execute(() -> {
            if (db.livreDao().countByIsbnExcludingId(isbnValue, livreId) > 0) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Cet ISBN existe deja",
                        Toast.LENGTH_SHORT
                ).show());
                return;
            }

            int quantiteDisponible = stockInt;
            if (livreId != -1) {
                Livre livreExistant = db.livreDao().getLivreByIdSync(livreId);
                if (livreExistant == null) {
                    runOnUiThread(() -> Toast.makeText(
                            this,
                            "Livre introuvable",
                            Toast.LENGTH_SHORT
                    ).show());
                    return;
                }

                int quantiteEmpruntee = livreExistant.getQuantiteTotale() - livreExistant.getQuantiteDisponible();
                if (stockInt < quantiteEmpruntee) {
                    runOnUiThread(() -> Toast.makeText(
                            this,
                            "Le stock total ne peut pas etre inferieur aux exemplaires deja empruntes",
                            Toast.LENGTH_LONG
                    ).show());
                    return;
                }
                quantiteDisponible = stockInt - quantiteEmpruntee;
            }

            Livre livre = new Livre(
                    titreValue,
                    isbnValue,
                    anneeInt,
                    editeurValue,
                    categorieId,
                    auteurId,
                    stockInt,
                    quantiteDisponible
            );

            if (livreId == -1) {
                db.livreDao().insert(livre);
            } else {
                livre.setId(livreId);
                db.livreDao().update(livre);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Livre enregistre", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
