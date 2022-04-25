package com.example.sigit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sigit.master_data.FragViewBuku;
import com.example.sigit.master_data.FragViewTransaksi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout draw;
    FloatingActionButton BtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        draw = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle t = new ActionBarDrawerToggle(this, draw, R.string.open, R.string.close);

        draw.addDrawerListener(t);
        t.syncState();

        Global.activity = MainActivity.this;

        NavigationView navigationview = findViewById(R.id.nav_view);
        navigationview.setItemIconTintList(null);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.mainFragment, new FragViewBuku()).addToBackStack(null).commit();

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.daftarBuku:
                        Global.isTransaction = false;
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.mainFragment, new FragViewBuku()).addToBackStack(null).commit();
                        break;
                    case R.id.transaksi:
                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.mainFragment, new FragViewTransaksi()).addToBackStack(null).commit();
                        break;
                    case R.id.item2:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        Toast.makeText(MainActivity.this, "Anda telah Logout!", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            openDrawer();
            if (item.getItemId() == android.R.id.home) {
                if ((draw) != null && (draw.isDrawerOpen(GravityCompat.START)))
                    closeDrawer();
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    private void closeDrawer() {
        draw.setDrawerListener(null);
        draw.closeDrawers();
    }

    @SuppressWarnings("deprecation")
    private void openDrawer() {
        draw.setDrawerListener(null);
        draw.openDrawer(GravityCompat.START);
    }
}