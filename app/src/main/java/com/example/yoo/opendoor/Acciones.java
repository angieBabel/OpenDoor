package com.example.yoo.opendoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Acciones extends AppCompatActivity{
    public static final String KEY_USERNAME="username";
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);

        Intent intent = getIntent();

        Toast.makeText(Acciones.this, "Bienvenido "+intent.getStringExtra(Login.KEY_USERNAME), Toast.LENGTH_LONG).show();
        user=intent.getStringExtra(Login.KEY_USERNAME);


    }



    public void alumnoVista(View ver){
        Intent i = new Intent(this, Alumnos.class );
        i.putExtra(KEY_USERNAME,user);
        startActivity(i);
    }
    public void Reportes(View v2){
        Intent i = new Intent(this, reportes.class );
        i.putExtra(KEY_USERNAME,user);
        startActivity(i);
    }
    public void Grupo(View v2){
        Intent i = new Intent(this, grupos.class );
        i.putExtra(KEY_USERNAME,user);
        startActivity(i);
    }
    public void Actividades(View v2){
        Intent i = new Intent(this,actividad.class );
        i.putExtra(KEY_USERNAME,user);
        startActivity(i);
    }
}
