package com.example.bibliotheque.ui.emprunts;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.model.EmpruntDisplayItem;
import com.example.bibliotheque.util.DateUtils;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.ArrayList;
import java.util.List;

public class EmpruntAdapter extends RecyclerView.Adapter<EmpruntAdapter.ViewHolder> {

    public interface OnEmpruntActionListener {
        void onReturn(EmpruntDisplayItem emprunt);
    }

    private List<EmpruntDisplayItem> list = new ArrayList<>();
    private OnEmpruntActionListener listener;

    public void setListener(OnEmpruntActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emprunt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EmpruntDisplayItem emprunt = list.get(position);

        long referenceDate = (emprunt.dateRetourReelle > 0)
                ? emprunt.dateRetourReelle
                : System.currentTimeMillis();

        long retardJours = DateUtils.getRetardJours(
                emprunt.dateRetourPrevue,
                referenceDate
        );

        holder.txtStatut.setText("Statut: " + emprunt.statut);
        holder.txtInfo.setText(
                "Membre: " + emprunt.membreNomComplet +
                        "\nLivre: " + emprunt.livreTitre
        );

        String details = "Emprunt: " + DateUtils.formatDate(emprunt.dateEmprunt) +
                "\nRetour prévu: " + DateUtils.formatDate(emprunt.dateRetourPrevue);

        if (emprunt.dateRetourReelle > 0) {
            details += "\nRetour réel: " + DateUtils.formatDate(emprunt.dateRetourReelle);
            if (retardJours > 0) {
                details += "\nRetard: " + retardJours + " jour(s)";
            }
        } else if (retardJours > 0) {
            details += "\nRetard actuel: " + retardJours + " jour(s)";
        }

        holder.txtDates.setText(details);

        holder.btnRetour.setVisibility(
                emprunt.dateRetourReelle == 0 ? View.VISIBLE : View.GONE
        );

        holder.btnRetour.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReturn(emprunt);
            }
        });

        if (LibraryConstants.STATUT_EMPRUNT_EN_RETARD.equalsIgnoreCase(emprunt.statut)) {
            holder.txtStatut.setTextColor(Color.parseColor("#E74C3C"));
        } else if (emprunt.dateRetourReelle > 0) {
            holder.txtStatut.setTextColor(Color.parseColor("#27AE60"));
        } else {
            holder.txtStatut.setTextColor(Color.parseColor("#F39C12"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<EmpruntDisplayItem> list) {
        this.list = (list != null) ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatut;
        TextView txtInfo;
        TextView txtDates;
        Button btnRetour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtStatut = itemView.findViewById(R.id.txtStatut);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtDates = itemView.findViewById(R.id.txtDates);
            btnRetour = itemView.findViewById(R.id.btnRetour);
        }
    }
}