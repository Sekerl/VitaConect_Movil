package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class agregar_gestionproductos extends AppCompatActivity {

    private EditText txtNombreProducto, txtConcentracion, txtAdicional, txtPrecio;
    private Spinner spinnerLaboratorio, spinnerTipoProducto, spinnerPresentacion;
    private OkHttpClient client;
    private Map<String, Integer> laboratorioMap, tipoProductoMap, presentacionMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestionproductos);

        client = new OkHttpClient();

        // Inicializar campos de entrada
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtConcentracion = findViewById(R.id.txtConcentracion);
        txtAdicional = findViewById(R.id.txtAdicional);
        txtPrecio = findViewById(R.id.txtPrecio);

        // Inicializar los Spinners y mapas para IDs
        spinnerLaboratorio = findViewById(R.id.spinnerLaboratorio);
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        spinnerPresentacion = findViewById(R.id.spinnerPresentacion);
        laboratorioMap = new HashMap<>();
        tipoProductoMap = new HashMap<>();
        presentacionMap = new HashMap<>();

        // Cargar datos en los Spinners
        cargarDatosSpinner("https://vcbd.accwebdeveloper.com.mx/api/laboratorio", spinnerLaboratorio, laboratorioMap, "nombre");
        cargarDatosSpinner("https://vcbd.accwebdeveloper.com.mx/api/tiproducto", spinnerTipoProducto, tipoProductoMap, "nombre");
        cargarDatosSpinner("https://vcbd.accwebdeveloper.com.mx/api/presentacion", spinnerPresentacion, presentacionMap, "nombre");

        // Configurar el botón para agregar el producto
        MaterialButton buttonAgregar = findViewById(R.id.buttonagregar);
        buttonAgregar.setOnClickListener(v -> {
            String nombre = txtNombreProducto.getText().toString();
            String concentracion = txtConcentracion.getText().toString();
            String adicional = txtAdicional.getText().toString();
            String precio = txtPrecio.getText().toString();
            String laboratorio = spinnerLaboratorio.getSelectedItem().toString();
            String tipoProducto = spinnerTipoProducto.getSelectedItem().toString();
            String presentacion = spinnerPresentacion.getSelectedItem().toString();

            // Obtener IDs de los mapas
            int laboratorioId = laboratorioMap.get(laboratorio);
            int tipoProductoId = tipoProductoMap.get(tipoProducto);
            int presentacionId = presentacionMap.get(presentacion);

            // Enviar producto a la API
            enviarProducto(nombre, concentracion, adicional, precio, laboratorioId, tipoProductoId, presentacionId);
        });

        // Configurar el botón para redirigir al menú principal
        MaterialButton buttonMenu = findViewById(R.id.buttonmenu);
        buttonMenu.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestionproductos.this, MenuPrincipal.class);
            startActivity(intent);
        });
    }

    private void cargarDatosSpinner(String url, Spinner spinner, Map<String, Integer> map, String key) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionproductos.this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ArrayList<String> items = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String nombre = jsonObject.getString(key);
                            int id = jsonObject.getInt("id");
                            items.add(nombre);
                            map.put(nombre, id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(() -> {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(agregar_gestionproductos.this, android.R.layout.simple_spinner_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    });
                }
            }
        });
    }

    private void enviarProducto(String nombre, String concentracion, String adicional, String precio, int laboratorioId, int tipoProductoId, int presentacionId) {
        RequestBody formBody = new FormBody.Builder()
                .add("nombre", nombre)
                .add("concentracion", concentracion)
                .add("adicional", adicional)
                .add("precio", precio)
                .add("prod_lab", String.valueOf(laboratorioId))
                .add("prod_tip_prod", String.valueOf(tipoProductoId))
                .add("prod_present", String.valueOf(presentacionId))
                .build();

        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto";

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionproductos.this, "Error al agregar producto", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(agregar_gestionproductos.this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                        // Limpiar los campos de entrada
                        txtNombreProducto.setText("");
                        txtConcentracion.setText("");
                        txtAdicional.setText("");
                        txtPrecio.setText("");
                        spinnerLaboratorio.setSelection(0);
                        spinnerTipoProducto.setSelection(0);
                        spinnerPresentacion.setSelection(0);
                    });
                        finish(); // Finalizar la actividad para regresar al

                } else {
                    runOnUiThread(() -> Toast.makeText(agregar_gestionproductos.this, "No se pudo agregar el producto", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}