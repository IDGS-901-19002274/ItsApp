package com.example.myapplication;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClienteRepositorio {
    private final ApiService apiService; //Declara la variable de apiservice

    //Crea el constructor del repositorio, usando de referencia APiSErvice para trabajar
    public ClienteRepositorio(ApiService apiService) {
        this.apiService = apiService;
    }

    //todo: Obtiene los clientes
    public MutableLiveData<List<ClienteModelo>> obtenerClientes() {
        MutableLiveData<List<ClienteModelo>> clientesData = new MutableLiveData<>();

        apiService.obtenerClientes().enqueue(new Callback<RespuestaClientesModelo>() {
            @Override
            public void onResponse(Call<RespuestaClientesModelo> call, Response<RespuestaClientesModelo> response) {
                clientesData.setValue(response.body().getClientes());
            }

            @Override
            public void onFailure(Call<RespuestaClientesModelo> call, Throwable t) {
                Log.e("NOMAMES SIES ERROR", t.getMessage());
            }
        });

        return clientesData;
    }

    //todo: Obtiene un solo cliente
    public MutableLiveData<ClienteModelo> obtenerCliente(int id){
        MutableLiveData<ClienteModelo> clienteData = new MutableLiveData<>();

        apiService.obtenerCliente(id).enqueue(new Callback<RespuestaClienteIndividualModelo>() {
            @Override
            public void onResponse(Call<RespuestaClienteIndividualModelo> call, Response<RespuestaClienteIndividualModelo> response) {
                clienteData.setValue(response.body().cliente.get(0));
            }

            @Override
            public void onFailure(Call<RespuestaClienteIndividualModelo> call, Throwable t) {
                Log.e("NOMAMES SIES ERROR", t.getMessage());
            }
        });

        return clienteData;
    }

    //Le da matarile a un cliente
    public MutableLiveData<Boolean> borrarCliente(int id) {
        MutableLiveData<Boolean> resultadoBorrado = new MutableLiveData<>();

        apiService.borrarCliente(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    resultadoBorrado.setValue(true);
                } else {
                    resultadoBorrado.setValue(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                resultadoBorrado.setValue(false); // Fallo al borrar el cliente
                Log.e("NOMAMES SIES ERROR", t.getMessage());
            }
        });

        return resultadoBorrado;
    }

    //Agrega a un desgraciado
    public MutableLiveData<Void> agregarCliente(ClienteModelo clienteModelo) {
        MutableLiveData<Void> respuestaData = new MutableLiveData<>();

        apiService.agregarCliente(clienteModelo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("SI ES ERROR", response.message());
                if (response.isSuccessful()) {
                    respuestaData.setValue(null); // Indica éxito
                } else {
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SI ES ERROR", t.getMessage());
            }
        });

        return respuestaData;
    }

    public MutableLiveData<Void> actualizarCliente(int id, ClienteModelo clienteModelo) {
        MutableLiveData<Void> respuestaData = new MutableLiveData<>();

        apiService.actualizarCliente(id, clienteModelo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    respuestaData.setValue(null); // Indica éxito
                } else {
                    // Puedes manejar el error aquí si es necesario
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Puedes manejar el fallo aquí si es necesario
            }
        });

        return respuestaData;
    }

    //----------------------------------------------------------------------------------------------
    //Obtiene los estados
    public MutableLiveData<List<String>> obtenerEstados(){
        MutableLiveData<List<String>> estadosData = new MutableLiveData<>();

        apiService.obtenerEstados().enqueue(new Callback<EstadosRespuestaModel>() {
            @Override
            public void onResponse(Call<EstadosRespuestaModel> call, Response<EstadosRespuestaModel> response) {
                estadosData.setValue(response.body().getEstados());
            }

            @Override
            public void onFailure(Call<EstadosRespuestaModel> call, Throwable t) {

            }
        });

        return estadosData;
    }

    public MutableLiveData<List<String>> obtenerMunicipios(String estado){
        MutableLiveData<List<String>> municipiosData = new MutableLiveData<>();

        apiService.obtenerMunicipios(estado).enqueue(new Callback<MunicipiosRespuestaModel>() {
            @Override
            public void onResponse(@NonNull Call<MunicipiosRespuestaModel> call, @NonNull Response<MunicipiosRespuestaModel> response) {
                municipiosData.setValue(response.body().getMunicipios());
            }

            @Override
            public void onFailure(Call<MunicipiosRespuestaModel> call, Throwable t) {

            }
        });

        return municipiosData;
    }
}
