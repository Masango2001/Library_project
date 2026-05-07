package com.example.bibliotheque.ui.auteurs;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.repository.AuteurRepository;

public class AddAuteurActivity extends AppCompatActivity {

    private EditText nom;
    private EditText prenom;
    private EditText nationalite;
    private EditText dateNaissance;
    private Button btnSave;
    private AuteurRepository repository;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auteur);

        nom = findViewById(R.id.edtNom);
        prenom = findViewById(R.id.edtPrenom);
        nationalite = findViewById(R.id.edtNationalite);
        dateNaissance = findViewById(R.id.edtDateNaissance);
        btnSave = findViewById(R.id.btnSave);

        repository = new AuteurRepository(getApplication());
        loadEditMode();

        btnSave.setOnClickListener(v -> saveAuteur());
    }

    private void loadEditMode() {
        if (!getIntent().hasExtra("id")) {
            return;
        }

        id = getIntent().getIntExtra("id", -1);
        nom.setText(getIntent().getStringExtra("nom"));
        prenom.setText(getIntent().getStringExtra("prenom"));
        nationalite.setText(getIntent().getStringExtra("nationalite"));
        dateNaissance.setText(getIntent().getStringExtra("date_naissance"));
        setTitle("Modifier auteur");
    }

    private void saveAuteur() {
        String nomStr = nom.getText().toString().trim();
        String prenomStr = prenom.getText().toString().trim();
        String natStr = nationalite.getText().toString().trim();
        String dateNaissanceStr = dateNaissance.getText().toString().trim();

        if (TextUtils.isEmpty(nomStr)
                || TextUtils.isEmpty(prenomStr)
                || TextUtils.isEmpty(natStr)
                || TextUtils.isEmpty(dateNaissanceStr)) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        Auteur auteur = new Auteur(nomStr, prenomStr, natStr, dateNaissanceStr);
        if (id == -1) {
            repository.insert(auteur);
        } else {
            auteur.setId(id);
            repository.update(auteur);
        }

        Toast.makeText(this, "Auteur enregistre", Toast.LENGTH_SHORT).show();
        finish();
    }
}
