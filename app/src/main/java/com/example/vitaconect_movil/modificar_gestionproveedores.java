package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class modificar_gestionproveedores extends AppCompatActivity {

    private EditText txtIdProveedor, txtNombreProveedor, txtTelefono, txtCorreo, txtDireccion;
    private static final String TAG = "ModificarProveedor";
    private static final String BASE_URL = "https://vcbd.accwebdeveloper.com.mx/api/proveedor/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_gestionproveedores);

        // Inicializa los EditText
        txtIdProveedor = findViewById(R.id.txtId); // Campo de ID
        txtNombreProveedor = findViewById(R.id.txtNombreProveedor);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtDireccion = findViewById(R.id.txtDireccion);

        // Obtiene los datos del Intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("proveedorId", -1); // Obtiene el ID con la clave "proveedorId"
        String nombre = intent.getStringExtra("nombre");
        String telefono = intent.getStringExtra("telefono");
        String correo = intent.getStringExtra("correo");
        String direccion = intent.getStringExtra("direccion");

        // Verifica que el ID no sea -1
        if (id == -1) {
            Toast.makeText(this, "Error: ID del proveedor no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Llena los EditText con los datos obtenidos
        txtIdProveedor.setText(String.valueOf(id)); // Muestra el ID
        txtNombreProveedor.setText(nombre);
        txtTelefono.setText(telefono);
        txtCorreo.setText(correo);
        txtDireccion.setText(direccion);

        // Botón para modificar el proveedor
        Button buttonModificar = findViewById(R.id.buttonmodificar);
        buttonModificar.setOnClickListener(v -> {
            String nuevoNombre = txtNombreProveedor.getText().toString();
            String nuevoTelefono = txtTelefono.getText().toString();
            String nuevoCorreo = txtCorreo.getText().toString();
            String nuevaDireccion = txtDireccion.getText().toString();

            modificarProveedor(String.valueOf(id), nuevoNombre, nuevoTelefono, nuevoCorreo, nuevaDireccion);
        });

        // Botón para regresar al menú principal
        Button buttonMenu = findViewById(R.id.buttonmenu);
        buttonMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(modificar_gestionproveedores.this, MenuPrincipal.class);
            startActivity(menuIntent);
        });
    }

    private void modificarProveedor(String id, String nombre, String telefono, String correo, String direccion) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("telefono", telefono);
            jsonObject.put("correo", correo);
            jsonObject.put("direccion", direccion);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(BASE_URL + id)  // Concatena el ID al final de la URL base
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(modificar_gestionproveedores.this, "Error en la conexión", Toast.LENGTH_SHORT).show());
                Log.e(TAG, "Error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(modificar_gestionproveedores.this, "Proveedor modificado exitosamente", Toast.LENGTH_SHORT).show();
                        finish(); // Finalizar la actividad para regresar al fragmento
                    });
                } else {
                    String errorMessage = response.body() != null ? response.body().string() : "Error desconocido";
                    runOnUiThread(() -> Toast.makeText(modificar_gestionproveedores.this, "Error al modificar proveedor: " + errorMessage, Toast.LENGTH_SHORT).show());
                    Log.e(TAG, "Error al modificar proveedor: " + response.code() + " - " + errorMessage);
                }
            }
        });
    }
}
