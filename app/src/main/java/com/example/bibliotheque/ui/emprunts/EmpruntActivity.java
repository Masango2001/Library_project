package com.example.bibliotheque.ui.emprunts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.model.EmpruntDisplayItem;
import com.example.bibliotheque.repository.EmpruntRepository;
import com.example.bibliotheque.util.DateUtils;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmpruntActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private EmpruntAdapter adapter;
    private EmpruntRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprunt);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerEmprunts);
        btnAdd = findViewById(R.id.btnAddEmprunt);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmpruntAdapter();
        recyclerView.setAdapter(adapter);

        repository = new EmpruntRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        adapter.setListener(this::enregistrerRetour);

        loadData();
        refreshRetards();

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEmpruntActivity.class)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshRetards();
    }

    private void loadData() {
        repository.getAllDetails().observe(this, list -> {
            if (list != null) {
                adapter.setList(list);
            }
        });
    }

    private void refreshRetards() {
        repository.updateRetards(System.currentTimeMillis());
    }

    private void enregistrerRetour(EmpruntDisplayItem emprunt) {

        executorService.execute(() -> {

            long now = System.currentTimeMillis();

            try {
                db.runInTransaction(() -> {

                    var empruntDb = db.empruntDao().getEmpruntByIdSync(emprunt.id);

                    if (empruntDb == null) {
                        throw new IllegalStateException("Emprunt introuvable");
                    }

                    if (empruntDb.getDateRetourReelle() != 0L) {
                        throw new IllegalStateException("Retour déjà enregistré");
                    }

                    db.empruntDao().enregistrerRetour(
                            emprunt.id,
                            now,
                            LibraryConstants.STATUT_EMPRUNT_TERMINE
                    );

                    db.livreDao().incrementStock(empruntDb.getLivreId());
                });

                long retardJours =
                        DateUtils.getRetardJours(emprunt.dateRetourPrevue, now);

                runOnUiThread(() -> {
                    String message = (retardJours > 0)
                            ? "Retour enregistré avec " + retardJours + " jour(s) de retard"
                            : "Retour enregistré";

                    Toast.makeText(EmpruntActivity.this, message, Toast.LENGTH_SHORT).show();

                    // 🔥 refresh UI après retour
                    loadData();
                });

            } catch (Exception e) {

                runOnUiThread(() ->
                        Toast.makeText(
                                EmpruntActivity.this,
                                e.getMessage() != null ? e.getMessage() : "Erreur retour",
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }
        });
    }
}