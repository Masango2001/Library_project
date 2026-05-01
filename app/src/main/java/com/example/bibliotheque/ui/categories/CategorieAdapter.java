package com.example.bibliotheque.ui.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.ViewHolder> {

    private List<Categorie> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categorie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorie c = list.get(position);
        holder.txtNom.setText(c.getNom());
        holder.txtDescription.setText(c.getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Categorie> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomCategorie);
            txtDescription = itemView.findViewById(R.id.txtDescriptionCategorie);
        }
    }
}
