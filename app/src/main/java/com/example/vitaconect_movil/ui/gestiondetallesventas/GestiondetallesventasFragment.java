package com.example.vitaconect_movil.ui.gestiondetallesventas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.Venta;
import com.example.vitaconect_movil.VentasAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GestiondetallesventasFragment extends Fragment {

    private RecyclerView recyclerView;
    private VentasAdapter ventasAdapter;
    private OkHttpClient client;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestiondetallesventas, container, false);

        // Configurar el Toolbar
        if (getActivity() instanceof AppCompatActivity) {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            if (toolbar != null) {
                ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Detalles Ventas");
                }
            }
        }

        // Configuración del RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewVentas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        client = new OkHttpClient();
        fetchVentas();

        return view;
    }

    private void fetchVentas() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/vistas/ventas_view";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Gson gson = new Gson();
                    Type ventaListType = new TypeToken<List<Venta>>() {}.getType();
                    List<Venta> ventasList = gson.fromJson(jsonResponse, ventaListType);

                    getActivity().runOnUiThread(() -> {
                        ventasAdapter = new VentasAdapter(ventasList, getContext());
                        recyclerView.setAdapter(ventasAdapter);
                    });
                }
            }
        });
    }
}
