package com.example.bibliotheque.ui.auteurs;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.repository.AuteurRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuteurActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private AuteurAdapter adapter;

    private AuteurRepository repository;
    private ExecutorService executorService;

    private SearchView searchView;
    private LiveData<List<Auteur>> currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auteur);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerAuteurs);
        btnAdd = findViewById(R.id.btnAddAuteur);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AuteurAdapter();
        recyclerView.setAdapter(adapter);

        repository = new AuteurRepository(getApplication());
        executorService = Executors.newSingleThreadExecutor();

        setupSearch();
        setupActions();

        loadAuteurs(null);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddAuteurActivity.class))
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupSearch() {
        searchView = findViewById(R.id.searchViewAuteur);

        searchView.setQueryHint("Rechercher un auteur");
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadAuteurs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadAuteurs(newText);
                return true;
            }
        });
    }

    private void setupActions() {

        adapter.setListener(new AuteurAdapter.OnItemClickListener() {

            @Override
            public void onDelete(Auteur auteur) {

                executorService.execute(() -> {
                    try {
                        repository.delete(auteur);

                        runOnUiThread(() ->
                                Toast.makeText(
                                        AuteurActivity.this,
                                        "Auteur supprimé",
                                        Toast.LENGTH_SHORT
                                ).show()
                        );

                    } catch (Exception e) {

                        runOnUiThread(() ->
                                Toast.makeText(
                                        AuteurActivity.this,
                                        "Suppression impossible: auteur utilisé",
                                        Toast.LENGTH_LONG
                                ).show()
                        );
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
    }

    private void loadAuteurs(String query) {

        if (currentSource != null) {
            currentSource.removeObservers(this);
        }

        if (TextUtils.isEmpty(query)) {
            currentSource = repository.getAllAuteurs();
        } else {
            currentSource = repository.searchAuteurs(query.trim());
        }

        currentSource.observe(this, adapter::setList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}