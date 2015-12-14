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

public class altaActividades extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText nombreAct;
    String aulaAct=" ";
    Button registrarAct;
    RequestQueue requestQueueA;
    //String insertURL= "http://192.168.43.64:8080/OpenDoor/insertActividad.php";
    String insertURL= "http://192.168.1.66:8080/OpenDoor/insertActividad.php";
    View v;

    //Traer datos
    RequestQueue requestQueueAA;
    String showURL= "http://192.168.43.64:8080/OpenDoor/showAula.php";
    //String showURL= "http://192.168.1.66:8080/OpenDoor/showAula.php";
    ArrayList<String> salon= new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    String[] listaA;

    //Spinner

    //protected ArrayAdapter<CharSequence> adapter;
    protected int position;
    private String selection;
    int tamnio=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_actividades);

        //Inicializar campos

        nombreAct = (EditText)findViewById(R.id.edtnombreact);
        registrarAct = (Button)findViewById(R.id.btnactividad);
        requestQueueA = Volley.newRequestQueue(getApplicationContext());

        //Inicializar Spinner
        showAulas();

        //Onclick boton
        registrarAct.setOnClickListener(new View.OnClickListener() {
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
                        parameters.put("nombre", nombreAct.getText().toString());
                        parameters.put("aula", aulaAct);

                        return parameters;
                    }
                };
                requestQueueA.add(request);
                listaActividades(v);
            }
        });
    }

    public void showAulas(){
        //spinner aula
        Spinner spinnerAulaAct = (Spinner) findViewById(R.id.spinneraulaAct);
        requestQueueAA = Volley.newRequestQueue(getApplicationContext());
        //listaA(la);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURL,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray salones = response.getJSONArray("salon");
                    tamnio=salones.length();
                    listaA= new  String[salones.length()];
                    for (int i = 0; i < salones.length(); i++) {
                        JSONObject aulas = salones.getJSONObject(i);
                        String idaula = aulas.getString("idaula");
                        String nombre = aulas.getString("nombre");
                        salon.add(idaula);
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
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,salon);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAulaAct.setAdapter(dataAdapter);
        spinnerAulaAct.setOnItemSelectedListener(altaActividades.this);
    }

    private void listaActividades(View vis) {
        finish();
        Intent i = new Intent(this, actividad.class );
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        altaActividades.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        aulaAct=selection;
        //Toast.makeText(this, "Selección actual: " + selection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
