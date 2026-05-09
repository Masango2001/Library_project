package com.example.bibliotheque.ui.livres;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.model.LivreCatalogueItem;

import java.util.ArrayList;
import java.util.List;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.ViewHolder> {

    private List<LivreCatalogueItem> list = new ArrayList<>();

    public interface OnLivreAction {
        void onDelete(LivreCatalogueItem livre);
        void onUpdate(LivreCatalogueItem livre);
        void onEmprunt(LivreCatalogueItem livre);
        void onRetour(LivreCatalogueItem livre);
    }

    private OnLivreAction listener;

    public void setListener(OnLivreAction listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LivreCatalogueItem livre = list.get(position);

        holder.txtTitre.setText(livre.titre);

        holder.txtIsbn.setText(
                "ISBN: " + livre.isbn +
                        " | Auteur: " + livre.auteurNomComplet +
                        " | Catégorie: " + livre.categorieNom
        );

        holder.txtStock.setText(
                "Stock: " + livre.quantiteDisponible + "/" + livre.quantiteTotale
        );

        holder.btnDelete.setOnClickListener(v ->
                safeCall(() -> listener.onDelete(livre))
        );

        holder.btnEdit.setOnClickListener(v ->
                safeCall(() -> listener.onUpdate(livre))
        );

        holder.btnEmprunt.setOnClickListener(v ->
                safeCall(() -> listener.onEmprunt(livre))
        );

        holder.btnRetour.setOnClickListener(v ->
                safeCall(() -> listener.onRetour(livre))
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<LivreCatalogueItem> list) {
        this.list = (list != null) ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    // 🔥 sécurité centralisée
    private void safeCall(Runnable action) {
        if (listener != null) {
            action.run();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitre;
        TextView txtIsbn;
        TextView txtStock;

        ImageButton btnDelete;
        ImageButton btnEdit;
        ImageButton btnEmprunt;
        ImageButton btnRetour;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitre = itemView.findViewById(R.id.txtTitre);
            txtIsbn = itemView.findViewById(R.id.txtIsbn);
            txtStock = itemView.findViewById(R.id.txtStock);

            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEmprunt = itemView.findViewById(R.id.btnEmprunt);
            btnRetour = itemView.findViewById(R.id.btnRetour);
        }
    }
}