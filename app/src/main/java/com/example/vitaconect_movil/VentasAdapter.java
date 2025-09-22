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

public class VentasAdapter extends RecyclerView.Adapter<VentasAdapter.VentaViewHolder> {

    private List<Venta> ventasList;
    private Context context;

    public VentasAdapter(List<Venta> ventasList, Context context) {
        this.ventasList = ventasList;
        this.context = context;
    }

    @NonNull
    @Override
    public VentaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venta, parent, false);
        return new VentaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VentaViewHolder holder, int position) {
        Venta venta = ventasList.get(position);
        holder.idVenta.setText("" + venta.getId());
        holder.cantidad.setText("" + venta.getCantidadn());
        holder.total.setText("$" + venta.getSubtotaln());
        holder.fecha.setText("" + venta.getCreated_at());

        // Configurar el OnClickListener para el botón Imprimir
        holder.btnDetalles.setOnClickListener(v -> {
            Intent intent = new Intent(context, FacturaActivity.class);
            // Pasar todos los datos necesarios
            intent.putExtra("idVenta", String.valueOf(venta.getId()));
            intent.putExtra("cantidad", String.valueOf(venta.getCantidadn()));
            intent.putExtra("total", String.valueOf(venta.getSubtotaln()));
            intent.putExtra("fecha", venta.getCreated_at());
            context.startActivity(intent); // Iniciar la actividad
        });
    }

    @Override
    public int getItemCount() {
        return ventasList.size();
    }

    public static class VentaViewHolder extends RecyclerView.ViewHolder {
        TextView idVenta, cantidad, total, fecha;
        Button btnDetalles;

        public VentaViewHolder(@NonNull View itemView) {
            super(itemView);
            idVenta = itemView.findViewById(R.id.idVenta);
            cantidad = itemView.findViewById(R.id.cantidad);
            total = itemView.findViewById(R.id.total);
            fecha = itemView.findViewById(R.id.fecha);
            btnDetalles = itemView.findViewById(R.id.btnDetalles);
        }
    }
}