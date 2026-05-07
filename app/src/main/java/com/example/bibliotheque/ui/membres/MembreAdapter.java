package com.example.bibliotheque.ui.membres;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Membre;
import com.example.bibliotheque.util.LibraryConstants;

import java.util.ArrayList;
import java.util.List;

public class MembreAdapter extends RecyclerView.Adapter<MembreAdapter.ViewHolder> {

    public interface OnMembreActionListener {
        void onEdit(Membre membre);
        void onToggleStatut(Membre membre);
    }

    private List<Membre> list = new ArrayList<>();
    private OnMembreActionListener listener;

    public void setListener(OnMembreActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_membre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Membre membre = list.get(position);

        holder.txtNomPrenom.setText(membre.getNom() + " " + membre.getPrenom());
        holder.txtEmail.setText(membre.getEmail());
        holder.txtStatut.setText(membre.getStatut());
        holder.txtDate.setText("Inscrit le: " + membre.getDateInscription());
        holder.txtStatut.setBackgroundColor(
                LibraryConstants.STATUT_MEMBRE_ACTIF.equals(membre.getStatut())
                        ? Color.parseColor("#27AE60")
                        : Color.parseColor("#E74C3C")
        );

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(membre);
            }
        });

        holder.btnToggle.setText(
                LibraryConstants.STATUT_MEMBRE_ACTIF.equals(membre.getStatut())
                        ? "Suspendre"
                        : "Activer"
        );
        holder.btnToggle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToggleStatut(membre);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Membre> list) {
        this.list = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNomPrenom;
        private final TextView txtEmail;
        private final TextView txtStatut;
        private final TextView txtDate;
        private final Button btnEdit;
        private final Button btnToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomPrenom = itemView.findViewById(R.id.txtNomPrenom);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtStatut = itemView.findViewById(R.id.txtStatut);
            txtDate = itemView.findViewById(R.id.txtDate);

            CardView cardView = (CardView) itemView;
            LinearLayout content = (LinearLayout) cardView.getChildAt(0);
            LinearLayout actions = new LinearLayout(itemView.getContext());
            actions.setOrientation(LinearLayout.HORIZONTAL);
            actions.setGravity(Gravity.END);

            btnEdit = new Button(itemView.getContext());
            btnEdit.setText("Modifier");
            btnToggle = new Button(itemView.getContext());

            actions.addView(btnEdit);
            actions.addView(btnToggle);
            content.addView(actions);
        }
    }
}
