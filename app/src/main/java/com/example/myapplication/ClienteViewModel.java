package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ClienteViewModel extends ViewModel {
    private ClienteRepositorio clienteRepositorio;
    private MutableLiveData<List<ClienteModelo>> clientesData;
    private MutableLiveData<ClienteModelo> clienteData;
    private MutableLiveData<List<String>> estadosData;
    private MutableLiveData<List<String>> municipiosData;

    public ClienteViewModel(){
        clienteRepositorio = new ClienteRepositorio(ApiServiceGenerator.createService(ApiService.class));
    }

    //FUnciones para acortar el codigo usando el MVVM
    //Obtiene Clientes del repositorio
    public MutableLiveData<List<ClienteModelo>> obtenerClientes(){
        if(clientesData == null){
            clientesData = clienteRepositorio.obtenerClientes();
        }
        return clientesData;
    }

    //Obtiene un cliente del repositorio
    public MutableLiveData<ClienteModelo> obtenerCliente(int id){
        if(clienteData == null) {
            clienteData = clienteRepositorio.obtenerCliente(id);
        }

        return clienteData;
    }

    //Borra a un cliente del repositorio
    public MutableLiveData<Boolean> borrarCliente(int id) {
        MutableLiveData<Boolean> resultado = clienteRepositorio.borrarCliente(id);
        return resultado;
    }

    //Borra un chango del repositorio
    public MutableLiveData<Void> agregarCliente(ClienteModelo clienteModelo) {
        return clienteRepositorio.agregarCliente(clienteModelo);
    }

    public MutableLiveData<Void> actualizarCliente(int id, ClienteModelo clienteModelo) {
        return clienteRepositorio.actualizarCliente(id, clienteModelo);
    }

    //----------------------------------------------------------------------------------------------
    //Obtiene estados
    public MutableLiveData<List<String>> obtenerEstados(){
        if(estadosData == null){
            estadosData = clienteRepositorio.obtenerEstados();
        }
        return estadosData;
    }

    //Obtiene municipios
    public MutableLiveData<List<String>> obtenerMunicipios(String estado) {
        if(municipiosData == null){
            municipiosData = clienteRepositorio.obtenerMunicipios(estado);
        }
        return municipiosData;
    }
}
