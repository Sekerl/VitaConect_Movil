package com.example.vitaconect_movil.ui.gestiontiproductos;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vitaconect_movil.Tiproducto;
import com.example.vitaconect_movil.TiproductoAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.agregar_gestiontiproductos;
import com.example.vitaconect_movil.modificar_gestiontiproductos;
import com.google.android.material.button.MaterialButton;
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

public class GestiontiproductosFragment extends Fragment {
    private RecyclerView recyclerView;
    private TiproductoAdapter adapter;
    private List<Tiproducto> tipoProductoList = new ArrayList<>();
    private OkHttpClient client = new OkHttpClient();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gestiontiproductos, container, false);

        // Inicializa el RecyclerView y el adaptador
        recyclerView = root.findViewById(R.id.recyclerViewTiproductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TiproductoAdapter(tipoProductoList, new TiproductoAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Tiproducto tipoProducto) {
                // Redirige a la actividad de edición con el ID y nombre del producto
                Intent intent = new Intent(getActivity(), modificar_gestiontiproductos.class);
                intent.putExtra("id", tipoProducto.getId());
                intent.putExtra("nombre", tipoProducto.getNombre());
                startActivityForResult(intent, 1); // Usar startActivityForResult
            }

            @Override
            public void onDeleteClick(Tiproducto tipoProducto) {
                eliminarTipoProducto(tipoProducto.getId());
            }
        });

        recyclerView.setAdapter(adapter);

        // Configura el botón "Agregar" y su listener
        MaterialButton buttonAdd = root.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), agregar_gestiontiproductos.class);
            startActivityForResult(intent, 1); // Usar startActivityForResult
        });

        // Cargar la lista de productos
        obtenerTiposProductos();

        return root;
    }

    // Método para obtener la lista de tipos de productos desde la API
    private void obtenerTiposProductos() {
        Request request = new Request.Builder()
                .url("https://vcbd.accwebdeveloper.com.mx/api/tiproducto")  // Cambia a la URL de tu servidor
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Tiproducto>>() {}.getType();
                    List<Tiproducto> productos = gson.fromJson(json, listType);
                    tipoProductoList.clear();
                    tipoProductoList.addAll(productos);

                    // Actualizar el adaptador en el hilo principal
                    requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                }
            }
        });
    }

    // Método para eliminar un tipo de producto
    private void eliminarTipoProducto(int id) {
        Request request = new Request.Builder()
                .url("https://vcbd.accwebdeveloper.com.mx/api/tiproducto/" + id)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Producto eliminado", Toast.LENGTH_SHORT).show();
                        obtenerTiposProductos(); // Recargar la lista
                    });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Verifica el código de solicitud
            if (resultCode == AppCompatActivity.RESULT_OK) {
                obtenerTiposProductos(); // Recarga la lista
            }
        }
    }
}
