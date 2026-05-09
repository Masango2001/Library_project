package com.example.bibliotheque.ui.livres;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.model.LivreCatalogueItem;
import com.example.bibliotheque.repository.LivreRepository;
import com.example.bibliotheque.ui.emprunts.AddEmpruntActivity;
import com.example.bibliotheque.ui.emprunts.EmpruntActivity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LivreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private LivreAdapter adapter;

    private LivreRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    private LiveData<List<LivreCatalogueItem>> currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livre);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerLivres);
        btnAdd = findViewById(R.id.btnAddLivre);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LivreAdapter();
        recyclerView.setAdapter(adapter);

        repository = new LivreRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        setupSearch();
        setupActions();

        observeCatalogue(null);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddLivreActivity.class))
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupSearch() {
        ViewGroup root = (ViewGroup) btnAdd.getParent();

        SearchView searchView = new SearchView(this);
        searchView.setQueryHint("Rechercher livre");
        searchView.setIconifiedByDefault(false);

        root.addView(searchView, 1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                observeCatalogue(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                observeCatalogue(newText);
                return true;
            }
        });
    }

    private void setupActions() {

        adapter.setListener(new LivreAdapter.OnLivreAction() {

            @Override
            public void onDelete(LivreCatalogueItem livre) {
                executorService.execute(() -> {
                    try {
                        db.livreDao().delete(livre.toLivre());

                        runOnUiThread(() ->
                                Toast.makeText(LivreActivity.this,
                                        "Livre supprimé",
                                        Toast.LENGTH_SHORT).show()
                        );

                    } catch (Exception e) {
                        runOnUiThread(() ->
                                Toast.makeText(LivreActivity.this,
                                        "Impossible de supprimer (emprunts liés)",
                                        Toast.LENGTH_LONG).show()
                        );
                    }
                });
            }

            @Override
            public void onUpdate(LivreCatalogueItem livre) {
                Intent intent = new Intent(LivreActivity.this, AddLivreActivity.class);

                intent.putExtra("id", livre.id);
                intent.putExtra("titre", livre.titre);
                intent.putExtra("isbn", livre.isbn);
                intent.putExtra("annee_publication", livre.anneePublication);
                intent.putExtra("editeur", livre.editeur);
                intent.putExtra("categorie_id", livre.categorieId);
                intent.putExtra("auteur_id", livre.auteurId);
                intent.putExtra("quantite_totale", livre.quantiteTotale);
                intent.putExtra("quantite_disponible", livre.quantiteDisponible);

                startActivity(intent);
            }

            @Override
            public void onEmprunt(LivreCatalogueItem livre) {
                if (livre.quantiteDisponible <= 0) {
                    Toast.makeText(LivreActivity.this,
                            "Stock indisponible",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(LivreActivity.this, AddEmpruntActivity.class);
                intent.putExtra("LIVRE_ID", livre.id);
                startActivity(intent);
            }

            @Override
            public void onRetour(LivreCatalogueItem livre) {
                startActivity(new Intent(LivreActivity.this, EmpruntActivity.class));

                Toast.makeText(LivreActivity.this,
                        "Choisissez l’emprunt à retourner",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeCatalogue(String query) {

        if (currentSource != null) {
            currentSource.removeObservers(this);
        }

        if (TextUtils.isEmpty(query)) {
            currentSource = repository.getCatalogue();
        } else {
            currentSource = repository.searchCatalogue(query.trim());
        }

        currentSource.observe(this, adapter::setList);
    }
}