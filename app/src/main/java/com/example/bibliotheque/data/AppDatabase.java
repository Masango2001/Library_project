package com.example.bibliotheque.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.bibliotheque.dao.*;
import com.example.bibliotheque.entities.*;

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
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}