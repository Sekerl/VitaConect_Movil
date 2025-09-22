package com.example.vitaconect_movil.ui.gestionproductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.Producto;
import com.example.vitaconect_movil.ProductoAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.agregar_gestionproductos;
import com.example.vitaconect_movil.modificar_gestionproductos;
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

public class GestionproductosFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productoList;
    private OkHttpClient client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gestionproductos, container, false);

        // Inicializar el cliente de OkHttp
        client = new OkHttpClient();

        // Configura el RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView);
        productoList = new ArrayList<>();
        adapter = new ProductoAdapter(productoList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Llamar al método para obtener productos
        obtenerProductos();

        // Botón agregar
        MaterialButton buttonAdd = root.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), agregar_gestionproductos.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerProductos(); // Recargar los productos al volver al fragmento
    }

    private void obtenerProductos() {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        requireActivity().runOnUiThread(() -> {
                            productoList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Producto producto = new Producto(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("nombre"),
                                            jsonObject.getString("concentracion"),
                                            jsonObject.getString("adicional"),
                                            jsonObject.getString("precio"),
                                            jsonObject.getJSONObject("laboratorio").getString("nombre"),
                                            jsonObject.getJSONObject("tipo").getString("nombre"),
                                            jsonObject.getJSONObject("presentacion").getString("nombre"),
                                            jsonObject.optInt("stock_total", 0) // Obtener el stock total
                                    );
                                    productoList.add(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onEditClick(Producto producto) {
        Intent intent = new Intent(getActivity(), modificar_gestionproductos.class);
        intent.putExtra("id", producto.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Producto producto) {
        eliminarProducto(producto);
    }

    private void eliminarProducto(Producto producto) {
        String url = "https://vcbd.accwebdeveloper.com.mx/api/producto/" + producto.getId();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        productoList.remove(producto);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
/*package com.example.vitaconect_movil.ui.gestionproductos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vitaconect_movil.Producto;
import com.example.vitaconect_movil.ProductoAdapter;
import com.example.vitaconect_movil.R;
import com.example.vitaconect_movil.agregar_gestionproductos;
import com.example.vitaconect_movil.modificar_gestionproductos;
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

public class GestionproductosFragment extends Fragment implements ProductoAdapter.OnProductoClickListener {

    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Producto> productoList;
    private OkHttpClient client;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gestionproductos, container, false);

        // Inicializar el cliente de OkHttp
        client = new OkHttpClient();

        // Ocultar imagen y texto en este fragmento
        ImageView imageView = getActivity().findViewById(R.id.imageView3);
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
        }

        // Configura el RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView);
        productoList = new ArrayList<>();
        adapter = new ProductoAdapter(productoList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // Llamar al método para obtener productos
        obtenerProductos();

        // Botón agregar
        MaterialButton buttonAdd = root.findViewById(R.id.buttonadd);
        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), agregar_gestionproductos.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        obtenerProductos(); // Recargar los productos al volver al fragmento
    }

    private void obtenerProductos() {
        String url = "http://192.168.0.5:80/api/producto";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al cargar los productos", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONArray jsonArray = new JSONArray(responseData);
                        requireActivity().runOnUiThread(() -> {
                            productoList.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Producto producto = new Producto(
                                            jsonObject.getString("id"),
                                            jsonObject.getString("nombre"),
                                            jsonObject.getString("concentracion"),
                                            jsonObject.getString("adicional"),
                                            jsonObject.getString("precio"),
                                            jsonObject.getJSONObject("laboratorio").getString("nombre"),
                                            jsonObject.getJSONObject("tipo").getString("nombre"),
                                            jsonObject.getJSONObject("presentacion").getString("nombre")
                                    );
                                    productoList.add(producto);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onEditClick(Producto producto) {
        Intent intent = new Intent(getActivity(), modificar_gestionproductos.class);
        intent.putExtra("id", producto.getId()); // Pasar el ID del producto a la actividad
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Producto producto) {
        eliminarProducto(producto);
    }

    private void eliminarProducto(Producto producto) {
        String url = "http://192.168.0.5:80/api/producto/" + producto.getId(); // URL de eliminación en la API

        // Crear la solicitud DELETE
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    requireActivity().runOnUiThread(() -> {
                        productoList.remove(producto); // Eliminar producto de la lista local
                        adapter.notifyDataSetChanged(); // Actualizar la vista del RecyclerView
                        Toast.makeText(getContext(), "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "No se pudo eliminar el producto", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
*/



