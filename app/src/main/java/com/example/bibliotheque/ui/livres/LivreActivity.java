package com.example.bibliotheque.ui.livres;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.repository.LivreRepository;
import com.example.bibliotheque.ui.emprunts.AddEmpruntActivity;

public class LivreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAdd;
    private LivreAdapter adapter;
    private LivreRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livre);

        recyclerView = findViewById(R.id.recyclerLivres);
        btnAdd = findViewById(R.id.btnAddLivre);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LivreAdapter();
        recyclerView.setAdapter(adapter);

        repository = new LivreRepository(getApplication());

        adapter.setListener(new LivreAdapter.OnLivreAction() {
            @Override
            public void onDelete(Livre livre) {
                repository.delete(livre);
                Toast.makeText(LivreActivity.this, "Livre supprimé", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdate(Livre livre) {
                // Implementation for update could go here
                Toast.makeText(LivreActivity.this, "Fonctionnalité de modification à venir", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmprunt(Livre livre) {
                if (livre.getQuantiteDisponible() > 0) {
                    Intent intent = new Intent(LivreActivity.this, AddEmpruntActivity.class);
                    intent.putExtra("LIVRE_ID", livre.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(LivreActivity.this, "Plus de stock disponible", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onRetour(Livre livre) {
                Toast.makeText(LivreActivity.this, "Utilisez l'onglet Emprunts pour les retours", Toast.LENGTH_SHORT).show();
            }
        });

        repository.getAllLivres().observe(this, livres -> adapter.setList(livres));

        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddLivreActivity.class)));
    }
}