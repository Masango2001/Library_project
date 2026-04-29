package com.example.bibliotheque.ui.membres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Membre;

public class MembreAdapter extends ListAdapter<Membre, MembreAdapter.ViewHolder> {
    public MembreAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Membre> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Membre>() {
                @Override
                public boolean areItemsTheSame(@NonNull Membre oldItem, @NonNull Membre newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Membre oldItem, @NonNull Membre newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membre membre = getItem(position);
        holder.txtNom.setText(membre.getNomComplet());
        holder.txtEmail.setText(membre.getEmail());
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNom, txtEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNom);
            txtEmail = itemView.findViewById(R.id.txtEmail);
        }
    }
}