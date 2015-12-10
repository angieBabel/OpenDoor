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
import java.util.List;

public class actividad extends AppCompatActivity implements AdapterView.OnItemClickListener {
    RequestQueue requestQueueLA;
    //String showURL = "http://192.168.43.64:8080/OpenDoor/showActividades.php";
    String showURL= "http://192.168.1.66:8080/OpenDoor/showGrupos.php";

    ArrayList<String> listaActividad = new ArrayList<String>();
    List listaAct= new ArrayList<String>();
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
        setContentView(R.layout.activity_actividad);
        PD = new ProgressDialog(this);
        PD.setMessage("Loading.....");
        PD.setCancelable(false);

        ReadDataFromDB();
    }
    public void ReadDataFromDB() {
        lista = (ListView) findViewById(R.id.lvlistaactividad);
        //resultado = (TextView)findViewById(R.id.textResult);
        requestQueueLA = Volley.newRequestQueue(getApplicationContext());

        PD.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showURL,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray actividades = response.getJSONArray("actividad");


                    for (int i = 0; i < actividades.length(); i++) {

                        JSONObject actividad= actividades.getJSONObject(i);
                        String nombreact = actividad.getString("nombre");
                        String aulaact = actividad.getString("aula");
                        //idactividad=actividad.getString("idactividad");
                        listaActividad.add(nombreact + "\n" + aulaact);
                        listaAct.add(i,nombreact);

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

        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaActividad);
        lista.setAdapter(ad);
        lista.setOnItemClickListener(actividad.this);

    }
    public void altaActividad(View ver){
        finish();
        Intent i = new Intent(this, altaActividades.class );
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
        Intent intent = new Intent(this, AlumnoActividad.class);
        intent.putExtra(KEY_datos,datos);
        startActivity(intent);

    }

}
