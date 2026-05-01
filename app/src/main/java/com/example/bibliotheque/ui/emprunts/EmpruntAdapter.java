package com.example.bibliotheque.ui.emprunts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bibliotheque.R;
import com.example.bibliotheque.entities.Emprunt;

import java.util.ArrayList;
import java.util.List;

public class EmpruntAdapter extends RecyclerView.Adapter<EmpruntAdapter.ViewHolder> {

    private List<Emprunt> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emprunt, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Emprunt e = list.get(position);

        holder.txtStatut.setText(e.getStatut());

        holder.txtInfo.setText(
                "Membre: " + e.getMembreId() +
                        " | Livre: " + e.getLivreId()
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<Emprunt> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtStatut, txtInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatut = itemView.findViewById(R.id.txtStatut);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}