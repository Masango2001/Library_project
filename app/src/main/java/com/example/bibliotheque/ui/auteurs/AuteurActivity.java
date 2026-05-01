package com.example.bibliotheque.ui.auteurs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.repository.AuteurRepository;

import java.util.List;
public class AuteurActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;

    AuteurAdapter adapter;
    AuteurRepository repository;

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

        // 🔥 Actions Adapter (modifier + supprimer)
        adapter.setListener(new AuteurAdapter.OnItemClickListener() {
            @Override
            public void onDelete(Auteur auteur) {
                repository.delete(auteur);
                Toast.makeText(AuteurActivity.this, "Auteur supprimé", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEdit(Auteur auteur) {
                Intent intent = new Intent(AuteurActivity.this, AddAuteurActivity.class);
                intent.putExtra("id", auteur.getId());
                intent.putExtra("nom", auteur.getNom());
                intent.putExtra("prenom", auteur.getPrenom());
                intent.putExtra("nationalite", auteur.getNationalite());
                startActivity(intent);
            }
        });

        loadAuteurs();

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddAuteurActivity.class)));
    }

    private void loadAuteurs() {
        repository.getAllAuteurs().observe(this, new Observer<List<Auteur>>() {
            @Override
            public void onChanged(List<Auteur> auteurs) {
                adapter.setList(auteurs);
            }
        });
    }
}