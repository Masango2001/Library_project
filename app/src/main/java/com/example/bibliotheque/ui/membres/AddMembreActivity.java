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
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.util.LibraryConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddMembreActivity extends AppCompatActivity {

    private EditText edtNom;
    private EditText edtPrenom;
    private EditText edtEmail;
    private EditText edtTelephone;
    private EditText edtAdresse;
    private Spinner spinnerStatut;
    private Button btnSave;
    private AppDatabase db;
    private ExecutorService executorService;
    private int membreId = -1;
    private String dateInscriptionExistante;

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

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        setupSpinner();
        loadEditMode();

        btnSave.setOnClickListener(v -> saveMembre());
    }

    private void setupSpinner() {
        String[] statuts = {
                LibraryConstants.STATUT_MEMBRE_ACTIF,
                LibraryConstants.STATUT_MEMBRE_SUSPENDU
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statuts
        );
        spinnerStatut.setAdapter(adapter);
    }

    private void loadEditMode() {
        if (!getIntent().hasExtra("id")) {
            return;
        }

        membreId = getIntent().getIntExtra("id", -1);
        edtNom.setText(getIntent().getStringExtra("nom"));
        edtPrenom.setText(getIntent().getStringExtra("prenom"));
        edtEmail.setText(getIntent().getStringExtra("email"));
        edtTelephone.setText(getIntent().getStringExtra("telephone"));
        edtAdresse.setText(getIntent().getStringExtra("adresse"));
        dateInscriptionExistante = getIntent().getStringExtra("date_inscription");

        String statut = getIntent().getStringExtra("statut");
        if (LibraryConstants.STATUT_MEMBRE_SUSPENDU.equals(statut)) {
            spinnerStatut.setSelection(1);
        }

        setTitle("Modifier membre");
    }

    private void saveMembre() {
        String nom = edtNom.getText().toString().trim();
        String prenom = edtPrenom.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String telephone = edtTelephone.getText().toString().trim();
        String adresse = edtAdresse.getText().toString().trim();
        String statut = spinnerStatut.getSelectedItem().toString();

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Nom, prenom et email sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        String dateInscription = dateInscriptionExistante;
        if (TextUtils.isEmpty(dateInscription)) {
            dateInscription = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date());
        }

        String finalDateInscription = dateInscription;
        executorService.execute(() -> {
            if (db.membreDao().countByEmailExcludingId(email, membreId) > 0) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Cet email est deja utilise",
                        Toast.LENGTH_SHORT
                ).show());
                return;
            }

            Membre membre = new Membre(
                    nom,
                    prenom,
                    email,
                    telephone,
                    adresse,
                    finalDateInscription,
                    statut
            );

            if (membreId == -1) {
                db.membreDao().insert(membre);
            } else {
                membre.setId(membreId);
                db.membreDao().update(membre);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Membre enregistre", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
