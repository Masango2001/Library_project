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
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.repository.AuteurRepository;
import com.example.bibliotheque.repository.CategorieRepository;
import com.example.bibliotheque.repository.LivreRepository;

import java.util.ArrayList;
import java.util.List;

public class AddLivreActivity extends AppCompatActivity {

    private EditText titre, isbn, annee, editeur, stock;
    private Spinner spinnerAuteur, spinnerCategorie;
    private Button btnSave;

    private LivreRepository repository;
    private AuteurRepository auteurRepository;
    private CategorieRepository categorieRepository;

    private List<Auteur> auteurList = new ArrayList<>();
    private List<Categorie> categorieList = new ArrayList<>();

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

        repository = new LivreRepository(getApplication());
        auteurRepository = new AuteurRepository(getApplication());
        categorieRepository = new CategorieRepository(getApplication());

        loadSpinners();

        btnSave.setOnClickListener(v -> saveLivre());
    }

    private void loadSpinners() {
        auteurRepository.getAllAuteurs().observe(this, auteurs -> {
            if (auteurs != null) {
                auteurList = auteurs;
                List<String> names = new ArrayList<>();
                for (Auteur a : auteurs) {
                    names.add(a.getNom() + " " + a.getPrenom());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
                spinnerAuteur.setAdapter(adapter);
            }
        });

        categorieRepository.getAllCategories().observe(this, categories -> {
            if (categories != null) {
                categorieList = categories;
                List<String> names = new ArrayList<>();
                for (Categorie c : categories) {
                    names.add(c.getNom());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);
                spinnerCategorie.setAdapter(adapter);
            }
        });
    }

    private void saveLivre() {
        String t = titre.getText().toString().trim();
        String i = isbn.getText().toString().trim();
        String a = annee.getText().toString().trim();
        String e = editeur.getText().toString().trim();
        String s = stock.getText().toString().trim();

        if (TextUtils.isEmpty(t) || TextUtils.isEmpty(i)) {
            Toast.makeText(this, "Titre et ISBN obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auteurList.isEmpty() || categorieList.isEmpty()) {
            Toast.makeText(this, "Veuillez d'abord ajouter un auteur et une catégorie", Toast.LENGTH_SHORT).show();
            return;
        }

        int anneeInt = TextUtils.isEmpty(a) ? 2024 : Integer.parseInt(a);
        int stockInt = TextUtils.isEmpty(s) ? 1 : Integer.parseInt(s);

        int auteurId = auteurList.get(spinnerAuteur.getSelectedItemPosition()).getId();
        int categorieId = categorieList.get(spinnerCategorie.getSelectedItemPosition()).getId();

        Livre livre = new Livre(t, i, anneeInt, e, categorieId, auteurId, stockInt, stockInt);
        repository.insert(livre);

        Toast.makeText(this, "Livre ajouté", Toast.LENGTH_SHORT).show();
        finish();
    }
}