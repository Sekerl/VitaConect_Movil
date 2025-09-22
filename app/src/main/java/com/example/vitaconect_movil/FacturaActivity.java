package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FacturaActivity extends AppCompatActivity {

    private TextView txtIdVenta, txtCantidad, txtTotal, txtFecha, txtProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura);

        // Inicializa los TextViews
        txtIdVenta = findViewById(R.id.idVenta);
        txtCantidad = findViewById(R.id.cantidad);
        txtTotal = findViewById(R.id.total);
        txtFecha = findViewById(R.id.fecha);
        txtProductos = findViewById(R.id.productos); // Agrega un TextView para mostrar los productos

        // Obtiene el Intent que inició esta actividad
        Intent intent = getIntent();
        String idVenta = intent.getStringExtra("idVenta");
        String cantidad = intent.getStringExtra("cantidad");
        String total = intent.getStringExtra("total");
        String fecha = intent.getStringExtra("fecha");

        // Asigna los datos a los TextViews
        txtIdVenta.setText("ID Venta: " + idVenta);
        txtCantidad.setText("Cantidad: " + cantidad);
        txtTotal.setText("Total: $" + total);
        txtFecha.setText("Fecha: " + fecha);

        // Llama al método para obtener los productos asociados a la venta
        obtenerProductosPorVenta(idVenta);
    }

    private void obtenerProductosPorVenta(String idVenta) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://vcbd.accwebdeveloper.com.mx/api/ventasproductos/" + idVenta; // Reemplaza 'localhost' por la dirección de tu servidor

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(FacturaActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        StringBuilder productosBuilder = new StringBuilder();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject producto = jsonArray.getJSONObject(i);
                            String nombreProducto = producto.getJSONObject("producto").getString("nombre");
                            int cantidadProducto = producto.getInt("cantidad");
                            double subtotalProducto = producto.getDouble("subtotal");

                            // Agregar el nombre del producto, la cantidad y el subtotal al StringBuilder
                            productosBuilder.append("Producto: ").append(nombreProducto)
                                    .append(", Cantidad: ").append(cantidadProducto)
                                    .append(", Subtotal: $").append(subtotalProducto)
                                    .append("\n");
                        }

                        runOnUiThread(() -> txtProductos.setText(productosBuilder.toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(FacturaActivity.this, "Error al obtener productos", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
