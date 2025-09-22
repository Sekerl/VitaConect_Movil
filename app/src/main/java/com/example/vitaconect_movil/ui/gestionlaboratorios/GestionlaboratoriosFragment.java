package com.example.vitaconect_movil.ui.gestionlaboratorios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.agregar_gestionlaboratorios;
import com.example.vitaconect_movil.Laboratorio;
import com.example.vitaconect_movil.LaboratorioAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.databinding.FragmentGestionlaboratoriosBinding;
import com.example.vitaconect_movil.modificar_gestionlaboratorios;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GestionlaboratoriosFragment extends Fragment {
    private FragmentGestionlaboratoriosBinding binding;
    private OkHttpClient client = new OkHttpClient();
    private List<Laboratorio> laboratorioList = new ArrayList<>();
    private LaboratorioAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGestionlaboratoriosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new LaboratorioAdapter(laboratorioList, new LaboratorioAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(Laboratorio laboratorio, int position) {
                deleteLaboratorio(laboratorio.getId(), position);
            }

            @Override
            public void onEditClick(Laboratorio laboratorio) {
                Intent intent = new Intent(getContext(), modificar_gestionlaboratorios.class);
                intent.putExtra("laboratorio_id", laboratorio.getId());
                intent.putExtra("laboratorio_nombre", laboratorio.getNombre());
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(adapter);

        binding.buttonadd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), agregar_gestionlaboratorios.class);
            startActivityForResult(intent, 1);
        });

        fetchLaboratorios();
        return root;
    }

    private void fetchLaboratorios() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/laboratorio";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Type listType = new TypeToken<List<Laboratorio>>() {}.getType();
                    List<Laboratorio> nuevaLista = new Gson().fromJson(json, listType);

                    getActivity().runOnUiThread(() -> {
                        laboratorioList.clear();
                        laboratorioList.addAll(nuevaLista);
                        adapter.notifyDataSetChanged();
                    });
                }
            }
        });
    }

    private void deleteLaboratorio(int id, int position) {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/laboratorio/" + id;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();

        Log.d("DELETE_LAB", "Eliminando laboratorio ID: " + id + " en posición: " + position);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("DELETE_LAB", "Error: " + e.getMessage());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Error de conexión: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("DELETE_LAB", "Respuesta: " + response.code() + " - " + responseBody);

                getActivity().runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        // Elimina el elemento visualmente inmediatamente
                        adapter.removeItem(position);
                        Toast.makeText(getContext(), "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        String errorMessage = "Error al eliminar - Código: " + response.code();
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                        // Si falla, recargamos la lista para estar sincronizados
                        fetchLaboratorios();
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                fetchLaboratorios();
            }
        }
    }
}