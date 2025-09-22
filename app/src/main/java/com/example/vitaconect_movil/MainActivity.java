package com.example.vitaconect_movil;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import com.example.vitaconect_movil.databinding.ActivityMenuPrincipalBinding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private Button acceder, registrarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar los insets del sistema (barra de estado, barra de navegación)
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

        // Inicialización de los campos de entrada y botón
        txtEmail = findViewById(R.id.txtEmailAddress);
        txtPassword = findViewById(R.id.txtPassword);
        acceder = findViewById(R.id.acceder);
        registrarCuenta = findViewById(R.id.registrarusuario); // Inicializa el botón


        acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                new LoginTask().execute(email, password);
            }
        });

        // Configurar el listener para el botón "Registrar Cuenta"
        registrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegistrarUsuarios.class);
                startActivity(intent);
            }
        });
    }

    // Clase AsyncTask para manejar el login
    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            try {


                URL url = new URL("https://vcbd.accwebdeveloper.com.mx/api/login"); // Cambia a la URL de tu API
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; utf-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                String jsonInputString = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                } else {
                    return "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Aquí puedes procesar la respuesta y manejar el éxito o fallo del login
            if (result.contains("status\":1")) {
                Toast.makeText(MainActivity.this, "Login exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Error en login: " + result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}





