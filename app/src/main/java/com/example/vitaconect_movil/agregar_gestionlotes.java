package com.example.vitaconect_movil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class agregar_gestionlotes extends AppCompatActivity {
    private OkHttpClient client;
    private Spinner loteProdSpinner, loteProvSpinner;
    private ArrayList<String> productoNombres, proveedorNombres;
    private Map<String, Integer> productoMap, proveedorMap;
    private int selectedProductoId, selectedProveedorId;
    private String nombreProducto;
    private TextView fechaInput;  // Cambiar EditText a TextView para mostrar la fecha

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestionlotes);

        client = new OkHttpClient();

        loteProdSpinner = findViewById(R.id.loteProdSpinner);
        loteProvSpinner = findViewById(R.id.loteProvSpinner);
        fechaInput = findViewById(R.id.fechaInput);  // Referencia al nuevo TextView de fecha
        Button buttonAgregar = findViewById(R.id.buttonagregar);
        Button buttonMenuInicio = findViewById(R.id.button_menu_inicio);

        productoNombres = new ArrayList<>();
        proveedorNombres = new ArrayList<>();
        productoMap = new HashMap<>();
        proveedorMap = new HashMap<>();

        nombreProducto = getIntent().getStringExtra("nombreProducto");

        // Mostrar DatePickerDialog cuando se hace clic en fechaInput
        fechaInput.setOnClickListener(v -> showDatePickerDialog());

        buttonAgregar.setOnClickListener(v -> agregarLote());

        buttonMenuInicio.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestionlotes.this, MenuPrincipal.class);
            startActivity(intent);
        });

        cargarProductos();
        cargarProveedores();
    }

    private void showDatePickerDialog() {
        // Configura el calendario inicial y abre el DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                agregar_gestionlotes.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    fechaInput.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void cargarProductos() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionlotes.this, "Error al cargar productos", Toast.LENGTH_SHORT).show());
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(agregar_gestionlotes.this, android.R.layout.simple_spinner_item, productoNombres);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            loteProdSpinner.setAdapter(adapter);

                            if (nombreProducto != null) {
                                int position = productoNombres.indexOf(nombreProducto);
                                if (position >= 0) {
                                    loteProdSpinner.setSelection(position);
                                    loteProdSpinner.setEnabled(false);
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

    private void cargarProveedores() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/proveedor";

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionlotes.this, "Error al cargar proveedores", Toast.LENGTH_SHORT).show());
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
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(agregar_gestionlotes.this, android.R.layout.simple_spinner_item, proveedorNombres);
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

    private void agregarLote() {
        String stock = ((EditText) findViewById(R.id.stockInput)).getText().toString();
        String vencimiento = fechaInput.getText().toString();  // Obtener la fecha seleccionada

        if (stock.isEmpty() || vencimiento.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String proveedorSeleccionado = (String) loteProvSpinner.getSelectedItem();
        selectedProveedorId = proveedorMap.get(proveedorSeleccionado);
        selectedProductoId = productoMap.get(loteProdSpinner.getSelectedItem().toString());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stock", stock);
            jsonObject.put("vencimiento", vencimiento);
            jsonObject.put("lote_id_prod", selectedProductoId);
            jsonObject.put("lote_id_prov", selectedProveedorId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://vcbd.accwebdeveloper.com.mx/api/lote";
        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder().url(url).post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionlotes.this, "Error al agregar el lote", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(agregar_gestionlotes.this, "Lote agregado correctamente", Toast.LENGTH_SHORT).show();
                        // Limpiar campos después de agregar el lote
                        ((EditText) findViewById(R.id.stockInput)).setText("");
                        fechaInput.setText("Seleccione Fecha de Vencimiento");
                        loteProdSpinner.setSelection(0);
                        loteProvSpinner.setSelection(0);
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(agregar_gestionlotes.this, "No se pudo agregar el lote", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
