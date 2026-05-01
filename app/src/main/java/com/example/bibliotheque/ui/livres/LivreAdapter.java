package com.example.bibliotheque.ui.livres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Livre;

import java.util.ArrayList;
import java.util.List;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.ViewHolder> {

    private List<Livre> list = new ArrayList<>();

    public interface OnLivreAction {
        void onDelete(Livre livre);
        void onUpdate(Livre livre);
        void onEmprunt(Livre livre);
        void onRetour(Livre livre);
    }

    private OnLivreAction listener;

    public void setListener(OnLivreAction listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {

        Livre l = list.get(i);

        h.txtTitre.setText(l.getTitre());
        h.txtIsbn.setText(l.getIsbn());

        // DELETE
        h.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(l);
        });

        // UPDATE
        h.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onUpdate(l);
        });

        // EMPRUNT
        h.btnEmprunt.setOnClickListener(v -> {
            if (listener != null) listener.onEmprunt(l);
        });

        // RETOUR
        h.btnRetour.setOnClickListener(v -> {
            if (listener != null) listener.onRetour(l);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Livre> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitre, txtIsbn;
        ImageButton btnDelete, btnEdit, btnEmprunt, btnRetour;

        public ViewHolder(@NonNull View v) {
            super(v);

            txtTitre = v.findViewById(R.id.txtTitre);
            txtIsbn = v.findViewById(R.id.txtIsbn);

            btnDelete = v.findViewById(R.id.btnDelete);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnEmprunt = v.findViewById(R.id.btnEmprunt);
            btnRetour = v.findViewById(R.id.btnRetour);
        }
    }
}