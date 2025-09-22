package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
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

public class agregar_gestionpresentaciones extends AppCompatActivity {

    private EditText editTextNombrePresentacion;
    private MaterialButton buttonAgregar,buttonToMenu;;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestionpresentaciones);

        // Inicializa el EditText y el botón
        editTextNombrePresentacion = findViewById(R.id.editTextNombrePresentacion);
        buttonAgregar = findViewById(R.id.buttonagregar);
        buttonToMenu = findViewById(R.id.buttonmenu);

        // Configurar el botón para redirigir al menú principal
        buttonToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestionpresentaciones.this, MenuPrincipal.class);
            startActivity(intent);
        });

        // Configura el OnClickListener para el botón "Agregar"
        buttonAgregar.setOnClickListener(v -> {
            String nombrePresentacion = editTextNombrePresentacion.getText().toString().trim();

            if (!nombrePresentacion.isEmpty()) {
                agregarPresentacion(nombrePresentacion);
            } else {
                Toast.makeText(agregar_gestionpresentaciones.this, "Ingrese el nombre de la presentación", Toast.LENGTH_SHORT).show();
            }



        });
    }

    private void agregarPresentacion(String nombrePresentacion) {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/presentacion";

        // Crea el objeto JSON con el nombre de la presentación
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombrePresentacion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Configura el cuerpo de la solicitud con el tipo de medio JSON
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);

        // Crea la solicitud POST
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // Envía la solicitud de manera asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(agregar_gestionpresentaciones.this, "Error al agregar la presentación", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(agregar_gestionpresentaciones.this, "Presentación agregada correctamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK); // Envía un resultado exitoso
                        finish(); // Cierra la actividad actual y regresa a la anterior
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(agregar_gestionpresentaciones.this, "Error: No se pudo agregar la presentación", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
