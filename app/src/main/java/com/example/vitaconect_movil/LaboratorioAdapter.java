package com.example.vitaconect_movil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LaboratorioAdapter extends RecyclerView.Adapter<LaboratorioAdapter.ViewHolder> {
    private List<Laboratorio> laboratorioList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(Laboratorio laboratorio, int position);
        void onEditClick(Laboratorio laboratorio);
    }

    public LaboratorioAdapter(List<Laboratorio> laboratorioList, OnItemClickListener listener) {
        this.laboratorioList = laboratorioList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laboratorio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Laboratorio laboratorio = laboratorioList.get(position);
        holder.nameTextView.setText(laboratorio.getNombre());

        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(laboratorio, position));
        holder.editButton.setOnClickListener(v -> listener.onEditClick(laboratorio));
    }

    @Override
    public int getItemCount() {
        return laboratorioList.size();
    }

    public void updateData(List<Laboratorio> newLaboratorioList) {
        laboratorioList.clear();
        laboratorioList.addAll(newLaboratorioList);
        notifyDataSetChanged();
    }

    // Método para eliminar item visualmente
    public void removeItem(int position) {
        laboratorioList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button deleteButton, editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textNombreLaboratorio);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            editButton = itemView.findViewById(R.id.buttonEdit);
        }
    }
}