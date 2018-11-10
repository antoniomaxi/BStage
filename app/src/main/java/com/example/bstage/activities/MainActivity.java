package com.example.bstage.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bstage.models.Evento;
import com.example.bstage.adapter.RecyclerViewAdapter;
import com.example.bstage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //PARA LISTAR LOS JSONS
    private final String JSON_URL = "https://backstage-backend.herokuapp.com/api/eventos";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Evento> lstEventos;
    private RecyclerView recyclerView;

    //PARA EL MENU
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MENU
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.Open, R.string.Close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LISTA JSON
        lstEventos = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerviewid);
        jsonrequest();
    }

    //BOTON DE MENU ACTIVADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void jsonrequest() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;

                for(int i=0; i< response.length(); i++){

                    try{
                        jsonObject = response.getJSONObject(i);
                        Evento evento = new Evento();

                        evento.setName(jsonObject.getString("Name"));
                        evento.setProductor(jsonObject.getString("Productor"));
                        evento.setFecha(jsonObject.getString("Fecha"));
                        evento.setLugar(jsonObject.getString("Lugar"));
                        evento.setCalificacion(jsonObject.getString("Calificacion"));
                        evento.setDescripcion(jsonObject.getString("Descripcion"));
                        evento.setPrecio(jsonObject.getString("Precio"));
                        evento.setImagen(jsonObject.getString("Imagen"));

                        lstEventos.add(evento);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(lstEventos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Evento> lstEventos) {

        RecyclerViewAdapter myadapter = new RecyclerViewAdapter(this, lstEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);

    }

}




