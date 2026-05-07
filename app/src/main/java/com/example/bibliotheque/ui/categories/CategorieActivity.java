package com.example.bibliotheque.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.repository.CategorieRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorieActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAdd;
    private CategorieAdapter adapter;
    private CategorieRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        recyclerView = findViewById(R.id.recyclerCategories);
        btnAdd = findViewById(R.id.btnAddCategorie);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategorieAdapter();
        recyclerView.setAdapter(adapter);

        repository = new CategorieRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        adapter.setListener(new CategorieAdapter.OnCategorieActionListener() {
            @Override
            public void onEdit(Categorie categorie) {
                Intent intent = new Intent(CategorieActivity.this, AddCategorieActivity.class);
                intent.putExtra("id", categorie.getId());
                intent.putExtra("nom", categorie.getNom());
                intent.putExtra("description", categorie.getDescription());
                startActivity(intent);
            }

            @Override
            public void onDelete(Categorie categorie) {
                executorService.execute(() -> {
                    try {
                        db.categorieDao().delete(categorie);
                        runOnUiThread(() -> Toast.makeText(
                                CategorieActivity.this,
                                "Categorie supprimee",
                                Toast.LENGTH_SHORT
                        ).show());
                    } catch (Exception exception) {
                        runOnUiThread(() -> Toast.makeText(
                                CategorieActivity.this,
                                "Suppression impossible: cette categorie est deja utilisee",
                                Toast.LENGTH_LONG
                        ).show());
                    }
                });
            }
        });

        loadCategories();
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddCategorieActivity.class)));
    }

    private void loadCategories() {
        repository.getAllCategories().observe(this, adapter::setList);
    }
}
