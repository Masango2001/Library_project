package com.example.bibliotheque.ui.membres;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.repository.MembreRepository;

import java.util.List;

public class MembreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;

    MembreAdapter adapter;
    MembreRepository repository;

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

        loadMembres();

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddMembreActivity.class)));
    }

    private void loadMembres() {
        repository.getAllMembres().observe(this, new Observer<List<Membre>>() {
            @Override
            public void onChanged(List<Membre> membres) {
                adapter.setList(membres);
            }
        });
    }
}