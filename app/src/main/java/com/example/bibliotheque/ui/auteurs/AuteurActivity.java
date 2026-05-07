package com.example.bibliotheque.ui.auteurs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.repository.AuteurRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuteurActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAdd;
    private AuteurAdapter adapter;
    private AuteurRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auteur);

        recyclerView = findViewById(R.id.recyclerAuteurs);
        btnAdd = findViewById(R.id.btnAddAuteur);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AuteurAdapter();
        recyclerView.setAdapter(adapter);

        repository = new AuteurRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        adapter.setListener(new AuteurAdapter.OnItemClickListener() {
            @Override
            public void onDelete(Auteur auteur) {
                executorService.execute(() -> {
                    try {
                        db.auteurDao().delete(auteur);
                        runOnUiThread(() -> Toast.makeText(
                                AuteurActivity.this,
                                "Auteur supprime",
                                Toast.LENGTH_SHORT
                        ).show());
                    } catch (Exception exception) {
                        runOnUiThread(() -> Toast.makeText(
                                AuteurActivity.this,
                                "Suppression impossible: cet auteur est deja utilise",
                                Toast.LENGTH_LONG
                        ).show());
                    }
                });
            }

            @Override
            public void onEdit(Auteur auteur) {
                Intent intent = new Intent(AuteurActivity.this, AddAuteurActivity.class);
                intent.putExtra("id", auteur.getId());
                intent.putExtra("nom", auteur.getNom());
                intent.putExtra("prenom", auteur.getPrenom());
                intent.putExtra("nationalite", auteur.getNationalite());
                intent.putExtra("date_naissance", auteur.getDateNaissance());
                startActivity(intent);
            }
        });

        loadAuteurs();
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddAuteurActivity.class)));
    }

    private void loadAuteurs() {
        repository.getAllAuteurs().observe(this, adapter::setList);
    }
}
