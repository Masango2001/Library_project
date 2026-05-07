package com.example.bibliotheque.ui.membres;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.MembreRepository;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MembreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAdd;
    private MembreAdapter adapter;
    private MembreRepository repository;
    private AppDatabase db;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre);

        recyclerView = findViewById(R.id.recyclerMembres);
        btnAdd = findViewById(R.id.btnAddMembre);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MembreAdapter();
        recyclerView.setAdapter(adapter);

        repository = new MembreRepository(getApplication());
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

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
                    String nouveauStatut = LibraryConstants.STATUT_MEMBRE_ACTIF.equals(membre.getStatut())
                            ? LibraryConstants.STATUT_MEMBRE_SUSPENDU
                            : LibraryConstants.STATUT_MEMBRE_ACTIF;

                    db.membreDao().updateStatut(membre.getId(), nouveauStatut);
                    runOnUiThread(() -> Toast.makeText(
                            MembreActivity.this,
                            "Statut mis a jour",
                            Toast.LENGTH_SHORT
                    ).show());
                });
            }
        });

        loadMembres();
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddMembreActivity.class)));
    }

    private void loadMembres() {
        repository.getAllMembres().observe(this, adapter::setList);
    }
}
