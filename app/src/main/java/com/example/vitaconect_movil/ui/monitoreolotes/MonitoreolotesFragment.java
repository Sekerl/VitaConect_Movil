package com.example.vitaconect_movil.ui.monitoreolotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.Lote;
import com.example.vitaconect_movil.LoteAdapter;
import com.example.vitaconect_movil.databinding.FragmentMonitoreolotesBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MonitoreolotesFragment extends Fragment {

    private FragmentMonitoreolotesBinding binding;
    private OkHttpClient client;
    private List<Lote> loteList;
    private LoteAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMonitoreolotesBinding.inflate(inflater, container, false);
        client = new OkHttpClient();
        loteList = new ArrayList<>();

        // Configuración del RecyclerView
        RecyclerView recyclerView = binding.recyclerViewLotes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LoteAdapter(loteList, client);
        recyclerView.setAdapter(adapter);

        // Cargar lotes
        loadLotes();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLotes(); // Recargar los proveedores cada vez que el fragmento esté visible
    }

    private void loadLotes() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/lote";
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
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        loteList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonLote = jsonArray.getJSONObject(i);
                            int id = jsonLote.getInt("id");
                            String nombre = jsonLote.getJSONObject("producto").getString("nombre");
                            int stock = jsonLote.getInt("stock");
                            String vencimiento = jsonLote.getString("vencimiento");
                            String concentracion = jsonLote.getJSONObject("producto").getString("concentracion");
                            String adicional = jsonLote.getJSONObject("producto").getString("adicional");
                            String laboratorio = jsonLote.getJSONObject("producto").getJSONObject("laboratorio").getString("nombre");
                            String tipo = jsonLote.getJSONObject("producto").getJSONObject("tipo").getString("nombre");
                            String presentacion = jsonLote.getJSONObject("producto").getJSONObject("presentacion").getString("nombre");
                            String proveedor = jsonLote.getJSONObject("proveedor").getString("nombre");

                            // Calcular estado de vencimiento
                            String estadoVencimiento = calculateEstadoVencimiento(vencimiento);

                            loteList.add(new Lote(id, nombre, stock, vencimiento, concentracion, adicional, laboratorio, tipo, presentacion, proveedor, estadoVencimiento));
                        }

                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    // Método para calcular el estado de vencimiento basado en la fecha de vencimiento
    private String calculateEstadoVencimiento(String vencimiento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaVencimiento = LocalDate.parse(vencimiento, formatter);
        LocalDate fechaActual = LocalDate.now();

        long diasRestantes = ChronoUnit.DAYS.between(fechaActual, fechaVencimiento);

        if (fechaVencimiento.isBefore(fechaActual)) {
            long diasCaducado = ChronoUnit.DAYS.between(fechaVencimiento, fechaActual);
            return "Caducado hace " + diasCaducado + " días";
        } else if (diasRestantes <= 90) { // 90 días es aproximadamente 3 meses
            return "Vigente, quedan " + diasRestantes + " días";
        } else {
            return "Vigente, quedan " + diasRestantes + " días";
        }
    }
}
