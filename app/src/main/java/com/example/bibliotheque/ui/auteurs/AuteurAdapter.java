package com.example.bibliotheque.ui.auteurs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Auteur;

import java.util.ArrayList;
import java.util.List;

public class AuteurAdapter extends RecyclerView.Adapter<AuteurAdapter.ViewHolder> {

    private List<Auteur> list = new ArrayList<>();

    public interface OnItemClickListener {
        void onDelete(Auteur auteur);
        void onEdit(Auteur auteur);
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_auteur, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Auteur a = list.get(position);

        holder.txtNom.setText("Nom: " + a.getNom());
        holder.txtPrenom.setText("Prénom: " + a.getPrenom());
        holder.txtNationalite.setText("Nationalité: " + a.getNationalite());
        holder.txtDateNaissance.setText("Né le: " + a.getDateNaissance());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(a);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Auteur> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtPrenom, txtNationalite, txtDateNaissance;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtPrenom = itemView.findViewById(R.id.txtPrenom);
            txtNationalite = itemView.findViewById(R.id.txtNationalite);
            txtDateNaissance = itemView.findViewById(R.id.txtDateNaissance);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}