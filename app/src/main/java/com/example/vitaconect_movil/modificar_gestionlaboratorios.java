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

public class modificar_gestionlaboratorios extends AppCompatActivity {

    private EditText editTextNombreLaboratorio;
    private int laboratorioId;
    private OkHttpClient client = new OkHttpClient();
    private static final String URL_API = "https://vcbd.accwebdeveloper.com.mx/api/laboratorio/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_gestionlaboratorios);

        editTextNombreLaboratorio = findViewById(R.id.editTextNombreLaboratorio);
        MaterialButton buttonModificar = findViewById(R.id.buttonmodificar);
        MaterialButton buttonMenu = findViewById(R.id.buttonmenu);

        // Obtener el ID y el nombre del laboratorio desde el Intent
        laboratorioId = getIntent().getIntExtra("laboratorio_id", -1);
        String nombreLaboratorio = getIntent().getStringExtra("laboratorio_nombre");

        // Configurar el nombre actual del laboratorio en el EditText
        editTextNombreLaboratorio.setText(nombreLaboratorio);

        // Configurar el botón "Modificar" para enviar la solicitud de actualización
        buttonModificar.setOnClickListener(v -> {
            String nuevoNombre = editTextNombreLaboratorio.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                modificarLaboratorio(nuevoNombre);
            } else {
                Toast.makeText(modificar_gestionlaboratorios.this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuración para redirigir al menú principal
        buttonMenu.setOnClickListener(v -> {
            Intent intent = new Intent(modificar_gestionlaboratorios.this, MenuPrincipal.class);
            startActivity(intent);
            finish();
        });
    }

    private void modificarLaboratorio(String nuevoNombre) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String json = "{ \"nombre\": \"" + nuevoNombre + "\" }";
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(URL_API + laboratorioId) // URL con el ID del laboratorio
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(modificar_gestionlaboratorios.this, "Error al modificar laboratorio", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(modificar_gestionlaboratorios.this, "Laboratorio modificado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(AppCompatActivity.RESULT_OK); // Devuelve resultado de éxito
                        finish(); // Cierra la actividad
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(modificar_gestionlaboratorios.this, "Error al modificar laboratorio", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
