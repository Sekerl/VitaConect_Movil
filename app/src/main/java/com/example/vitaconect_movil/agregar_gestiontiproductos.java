package com.example.vitaconect_movil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class agregar_gestiontiproductos extends AppCompatActivity {

    private EditText editTextNombreTiproducto;
    private MaterialButton buttonAgregar,buttonToMenu;
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_gestiontiproductos);

        // Vincula el EditText y el botón con el diseño
        editTextNombreTiproducto = findViewById(R.id.editTextNombreTiproducto);
        buttonAgregar = findViewById(R.id.buttonagregar);
        buttonToMenu = findViewById(R.id.buttonmenu);

        // Configurar el botón para redirigir al menú principal
        buttonToMenu.setOnClickListener(v -> {
            Intent intent = new Intent(agregar_gestiontiproductos.this, MenuPrincipal.class);
            startActivity(intent);
        });

        // Configura el OnClickListener para el botón
        buttonAgregar.setOnClickListener(view -> agregarTipoProducto());
    }

    // Método para agregar un nuevo tipo de producto
    private void agregarTipoProducto() {
        String nombreTipoProducto = editTextNombreTiproducto.getText().toString().trim();

        if (nombreTipoProducto.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese un nombre para el tipo de producto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el cuerpo de la solicitud en formato JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombreTipoProducto);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el RequestBody
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        // Configurar la solicitud POST
        Request request = new Request.Builder()
                .url("https://vcbd.accwebdeveloper.com.mx/api/tiproducto")
                .post(body)
                .build();

        // Enviar la solicitud de forma asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(agregar_gestiontiproductos.this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(agregar_gestiontiproductos.this, "Tipo de producto agregado con éxito", Toast.LENGTH_SHORT).show();
                        setResult(AppCompatActivity.RESULT_OK); // Indica que la operación fue exitosa
                        finish(); // Cierra la actividad y regresa a la vista anterior
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(agregar_gestiontiproductos.this, "Error al agregar el tipo de producto", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}