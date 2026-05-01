package com.example.bibliotheque.ui.emprunts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.repository.EmpruntRepository;

import java.util.List;

public class EmpruntActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;

    EmpruntAdapter adapter;
    EmpruntRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprunt);

        recyclerView = findViewById(R.id.recyclerEmprunts);
        btnAdd = findViewById(R.id.btnAddEmprunt);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmpruntAdapter();
        recyclerView.setAdapter(adapter);

        repository = new EmpruntRepository(getApplication());

        loadData();

        // mise à jour automatique des retards
        repository.updateRetards(System.currentTimeMillis());

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEmpruntActivity.class)));
    }

    private void loadData() {
        repository.getAll().observe(this, new Observer<List<Emprunt>>() {
            @Override
            public void onChanged(List<Emprunt> emprunts) {
                adapter.setList(emprunts);
            }
        });
    }
}