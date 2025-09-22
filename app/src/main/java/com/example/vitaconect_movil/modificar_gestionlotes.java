package com.example.vitaconect_movil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class modificar_gestionlotes extends AppCompatActivity {

    private OkHttpClient client;
    private Spinner loteProdSpinner, loteProvSpinner;
    private EditText fechaInput, stockInput;
    private Button buttonModificar, buttonMenuInicio;
    private ArrayList<String> productoNombres, proveedorNombres;
    private Map<String, Integer> productoMap, proveedorMap;
    private int loteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_gestionlotes);

        client = new OkHttpClient();

        loteProdSpinner = findViewById(R.id.loteProdSpinner);
        loteProvSpinner = findViewById(R.id.loteProvSpinner);
        fechaInput = findViewById(R.id.fechaInput);
        stockInput = findViewById(R.id.stockInput);
        buttonModificar = findViewById(R.id.buttonModificar);
        buttonMenuInicio = findViewById(R.id.button_menu_inicio);

        productoNombres = new ArrayList<>();
        proveedorNombres = new ArrayList<>();
        productoMap = new HashMap<>();
        proveedorMap = new HashMap<>();

        // Obtener el ID del lote desde el Intent
        loteId = getIntent().getIntExtra("loteId", -1);

        cargarProductos();
        cargarProveedores();
        cargarLoteDetalles();

        // Configurar el botón para redirigir al menú principal
        buttonMenuInicio.setOnClickListener(v -> {
            Intent intent = new Intent(modificar_gestionlotes.this, MenuPrincipal.class);
            startActivity(intent);
            finish(); // Opcional: cierra la actividad actual para que no se pueda volver atrás
        });

        // Hacer que el Spinner de productos no sea editable
        loteProdSpinner.setEnabled(false);

        // Configurar DatePickerDialog para fechaInput
        fechaInput.setOnClickListener(v -> showDatePickerDialog());

        buttonModificar.setOnClickListener(v -> modificarLote());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatear y establecer la fecha en el EditText
                    String fecha = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    fechaInput.setText(fecha);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void cargarProductos() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionlotes.this, "Error al cargar productos", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonProducto = jsonArray.getJSONObject(i);
                            String nombre = jsonProducto.getString("nombre");
                            int id = jsonProducto.getInt("id");
                            productoNombres.add(nombre);
                            productoMap.put(nombre, id);
                        }
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(modificar_gestionlotes.this, android.R.layout.simple_spinner_item, productoNombres);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            loteProdSpinner.setAdapter(adapter);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void cargarProveedores() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/proveedor";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionlotes.this, "Error al cargar proveedores", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonProveedor = jsonArray.getJSONObject(i);
                            String nombre = jsonProveedor.getString("nombre");
                            int id = jsonProveedor.getInt("id");
                            proveedorNombres.add(nombre);
                            proveedorMap.put(nombre, id);
                        }
                        runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(modificar_gestionlotes.this, android.R.layout.simple_spinner_item, proveedorNombres);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            loteProvSpinner.setAdapter(adapter);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void cargarLoteDetalles() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/lote/" + loteId;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionlotes.this, "Error al cargar detalles del lote", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonLote = new JSONObject(response.body().string());
                        int stock = jsonLote.getInt("stock");
                        String vencimiento = jsonLote.getString("vencimiento");
                        int productoId = jsonLote.getInt("lote_id_prod");
                        int proveedorId = jsonLote.getInt("lote_id_prov");

                        runOnUiThread(() -> {
                            stockInput.setText(String.valueOf(stock));
                            fechaInput.setText(vencimiento);

                            // Seleccionar producto en el Spinner
                            for (Map.Entry<String, Integer> entry : productoMap.entrySet()) {
                                if (entry.getValue() == productoId) {
                                    int position = productoNombres.indexOf(entry.getKey());
                                    loteProdSpinner.setSelection(position);
                                    break;
                                }
                            }

                            // Seleccionar proveedor en el Spinner
                            for (Map.Entry<String, Integer> entry : proveedorMap.entrySet()) {
                                if (entry.getValue() == proveedorId) {
                                    int position = proveedorNombres.indexOf(entry.getKey());
                                    loteProvSpinner.setSelection(position);
                                    break;
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void modificarLote() {
        String stock = stockInput.getText().toString();
        String vencimiento = fechaInput.getText().toString();
        String productoSeleccionado = (String) loteProdSpinner.getSelectedItem();
        String proveedorSeleccionado = (String) loteProvSpinner.getSelectedItem();

        int productoId = productoMap.get(productoSeleccionado);
        int proveedorId = proveedorMap.get(proveedorSeleccionado);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stock", stock);
            jsonObject.put("vencimiento", vencimiento);
            jsonObject.put("lote_id_prod", productoId);
            jsonObject.put("lote_id_prov", proveedorId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://vcbd.accwebdeveloper.com.mx/api/lote/" + loteId;
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder().url(url).put(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionlotes.this, "Error al modificar el lote", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(modificar_gestionlotes.this, "Lote modificado exitosamente", Toast.LENGTH_SHORT).show();
                        finish(); // Finalizar la actividad para regresar al fragmento
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(modificar_gestionlotes.this, "No se pudo modificar el lote", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}




