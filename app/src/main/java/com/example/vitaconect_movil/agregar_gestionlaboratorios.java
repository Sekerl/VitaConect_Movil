package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class agregar_gestionlaboratorios extends AppCompatActivity {

    private EditText editTextNombreLaboratorio;
    private OkHttpClient client = new OkHttpClient();
    private static final String URL_API = "https://vcbd.accwebdeveloper.com.mx/api/laboratorio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestionlaboratorios);

        editTextNombreLaboratorio = findViewById(R.id.editTextNombreLaboratorio);
        MaterialButton buttonAgregar = findViewById(R.id.buttonagregar);
        MaterialButton buttonMenu = findViewById(R.id.buttonmenu);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreLaboratorio = editTextNombreLaboratorio.getText().toString().trim();
                if (!nombreLaboratorio.isEmpty()) {
                    agregarLaboratorio(nombreLaboratorio);
                } else {
                    Toast.makeText(agregar_gestionlaboratorios.this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonMenu.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestionlaboratorios.this, MenuPrincipal.class);
            startActivity(intent);
            finish(); // Cierra la actividad actual si no deseas volver
        });
    }

    private void agregarLaboratorio(String nombreLaboratorio) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"nombre\": \"" + nombreLaboratorio + "\" }";
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(URL_API)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(agregar_gestionlaboratorios.this, "Error al agregar laboratorio", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(agregar_gestionlaboratorios.this, "Laboratorio agregado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(AppCompatActivity.RESULT_OK); // Devuelve resultado de éxito
                        finish(); // Cierra la actividad
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(agregar_gestionlaboratorios.this, "Error al agregar laboratorio", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
