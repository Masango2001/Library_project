package com.example.bibliotheque.ui.membres;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.MembreRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddMembreActivity extends AppCompatActivity {

    private EditText edtNom, edtPrenom, edtEmail, edtTelephone, edtAdresse;
    private Spinner spinnerStatut;
    private Button btnSave;
    private MembreRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_membre);

        edtNom = findViewById(R.id.edtNom);
        edtPrenom = findViewById(R.id.edtPrenom);
        edtEmail = findViewById(R.id.edtEmail);
        edtTelephone = findViewById(R.id.edtTelephone);
        edtAdresse = findViewById(R.id.edtAdresse);
        spinnerStatut = findViewById(R.id.spinnerStatut);
        btnSave = findViewById(R.id.btnSave);

        repository = new MembreRepository(getApplication());

        setupSpinner();

        btnSave.setOnClickListener(v -> saveMembre());
    }

    private void setupSpinner() {
        String[] statuts = {"actif", "suspendu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statuts);
        spinnerStatut.setAdapter(adapter);
    }

    private void saveMembre() {
        String nom = edtNom.getText().toString().trim();
        String prenom = edtPrenom.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telephone = edtTelephone.getText().toString().trim();
        String adresse = edtAdresse.getText().toString().trim();
        String statut = spinnerStatut.getSelectedItem().toString();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Veuillez remplir le nom et l'email", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateInscription = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Membre membre = new Membre(nom, prenom, email, telephone, adresse, dateInscription, statut);
        repository.insert(membre);

        Toast.makeText(this, "Membre enregistré", Toast.LENGTH_SHORT).show();
        finish();
    }
}