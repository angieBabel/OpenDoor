package com.example.yoo.opendoor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class grupos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String KEY_USERNAME="username";
    String user;

    RequestQueue requestQueueLA;
    String showURL = "http://192.168.1.66:8080/OpenDoor/showGrupos.php";
    //String showURL= "http://192.168.78.67:8080/OpenDoor/showGrupos.php";

    ArrayList<String> listaGrupos = new ArrayList<String>();
    ArrayAdapter<String> ad;


    ListView lista;
    //ID
    public static final String KEY_datos="datos";
    String datos;

    //nuevo metodo
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        Intent intent = getIntent();
        user=intent.getStringExtra(Login.KEY_USERNAME);

        ReadDataFromDB();
    }

    public void ReadDataFromDB() {
        lista = (ListView) findViewById(R.id.lvlistagrupos);
        //resultado = (TextView)findViewById(R.id.textResult);
        requestQueueLA = Volley.newRequestQueue(getApplicationContext());

        PD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showURL,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray grupos = response.getJSONArray("grupos");


                    for (int i = 0; i < grupos.length(); i++) {

                        JSONObject grupo= grupos.getJSONObject(i);
                        String materia = grupo.getString("materia");
                        String aula = grupo.getString("aula");

                        listaGrupos.add(materia + "\n" + aula);

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
        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaGrupos);
        lista.setAdapter(ad);
        lista.setOnItemClickListener(grupos.this);

    }

    public void AltaGrupo(View v){
        Intent i = new Intent(this,altagrupos.class);
        i.putExtra(KEY_USERNAME, user);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        datos = (String) lista.getItemAtPosition(position);
        //Toast.makeText(this,datos,Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Seleccione una opción")
                .setTitle("")
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Eliminar.");

                    }
                })
                .setNegativeButton("Agregar Alumnos", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("Dialogos", "Confirmacion Agregar.");
                        agregar();
                    }
                });
        builder.show();
    }

    public void agregar(){
        finish();
        Intent intent = new Intent(this, AlumnoGrupo.class);
        intent.putExtra(KEY_datos, datos);
        startActivity(intent);

    }
}
