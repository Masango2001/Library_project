package com.example.bibliotheque.ui.categories;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.repository.CategorieRepository;

public class AddCategorieActivity extends AppCompatActivity {

    private EditText edtNom, edtDescription;
    private Button btnSave;
    private CategorieRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categorie);

        edtNom = findViewById(R.id.edtNom);
        edtDescription = findViewById(R.id.edtDescription);
        btnSave = findViewById(R.id.btnSave);

        repository = new CategorieRepository(getApplication());

        btnSave.setOnClickListener(v -> {
            String nom = edtNom.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            if (nom.isEmpty()) {
                Toast.makeText(this, "Veuillez saisir un nom", Toast.LENGTH_SHORT).show();
                return;
            }

            Categorie categorie = new Categorie(nom, description);
            repository.insert(categorie);
            
            Toast.makeText(this, "Catégorie ajoutée", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}