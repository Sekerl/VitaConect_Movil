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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;
    private OnProductoClickListener listener;
    private OkHttpClient client;
    private Context context;

    public interface OnProductoClickListener {
        void onEditClick(Producto producto);
        void onDeleteClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productoList, OnProductoClickListener listener) {
        this.productoList = productoList;
        this.listener = listener;
        this.client = new OkHttpClient();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        context = parent.getContext();
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView idTextView, nombreTextView, concentracionTextView, adicionalTextView, precioTextView, laboratorioTextView, tipoTextView, presentacionTextView, stockTextView;
        private Button btnEdit, btnDelete, btnAgregarLote;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            concentracionTextView = itemView.findViewById(R.id.concentracionTextView);
            adicionalTextView = itemView.findViewById(R.id.adicionalTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            laboratorioTextView = itemView.findViewById(R.id.laboratorioTextView);
            tipoTextView = itemView.findViewById(R.id.tipoTextView);
            presentacionTextView = itemView.findViewById(R.id.presentacionTextView);
            stockTextView = itemView.findViewById(R.id.stockTextView); // Añadir referencia al TextView de stock total
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnAgregarLote = itemView.findViewById(R.id.btnAgregarLote);
        }

        public void bind(Producto producto, OnProductoClickListener listener) {
            idTextView.setText(producto.getId());
            nombreTextView.setText(producto.getNombre());
            concentracionTextView.setText(producto.getConcentracion());
            adicionalTextView.setText(producto.getAdicional());
            precioTextView.setText("$" + producto.getPrecio());
            laboratorioTextView.setText(producto.getLaboratorio());
            tipoTextView.setText(producto.getTipo());
            presentacionTextView.setText(producto.getPresentacion());
            stockTextView.setText(String.valueOf(producto.getStockTotal())); // Mostrar el stock total

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, modificar_gestionproductos.class);
                intent.putExtra("id", producto.getId()); // Pasar el ID del producto a la actividad
                context.startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> deleteProducto(producto, getAdapterPosition()));

            btnAgregarLote.setOnClickListener(v -> {
                Intent intent = new Intent(context, agregar_gestionlotes.class);
                intent.putExtra("idProducto", producto.getId());
                intent.putExtra("nombreProducto", producto.getNombre());
                context.startActivity(intent);
            });
        }

        private void deleteProducto(Producto producto, int position) {
            String url = "https://vcbd.accwebdeveloper.com.mx/api/producto/" + producto.getId();
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ((Activity) context).runOnUiThread(() -> {
                            productoList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        ((Activity) context).runOnUiThread(() ->
                                Toast.makeText(context, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });
        }
    }
}
/*package com.example.vitaconect_movil;

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

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto> productoList;
    private OnProductoClickListener listener;
    private OkHttpClient client;
    private Context context;

    public interface OnProductoClickListener {
        void onEditClick(Producto producto);
        void onDeleteClick(Producto producto);
    }

    public ProductoAdapter(List<Producto> productoList, OnProductoClickListener listener) {
        this.productoList = productoList;
        this.listener = listener;
        this.client = new OkHttpClient();
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        context = parent.getContext();
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.bind(producto, listener);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        private TextView idTextView, nombreTextView, concentracionTextView, adicionalTextView, precioTextView, laboratorioTextView, tipoTextView, presentacionTextView;
        private Button btnEdit, btnDelete, btnAgregarLote;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            concentracionTextView = itemView.findViewById(R.id.concentracionTextView);
            adicionalTextView = itemView.findViewById(R.id.adicionalTextView);
            precioTextView = itemView.findViewById(R.id.precioTextView);
            laboratorioTextView = itemView.findViewById(R.id.laboratorioTextView);
            tipoTextView = itemView.findViewById(R.id.tipoTextView);
            presentacionTextView = itemView.findViewById(R.id.presentacionTextView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnAgregarLote = itemView.findViewById(R.id.btnAgregarLote);
        }

        public void bind(Producto producto, OnProductoClickListener listener) {
            idTextView.setText(producto.getId());
            nombreTextView.setText(producto.getNombre());
            concentracionTextView.setText(producto.getConcentracion());
            adicionalTextView.setText(producto.getAdicional());
            precioTextView.setText("$" + producto.getPrecio());
            laboratorioTextView.setText(producto.getLaboratorio());
            tipoTextView.setText(producto.getTipo());
            presentacionTextView.setText(producto.getPresentacion());

            btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, modificar_gestionproductos.class);
                intent.putExtra("id", producto.getId()); // Pasar el ID del producto a la actividad
                context.startActivity(intent);
            });

            btnDelete.setOnClickListener(v -> deleteProducto(producto, getAdapterPosition()));

            btnAgregarLote.setOnClickListener(v -> {
                Intent intent = new Intent(context, agregar_gestionlotes.class);
                intent.putExtra("idProducto", producto.getId());
                intent.putExtra("nombreProducto", producto.getNombre());
                context.startActivity(intent);
            });
        }

        private void deleteProducto(Producto producto, int position) {
            String url = "http://192.168.0.5:80/api/producto/" + producto.getId();
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    ((Activity) context).runOnUiThread(() ->
                            Toast.makeText(context, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        ((Activity) context).runOnUiThread(() -> {
                            productoList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        ((Activity) context).runOnUiThread(() ->
                                Toast.makeText(context, "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            });
        }
    }
}*/