package com.example.yoo.opendoor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class reportes extends AppCompatActivity {
    String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes);
        Intent intent = getIntent();
        user=intent.getStringExtra(Login.KEY_USERNAME);


    }

    public void Listas(View ver){
        Intent i = new Intent(this, VerListas.class );

        startActivity(i);
    }

    public void ArchivosPDF
            (View ver){
        Intent i = new Intent(this, Generadorpdf.class );

        startActivity(i);
    }

}
