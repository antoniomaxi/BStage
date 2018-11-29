package com.example.bstage.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.adapter.LocalViewAdapter;
import com.example.bstage.R;
import com.example.bstage.models.Local;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static com.example.bstage.activities.IniciarSesionActivity.usuario;

public class LocalMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FloatingActionButton btnAñadirLocal;

    //PARA LISTAR LOS JSONS
    private final String JSON_URL = "https://backstage-backend.herokuapp.com/api/locales";
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private List<Local> lstLocales, notFilter;
    private RecyclerView recyclerView;
    private LocalViewAdapter myadapter;

    //PARA EL MENU
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_local);
        hideItem();

        //MENU
        mDrawer = (DrawerLayout) findViewById(R.id.drawerlocal);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.Open, R.string.Close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Añadir evento
        btnAñadirLocal = findViewById(R.id.btnAñadirLocales);
        btnAñadirLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LocalMainActivity.this, AddLocalesActivity.class);
                startActivity(i);
            }
        });

        //LISTA JSON
        lstLocales = new ArrayList<>();
        notFilter = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewlocal);
        myadapter = new LocalViewAdapter(this, lstLocales);
        jsonrequest();

        //Lineas de salvacion del prepa Quevedo
        NavigationView navigationView = findViewById(R.id.drawermenu);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
    }

    //BOTON DE MENU ACTIVADO
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideItem(){
        Log.e("err", "hideItem");
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawermenu);
        Menu nav_menu = navigationView.getMenu();
        View headerView = navigationView.getHeaderView(0);
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.btnAñadirLocales);

        if(usuario.getToken()!=null){
            nav_menu.findItem(R.id.iniciarsesion).setVisible(false);
            nav_menu.findItem(R.id.cerrarsesion).setVisible(true);
            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.img).error(R.drawable.img);
            ImageView fotoperfil = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.fotoperfil);
            TextView nombre = (TextView) headerView.findViewById(R.id.nombreHeader);
            TextView correo = (TextView)headerView.findViewById(R.id.correoHeader);
            if(usuario.getAdmin()){
                btn.show();
                Log.e("if", "admin "+usuario.getAdmin());
            }

            if(fotoperfil != null) {
                Glide.with(this).load(usuario.getImagen()).apply(options.circleCropTransform()).into(fotoperfil);
                nombre.setText(usuario.getName());
                correo.setText(usuario.getCorreo());
            } else {
                Log.e("IMAGEN", "NULL");
            }
        }
        else{
            nav_menu.findItem(R.id.iniciarsesion).setVisible(true);
            nav_menu.findItem(R.id.cerrarsesion).setVisible(false);

        }
    }

    //Manejando las acciones del menu (Renderizacion)
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.eventos){

            Intent i = new Intent(LocalMainActivity.this, MainActivity.class);
            startActivity(i);
        }
        else if (id == R.id.locales){
            //No hacer nada!
        }
        else if(id == R.id.iniciarsesion){

            Intent i = new Intent(LocalMainActivity.this, IniciarSesionActivity.class);
            startActivity(i);
        }else if(id == R.id.cerrarsesion){
            usuario.set_id(null);
            usuario.setCorreo(null);
            usuario.setName(null);
            usuario.setToken(null);
            usuario.setImagen(null);
            usuario.setAdmin(null);
            Intent i = new Intent(LocalMainActivity.this, LocalMainActivity.class);
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerlocal);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

                        local.set_id(jsonObject.getString("_id"));
                        local.setName(jsonObject.getString("Name"));
                        local.setCategoria(jsonObject.getString("Categoria"));
                        local.setDescripcion(jsonObject.getString("Descripcion"));
                        local.setPrecio(jsonObject.getString("Precio"));
                        local.setImagen(jsonObject.getString("Imagen"));
                        local.setCalificacion(jsonObject.getString("Calificacion"));
                        local.setDireccion(jsonObject.getString("Direccion"));
                        local.setContador(jsonObject.getString("Contador"));
                        local.setAcumulador(jsonObject.getString("Acumulador"));

                        lstLocales.add(local);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                notFilter.addAll(lstLocales);
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

        myadapter = new LocalViewAdapter(this, lstLocales);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(myadapter);

    }

    //Busqueda
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawermenu, menu);

        MenuItem search = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }

    public void search(SearchView searchView){

        //Listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if(s == null || s.equals("")) {
                    lstLocales.clear();
                    lstLocales.addAll(notFilter);
                    myadapter.notifyDataSetChanged();
                } else {
                    Log.e("change", s);
                    //filter recycler view when text is changed
                    ArrayList<Local> e = new ArrayList<>();
                    for (Local lo: lstLocales) {
                        if(lo.getName().toLowerCase().contains(s) || lo.getCategoria().toLowerCase().contains(s) || lo.getPrecio().contains(s)) {
                            e.add(lo);
                            Log.e("For", lo.getName());
                        }
                    }
                    lstLocales.clear();
                    lstLocales.addAll(e);
                    myadapter.notifyDataSetChanged();
                }

                return true;
            }
        });
    }

}



