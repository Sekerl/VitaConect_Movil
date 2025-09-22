package com.example.vitaconect_movil.ui.gestionproveedores;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.Proveedor;
import com.example.vitaconect_movil.ProveedorAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.agregar_gestionproveedores;
import com.example.vitaconect_movil.databinding.FragmentGestionproveedoresBinding;
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

public class GestionproveedoresFragment extends Fragment {

    private FragmentGestionproveedoresBinding binding;
    private RecyclerView recyclerView;
    private ProveedorAdapter adapter;
    private List<Proveedor> proveedorList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Ocultar la ImageView y TextView si están presentes
        ImageView imageView = getActivity().findViewById(R.id.imageView3);
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }



        binding = FragmentGestionproveedoresBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configura el Intent para el botón Agregar
        MaterialButton buttonAdd = view.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), agregar_gestionproveedores.class);
            startActivity(intent);
        });

        // Configura el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewProveedores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProveedorAdapter(proveedorList);
        recyclerView.setAdapter(adapter);

        // Cargar proveedores inicialmente
        cargarProveedores();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarProveedores(); // Recargar los proveedores cada vez que el fragmento esté visible
    }

    private void cargarProveedores() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://vcbd.accwebdeveloper.com.mx/api/proveedor")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());

                        // Limpiar la lista actual para evitar duplicados
                        proveedorList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Proveedor proveedor = new Proveedor(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("nombre"),
                                    jsonObject.getString("telefono"),
                                    jsonObject.getString("correo"),
                                    jsonObject.getString("direccion")
                            );
                            proveedorList.add(proveedor);
                        }

                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
