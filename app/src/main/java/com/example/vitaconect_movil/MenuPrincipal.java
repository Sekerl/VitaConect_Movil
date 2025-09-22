package com.example.vitaconect_movil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vitaconect_movil.databinding.ActivityMenuPrincipalBinding;

public class MenuPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuPrincipalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMenuPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        setSupportActionBar(binding.appBarMenuPrincipal.toolbar);

        // Cargar estilos del Toolbar al iniciar sesión
        setToolbarTitleStyle();

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_gestionproductos, R.id.nav_gestionproductos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.cerrar_sesion) {
                    // Redirigir a la MainActivity y cerrar la sesión
                    Intent intent = new Intent(MenuPrincipal.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpiar el stack
                    startActivity(intent);
                    finish(); // Cierra la actividad actual
                    // Cerrar el menú de navegación si no se ha hecho ninguna selección
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                } else {
                    // Cerrar el menú de navegación si no se ha hecho ninguna selección
                    drawer.closeDrawer(GravityCompat.START);
                    // Para otros ítems, usa el NavController para manejar la navegación
                    return NavigationUI.onNavDestinationSelected(item, navController);
                }
            }
        });

    }



    private void setToolbarTitleStyle() {
        if (getSupportActionBar() != null) {

            for (int i = 0; i < binding.appBarMenuPrincipal.toolbar.getChildCount(); i++) {
                View child = binding.appBarMenuPrincipal.toolbar.getChildAt(i);
                if (child instanceof TextView) {
                    TextView toolbarTitle = (TextView) child;
                    toolbarTitle.setTextSize(24); // Tamaño en sp
                    toolbarTitle.setTypeface(null, Typeface.BOLD); // Negrita

                    // Agregar padding a la izquierda y derecha
                    int paddingLeftInDp = 100;
                    float scale = getResources().getDisplayMetrics().density;
                    int paddingLeftInPixels = (int) (paddingLeftInDp * scale + 0.5f);

                    int paddingRightInDp = 100; // Ajusta este valor según sea necesario
                    int paddingRightInPixels = (int) (paddingRightInDp * scale + 0.5f);

                    toolbarTitle.setPadding(paddingLeftInPixels, 0, paddingRightInPixels, 0); // Padding left and right
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_principal, menu);
        //return true;
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}

