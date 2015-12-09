package com.example.yoo.opendoor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerListas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_listas);
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
