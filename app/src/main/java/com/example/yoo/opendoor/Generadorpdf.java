package com.example.yoo.opendoor;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import harmony.java.awt.Color;

public class Generadorpdf extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generadorpdf);
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

    public void creararch(View v){

        // Creamos el documento.
        Document documento = new Document();
        // Creamos el fichero con el nombre que deseemos.
        try {

            // Creamos el fichero con el nombre que deseemos.
            File f = crearFichero(NOMBRE_DOCUMENTO);

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.
            FileOutputStream ficheroPdf = new FileOutputStream(
                    f.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el píe de página y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "Instituto Tecnológico de Colima"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    user), false);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            // Abrimos el documento.
            documento.open();


            // Añadimos un título con una fuente personalizada.
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 16,
                    Font.BOLD, Color.BLUE);
            documento.add(new Paragraph("Lista de Alumnos", font));

            documento.add(new Paragraph(""));

            final PdfPTable tabla = new PdfPTable(2);
            requestQueueLA = Volley.newRequestQueue(getApplicationContext());


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showURL,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray alumnos = response.getJSONArray("alumnos");

                        for (int i = 0; i < alumnos.length(); i++) {

                            JSONObject alumno = alumnos.getJSONObject(i);
                            String nocontrol = alumno.getString("nocontrol");
                            //Toast.makeText(this, nocontrol, Toast.LENGTH_LONG).show();
                            String nombre = alumno.getString("nombre");
                            String apellido = alumno.getString("apellido");
                            if (i==0){
                                tabla.addCell("No. Control");
                            }
                            if (i==1){
                                tabla.addCell("Nombre");
                            }
                            tabla.addCell(nocontrol);
                            tabla.addCell(nombre+" "+ apellido);

                            listaAlumnos.add(nocontrol + "\n" + nombre);

                        } // for loop ends


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            requestQueueLA.add(jsonObjectRequest);

            // Insertamos una tabla.
            /*PdfPTable tabla = new PdfPTable(2);
            for (int i = 0; i < 15; i++) {
                tabla.addCell("Celda " + i);

            }*/
            documento.add(tabla);

            // Agregar marca de agua
            /*font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                    Color.GRAY);
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    Element.ALIGN_CENTER, new Paragraph(
                            "amatellanes.wordpress.com", font), 297.5f, 421,
                    writer.getPageNumber() % 2 == 1 ? 45 : -45);*/

        } catch (DocumentException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } catch (IOException e) {

            Log.e(ETIQUETA_ERROR, e.getMessage());

        } finally {

            // Cerramos el documento.
            documento.close();
        }
    }

    private File crearFichero(String nombreDocumento) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreDocumento);
        return fichero;
    }

    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }
}
