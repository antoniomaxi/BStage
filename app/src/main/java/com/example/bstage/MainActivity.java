package com.example.bstage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String  TAG = MainActivity.class.getSimpleName();
    private ListView Lista;
    ArrayList<HashMap<String, String>> listaEventos;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ImageView fotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv_main);
        nvDrawer.setNavigationItemSelectedListener(this);

        listaEventos = new ArrayList<>();
        Lista = (ListView) findViewById(R.id.list);
        fotoImageView = (ImageView) findViewById(R.id.thumbnail);

        new GetDataTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
                        String urlImagen = e.getString("Imagen");

                        getImage(urlImagen);
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
                    R.layout.list_item, new String[]{ "Name","Precio","Categoria","Calificacion"},
                    new int[]{R.id.name, R.id.precio, R.id.categoria, R.id.calificacion, });
            Lista.setAdapter(adapter);
        }
        }

        public boolean onNavigationItemSelected(MenuItem item) {

            int id= item.getItemId();

            if(id==R.id.nv_main){
                Toast.makeText(this, "Esto es main", Toast.LENGTH_SHORT).show();
            }
            if(id==R.id.nv_miseventos){
                Toast.makeText(this, "Esto es mis eventos", Toast.LENGTH_SHORT).show();
            }
            if(id==R.id.nv_buscar){
                Toast.makeText(this, "Esto es buscar", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        public void getImage(String url){

            Glide.with(this)
                    .load(url)
                    .into(fotoImageView);
        }
    }

