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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class altagrupos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //DAtos
    public static final String KEY_USERNAME="username";
    String user;
    //Variables
    EditText nombre,horario;
    String aula, semestre;
    Button registrarA;
    RequestQueue requestQueueA;
    String insertURL= "http://192.168.43.64:8080/OpenDoor/insertGrupo.php";
    //String insertURL= "http://192.168.1.66:8080/OpenDoor/insertAlumno.php";
    View v;
    protected ArrayAdapter<CharSequence> adapter;
    private String selection;
    protected int position;

    //Traer datos
    RequestQueue requestQueueAA;
    String showURL= "http://192.168.43.64:8080/OpenDoor/showAula.php";
    //String showURL= "http://192.168.1.66:8080/OpenDoor/showAula.php";
    ArrayList<String> salon= new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    RequestQueue requestQueueAG;

    ArrayAdapter<CharSequence> adapterAula;
    ArrayList<String> listaAula= new ArrayList<String>();
    //ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altagrupos);

        //recibir datos
        Intent intent = getIntent();
        user=intent.getStringExtra(Login.KEY_USERNAME);

        //Inicializar campos
        nombre = (EditText)findViewById(R.id.edtnombregrp);
        horario = (EditText)findViewById(R.id.edtHorario);

        //Obtener instancia del Spinner semestre
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSemestregrp);
        adapter = ArrayAdapter.createFromResource(this, R.array.semestre,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(altagrupos.this);//Seleccion de dato de spinner

        //spinner aula
        showAulas();

    }

    //insertar grupo
    public void insertGrupo(View v2){
        requestQueueA = Volley.newRequestQueue(getApplicationContext());
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
                parameters.put("materia", nombre.getText().toString());
                parameters.put("aula", aula);
                parameters.put("horario", horario.getText().toString());
                parameters.put("aula", semestre);
                parameters.put("username", user);

                return parameters;
            }
        };
        requestQueueA.add(request);
        Listagrupo(v);
    }

    //traer aulas
    public void showAulas(){
        //spinner aula
        Spinner spinnerAula = (Spinner) findViewById(R.id.spinnerAulagrp);
        requestQueueAG = Volley.newRequestQueue(getApplicationContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURL,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray salones = response.getJSONArray("salon");
                    for (int i = 0; i < salones.length(); i++) {
                        JSONObject aulas = salones.getJSONObject(i);
                        String idaula = aulas.getString("idaula");
                        salon.add(idaula);
                    }
                    dataAdapter.notifyDataSetChanged();

                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());

            }
        });

        requestQueueAG.add(jsonObjectRequest);
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,salon);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAula.setAdapter(dataAdapter);
        spinnerAula.setOnItemSelectedListener(altagrupos.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){
            case R.id.spinnerAulagrp:
                altagrupos.this.position = position;
                selection = parent.getItemAtPosition(position).toString();
                aula=selection;
                break;
            case R.id.spinnerSemestregrp:
                altagrupos.this.position = position;
                selection = parent.getItemAtPosition(position).toString();
                semestre=selection;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //lamada al la lista
    public void Listagrupo(View v2){
        Intent i = new Intent(this, grupos.class );
        startActivity(i);
    }
}
