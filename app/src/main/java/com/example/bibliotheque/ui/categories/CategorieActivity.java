package com.example.bibliotheque.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.repository.CategorieRepository;

import java.util.List;

public class CategorieActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button btnAdd;

    CategorieAdapter adapter;
    CategorieRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorie);

        recyclerView = findViewById(R.id.recyclerCategories);
        btnAdd = findViewById(R.id.btnAddCategorie);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategorieAdapter();
        recyclerView.setAdapter(adapter);

        repository = new CategorieRepository(getApplication());

        loadCategories();

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddCategorieActivity.class)));
    }

    private void loadCategories() {
        repository.getAllCategories().observe(this, new Observer<List<Categorie>>() {
            @Override
            public void onChanged(List<Categorie> categories) {
                adapter.setList(categories);
            }
        });
    }
}