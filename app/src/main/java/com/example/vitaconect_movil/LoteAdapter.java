package com.example.vitaconect_movil;

import android.app.Activity;
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

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoteAdapter extends RecyclerView.Adapter<LoteAdapter.ViewHolder> {
    private List<Lote> loteList;
    private OkHttpClient client;
    private Context context;

    public LoteAdapter(List<Lote> lotes, OkHttpClient client) {
        this.loteList = lotes;
        this.client = client;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lote, parent, false);
        context = parent.getContext(); // Obtiene el contexto del adaptador
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lote lote = loteList.get(position);
        holder.bind(lote);
    }

    @Override
    public int getItemCount() {
        return loteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView loteNombre, loteStock, loteVencimiento, loteConcentracion, loteAdicional, loteLaboratorio, loteTipo, lotePresentacion, loteProveedor;
        private TextView estadoVencimiento, estadoStock;
        private Button deleteButton, editButton;

        ViewHolder(View itemView) {
            super(itemView);
            loteNombre = itemView.findViewById(R.id.loteNombre);
            loteStock = itemView.findViewById(R.id.loteStock);
            loteVencimiento = itemView.findViewById(R.id.loteVencimiento);
            loteConcentracion = itemView.findViewById(R.id.loteConcentracion);
            loteAdicional = itemView.findViewById(R.id.loteAdicional);
            loteLaboratorio = itemView.findViewById(R.id.loteLaboratorio);
            loteTipo = itemView.findViewById(R.id.LoteTipo);
            lotePresentacion = itemView.findViewById(R.id.lotePresentacion);
            loteProveedor = itemView.findViewById(R.id.loteProveedor);
            estadoVencimiento = itemView.findViewById(R.id.estadoVencimiento);
            estadoStock = itemView.findViewById(R.id.estadoStock);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        void bind(Lote lote) {
            loteNombre.setText(lote.getNombre());
            loteStock.setText(String.valueOf(lote.getStock()));
            loteVencimiento.setText(lote.getVencimiento());
            loteConcentracion.setText(lote.getConcentracion());
            loteAdicional.setText(lote.getAdicional());
            loteLaboratorio.setText(lote.getLaboratorio());
            loteTipo.setText(lote.getTipo());
            lotePresentacion.setText(lote.getPresentacion());
            loteProveedor.setText(lote.getProveedor());

            estadoVencimiento.setText(lote.getEstadoVencimiento());
            if (lote.getEstadoVencimiento().contains("Caducado")) {
                estadoVencimiento.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            } else if (lote.getEstadoVencimiento().contains("quedan") && Integer.parseInt(lote.getEstadoVencimiento().replaceAll("[^0-9]", "")) <= 90) {
                estadoVencimiento.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_orange_light));
            } else {
                estadoVencimiento.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_green_dark));
            }

            if (lote.getStock() == 0) {
                estadoStock.setText("--Sin Stock--");
                estadoStock.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            } else if (lote.getStock() <= 100) {
                estadoStock.setText("Stock Bajo");
                estadoStock.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_orange_light));
            } else {
                estadoStock.setText("Stock Estable");
                estadoStock.setBackgroundColor(itemView.getResources().getColor(android.R.color.holo_green_dark));
            }

            editButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, modificar_gestionlotes.class);
                intent.putExtra("loteId", lote.getId());
                context.startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> deleteLote(lote.getId(), getAdapterPosition()));
        }

        private void deleteLote(int loteId, int position) {
            String url = "https://vcbd.accwebdeveloper.com.mx/api/lote/" + loteId;
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        if (context instanceof Activity) {
                            ((Activity) context).runOnUiThread(() -> {
                                loteList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Lote eliminado correctamente", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Error al eliminar el lote", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        }
    }
}