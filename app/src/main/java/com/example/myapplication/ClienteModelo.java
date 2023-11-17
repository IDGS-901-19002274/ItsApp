package com.example.myapplication;

public class ClienteModelo {
    public ClienteModelo(int id_cliente, String nombre, String telefono, String email, String estado, String municipio, String colonia, String calle, int cp, double latitud, double longitud) {
        this.id_cliente = id_cliente;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
        this.municipio = municipio;
        this.colonia = colonia;
        this.calle = calle;
        this.cp = cp;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getEstado() {
        return estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCalle() {
        return calle;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public int getCp() { return cp; }

    private int id_cliente;
    private String nombre;
    private String telefono;
    private String email;
    private String estado;
    private String municipio;
    private String colonia;
    private String calle;
    private int cp;
    private double latitud;
    private double longitud;
}
