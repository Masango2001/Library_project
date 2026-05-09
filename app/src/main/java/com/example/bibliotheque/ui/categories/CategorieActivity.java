package com.example.bibliotheque.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.repository.CategorieRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategorieActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private CategorieAdapter adapter;
    private CategorieRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;
    private SearchView searchView;
    private LiveData<List<Categorie>> currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerCategories);
        btnAdd = findViewById(R.id.btnAddCategorie);
        searchView = findViewById(R.id.searchCategorie);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategorieAdapter();
        recyclerView.setAdapter(adapter);

        repository = new CategorieRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        setupSearch();

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
                        runOnUiThread(() ->
                                Toast.makeText(CategorieActivity.this,
                                        "Catégorie supprimée",
                                        Toast.LENGTH_SHORT).show()
                        );
                    } catch (Exception e) {
                        runOnUiThread(() ->
                                Toast.makeText(CategorieActivity.this,
                                        "Suppression impossible: catégorie utilisée",
                                        Toast.LENGTH_LONG).show()
                        );
                    }
                });
            }
        });

        loadCategories(null);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddCategorieActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupSearch() {
        searchView.setQueryHint("Rechercher une catégorie");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadCategories(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadCategories(newText);
                return true;
            }
        });
    }

    private void loadCategories(String query) {

        if (currentSource != null) {
            currentSource.removeObservers(this);
        }

        if (TextUtils.isEmpty(query)) {
            currentSource = repository.getAllCategories();
        } else {
            currentSource = repository.searchCategories(query.trim());
        }

        currentSource.observe(this, adapter::setList);
    }
}