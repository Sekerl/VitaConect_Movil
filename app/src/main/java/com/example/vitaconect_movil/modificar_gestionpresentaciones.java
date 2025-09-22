package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

public class modificar_gestionpresentaciones extends AppCompatActivity {

    private EditText editTextNombrePresentacion;
    private MaterialButton buttonModificar;
    private int presentacionId;
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_gestionpresentaciones);

        presentacionId = getIntent().getIntExtra("presentacion_id", -1);
        String nombrePresentacion = getIntent().getStringExtra("presentacion_nombre");

        editTextNombrePresentacion = findViewById(R.id.editTextNombrePresentacion);
        buttonModificar = findViewById(R.id.buttonmodificar);

        editTextNombrePresentacion.setText(nombrePresentacion);

        buttonModificar.setOnClickListener(v -> modificarPresentacion());

        // Botón para regresar al menú principal
        Button buttonMenu = findViewById(R.id.buttonmenu);
        buttonMenu.setOnClickListener(v -> {
            Intent menuIntent = new Intent(modificar_gestionpresentaciones.this, MenuPrincipal.class);
            startActivity(menuIntent);
        });
    }

    private void modificarPresentacion() {
        String nuevoNombre = editTextNombrePresentacion.getText().toString().trim();

        if (nuevoNombre.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nuevoNombre);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "https://vcbd.accwebdeveloper.com.mx/api/presentacion/" + presentacionId;
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(jsonObject.toString(), mediaType);

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(modificar_gestionpresentaciones.this, "Error al modificar la presentación", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(modificar_gestionpresentaciones.this, "Presentación modificada correctamente", Toast.LENGTH_SHORT).show();
                        finish(); // Cierra la actividad y regresa a la anterior
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(modificar_gestionpresentaciones.this, "Error: No se pudo modificar la presentación", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
