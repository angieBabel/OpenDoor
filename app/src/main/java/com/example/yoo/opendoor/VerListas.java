package com.example.yoo.opendoor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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
    String showURLG= "http://192.168.43.64:8080/OpenDoor/showGrupos.php";
    //String showURLG= "http://192.168.1.66:8080/OpenDoor/showGrupos.php";
    ArrayList<String> listaGrupo= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterGrp;


    //Traer datos
    RequestQueue requestQueueVLA;
    String showURLA= "http://192.168.43.64:8080/OpenDoor/showActividades.php";
    //String showURLA= "http://192.168.1.66:8080/OpenDoor/showActividades.php";
    ArrayList<String> listaActividad= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterAct;

    //Leer asistencia
    ProgressDialog PD;
    RequestQueue requestQueueVL;
    String showLista = "http://192.168.43.64:8080/OpenDoor/showLista.php";
    //String showLista = "http://192.168.1.66:8080/OpenDoor/showLista.php";

    String showListaG = "http://192.168.43.64:8080/OpenDoor/showListaG.php";
    //String showListaG = "http://192.168.1.66:8080/OpenDoor/showListaG.php";
    ListView listaAl;
    ArrayList<String> listaAlumnos = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_listas);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        spinnerDatos = (Spinner) findViewById(R.id.spinnerDatos);
        switchgrupo = (Switch) findViewById(R.id.switchGrp);
        switchactividad = (Switch) findViewById(R.id.switchAct);

        spinnerDatos.setEnabled(false);

        switchgrupo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                if (isChecked) {
                    listaGrupo.clear();
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
                    listaActividad.clear();
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

    public void ReadDataActividad() {
        listaAlumnos.clear();
        listaAl = (ListView) findViewById(R.id.listAlum);
        requestQueueVL = Volley.newRequestQueue(getApplicationContext());
        showLista=showLista+"?"+"nombre="+datos[0]+"&aula="+datos[1];

        PD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showLista,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray alumnos = response.getJSONArray("alumnos");
                    for (int i = 0; i < alumnos.length(); i++) {

                        JSONObject alumno = alumnos.getJSONObject(i);
                        String nocontrol = alumno.getString("nocontrol");
                        String nombre = alumno.getString("nombre");

                        listaAlumnos.add(nocontrol + "\n" + nombre);

                    } // for loop ends
                    dataAdapter.notifyDataSetChanged();

                    PD.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });
        requestQueueVL.add(jsonObjectRequest);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAlumnos);
        listaAl.setAdapter(dataAdapter);
        showLista = "http://192.168.43.64:8080/OpenDoor/showLista.php";
        //showLista = "http://192.168.1.66:8080/OpenDoor/showLista.php";
        listaAlumnos.clear();

    }


    public void ReadDataGrupo() {

        listaAl = (ListView) findViewById(R.id.listAlum);
        requestQueueVL = Volley.newRequestQueue(getApplicationContext());
        showListaG=showListaG+"?"+"materia="+datos[0]+"&aula="+datos[1];
        PD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showListaG,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray alumnos = response.getJSONArray("alumnos");
                    for (int i = 0; i < alumnos.length(); i++) {

                        JSONObject alumno = alumnos.getJSONObject(i);
                        String nocontrol = alumno.getString("nocontrol");
                        String nombre = alumno.getString("nombre");

                        listaAlumnos.add(nocontrol + "\n" + nombre);

                    } // for loop ends
                    dataAdapter.notifyDataSetChanged();

                    PD.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });
        requestQueueVL.add(jsonObjectRequest);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAlumnos);
        listaAl.setAdapter(dataAdapter);
        listaAlumnos.clear();
        showListaG = "http://192.168.43.64:8080/OpenDoor/showListaG.php";
        //showListaG = "http://192.168.1.66:8080/OpenDoor/showListaG.php";

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       VerListas.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        datos=selection.split("\n");
        dato=datos[0];
        //Toast.makeText(AlumnoActividad.this, alumno, Toast.LENGTH_LONG).show();
        if(switchactividad.isChecked()) {
            ReadDataActividad();
        }
        if(switchgrupo.isChecked()) {
            ReadDataGrupo();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
