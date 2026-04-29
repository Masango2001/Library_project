package com.example.bibliotheque.data;

import android.content.Context;

public class DatabaseInitializer {

    public static void initialize(Context context) {
        AppDatabase.getInstance(context);
    }
}