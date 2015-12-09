package com.example.yoo.opendoor;

import android.os.Bundle;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
=======
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
>>>>>>> origin/desarrollo
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
<<<<<<< HEAD

import java.util.ArrayList;
=======
>>>>>>> origin/desarrollo

public class VerListas extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Switch switchgrupo;
    private Switch switchactividad;
    Spinner spinnerDatos;

    String[] datos;
    //Datos
    String dato;
    String selection;
    protected int position;

    //Traer datos
    RequestQueue requestQueueVLG;
    String showURLG= "http://192.168.1.66:8080/OpenDoor/showGrupos.php";
    ArrayList<String> listaGrupo= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterGrp;


    //Traer datos
    RequestQueue requestQueueVLA;
    String showURLA= "http://192.168.1.66:8080/OpenDoor/showActividades.php";
    ArrayList<String> listaActividad= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_listas);

        spinnerDatos = (Spinner) findViewById(R.id.spinnerDatos);
        switchgrupo = (Switch) findViewById(R.id.switchGrp);
        switchactividad = (Switch) findViewById(R.id.switchAct);

        spinnerDatos.setEnabled(false);

        switchgrupo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if (isChecked) {
                    switchactividad.setChecked(false);
                    spinnerDatos.setEnabled(true);
                    ListaGrupos();
                }else{
                    spinnerDatos.setEnabled(false);
                }
            }
        });
        switchactividad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    switchgrupo.setChecked(false);
                    spinnerDatos.setEnabled(true);
                    ListaActividades();
                } else {
                    spinnerDatos.setEnabled(false);
                }
            }
        });
    }

    public  void ListaGrupos(){
        requestQueueVLG = Volley.newRequestQueue(getApplicationContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURLG,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray grupos = response.getJSONArray("grupos");

                    for (int i = 0; i < grupos.length(); i++) {

                        JSONObject grupo= grupos.getJSONObject(i);
                        String materia = grupo.getString("materia");
                        String aula = grupo.getString("aula");

                        listaGrupo.add(materia + "\n" + aula);

                        //listaA[i]=idaula;
                    }
                    dataAdapterGrp.notifyDataSetChanged();

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

        requestQueueVLG.add(jsonObjectRequest);
        dataAdapterGrp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listaGrupo);
        dataAdapterGrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDatos.setAdapter(dataAdapterGrp);
        spinnerDatos.setOnItemSelectedListener(VerListas.this);
    }
    public  void ListaActividades(){
        requestQueueVLA = Volley.newRequestQueue(getApplicationContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURLA,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray actividades = response.getJSONArray("actividad");
                    for (int i = 0; i < actividades.length(); i++) {

                        JSONObject actividad= actividades.getJSONObject(i);
                        String nombreact = actividad.getString("nombre");
                        String aulaact = actividad.getString("aula");
                        listaActividad.add(nombreact + "\n" + aulaact);
                        //listaA[i]=idaula;
                    }
                    dataAdapterAct.notifyDataSetChanged();

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

        requestQueueVLA.add(jsonObjectRequest);
        dataAdapterAct = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listaActividad);
        dataAdapterAct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDatos.setAdapter(dataAdapterAct);
        spinnerDatos.setOnItemSelectedListener(VerListas.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       VerListas.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        datos=selection.split("\n");
        dato=datos[0];
        //Toast.makeText(AlumnoActividad.this, alumno, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void showAlumnos(){
        //spinner aula
        Spinner spinnerAlumAc = (Spinner) findViewById(R.id.spinnerAlAc);
        requestQueueAA = Volley.newRequestQueue(getApplicationContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURL,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray alumnos = response.getJSONArray("alumnos");
                    for (int i = 0; i < alumnos.length(); i++) {

                        JSONObject alumno = alumnos.getJSONObject(i);
                        String nocontrol = alumno.getString("nocontrol");
                        String nombre = alumno.getString("nombre");
                        listaAlumnos.add(nocontrol + "\n" + nombre);
                        //listaA[i]=idaula;
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

        requestQueueAA.add(jsonObjectRequest);
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listaAlumnos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlumAc.setAdapter(dataAdapter);
        spinnerAlumAc.setOnItemSelectedListener(AlumnoActividad.this);
    }
}
