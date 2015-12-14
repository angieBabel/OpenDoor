package com.example.yoo.opendoor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

public class AlumnoGrupo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Vectores
    String[] datos,alumnos;
    //Datos
    String dato;
    String selection,alumno,nombreactividad,salonactividad;
    protected int position;
    TextView nombre;

    //Traer datos
    RequestQueue requestQueueAA;
    String showURL= "http://192.168.43.64:8080/OpenDoor/showAlumnos.php";
    //String showURL= "http://192.168.1.66:8080/OpenDoor/showAlumnos.php";

    ArrayList<String> listaAlumnos= new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    String[] listaA;

    //Inserta
    RequestQueue requestQueueAlmAct;
    String insertURL= "http://192.168.43.64:8080/OpenDoor/insertAlumnoGrupo.php";
    //String insertURL= "http://192.168.1.66:8080/OpenDoor/insertAlumno.php";
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumno_grupo);

        nombre= (TextView)findViewById(R.id.textGrupo);

        Intent intent = getIntent();

        //Toast.makeText(AlumnoActividad.this, "Traes " + intent.getStringExtra(actividad.KEY_datos), Toast.LENGTH_LONG).show();
        dato=intent.getStringExtra(actividad.KEY_datos);
        datos=dato.split("\n");
        nombre.setText(datos[0]);
        nombreactividad=datos[0];
        salonactividad=datos[1];
        requestQueueAlmAct = Volley.newRequestQueue(getApplicationContext());

        showAlumnos();
    }

    public void AlumGrup(View v){
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
                parameters.put("nocontrol", alumno);
                parameters.put("nombregrupo",nombreactividad);
                parameters.put("salongrupo", salonactividad);

                return parameters;
            }
        };
        /*RequestQueue requestQueue = Volley.newRequestQueue(AlumnoActividad.this);
        requestQueue.add(request);*/
        requestQueueAlmAct.add(request);
        listaActividades(v);
        //listaAlumno(v);

    }

    public void showAlumnos(){
        listaAlumnos.clear();
        //spinner aula
        Spinner spinnerAlumAc = (Spinner) findViewById(R.id.spinnerAlGp);
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
        spinnerAlumAc.setOnItemSelectedListener(AlumnoGrupo.this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        AlumnoGrupo.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        alumnos=selection.split("\n");
        alumno=alumnos[0];
        //Toast.makeText(AlumnoActividad.this, alumno, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void listaActividades(View vis) {
        finish();
        Intent i = new Intent(this, actividad.class );
        startActivity(i);
    }
}
