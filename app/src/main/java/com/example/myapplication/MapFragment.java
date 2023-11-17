package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla el diseño XML para este fragmento
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Obtiene el SupportMapFragment y recibe una notificación cuando el mapa está listo para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Guarda la referencia al mapa para su uso posterior
        googleMap = map;

        // Añade un marcador en Sydney, Australia, y mueve la cámara.
        LatLng leon = new LatLng(19.4264818080295, -99.17029700412691);
        googleMap.addMarker(new MarkerOptions().position(leon).title("Marca en Leon gto"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(leon));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(leon, 15.0f));
    }
}
