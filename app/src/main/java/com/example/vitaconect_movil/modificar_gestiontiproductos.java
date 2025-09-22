package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class modificar_gestiontiproductos extends AppCompatActivity {

        private EditText editTextNombre;
        private MaterialButton btnGuardar;
        private int tipoProductoId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.modificar_gestiontiproductos);

                // Obtener los datos pasados desde el Intent
                tipoProductoId = getIntent().getIntExtra("id", -1);
                String nombre = getIntent().getStringExtra("nombre");

                // Inicializa los elementos de la vista
                editTextNombre = findViewById(R.id.editTextNombreTiproducto); // Cambia el ID si es diferente
                btnGuardar = findViewById(R.id.btnGuardar);

                // Muestra el nombre en el EditText
                if (nombre != null) {
                        editTextNombre.setText(nombre);
                }

                // Configura la lógica para el botón de guardar
                btnGuardar.setOnClickListener(view -> {
                        String nuevoNombre = editTextNombre.getText().toString();
                        if (!nuevoNombre.isEmpty()) {
                                actualizarTipoProducto(tipoProductoId, nuevoNombre);
                        } else {
                                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                        }
                });

                // Botón para regresar al menú principal
                Button buttonMenu = findViewById(R.id.buttonmenu);
                buttonMenu.setOnClickListener(v -> {
                        Intent menuIntent = new Intent(modificar_gestiontiproductos.this, MenuPrincipal.class);
                        startActivity(menuIntent);
                });
        }

        private void actualizarTipoProducto(int id, String nombre) {
                OkHttpClient client = new OkHttpClient();
                MediaType JSON = MediaType.get("application/json; charset=utf-8");

                JSONObject jsonObject = new JSONObject();
                try {
                        jsonObject.put("nombre", nombre);
                } catch (JSONException e) {
                        e.printStackTrace();
                }

                RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
                Request request = new Request.Builder()
                        .url("https://vcbd.accwebdeveloper.com.mx/api/tiproducto/" + id)
                        .put(body)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (response.isSuccessful()) {
                                        runOnUiThread(() -> {
                                                Toast.makeText(modificar_gestiontiproductos.this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                                                setResult(AppCompatActivity.RESULT_OK); // Indica que la operación fue exitosa
                                                finish(); // Finaliza la actividad para volver a la lista de productos
                                        });
                                } else {
                                        runOnUiThread(() -> Toast.makeText(modificar_gestiontiproductos.this, "Error al actualizar", Toast.LENGTH_SHORT).show());
                                }
                        }
                });
        }
}