package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Declaraciones de los elementos en pantalla
        RecyclerView recyclerList = (RecyclerView) findViewById(R.id.lmanagerList);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdd);

        //Declaraciones de elementos API
        ClienteViewModel vm = new ViewModelProvider(this).get(ClienteViewModel.class);

        //GET de clientes general, muestra la lista
        vm.obtenerClientes().observe(this, clientes -> {
            //Obtiene todos los clientes y crea el adapter para inflar el recyclerview
            ClienteAdapter clienteAdapter = new ClienteAdapter(this, clientes);
            recyclerList.setAdapter(clienteAdapter);
        });

        //Funcion del boton de add
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddClienteActivity.class);
                startActivity(intent);
            }
        });
    }
}