package com.example.bibliotheque.ui.membres;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.repository.MembreRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class MembreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MembreAdapter adapter;
    private MembreRepository repository;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membre);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fabAdd);

        adapter = new MembreAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        repository = new MembreRepository(getApplication());

        repository.getAllMembres().observe(this, membres -> {
            adapter.submitList(membres);
        });

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMembreActivity.class);
            startActivity(intent);
        });
    }
}