package com.example.yoo.opendoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogin;
    private EditText inputUser,inputPassword;
    public static final String KEY_USERNAME="username";
    public static final String KEY_ID="idusuario";
    public  String username= "username";
    public  String idusuario= "idusuario";

    String LOGIN_URL= "http://192.168.1.68:8080/OpenDoor/login.php";
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUser = (EditText) findViewById(R.id.editUserL);
        inputPassword = (EditText) findViewById(R.id.editContraseniaL);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        userLogin();
    }//Login

    //Logueo
    private void userLogin() {
        username = inputUser.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")){
                            openProfile();
                        }else{
                            Toast.makeText(Login.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("username", inputUser.getText().toString());
                param.put("contrasenia", inputPassword.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);

    }
    //Envio de datos del perfil
    private  void openProfile(){
        finish();
        Intent intent = new Intent(this, Acciones.class);
        intent.putExtra(KEY_USERNAME,username);
        intent.putExtra(KEY_ID,idusuario);
        startActivity(intent);
    }

    //Envio a siguiente pantalla
    public void registrar(View v){
        Intent i = new Intent(this, Registrar.class );
        startActivity(i);
    }
}
