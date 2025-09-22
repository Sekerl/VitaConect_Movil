package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

public class modificar_gestionproductos extends AppCompatActivity {

    private EditText txtNombreProducto, txtConcentracion, txtAdicional, txtPrecio;
    private Spinner spinnerLaboratorio, spinnerTipoProducto, spinnerPresentacion;
    private String productoId;
    private List<String> laboratoriosList, tiposList, presentacionesList;
    private Map<String, Integer> laboratorioMap, tipoMap, presentacionMap;
    private OkHttpClient client;

    // Banderas para verificar si los datos de los spinners están listos
    private boolean laboratorioListo = false;
    private boolean tipoListo = false;
    private boolean presentacionListo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_gestionproductos);

        // Inicializar campos de entrada y spinners
        txtNombreProducto = findViewById(R.id.txtNombreProducto);
        txtConcentracion = findViewById(R.id.txtConcentracion);
        txtAdicional = findViewById(R.id.txtAdicional);
        txtPrecio = findViewById(R.id.txtPrecio);
        spinnerLaboratorio = findViewById(R.id.spinnerLaboratorio);
        spinnerTipoProducto = findViewById(R.id.spinnerTipoProducto);
        spinnerPresentacion = findViewById(R.id.spinnerPresentacion);

        // Inicializar cliente y listas/mapas
        client = new OkHttpClient();
        laboratoriosList = new ArrayList<>();
        tiposList = new ArrayList<>();
        presentacionesList = new ArrayList<>();
        laboratorioMap = new HashMap<>();
        tipoMap = new HashMap<>();
        presentacionMap = new HashMap<>();

        // Obtener ID del producto
        productoId = getIntent().getStringExtra("id");
        if (productoId != null) {
            cargarLaboratorios();
            cargarTipos();
            cargarPresentaciones();
            cargarProductoDetalles();

        }



        MaterialButton buttonModificar = findViewById(R.id.buttonModificar);
        buttonModificar.setOnClickListener(v -> modificarProducto());


        MaterialButton buttonMenu2 = findViewById(R.id.buttonmenu2);
        buttonMenu2.setOnClickListener(v -> {
            Intent intent = new Intent(modificar_gestionproductos.this, MenuPrincipal.class);
            startActivity(intent);
        });
    }

    private void cargarLaboratorios() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/laboratorio";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al cargar laboratorios", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        laboratoriosList.clear();
                        laboratorioMap.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonLaboratorio = jsonArray.getJSONObject(i);
                            String nombre = jsonLaboratorio.getString("nombre");
                            int id = jsonLaboratorio.getInt("id");
                            laboratoriosList.add(nombre);
                            laboratorioMap.put(nombre, id);
                        }
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(modificar_gestionproductos.this, android.R.layout.simple_spinner_item, laboratoriosList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerLaboratorio.setAdapter(adapter);
                            laboratorioListo = true;
                            cargarDatosProducto(productoId);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void cargarTipos() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/tiproducto";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al cargar tipos de producto", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        tiposList.clear();
                        tipoMap.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonTipo = jsonArray.getJSONObject(i);
                            String nombre = jsonTipo.getString("nombre");
                            int id = jsonTipo.getInt("id");
                            tiposList.add(nombre);
                            tipoMap.put(nombre, id);
                        }
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(modificar_gestionproductos.this, android.R.layout.simple_spinner_item, tiposList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTipoProducto.setAdapter(adapter);
                            tipoListo = true;
                            cargarDatosProducto(productoId);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void cargarPresentaciones() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/presentacion";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al cargar presentaciones", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        presentacionesList.clear();
                        presentacionMap.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonPresentacion = jsonArray.getJSONObject(i);
                            String nombre = jsonPresentacion.getString("nombre");
                            int id = jsonPresentacion.getInt("id");
                            presentacionesList.add(nombre);
                            presentacionMap.put(nombre, id);
                        }
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(modificar_gestionproductos.this, android.R.layout.simple_spinner_item, presentacionesList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerPresentacion.setAdapter(adapter);
                            /*presentacionListo = true;*/
                            cargarDatosProducto(productoId);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void cargarProductoDetalles() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto/" + productoId;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al cargar detalles de productos", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Asegúrate de que el cuerpo de la respuesta no sea nulo
                        String responseBody = response.body().string();
                        JSONObject jsonProducto = new JSONObject(responseBody);

                        // Verificar y obtener los datos
                        int laboratorioId = jsonProducto.optInt("prod_lab", -1);
                        int presentacionId = jsonProducto.optInt("prod_present", -1);
                        int tipoId = jsonProducto.optInt("prod_tip_prod", -1);

                        runOnUiThread(() -> {
                            // Seleccionar la presentación en el Spinner
                            if (laboratorioId != -1) {
                                for (Map.Entry<String, Integer> entry : laboratorioMap.entrySet()) {
                                    if (entry.getValue() == laboratorioId) {
                                        int position = laboratoriosList.indexOf(entry.getKey());
                                        if (position >= 0) spinnerLaboratorio.setSelection(position);
                                        break;
                                    }
                                }
                            }

                            // Seleccionar la presentación en el Spinner
                            if (presentacionId != -1) {
                                for (Map.Entry<String, Integer> entry : presentacionMap.entrySet()) {
                                    if (entry.getValue() == presentacionId) {
                                        int position = presentacionesList.indexOf(entry.getKey());
                                        if (position >= 0) spinnerPresentacion.setSelection(position);
                                        break;
                                    }
                                }
                            }

                            // Seleccionar el tipo en el Spinner
                            if (tipoId != -1) {
                                for (Map.Entry<String, Integer> entry : tipoMap.entrySet()) {
                                    if (entry.getValue() == tipoId) {
                                        int position = tiposList.indexOf(entry.getKey());
                                        if (position >= 0) spinnerTipoProducto.setSelection(position);
                                        break;
                                    }
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al procesar datos del producto", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Respuesta no válida del servidor", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }





    private void cargarDatosProducto(String productoId) {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto/" + productoId;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al cargar el producto", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        runOnUiThread(() -> {
                            try {
                                txtNombreProducto.setText(jsonObject.getString("nombre"));
                                txtConcentracion.setText(jsonObject.getString("concentracion"));
                                txtAdicional.setText(jsonObject.getString("adicional"));
                                txtPrecio.setText(jsonObject.getString("precio"));

                                String laboratorio = jsonObject.getJSONObject("laboratorio").getString("nombre");
                                String tipo = jsonObject.getJSONObject("tipo").getString("nombre");
                                String presentacion = jsonObject.getJSONObject("presentacion").getString("nombre");

                                seleccionarValoresSiListo(laboratorio, tipo, presentacion);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void seleccionarValoresSiListo(String laboratorio, String tipo, String presentacion) {
        if (laboratorioListo && tipoListo && presentacionListo) {
            seleccionarValorSpinner(spinnerLaboratorio, laboratorio);
            seleccionarValorSpinner(spinnerTipoProducto, tipo);
            seleccionarValorSpinner(spinnerPresentacion, presentacion);
        }
    }

    private void seleccionarValorSpinner(Spinner spinner, String valor) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int posicion = adapter.getPosition(valor);
        if (posicion >= 0) {
            spinner.setSelection(posicion);
        }
    }

    private void modificarProducto() {
        String nombre = txtNombreProducto.getText().toString();
        String concentracion = txtConcentracion.getText().toString();
        String adicional = txtAdicional.getText().toString();
        String precio = txtPrecio.getText().toString();

        int laboratorioId = laboratorioMap.get(spinnerLaboratorio.getSelectedItem().toString());
        int tipoProductoId = tipoMap.get(spinnerTipoProducto.getSelectedItem().toString());
        int presentacionId = presentacionMap.get(spinnerPresentacion.getSelectedItem().toString());

        RequestBody formBody = new FormBody.Builder()
                .add("nombre", nombre)
                .add("concentracion", concentracion)
                .add("adicional", adicional)
                .add("precio", precio)
                .add("prod_lab", String.valueOf(laboratorioId))
                .add("prod_tip_prod", String.valueOf(tipoProductoId))
                .add("prod_present", String.valueOf(presentacionId))
                .build();

        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto/" + productoId;
        Request request = new Request.Builder()
                .url(url)
                .put(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "Error al modificar el producto", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(modificar_gestionproductos.this, "Producto modificado con éxito", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(modificar_gestionproductos.this, "No se pudo modificar el producto", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}