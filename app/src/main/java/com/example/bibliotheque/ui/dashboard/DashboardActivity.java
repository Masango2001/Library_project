package com.example.bibliotheque.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.ui.auteurs.AuteurActivity;
import com.example.bibliotheque.ui.categories.CategorieActivity;
import com.example.bibliotheque.ui.emprunts.EmpruntActivity;
import com.example.bibliotheque.ui.livres.LivreActivity;
import com.example.bibliotheque.ui.membres.MembreActivity;

public class DashboardActivity extends AppCompatActivity {

    private TextView txtCountLivres;
    private TextView txtCountMembres;
    private TextView txtCountEmprunts;
    private TextView txtCountRetards;

    private Button btnManageLivres;
    private Button btnManageMembres;
    private Button btnManageCategories;
    private Button btnManageAuteurs;
    private Button btnManageEmprunts;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();

        db = AppDatabase.getInstance(getApplicationContext());

        setupNavigation();
        observeStatistics();
    }

    private void initViews() {
        // Statistiques
        txtCountLivres = findViewById(R.id.txtCountLivres);
        txtCountMembres = findViewById(R.id.txtCountMembres);
        txtCountEmprunts = findViewById(R.id.txtCountEmprunts);
        txtCountRetards = findViewById(R.id.txtCountRetards);

        // Boutons
        btnManageLivres = findViewById(R.id.btnManageLivre);
        btnManageMembres = findViewById(R.id.btnManageMembres);
        btnManageCategories = findViewById(R.id.btnManageCategories);
        btnManageAuteurs = findViewById(R.id.btnManageAuteurs);
        btnManageEmprunts = findViewById(R.id.btnManageEmprunts);
    }

    private void setupNavigation() {

        btnManageLivres.setOnClickListener(v ->
                startActivity(new Intent(this, LivreActivity.class)));

        btnManageMembres.setOnClickListener(v ->
                startActivity(new Intent(this, MembreActivity.class)));

        btnManageCategories.setOnClickListener(v ->
                startActivity(new Intent(this, CategorieActivity.class)));

        btnManageAuteurs.setOnClickListener(v ->
                startActivity(new Intent(this, AuteurActivity.class)));

        btnManageEmprunts.setOnClickListener(v ->
                startActivity(new Intent(this, EmpruntActivity.class)));
    }

    private void observeStatistics() {

        // Nombre de livres
        db.livreDao().countLivres().observe(this, count -> {
            if (count == null) count = 0;
            txtCountLivres.setText(String.valueOf(count));
        });

        // Nombre de membres
        db.membreDao().countAll().observe(this, count -> {
            if (count == null) count = 0;
            txtCountMembres.setText(String.valueOf(count));
        });

        // Nombre d'emprunts
        db.empruntDao().getTotalEmprunts().observe(this, count -> {
            if (count == null) count = 0;
            txtCountEmprunts.setText(String.valueOf(count));
        });

        // Nombre de retards
        db.empruntDao().getEmpruntsEnRetard().observe(this, count -> {
            if (count == null) count = 0;
            txtCountRetards.setText(String.valueOf(count));
        });
    }
}