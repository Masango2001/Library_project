package com.example.bibliotheque.ui.membres;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.MembreRepository;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MembreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private MembreAdapter adapter;

    private MembreRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    private SearchView searchView;
    private LiveData<List<Membre>> currentSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerMembres);
        btnAdd = findViewById(R.id.btnAddMembre);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MembreAdapter();
        recyclerView.setAdapter(adapter);

        repository = new MembreRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        setupSearch();
        setupActions();

        loadMembres(null);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddMembreActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupSearch() {
        searchView = new SearchView(this);
        searchView.setQueryHint("Rechercher un membre");
        searchView.setIconifiedByDefault(false);

        ViewGroup root = (ViewGroup) btnAdd.getParent();
        root.addView(searchView, 1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                loadMembres(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadMembres(newText);
                return true;
            }
        });
    }

    private void setupActions() {

        adapter.setListener(new MembreAdapter.OnMembreActionListener() {

            @Override
            public void onEdit(Membre membre) {
                Intent intent = new Intent(MembreActivity.this, AddMembreActivity.class);

                intent.putExtra("id", membre.getId());
                intent.putExtra("nom", membre.getNom());
                intent.putExtra("prenom", membre.getPrenom());
                intent.putExtra("email", membre.getEmail());
                intent.putExtra("telephone", membre.getTelephone());
                intent.putExtra("adresse", membre.getAdresse());
                intent.putExtra("date_inscription", membre.getDateInscription());
                intent.putExtra("statut", membre.getStatut());

                startActivity(intent);
            }

            @Override
            public void onToggleStatut(Membre membre) {

                executorService.execute(() -> {

                    String nouveauStatut =
                            LibraryConstants.STATUT_MEMBRE_ACTIF.equals(membre.getStatut())
                                    ? LibraryConstants.STATUT_MEMBRE_SUSPENDU
                                    : LibraryConstants.STATUT_MEMBRE_ACTIF;

                    db.membreDao().updateStatut(membre.getId(), nouveauStatut);

                    runOnUiThread(() ->
                            Toast.makeText(MembreActivity.this,
                                    "Statut mis à jour",
                                    Toast.LENGTH_SHORT).show()
                    );
                });
            }

            @Override
            public void onDelete(Membre membre) {

                executorService.execute(() -> {

                    try {
                        db.membreDao().delete(membre);

                        runOnUiThread(() ->
                                Toast.makeText(MembreActivity.this,
                                        "Membre supprimé",
                                        Toast.LENGTH_SHORT).show()
                        );

                    } catch (Exception e) {

                        runOnUiThread(() ->
                                Toast.makeText(MembreActivity.this,
                                        "Impossible de supprimer ce membre",
                                        Toast.LENGTH_LONG).show()
                        );
                    }
                });
            }
        });
    }

    private void loadMembres(String query) {

        if (currentSource != null) {
            currentSource.removeObservers(this);
        }

        if (TextUtils.isEmpty(query)) {
            currentSource = repository.getAllMembres();
        } else {
            currentSource = repository.searchMembres(query.trim());
        }

        currentSource.observe(this, adapter::setList);
    }
}