package com.example.bstage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //private TextView mResult; //VIEJO
    private String  TAG = MainActivity.class.getSimpleName(); // NUEVA
    private ListView Lista; //Lista = lv
    ArrayList<HashMap<String, String>> listaEventos; //listaEventos = contactList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mResult = (TextView) findViewById(R.id.txtvista);// VIEJO

        listaEventos = new ArrayList<>();// NUEVO
        Lista = (ListView) findViewById(R.id.list);


        //Get//
        //new GetDataTask().execute("http://backstage-backend.herokuapp.com/api/eventos");
        new GetDataTask().execute();
    }


    //class GetDataTask extends AsyncTask<String, Void, String> {
    class GetDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"Cargando...",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String url = "http://backstage-backend.herokuapp.com/api/eventos";
            String jsonStr = sh.makeServiceCall(url);


            Log.e("json", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {

                    JSONArray eventos =new JSONArray(jsonStr);


                    // looping through All Contacts
                    for (int i = 0; i < eventos.length(); i++) {
                        JSONObject e = eventos.getJSONObject(i);
                        String name = e.getString("Name");
                        String productor = e.getString("Productor");
                        String fecha = e.getString("Fecha");
                        String lugar = e.getString("Lugar");
                        String calificacion = e.getString("Calificacion");
                        String descripcion = e.getString("Descripcion");
                        String precio = e.getString("Precio");
                        String categoria = e.getString("Categoria");


                        // tmp hash map for single contact
                        HashMap<String, String> evento = new HashMap<>();

                        // adding each child node to HashMap key => value
                        evento.put("Name", name);
                        evento.put("Productor", productor);
                        evento.put("Fecha", fecha);
                        evento.put("Lugar", lugar);
                        evento.put("Calificacion", calificacion);
                        evento.put("Descripcion", descripcion);
                        evento.put("Precio", precio);
                        evento.put("Categoria", categoria);

                        // adding contact to contact list
                        listaEventos.add(evento);
                    }
                } catch (final JSONException ex) {
                    Log.e(TAG, "Json parsing error: " + ex.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + ex.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, listaEventos,
                    R.layout.list_item, new String[]{ "Name","Precio","Fecha","Lugar"},
                    new int[]{R.id.name, R.id.precio, R.id.fecha, R.id.lugar});
            Lista.setAdapter(adapter);
        }
        }
    }

