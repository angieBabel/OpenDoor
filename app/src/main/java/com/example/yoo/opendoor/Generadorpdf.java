package com.example.yoo.opendoor;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

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
import java.util.ArrayList;

import harmony.java.awt.Color;

public class Generadorpdf extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final static String NOMBRE_DIRECTORIO = "OpenDoor";
    private final static String NOMBRE_DOCUMENTO = "ListaAlumnos.pdf";
    private final static String ETIQUETA_ERROR = "ERROR";


    private Switch switchgrupo;
    private Switch switchactividad;
    Spinner spinnerDatoPDF;

    String[] datos;
    //Datos
    String dato;
    String selection;
    protected int position;

    //Traer datos Grupo
    RequestQueue requestQueueVLG;
    //String showURLG= "http://192.168.43.64:8080/OpenDoor/showGrupos.php";
    String showURLG= "http://192.168.1.66:8080/OpenDoor/showGrupos.php";
    ArrayList<String> listaGrupo= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterGrp;


    //Traer datos Alumnos
    RequestQueue requestQueueVLA;
    //String showURLA= "http://192.168.43.64:8080/OpenDoor/showActividades.php";
    String showURLA= "http://192.168.1.66:8080/OpenDoor/showActividades.php";
    ArrayList<String> listaActividad= new ArrayList<String>();
    ArrayAdapter<String> dataAdapterAct;


    //Json
    String user;
    View la;
    RequestQueue requestQueueLA;
    //String showURL = "http://192.168.43.64:8080/OpenDoor/showAlumnos.php";
    String showURL= "http://192.168.1.66:8080/OpenDoor/showAlumnos.php";
    ArrayList<String> listaAlumnos = new ArrayList<String>();
    ListView lista;

    ProgressDialog PD;
    RequestQueue requestQueuePDF;
    //String showLista = "http://192.168.43.64:8080/OpenDoor/showLista.php";
    String showLista = "http://192.168.1.66:8080/OpenDoor/showLista.php";
    //String showListaG = "http://192.168.43.64:8080/OpenDoor/showListaG.php";
    String showListaG = "http://192.168.1.66:8080/OpenDoor/showListaG.php";
    ArrayList<String> listaAlum = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generadorpdf);


        spinnerDatoPDF = (Spinner) findViewById(R.id.spinnerDatoPFD);
        switchgrupo = (Switch) findViewById(R.id.switchGrupo);
        switchactividad = (Switch) findViewById(R.id.switchActividades);

        spinnerDatoPDF.setEnabled(false);

        switchgrupo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listaGrupo.clear();
                    switchactividad.setChecked(false);
                    spinnerDatoPDF.setEnabled(true);
                    ListaGrupos();
                } else {
                    spinnerDatoPDF.setEnabled(false);
                }
            }
        });
        switchactividad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    listaActividad.clear();
                    switchgrupo.setChecked(false);
                    spinnerDatoPDF.setEnabled(true);
                    ListaActividades();
                } else {
                    spinnerDatoPDF.setEnabled(false);
                }
            }
        });
    }

    public  void ListaGrupos(){
        requestQueueVLG = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURLG,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray grupos = response.getJSONArray("grupos");

                    for (int i = 0; i < grupos.length(); i++) {

                        JSONObject grupo= grupos.getJSONObject(i);
                        String materia = grupo.getString("materia");
                        String aula = grupo.getString("aula");
                        listaGrupo.add(materia + "\t" + aula);
                    }
                    dataAdapterGrp.notifyDataSetChanged();

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

        requestQueueVLG.add(jsonObjectRequest);
        dataAdapterGrp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listaGrupo);
        dataAdapterGrp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDatoPDF.setAdapter(dataAdapterGrp);
        spinnerDatoPDF.setOnItemSelectedListener(Generadorpdf.this);
    }

    public  void ListaActividades(){
        requestQueueVLA = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                showURLA,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)  {
                try {
                    JSONArray actividades = response.getJSONArray("actividad");
                    for (int i = 0; i < actividades.length(); i++) {

                        JSONObject actividad= actividades.getJSONObject(i);
                        String nombreact = actividad.getString("nombre");
                        String aulaact = actividad.getString("aula");
                        listaActividad.add(nombreact + "\t" + aulaact);
                        //listaA[i]=idaula;
                    }
                    dataAdapterAct.notifyDataSetChanged();

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

        requestQueueVLA.add(jsonObjectRequest);
        dataAdapterAct = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listaActividad);
        dataAdapterAct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDatoPDF.setAdapter(dataAdapterAct);
        spinnerDatoPDF.setOnItemSelectedListener(Generadorpdf.this);
    }

    public void CreatePDFActi(){

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

            documento.add(new Paragraph("\n"));

            final PdfPTable tabla = new PdfPTable(2);
            showLista=showLista+"?"+"nombre="+datos[0]+"&aula="+datos[1];// Envio de datos

            requestQueuePDF = Volley.newRequestQueue(getApplicationContext());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,showLista,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray alumnos = response.getJSONArray("alumnos");
                        for (int i = 0; i < alumnos.length(); i++) {

                            JSONObject alumno = alumnos.getJSONObject(i);
                            String nocontrol = alumno.getString("nocontrol");
                            String nombre = alumno.getString("nombre");
                            listaAlum.add(nocontrol + "\n" + nombre);

                            /*tabla.addCell(nocontrol);
                            tabla.addCell(nombre);*/

                        } // for loop ends
                        dataAdapter.notifyDataSetChanged();
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
            requestQueuePDF.add(jsonObjectRequest);
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaAlum);

            showLista = "http://192.168.1.66:8080/OpenDoor/showLista.php";
            // Insertamos una tabla.
            PdfPTable tablita = new PdfPTable(2);
            for (int i = 0; i < 16; i++) {
                tablita.addCell("Celda " + i);

            }

            PdfPTable tablon = new PdfPTable(2);
            for (int i = 0; i < listaAlum.size(); i++) {
                tablon.addCell(listaAlum.get(i));

            }
            documento.add(tablon);
            documento.add(tablita);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        Generadorpdf.this.position = position;
        selection = parent.getItemAtPosition(position).toString();
        datos=selection.split("\t");
        dato=datos[0];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void GuardarPDF(View view){
        if(switchactividad.isChecked()) {
            CreatePDFActi();
        }
        if(switchgrupo.isChecked()) {
            //CreatePDFGrupo();
        }
    }

}
