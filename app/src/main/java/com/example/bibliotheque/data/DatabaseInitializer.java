package com.example.bibliotheque.data;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseInitializer {

    private static boolean initialized = false;

    public static void initialize(Context context) {

        if (initialized) return;

        AppDatabase db = AppDatabase.getInstance(context);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            // 🔥 ici tu peux ajouter des données par défaut si besoin
            // exemple:
            // if (db.categorieDao().countCategories().getValue() == 0) { ... }

        });

        initialized = true;
    }
}