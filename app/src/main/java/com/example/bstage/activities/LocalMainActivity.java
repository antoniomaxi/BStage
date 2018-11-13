package com.example.bstage.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.bstage.adapter.LocalViewAdapter;
import com.example.bstage.R;
import com.example.bstage.models.Local;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class LocalMainActivity extends AppCompatActivity {

    //PARA LISTAR LOS JSONS
    private final String JSON_URL = "https://backstage-backend.herokuapp.com/api/locales";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Local> lstLocales;
    private RecyclerView recyclerView;

    //PARA EL MENU
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_local);

        //MENU
        mDrawer = (DrawerLayout) findViewById(R.id.drawerlocal);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.Open, R.string.Close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LISTA JSON
        lstLocales = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerviewlocal);
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
                        Local local = new Local();

                        local.setName(jsonObject.getString("Name"));
                        local.setCategoria(jsonObject.getString("Categoria"));
                        local.setDescripcion(jsonObject.getString("Descripcion"));
                        local.setPrecio(jsonObject.getString("Precio"));
                        local.setImagen(jsonObject.getString("Imagen"));
                        local.setCalificacion(jsonObject.getString("Calificacion"));

                        lstLocales.add(local);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setuprecyclerview(lstLocales);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue = Volley.newRequestQueue(LocalMainActivity.this);
        requestQueue.add(request);
    }

    private void setuprecyclerview(List<Local> lstLocales) {

        LocalViewAdapter myadapter = new LocalViewAdapter(this, lstLocales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);

    }

}



