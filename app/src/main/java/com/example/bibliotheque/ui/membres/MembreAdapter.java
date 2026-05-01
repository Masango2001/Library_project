package com.example.bibliotheque.ui.membres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Membre;

import java.util.ArrayList;
import java.util.List;

public class MembreAdapter extends RecyclerView.Adapter<MembreAdapter.ViewHolder> {

    List<Membre> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membre, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Membre m = list.get(position);

        holder.txtNom.setText(m.getNom());
        holder.txtEmail.setText(m.getEmail());
        holder.txtStatut.setText(m.getStatut());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Membre> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNom, txtEmail, txtStatut;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNom = itemView.findViewById(R.id.txtNom);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtStatut = itemView.findViewById(R.id.txtStatut);
        }
    }
}