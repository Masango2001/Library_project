package com.example.bibliotheque.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bibliotheque.R;
import com.example.bibliotheque.data.AppDatabase;
import com.example.bibliotheque.model.TopLivreStat;
import com.example.bibliotheque.model.TopMembreStat;
import com.example.bibliotheque.repository.EmpruntRepository;
import com.example.bibliotheque.ui.auteurs.AuteurActivity;
import com.example.bibliotheque.ui.categories.CategorieActivity;
import com.example.bibliotheque.ui.emprunts.EmpruntActivity;
import com.example.bibliotheque.ui.livres.AddLivreActivity;
import com.example.bibliotheque.ui.livres.LivreActivity;
import com.example.bibliotheque.ui.membres.MembreActivity;
import com.example.bibliotheque.util.LibraryConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private AppDatabase db;
    private EmpruntRepository empruntRepository;
    private TextView txtTopLivres;
    private TextView txtTopMembres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = AppDatabase.getInstance(this);
        empruntRepository = new EmpruntRepository(getApplication());

        initViews();
        setupDrawer();
        setupDashboard();
        setupActions();
        setupBackPressed();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        ensureStatsSection();
    }

    private void setupDrawer() {
        NavigationView navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else if (id == R.id.nav_livres) {
                startActivity(new Intent(this, LivreActivity.class));
            } else if (id == R.id.nav_auteurs) {
                startActivity(new Intent(this, AuteurActivity.class));
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(this, CategorieActivity.class));
            } else if (id == R.id.nav_membres) {
                startActivity(new Intent(this, MembreActivity.class));
            } else if (id == R.id.nav_emprunts) {
                startActivity(new Intent(this, EmpruntActivity.class));
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setupDashboard() {
        toolbar.setTitle("Tableau de bord");
        empruntRepository.updateRetards(System.currentTimeMillis());

        TextView txtLivres = findViewById(R.id.txtCountLivres);
        TextView txtMembres = findViewById(R.id.txtCountMembres);
        TextView txtEmprunts = findViewById(R.id.txtCountEmprunts);
        TextView txtRetards = findViewById(R.id.txtCountRetards);

        db.livreDao().countLivres().observe(this, value ->
                txtLivres.setText(String.valueOf(value != null ? value : 0)));

        db.membreDao().countAll().observe(this, value ->
                txtMembres.setText(String.valueOf(value != null ? value : 0)));

        db.empruntDao().getTotalEmprunts().observe(this, value ->
                txtEmprunts.setText(String.valueOf(value != null ? value : 0)));

        db.empruntDao().getEmpruntsEnRetard().observe(this, value ->
                txtRetards.setText(String.valueOf(value != null ? value : 0)));

        empruntRepository.getTopLivres(LibraryConstants.DASHBOARD_TOP_LIMIT)
                .observe(this, stats -> txtTopLivres.setText(formatTopLivres(stats)));

        empruntRepository.getTopMembres(LibraryConstants.DASHBOARD_TOP_LIMIT)
                .observe(this, stats -> txtTopMembres.setText(formatTopMembres(stats)));
    }

    private void setupActions() {
        Button btnManageLivre = findViewById(R.id.btnManageLivre);
        Button btnManageMembres = findViewById(R.id.btnManageMembres);
        Button btnManageCategories = findViewById(R.id.btnManageCategories);
        Button btnManageAuteurs = findViewById(R.id.btnManageAuteurs);
        Button btnManageEmprunts = findViewById(R.id.btnManageEmprunts);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        btnManageLivre.setOnClickListener(v -> startActivity(new Intent(this, LivreActivity.class)));
        btnManageMembres.setOnClickListener(v -> startActivity(new Intent(this, MembreActivity.class)));
        btnManageCategories.setOnClickListener(v -> startActivity(new Intent(this, CategorieActivity.class)));
        btnManageAuteurs.setOnClickListener(v -> startActivity(new Intent(this, AuteurActivity.class)));
        btnManageEmprunts.setOnClickListener(v -> startActivity(new Intent(this, EmpruntActivity.class)));
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AddLivreActivity.class)));
    }

    private void ensureStatsSection() {
        LinearLayout parent = (LinearLayout) findViewById(R.id.btnManageEmprunts).getParent();

        TextView titreLivres = new TextView(this);
        titreLivres.setText("Livres les plus empruntes");
        titreLivres.setTextSize(18f);
        titreLivres.setPadding(0, 32, 0, 8);
        parent.addView(titreLivres);

        txtTopLivres = new TextView(this);
        txtTopLivres.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        parent.addView(txtTopLivres);

        TextView titreMembres = new TextView(this);
        titreMembres.setText("Membres les plus actifs");
        titreMembres.setTextSize(18f);
        titreMembres.setPadding(0, 24, 0, 8);
        parent.addView(titreMembres);

        txtTopMembres = new TextView(this);
        txtTopMembres.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        parent.addView(txtTopMembres);
    }

    private String formatTopLivres(List<TopLivreStat> stats) {
        if (stats == null || stats.isEmpty()) {
            return "Aucun emprunt enregistre";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < stats.size(); index++) {
            TopLivreStat stat = stats.get(index);
            builder.append(index + 1)
                    .append(". ")
                    .append(stat.livreTitre)
                    .append(" - ")
                    .append(stat.totalEmprunts)
                    .append(" emprunt(s)");
            if (index < stats.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private String formatTopMembres(List<TopMembreStat> stats) {
        if (stats == null || stats.isEmpty()) {
            return "Aucun membre actif pour le moment";
        }

        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < stats.size(); index++) {
            TopMembreStat stat = stats.get(index);
            builder.append(index + 1)
                    .append(". ")
                    .append(stat.membreNomComplet)
                    .append(" - ")
                    .append(stat.totalEmprunts)
                    .append(" emprunt(s)");
            if (index < stats.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    private void setupBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
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
