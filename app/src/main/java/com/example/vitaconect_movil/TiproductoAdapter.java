package com.example.vitaconect_movil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TiproductoAdapter extends RecyclerView.Adapter<TiproductoAdapter.ViewHolder> {

    private List<Tiproducto> tipoProductoList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Tiproducto tipoProducto);
        void onDeleteClick(Tiproducto tipoProducto);
    }

    public TiproductoAdapter(List<Tiproducto> tipoProductoList, OnItemClickListener listener) {
        this.tipoProductoList = tipoProductoList;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreProducto;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            // Configura el listener para el botón Editar
            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Tiproducto tipoProducto = tipoProductoList.get(position);
                    listener.onEditClick(tipoProducto);
                }
            });

            // Configura el listener para el botón Eliminar
            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Tiproducto tipoProducto = tipoProductoList.get(position);
                    listener.onDeleteClick(tipoProducto);
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tiproducto, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tiproducto tipoProducto = tipoProductoList.get(position);
        holder.nombreProducto.setText(tipoProducto.getNombre());
    }

    @Override
    public int getItemCount() {
        return tipoProductoList.size();
    }
}
