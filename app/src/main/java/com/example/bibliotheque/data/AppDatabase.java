package com.example.bibliotheque.data;

import android.content.Context;

import androidx.annotation.NonNull;
import android.database.Cursor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.bibliotheque.dao.AuteurDao;
import com.example.bibliotheque.dao.CategorieDao;
import com.example.bibliotheque.dao.EmpruntDao;
import com.example.bibliotheque.dao.LivreDao;
import com.example.bibliotheque.dao.MembreDao;
import com.example.bibliotheque.entities.Auteur;
import com.example.bibliotheque.entities.Categorie;
import com.example.bibliotheque.entities.Emprunt;
import com.example.bibliotheque.entities.Livre;
import com.example.bibliotheque.entities.Membre;

@Database(
        entities = {
                Auteur.class,
                Categorie.class,
                Livre.class,
                Membre.class,
                Emprunt.class
        },
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract AuteurDao auteurDao();
    public abstract CategorieDao categorieDao();
    public abstract LivreDao livreDao();
    public abstract MembreDao membreDao();
    public abstract EmpruntDao empruntDao();

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_membres_email ON membres(email)");
        }
    };

    private static final RoomDatabase.Callback DATABASE_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            populateInitialData(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            if (isDatabaseEmpty(db)) {
                populateInitialData(db);
            }
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "bibliotheque_db"
                            )
                            .addMigrations(MIGRATION_2_3)
                            .addCallback(DATABASE_CALLBACK)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static boolean isDatabaseEmpty(@NonNull SupportSQLiteDatabase db) {
        try (Cursor cursor = db.query("SELECT COUNT(*) FROM membres")) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) == 0;
            }
        }
        return true;
    }

    private static void populateInitialData(@NonNull SupportSQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON");

        long now = System.currentTimeMillis();
        long borrowDate1 = now - 7L * 24 * 60 * 60 * 1000;
        long dueDate1 = now + 7L * 24 * 60 * 60 * 1000;
        long borrowDate2 = now - 20L * 24 * 60 * 60 * 1000;
        long dueDate2 = now - 6L * 24 * 60 * 60 * 1000;

        db.execSQL("INSERT OR IGNORE INTO auteurs (id, nom, prenom, nationalite, date_naissance) VALUES " +
                "(1, 'Hugo', 'Victor', 'Française', '1802-02-26'), " +
                "(2, 'Rowling', 'J.K.', 'Britannique', '1965-07-31')");

        db.execSQL("INSERT OR IGNORE INTO categories (id, nom, description) VALUES " +
                "(1, 'Roman', 'Fiction littéraire'), " +
                "(2, 'Science-fiction', 'Livres de science-fiction')");

        db.execSQL("INSERT OR IGNORE INTO membres (id, nom, prenom, email, telephone, adresse, date_inscription, statut) VALUES " +
                "(1, 'Dubois', 'Alice', 'alice@example.com', '0612345678', 'Paris', '2025-01-10', 'ACTIF'), " +
                "(2, 'Martin', 'Bob', 'bob@example.com', '0698765432', 'Lyon', '2025-03-22', 'ACTIF')");

        db.execSQL("INSERT OR IGNORE INTO livres (id, titre, isbn, annee_publication, editeur, categorie_id, auteur_id, quantite_totale, quantite_disponible) VALUES " +
                "(1, 'Les Misérables', '9782070409180', 1862, 'Gallimard', 1, 1, 5, 4), " +
                "(2, 'Harry Potter à l''école des sorciers', '9782070643028', 1997, 'Gallimard', 2, 2, 3, 2)");

        db.execSQL("INSERT OR IGNORE INTO emprunts (id, membre_id, livre_id, date_emprunt, date_retour_prevue, date_retour_reelle, statut) VALUES " +
                "(1, 1, 1, " + borrowDate1 + ", " + dueDate1 + ", NULL, 'EN_COURS'), " +
                "(2, 2, 2, " + borrowDate2 + ", " + dueDate2 + ", NULL, 'EN_RETARD')");
    }
}
