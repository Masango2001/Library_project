package com.example.bibliotheque.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract AuteurDao auteurDao();
    public abstract CategorieDao categorieDao();
    public abstract LivreDao livreDao();
    public abstract MembreDao membreDao();
    public abstract EmpruntDao empruntDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "bibliotheque_db"
                            )
                            // 🔥 option utile en développement
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}