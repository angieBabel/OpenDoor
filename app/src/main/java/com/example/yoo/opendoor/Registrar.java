package com.example.yoo.opendoor;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {

    EditText nombre,apellido,username,contrasenia,clave;
    Button registrar;
    RequestQueue requestQueue;
    String insertURL= "http://192.168.1.68:8080/OpenDoor/insertUser.php";
    //String insertURL= "http://192.168.78.67:8080/OpenDoor/insertUser.php";
    View vista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //Inicializar campos
        nombre = (EditText)findViewById(R.id.editNombreR);
        apellido = (EditText)findViewById(R.id.editApellidoR);
        username = (EditText)findViewById(R.id.editUserR);
        contrasenia = (EditText)findViewById(R.id.editContraseniaR);
        clave = (EditText)findViewById(R.id.edtclaveR);
        registrar = (Button)findViewById(R.id.btnRegistrar);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //Onclick boton
       registrar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       System.out.println(response.toString());
                       Toast.makeText(Registrar.this, response, Toast.LENGTH_LONG).show();
                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Toast.makeText(Registrar.this,error.toString(),Toast.LENGTH_LONG).show();
                   }
               }) {

                   @Override
                   protected Map<String, String> getParams() throws AuthFailureError {
                       Map<String, String> parameters = new HashMap<String, String>();
                       parameters.put("nombre", nombre.getText().toString());
                       parameters.put("apellido", apellido.getText().toString());
                       parameters.put("username", username.getText().toString());
                       parameters.put("contrasenia", contrasenia.getText().toString());
                       parameters.put("clave",clave.getText().toString());

                       return parameters;
                   }
               };
               RequestQueue requestQueue = Volley.newRequestQueue(Registrar.this);
               requestQueue.add(request);
               log(vista);
           }
       });

    }

    private void log(View v) {
        finish();
        Intent i = new Intent(this, Login.class );
        startActivity(i);
    }


}
