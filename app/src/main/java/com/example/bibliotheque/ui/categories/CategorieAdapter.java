package com.example.bibliotheque.ui.categories;

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
import com.example.bibliotheque.entities.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategorieAdapter extends RecyclerView.Adapter<CategorieAdapter.ViewHolder> {

    public interface OnCategorieActionListener {
        void onEdit(Categorie categorie);
        void onDelete(Categorie categorie);
    }

    private List<Categorie> list = new ArrayList<>();
    private OnCategorieActionListener listener;

    public void setListener(OnCategorieActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categorie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categorie categorie = list.get(position);
        holder.txtNom.setText(categorie.getNom());
        holder.txtDescription.setText(categorie.getDescription());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(categorie);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(categorie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Categorie> list) {
        this.list = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtNom;
        private final TextView txtDescription;
        private final Button btnEdit;
        private final Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNom = itemView.findViewById(R.id.txtNomCategorie);
            txtDescription = itemView.findViewById(R.id.txtDescriptionCategorie);

            CardView cardView = (CardView) itemView;
            LinearLayout content = (LinearLayout) cardView.getChildAt(0);
            LinearLayout actions = new LinearLayout(itemView.getContext());
            actions.setOrientation(LinearLayout.HORIZONTAL);
            actions.setGravity(Gravity.END);

            btnEdit = new Button(itemView.getContext());
            btnEdit.setText("Modifier");
            btnDelete = new Button(itemView.getContext());
            btnDelete.setText("Supprimer");

            actions.addView(btnEdit);
            actions.addView(btnDelete);
            content.addView(actions);
        }
    }
}
