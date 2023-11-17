package com.example.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/clientes")
    public Call<RespuestaClientesModelo> obtenerClientes();

    @GET("api/clientes/{id}")
    Call<RespuestaClienteIndividualModelo> obtenerCliente(@Path("id") int id);

    @POST("api/clientes")
    Call<Void> agregarCliente(@Body ClienteModelo clienteModelo);

    @PUT("api/clientes/{id}")
    Call<Void> actualizarCliente(@Path("id") int id, @Body ClienteModelo clienteModelo);

    @DELETE("api/clientes/{id}")
    Call<Void> borrarCliente(@Path("id") int id);

    //----------------------------------------------------------------------------------------------
    @GET("api/services")
    public Call<EstadosRespuestaModel> obtenerEstados();

    @GET("api/services/{estado}")
    public Call<MunicipiosRespuestaModel> obtenerMunicipios(@Path("estado") String estado);

}
