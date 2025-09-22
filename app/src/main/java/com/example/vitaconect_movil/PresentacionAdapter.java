package com.example.vitaconect_movil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PresentacionAdapter extends RecyclerView.Adapter<PresentacionAdapter.ViewHolder> {

    private List<Presentacion> presentaciones;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Presentacion presentacion, int position);
        void onDeleteClick(Presentacion presentacion, int position);
    }

    public PresentacionAdapter(List<Presentacion> presentaciones, Context context, OnItemClickListener listener) {
        this.presentaciones = presentaciones;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_presentacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Presentacion presentacion = presentaciones.get(position);
        holder.nombreTextView.setText(presentacion.getNombre());

        holder.editButton.setOnClickListener(v -> listener.onEditClick(presentacion, position));

        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(presentacion, position));
    }

    @Override
    public int getItemCount() {
        return presentaciones.size();
    }

    // Método para eliminar item visualmente
    public void removeItem(int position) {
        presentaciones.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        Button deleteButton;
        Button editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }
}