package com.example.vitaconect_movil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.Product;
import com.example.vitaconect_movil.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList; // Lista de productos
    private Context context; // Contexto de la actividad

    // Constructor
    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_product.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Obtiene el producto actual
        Product product = productList.get(position);

        // Asigna los valores a los TextViews
        holder.tvNombreProducto.setText(product.getNombre());
        holder.tvCantidad.setText("Cantidad: " + product.getCantidad());
        holder.tvSubtotal.setText("Subtotal: $" + String.format("%.2f", product.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        // Devuelve el tamaño de la lista de productos
        return productList.size();
    }

    // Clase interna para el ViewHolder
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreProducto, tvCantidad, tvSubtotal;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa los TextViews
            tvNombreProducto = itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }
    }
}
