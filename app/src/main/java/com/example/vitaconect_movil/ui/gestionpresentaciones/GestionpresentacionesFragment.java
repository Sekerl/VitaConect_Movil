package com.example.vitaconect_movil.ui.gestionpresentaciones;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Toast;

import com.example.vitaconect_movil.Presentacion;
import com.example.vitaconect_movil.PresentacionAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.modificar_gestionpresentaciones;
import com.example.vitaconect_movil.agregar_gestionpresentaciones;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GestionpresentacionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PresentacionAdapter adapter;
    private List<Presentacion> presentacionesList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();
    private MaterialButton buttonAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestionpresentaciones, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PresentacionAdapter(presentacionesList, getContext(), new PresentacionAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Presentacion presentacion, int position) {
                Intent intent = new Intent(getContext(), modificar_gestionpresentaciones.class);
                intent.putExtra("presentacion_id", presentacion.getId());
                intent.putExtra("presentacion_nombre", presentacion.getNombre());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Presentacion presentacion, int position) {
                deletePresentacion(presentacion.getId(), position);
            }
        });

        recyclerView.setAdapter(adapter);

        buttonAdd = view.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), agregar_gestionpresentaciones.class);
            startActivity(intent);
        });

        fetchPresentaciones();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchPresentaciones();
    }

    private void fetchPresentaciones() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/presentacion";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al cargar las presentaciones", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<Presentacion> nuevaLista = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String nombre = object.getString("nombre");
                            nuevaLista.add(new Presentacion(id, nombre));
                        }
                        getActivity().runOnUiThread(() -> {
                            presentacionesList.clear();
                            presentacionesList.addAll(nuevaLista);
                            adapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void deletePresentacion(int id, int position) {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/presentacion/" + id;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Log.d("DELETE_PRES", "Eliminando presentación ID: " + id + " en posición: " + position);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("DELETE_PRES", "Error: " + e.getMessage());
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("DELETE_PRES", "Respuesta: " + response.code() + " - " + responseBody);

                getActivity().runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        // Elimina el elemento visualmente inmediatamente
                        adapter.removeItem(position);
                        Toast.makeText(getContext(), "Presentación eliminada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = "Error al eliminar - Código: " + response.code();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        // Si falla, recargamos la lista para estar sincronizados
                        fetchPresentaciones();
                    }
                });
            }
        });
    }
}