package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class agregar_gestionproveedores extends AppCompatActivity {

    private EditText txtNombre, txtTelefono, txtCorreo, txtDireccion;
    private Button buttonAgregarProveedor, buttonToMenu;

    // MediaType para JSON
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestionproveedores);

        // Inicializar los campos de texto
        txtNombre = findViewById(R.id.txtNombreProveedor);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtDireccion = findViewById(R.id.txtDireccion);

        // Inicializar botones
        buttonAgregarProveedor = findViewById(R.id.buttonagregar);
        buttonToMenu = findViewById(R.id.buttonmenu);

        // Configurar el botón para agregar proveedor
        buttonAgregarProveedor.setOnClickListener(v -> agregarProveedor());

        // Configurar el botón para redirigir al menú principal
        buttonToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestionproveedores.this, MenuPrincipal.class);
            startActivity(intent);
        });
    }

    private void agregarProveedor() {
        // Obtener los valores ingresados por el usuario
        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String correo = txtCorreo.getText().toString();
        String direccion = txtDireccion.getText().toString();

        OkHttpClient client = new OkHttpClient();

        try {
            // Crear objeto JSON (igual que en registro)
            JSONObject json = new JSONObject();
            json.put("nombre", nombre);
            json.put("telefono", telefono);
            json.put("correo", correo);
            json.put("direccion", direccion);

            RequestBody body = RequestBody.create(json.toString(), JSON);

            // Crear la solicitud POST con headers (IMPORTANTE)
            Request request = new Request.Builder()
                    .url("https://vcbd.accwebdeveloper.com.mx/api/proveedor")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    // Si requiere autenticación, agrega esta línea:
                    // .addHeader("Authorization", "Bearer " + obtenerToken())
                    .build();

            // Enviar la solicitud de manera asíncrona
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Log.e("API_ERROR", "Error: " + e.getMessage());
                        Toast.makeText(agregar_gestionproveedores.this,
                                "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    Log.d("API_RESPONSE", "Código: " + response.code() + " - Respuesta: " + responseBody);

                    if (response.isSuccessful()) {
                        runOnUiThread(() -> {
                            Toast.makeText(agregar_gestionproveedores.this,
                                    "Proveedor agregado exitosamente", Toast.LENGTH_SHORT).show();
                            // Limpiar los campos
                            txtNombre.setText("");
                            txtTelefono.setText("");
                            txtCorreo.setText("");
                            txtDireccion.setText("");
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            String errorMessage = "Error al agregar proveedor: Código " + response.code();
                            errorMessage += " - " + responseBody;
                            Toast.makeText(agregar_gestionproveedores.this,
                                    errorMessage, Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creando JSON", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obtener token si es necesario
    private String obtenerToken() {
        // Implementa la lógica para obtener el token de tu sesión
        // Por ejemplo, usando SharedPreferences
        return "";
    }
}