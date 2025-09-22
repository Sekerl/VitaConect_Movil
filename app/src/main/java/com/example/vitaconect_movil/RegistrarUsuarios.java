package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegistrarUsuarios extends AppCompatActivity {
    private Button registrarCuenta;
    private Button login;
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtNombre;

    // Tipo de contenido para el JSON
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarusuario);

        // Inicializa los elementos de la vista
        registrarCuenta = findViewById(R.id.registrarcuenta);
        login = findViewById(R.id.login);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        txtNombre = findViewById(R.id.txtNombre);

        // Configura el listener para el botón "Registrar Cuenta"
        registrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

        // Configura el listener para el botón "Login"
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrarUsuarios.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registrarUsuario() {
        String email = txtEmailAddress.getText().toString();
        String password = txtPassword.getText().toString();
        String nombre = txtNombre.getText().toString();

        // Crear el objeto JSON
        JSONObject json = new JSONObject();
        try {
            json.put("name", nombre);
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Realiza la solicitud HTTP
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://vcbd.accwebdeveloper.com.mx/api/register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RegistrarUsuarios.this, "Error en la conexión", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Aquí puedes manejar la respuesta exitosa
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        Toast.makeText(RegistrarUsuarios.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        // Redirigir a otra actividad si es necesario
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(RegistrarUsuarios.this, "Registro fallido", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
