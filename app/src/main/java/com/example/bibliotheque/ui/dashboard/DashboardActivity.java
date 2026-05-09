package com.example.bibliotheque.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.model.TopLivreStat;
import com.example.bibliotheque.model.TopMembreStat;
import com.example.bibliotheque.repository.AuteurRepository;
import com.example.bibliotheque.repository.CategorieRepository;
import com.example.bibliotheque.repository.EmpruntRepository;
import com.example.bibliotheque.repository.LivreRepository;
import com.example.bibliotheque.repository.MembreRepository;

import android.view.MenuItem;

import androidx.lifecycle.LiveData;

import com.example.bibliotheque.ui.auteurs.AuteurActivity;
import com.example.bibliotheque.ui.categories.CategorieActivity;
import com.example.bibliotheque.ui.emprunts.EmpruntActivity;
import com.example.bibliotheque.ui.livres.LivreActivity;
import com.example.bibliotheque.ui.membres.MembreActivity;
import com.example.bibliotheque.util.LibraryConstants;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;

    private AppDatabase db;


    private LivreRepository livreRepository;
    private AuteurRepository auteurRepository;
    private CategorieRepository categorieRepository;
    private MembreRepository membreRepository;
    private EmpruntRepository empruntRepository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = AppDatabase.getInstance(this);

        livreRepository = new LivreRepository(getApplication());
        auteurRepository = new AuteurRepository(getApplication());
        categorieRepository = new CategorieRepository(getApplication());
        membreRepository = new MembreRepository(getApplication());
        empruntRepository = new EmpruntRepository(getApplication());



        initViews();
        setupDrawer();
        setupDashboard();
        setupBackPressed();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    private void setupDrawer() {
        NavigationView navigationView = findViewById(R.id.navView);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setCheckedItem(R.id.nav_dashboard);

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();
            handleNavigation(id);

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle != null && toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toggle != null) {
            toggle.syncState();
        }
    }

    private void handleNavigation(int id) {

        Intent intent = null;

        if (id == R.id.nav_dashboard) {
            return;

        } else if (id == R.id.nav_livres) {
            intent = new Intent(this, LivreActivity.class);

        } else if (id == R.id.nav_membres) {
            intent = new Intent(this, MembreActivity.class);

        } else if (id == R.id.nav_emprunts) {
            intent = new Intent(this, EmpruntActivity.class);

        } else if (id == R.id.nav_auteurs) {
            intent = new Intent(this, AuteurActivity.class);

        } else if (id == R.id.nav_categories) {
            intent = new Intent(this, CategorieActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    private void setupDashboard() {

        toolbar.setTitle("Tableau de bord");

        empruntRepository.updateRetards(System.currentTimeMillis());

        empruntRepository.getTopLivres(LibraryConstants.DASHBOARD_TOP_LIMIT)
                .observe(this,
                        stats -> setText(R.id.txtTopLivres, formatTopLivres(stats)));

        empruntRepository.getTopMembres(LibraryConstants.DASHBOARD_TOP_LIMIT)
                .observe(this,
                        stats -> setText(R.id.txtTopMembres, formatTopMembres(stats)));
    }

    private void setText(int id, Object value) {
        if (findViewById(id) instanceof android.widget.TextView) {
            ((android.widget.TextView) findViewById(id))
                    .setText(String.valueOf(value != null ? value : 0));
        }
    }

    private String formatTopLivres(List<TopLivreStat> stats) {
        if (stats == null || stats.isEmpty())
            return "Aucun emprunt enregistré";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < stats.size(); i++) {
            TopLivreStat s = stats.get(i);
            builder.append(i + 1)
                    .append(". ")
                    .append(s.livreTitre)
                    .append(" - ")
                    .append(s.totalEmprunts)
                    .append(" emprunt(s)\n");
        }

        return builder.toString().trim();
    }

    private String formatTopMembres(List<TopMembreStat> stats) {
        if (stats == null || stats.isEmpty())
            return "Aucun membre actif";

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < stats.size(); i++) {
            TopMembreStat s = stats.get(i);
            builder.append(i + 1)
                    .append(". ")
                    .append(s.membreNomComplet)
                    .append(" - ")
                    .append(s.totalEmprunts)
                    .append(" emprunt(s)\n");
        }

        return builder.toString().trim();
    }

    private void setupBackPressed() {
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {

                        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            finish();
                        }
                    }
                });
    }
}