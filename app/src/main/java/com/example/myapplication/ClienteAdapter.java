package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder>{
    private Context context;
    private List<ClienteModelo> listaClientes;

    public ClienteAdapter(Context context, List<ClienteModelo> listaClientes) {
        this.context = context;
        this.listaClientes = listaClientes;
    }

    @NonNull
    @Override
    public ClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cliente_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteAdapter.ViewHolder holder, int position) {
        final ClienteModelo cliente = listaClientes.get(position);
        holder.titulo.setText(cliente.getNombre());
        holder.subtitulo.setText(cliente.getEmail());

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPopupEliminar(cliente);
            }
        });

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddClienteActivity.class);
                intent.putExtra("idCliente", cliente.getId_cliente());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaClientes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titulo;
        public TextView subtitulo;
        public Button btnEliminar;

        public Button btnEditar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.txtTarjetaTitulo);
            subtitulo = (TextView) itemView.findViewById(R.id.txtTarjetaSubtitulo);
            btnEliminar = (Button) itemView.findViewById(R.id.btnEliminar);
            btnEditar = (Button) itemView.findViewById(R.id.btnEditar);
        }
    }

    private void mostrarPopupEliminar(ClienteModelo cliente) {
        // Inflar el diseño del popup
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup, null);

        // Crear un AlertDialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(popupView);

        // Configurar los botones del popup
        Button btnDismiss = popupView.findViewById(R.id.btnDismiss);
        Button btnAceptar = popupView.findViewById(R.id.btnAcceptar);

        // Crear y mostrar el AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        //Funciones de los botones.
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClienteViewModel vm = new ViewModelProvider((AppCompatActivity) context).get(ClienteViewModel.class);
                vm.borrarCliente(cliente.getId_cliente()).observe((AppCompatActivity) context, result ->{
                });
                Toast.makeText((AppCompatActivity) context, "Se ha eliminado con exito el Cliente", Toast.LENGTH_LONG);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((AppCompatActivity) context).finish();
                alertDialog.dismiss();
            }
        });
    }
}
