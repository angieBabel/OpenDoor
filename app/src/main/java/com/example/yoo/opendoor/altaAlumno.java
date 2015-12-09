package com.example.yoo.opendoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class altaAlumno extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText nocontrol,nombreA,apellidoA;
    String semestre;
    Button registrarA;
    protected ArrayAdapter<CharSequence> adapter;
    RequestQueue requestQueueA;
    String insertURL= "http://192.168.1.68:8080/OpenDoor/insertAlumno.php";
    //String insertURL= "http://192.168.78.67:8080/OpenDoor/insertAlumno.php";
    View v;
    protected int position;
    private String selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_alumno);

        //Inicializar campos
        nocontrol = (EditText)findViewById(R.id.editNocontrol);
        nombreA = (EditText)findViewById(R.id.editNombreA);
        apellidoA = (EditText)findViewById(R.id.editapellidoA);
        registrarA = (Button)findViewById(R.id.btnguardarA);
        requestQueueA = Volley.newRequestQueue(getApplicationContext());

        //Spinner semestre
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSemAl);
        adapter = ArrayAdapter.createFromResource(this, R.array.semestre,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(altaAlumno.this);

        //Onclick boton
        registrarA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("nocontrol", nocontrol.getText().toString());
                        parameters.put("nombre", nombreA.getText().toString());
                        parameters.put("apellido", apellidoA.getText().toString());
                        parameters.put("semestre", semestre);

                        return parameters;
                    }
                };
                requestQueueA.add(request);
                listaAlumno(v);
            }
        });

    }
    private void listaAlumno(View vis) {
        finish();
        Intent i = new Intent(this, Alumnos.class );
        startActivity(i);
    }


    public void limpia() {
        nombreA.setText("");
        apellidoA.setText("");
        nocontrol.setText("");
        //semestre.setText("");
        listaAlumno(v);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Salvar la posición y valor del item actual
        altaAlumno.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        semestre=selection;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
