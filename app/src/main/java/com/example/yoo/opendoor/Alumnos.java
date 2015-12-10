package com.example.yoo.opendoor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.Request.Method;

public class Alumnos extends AppCompatActivity {
    //TextView resultado;

    View la;
    RequestQueue requestQueueLA;
    //String showURL = "http://192.168.43.64:8080/OpenDoor/showAlumnos.php";
    String showURL= "http://192.168.1.66:8080/OpenDoor/showAlumnos.php";

    ArrayList<String> listaAlumnos = new ArrayList<String>();
    ArrayAdapter<String> ad;


    ListView lista;

    //nuevo metodo
    ProgressDialog PD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);
        ReadDataFromDB();
    }

    public void ReadDataFromDB() {
        lista = (ListView) findViewById(R.id.lvlistalumnos);
        requestQueueLA = Volley.newRequestQueue(getApplicationContext());

        PD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,showURL,new Response.Listener<JSONObject>() {

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
                        ad.notifyDataSetChanged();

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
        requestQueueLA.add(jsonObjectRequest);

        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAlumnos);
        lista.setAdapter(ad);

    }


    public void insertaA(View v) {
        finish();
        Intent i = new Intent(this, altaAlumno.class);
        startActivity(i);
    }


}
