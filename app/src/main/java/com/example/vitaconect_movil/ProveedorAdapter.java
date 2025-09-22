package com.example.vitaconect_movil;

import android.content.Intent;
import android.util.Log;
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

public class ProveedorAdapter extends RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder> {

    private List<Proveedor> proveedorList;

    public ProveedorAdapter(List<Proveedor> proveedorList) {
        this.proveedorList = proveedorList;
    }

    @NonNull
    @Override
    public ProveedorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_proveedor, parent, false);
        return new ProveedorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProveedorViewHolder holder, int position) {
        Proveedor proveedor = proveedorList.get(position);
        holder.idTextView.setText(String.valueOf(proveedor.getId()));
        holder.nombreTextView.setText(proveedor.getNombre());
        holder.telefonoTextView.setText(proveedor.getTelefono());
        holder.correoTextView.setText(proveedor.getCorreo());
        holder.direccionTextView.setText(proveedor.getDireccion());


        // Configura el botón Modificar
        holder.btnModificar.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), modificar_gestionproveedores.class);
            // Pasa los datos del proveedor en el Intent
            intent.putExtra("proveedorId", proveedor.getId()); // Usa "proveedorId" como clave para el ID
            intent.putExtra("nombre", proveedor.getNombre());
            intent.putExtra("telefono", proveedor.getTelefono());
            intent.putExtra("correo", proveedor.getCorreo());
            intent.putExtra("direccion", proveedor.getDireccion());
            holder.itemView.getContext().startActivity(intent);
        });

        // Configurar el botón Eliminar
        holder.btnEliminar.setOnClickListener(v -> eliminarProveedor(holder, proveedor.getId()));
    }

    @Override
    public int getItemCount() {
        return proveedorList.size();
    }

    private void eliminarProveedor(ProveedorViewHolder holder, int proveedorId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://vcbd.accwebdeveloper.com.mx/api/proveedor/" + proveedorId;

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Content-Type", "application/json") // ← Agrega esto
                .addHeader("Accept", "application/json")       // ← Agrega esto
                // .addHeader("Authorization", "Bearer " + obtenerToken()) // Si necesita auth
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                holder.itemView.post(() ->
                        Toast.makeText(holder.itemView.getContext(),
                                "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("DELETE_RESPONSE", "Código: " + response.code() + " - Respuesta: " + responseBody);

                if (response.isSuccessful()) {
                    holder.itemView.post(() -> {
                        proveedorList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        Toast.makeText(holder.itemView.getContext(),
                                "Proveedor eliminado", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    holder.itemView.post(() ->
                            Toast.makeText(holder.itemView.getContext(),
                                    "Error: " + response.code() + " - " + responseBody, Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    public static class ProveedorViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nombreTextView, telefonoTextView, correoTextView, direccionTextView;
        Button btnEliminar, btnModificar;

        public ProveedorViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            telefonoTextView = itemView.findViewById(R.id.telefonoTextView);
            correoTextView = itemView.findViewById(R.id.correoTextView);
            direccionTextView = itemView.findViewById(R.id.direccionTextView);
            btnEliminar = itemView.findViewById(R.id.btnEliminar); // Botón para eliminar
            btnModificar = itemView.findViewById(R.id.btnModificar); // Botón para modificar
        }
    }
}
