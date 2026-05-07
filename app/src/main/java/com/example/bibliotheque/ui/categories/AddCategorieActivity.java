package com.example.bibliotheque.ui.categories;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Categorie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddCategorieActivity extends AppCompatActivity {

    private EditText edtNom;
    private EditText edtDescription;
    private Button btnSave;
    private AppDatabase db;
    private ExecutorService executorService;
    private int categorieId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categorie);

        edtNom = findViewById(R.id.edtNom);
        edtDescription = findViewById(R.id.edtDescription);
        btnSave = findViewById(R.id.btnSave);

        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        loadEditMode();

        btnSave.setOnClickListener(v -> saveCategorie());
    }

    private void loadEditMode() {
        if (!getIntent().hasExtra("id")) {
            return;
        }

        categorieId = getIntent().getIntExtra("id", -1);
        edtNom.setText(getIntent().getStringExtra("nom"));
        edtDescription.setText(getIntent().getStringExtra("description"));
        setTitle("Modifier categorie");
    }

    private void saveCategorie() {
        String nom = edtNom.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(nom)) {
            Toast.makeText(this, "Veuillez saisir un nom", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            if (db.categorieDao().existsByNameExcludingId(nom, categorieId) > 0) {
                runOnUiThread(() -> Toast.makeText(
                        this,
                        "Cette categorie existe deja",
                        Toast.LENGTH_SHORT
                ).show());
                return;
            }

            Categorie categorie = new Categorie(nom, description);
            if (categorieId == -1) {
                db.categorieDao().insert(categorie);
            } else {
                categorie.setId(categorieId);
                db.categorieDao().update(categorie);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Categorie enregistree", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
