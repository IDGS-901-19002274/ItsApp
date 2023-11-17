package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AddClienteActivity extends AppCompatActivity {
    //Declaracion de las variables, en este caso InputLayout
    private TextInputLayout txtNombreLayout;
    private TextInputLayout txtTelefonoLayout;
    private TextInputLayout txtCorreoLayout;
    private TextInputLayout txtCPLayout;
    private TextInputLayout txtColoniaLayout;
    private TextInputLayout txtCalleLayout;
    private View dropDownEstadoLayout;
    private View dropDownMunicipioLayout;

    //Declaracion de las variables, ene ste caso TextEdit
    private TextInputEditText txtId;
    private TextInputEditText txtNombre;
    private TextInputEditText txtTelefono;
    private TextInputEditText txtCorreo;
    private TextInputEditText txtFechaCreacion;
    private TextInputEditText txtCP;
    private TextInputEditText txtColonia;
    private TextInputEditText txtCalle;
    private TextInputEditText txtX;
    private TextInputEditText txtY;
    private ProgressBar progressbar;
    private AutoCompleteTextView estadoAutoCompleteTextView;
    private AutoCompleteTextView municipioAutoCompleteTextView;
    //telefono, correo, CP, colonia, calle

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cliente);
        //Obtiene el id del cliente, esto tiene truco, pues esta msima pantalla se usa tanto para
        //crear, como para editar, en caso de ser -1 el valor, se esta informando que se va a crear
        //Una nueva entrada, en otro caso, se pasara el id del cliente a y entonces se hara un get
        //de los datos del cliente
        int idCliente = getIntent().getIntExtra("idCliente", -1);
        TextInputEditText txtFechaCreation = (TextInputEditText) findViewById(R.id.txtFechaCreacion);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtFechaCreation.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE).toString());
        }

        //InputsLayouts
        txtNombreLayout = (TextInputLayout) findViewById(R.id.txtNombreCompletoLayout);
        txtTelefonoLayout = (TextInputLayout) findViewById(R.id.txtTelefonoLayout);
        txtCorreoLayout = (TextInputLayout) findViewById(R.id.txtCorreoLayout);
        txtCPLayout = (TextInputLayout) findViewById(R.id.txtCPLayout);
        txtColoniaLayout = (TextInputLayout) findViewById(R.id.txtColoniaLayout);
        txtCalleLayout = (TextInputLayout) findViewById(R.id.txtCalleLayout);

        //InputTexts
        txtId = (TextInputEditText) findViewById(R.id.txtIdCliente);
        txtNombre = (TextInputEditText) findViewById(R.id.txtNombreCompleto);
        txtTelefono = (TextInputEditText) findViewById(R.id.txtTelefono);
        txtCorreo = (TextInputEditText) findViewById(R.id.txtCorreo);
        txtFechaCreacion = (TextInputEditText) findViewById(R.id.txtFechaCreacion);
        txtCP = (TextInputEditText) findViewById(R.id.txtCP);
        txtColonia = (TextInputEditText) findViewById(R.id.txtColonia);
        txtCalle = (TextInputEditText) findViewById(R.id.txtCalle);
        txtX = (TextInputEditText) findViewById(R.id.txtX);
        txtY = (TextInputEditText) findViewById(R.id.txtY);
        
        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        //19.4264818080295, -99.17029700412691
        txtX.setText("19.4264818080295");
        txtY.setText("-99.17029700412691");

        //Todo: Instancia el mapa en la pantalla
        // Declaramos el FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Reemplaza el contenedor de fragmentos con el fragmento deseado
        fragmentTransaction.replace(R.id.mapFragment, new MapFragment());

        // Realiza el cambio
        fragmentTransaction.commit();

        ClienteViewModel vm = new ViewModelProvider(this).get(ClienteViewModel.class);
        //Todo: Verifica idCLiente para determinar si es un insert o un edit
        if (idCliente > 0) {
            //Settea la visibilidad del Progressbar en visible y desactiva todos los campos como
            //Desactivados en lo que obtiene los datos del cliente y los settea.
            progressbar.setVisibility(ProgressBar.VISIBLE);
            enableOrDisableImportantFields(false);
            //GET de cliente general, agrega los datos del cliente
            vm.obtenerCliente(idCliente).observe(this, this::setValuesWithClientData);
        }
        //Si no es cliente nuevo, quita de la pantalla la bolita de carga :)
        else{
            progressbar.setVisibility(ProgressBar.GONE);
        }

        //------------------------------------------------------------------------------------------
        //Todo: Ajusta los valores de estados, basandose en la info de la api
        //Obtengo el elemento del dropdown de municipios por casos practicos jeje
        municipioAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteMunicipio);
        dropDownMunicipioLayout = findViewById(R.id.dropDownMunicipio);
        List<String> opcionesMunicipios = new ArrayList<>();
        opcionesMunicipios.add("Leon");
        opcionesMunicipios.add("Guanajuato");
        opcionesMunicipios.add("Celaya");
        dropDownMunicipioLayout.setEnabled(false);

        // Crear un adaptador para las opciones
        ArrayAdapter<String> municipiosAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, opcionesMunicipios);
        //Todo: Dropdown de Estados
        //Obtiene las referencias de los estados
        dropDownEstadoLayout = findViewById(R.id.dropDownEstado);
        estadoAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteEstado);
        /*vm.obtenerEstados().observe(this, estados -> {
            // Definir opciones
            List<String> opcionesEstados = null;
            opcionesEstados = estados;

            // Crear un adaptador para las opciones
            ArrayAdapter<String> estadosAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, opcionesEstados);

            // Configurar el adaptador en el menú desplegable
            estadoAutoCompleteTextView.setAdapter(estadosAdapter);

            // Agregar un listener para manejar las selecciones
            estadoAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                // Manejar la selección, por ejemplo:
                String selectedOption = (String) parent.getItemAtPosition(position);
            });
        });*/

        // Definir opciones
        List<String> opcionesEstados = new ArrayList<>();
        opcionesEstados.add("Guanajuato");

        // Crear un adaptador para las opciones
        ArrayAdapter<String> estadosAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, opcionesEstados);

        // Configurar el adaptador en el menú desplegable
        estadoAutoCompleteTextView.setAdapter(estadosAdapter);

        // Agregar un listener para manejar las selecciones
        estadoAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            // Manejar la selección, por ejemplo:
            String selectedOption = (String) parent.getItemAtPosition(position); });

        estadoAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /*vm.obtenerMunicipios(estadoAutoCompleteTextView.getText().toString()).observe(AddClienteActivity.this, municipios -> {
                    municipiosAdapter.clear();
                    municipiosAdapter.addAll(municipios);
                    municipiosAdapter.notifyDataSetChanged();
                    dropDownMunicipioLayout.setEnabled(true);
                    dropDownEstadoLayout.setEnabled(false);
                });*/
                dropDownMunicipioLayout.setEnabled(true);
                municipioAutoCompleteTextView.setEnabled(false);
            }
        });

        //Todo: Dropdown de Municipios

        // Configurar el adaptador en el menú desplegable
        municipioAutoCompleteTextView.setAdapter(municipiosAdapter);

        // Agregar un listener para manejar las selecciones
        municipioAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            // Manejar la selección, por ejemplo:
            String selectedOption = (String) parent.getItemAtPosition(position);
        });

        //------------------------------------------------------------------------------------------
        //LEE LAS VALIDACIONES AL DARLE CLICK EN GUARDAR
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsValidated()){;
                    if (idCliente > 0) {
                        ClienteModelo nuevoCliente = new ClienteModelo(
                                Integer.parseInt(txtId.getText().toString().trim()),
                                txtNombre.getText().toString().trim(),
                                txtTelefono.getText().toString().trim(),
                                txtCorreo.getText().toString().trim(),
                                estadoAutoCompleteTextView.getText().toString().trim(),
                                municipioAutoCompleteTextView.getText().toString().trim(),
                                txtColonia.getText().toString().trim(),
                                txtCalle.getText().toString().trim(),
                                Integer.parseInt(txtCP.getText().toString().trim()),
                                Double.parseDouble(txtX.getText().toString().trim()),
                                Double.parseDouble(txtY.getText().toString().trim())
                        );

                        vm.actualizarCliente(idCliente, nuevoCliente).observe(AddClienteActivity.this, response -> {
                            // Manejar la respuesta después de actualizar el cliente
                            if (response == null) {
                                Toast.makeText(AddClienteActivity.this, "Cliente Actualizado con exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddClienteActivity.this, MainActivity.class);
                                AddClienteActivity.this.startActivity(intent);
                                ((AppCompatActivity) AddClienteActivity.this).finish();
                            }
                        });
                    }

                    else{
                        ClienteModelo nuevoCliente = new ClienteModelo(
                                0,
                                txtNombre.getText().toString().trim(),
                                txtTelefono.getText().toString().trim(),
                                txtCorreo.getText().toString().trim(),
                                estadoAutoCompleteTextView.getText().toString().trim(),
                                municipioAutoCompleteTextView.getText().toString().trim(),
                                txtColonia.getText().toString().trim(),
                                txtCalle.getText().toString().trim(),
                                Integer.parseInt(txtCP.getText().toString().trim()),
                                Double.parseDouble(txtX.getText().toString().trim()),
                                Double.parseDouble(txtY.getText().toString().trim())
                        );

                        vm.agregarCliente(nuevoCliente).observe(AddClienteActivity.this, response -> {
                            if (response == null) {
                                Toast.makeText(AddClienteActivity.this, "Cliente agregado con exito", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddClienteActivity.this, MainActivity.class);
                                AddClienteActivity.this.startActivity(intent);
                                ((AppCompatActivity) AddClienteActivity.this).finish();
                            }
                        });
                    }
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setValuesWithClientData(ClienteModelo cliente){
        //Settea los datos de todos los inputs en caso de tener datos del cliente
        txtId.setText(String.format("%d", cliente.getId_cliente()));
        txtNombre.setText(cliente.getNombre());
        txtTelefono.setText(cliente.getTelefono());
        txtCorreo.setText(cliente.getEmail());
        txtCP.setText(String.format("%d", cliente.getCp()));
        txtColonia.setText(cliente.getColonia());
        txtCalle.setText(cliente.getCalle());
        txtX.setText(String.format("%f", cliente.getLatitud()));
        txtY.setText(String.format("%f", cliente.getLongitud()));
        estadoAutoCompleteTextView.setText(cliente.getEstado());
        municipioAutoCompleteTextView.setText(cliente.getMunicipio());
        enableOrDisableImportantFields(true);
        progressbar.setVisibility(ProgressBar.GONE);
    }
    private void enableOrDisableImportantFields(boolean enable){
        txtNombreLayout.setEnabled(enable);
        txtTelefonoLayout.setEnabled(enable);
        txtCorreoLayout.setEnabled(enable);
        txtCPLayout.setEnabled(enable);
        txtColoniaLayout.setEnabled(enable);
        txtCalleLayout.setEnabled(enable);
    }
    private boolean IsValidated(){
        boolean nombre = ValidateNombre();
        boolean telefono = ValidateTelefono();
        boolean email = ValidateEmail();
        boolean cp = ValidateCP();
        boolean colonia = ValidateColonia();
        boolean calle = ValidateCalle();
        boolean estado = ValidateEstado();
        boolean municipio = ValidateMunicipio();

        return !nombre && !telefono && !email && !cp && !colonia && !calle && !estado && !municipio;
    }

    private boolean ValidateNombre(){
        boolean hasExceptions = false;
        String messages = "";
        if(txtNombre.getText().toString().length() < 17){
            messages += "-Minimo de caracteres es 17\n";
            hasExceptions = true;
        }
        if(txtNombre.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }
        if(txtNombre.getText().toString().matches(".*\\d.*")){
            messages += "-El campo no puede contener números\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtNombreLayout.setError(messages);
        } else {
            txtNombreLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateTelefono(){
        boolean hasExceptions = false;
        String messages = "";

        if(txtTelefono.getText().toString().length() != 10){
            messages += "-La longitud de un número de teléfono es de 10 dígitos\n";
            hasExceptions = true;
        }
        if(txtTelefono.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }
        if (!txtTelefono.getText().toString().matches("\\d+")) {
            messages += "-El campo solo puede contener números\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtTelefonoLayout.setError(messages);
        } else {
            txtTelefonoLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateEmail() {
        boolean hasExceptions = false;
        String messages = "";
        if(txtCorreo.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }

        if(!txtCorreo.getText().toString().contains("@") || !txtCorreo.getText().toString().contains(".com")){
            messages += "-El campo debe ser de tipo Email\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtCorreoLayout.setError(messages);
        } else {
            txtCorreoLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateCP(){
        boolean hasExceptions = false;
        String messages = "";
        if(txtCP.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }
        if(txtCP.getText().toString().length() != 5){
            messages += "-El codigo postal solo consta de 5 numeros\n";
            hasExceptions = true;
        }
        if (!txtCP.getText().toString().matches("\\d+")) {
            messages += "-El campo solo puede contener números\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtCPLayout.setError(messages);
        } else {
            txtCPLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateColonia() {
        boolean hasExceptions = false;
        String messages = "";

        if(txtColonia.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }

        if(txtColonia.getText().toString().length() < 5){
            messages += "-El minimo de caracteres es 5\n";
            hasExceptions = true;
        }

        if (txtColonia.getText().toString().matches("\\d+")) {
            messages += "-El campo no puede contener números\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtColoniaLayout.setError(messages);
        } else {
            txtColoniaLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateCalle() {
        boolean hasExceptions = false;
        String messages = "";

        if(txtCalle.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }

        if(txtCalle.getText().toString().length() < 5){
            messages += "-El minimo de caracteres es 5\n";
            hasExceptions = true;
        }

        if (txtCalle.getText().toString().matches("\\d+")) {
            messages += "-El campo no puede contener números\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            txtCalleLayout.setError(messages);
        } else {
            txtCalleLayout.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateEstado() {
        boolean hasExceptions = false;
        String messages = "";

        if (estadoAutoCompleteTextView.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            estadoAutoCompleteTextView.setError(messages);
        } else {
            estadoAutoCompleteTextView.setError(null);
        }

        return hasExceptions;
    }
    private boolean ValidateMunicipio() {
        boolean hasExceptions = false;
        String messages = "";

        if (municipioAutoCompleteTextView.getText().toString().length() == 0){
            messages += "-El campo no puede estar vacio\n";
            hasExceptions = true;
        }

        if (hasExceptions){
            municipioAutoCompleteTextView.setError(messages);
        } else {
            municipioAutoCompleteTextView.setError(null);
        }

        return hasExceptions;
    }
}