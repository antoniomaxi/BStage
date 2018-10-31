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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

public class MainLocal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    ArrayList<Local> arrayList;
    ListView localv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_local);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlocal);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.Open, R.string.Close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv_local);
        nvDrawer.setNavigationItemSelectedListener(this);

        //Inicio Logica onCreate

        setContentView(R.layout.activity_main_local);
        arrayList = new ArrayList<>();
        localv = (ListView) findViewById(R.id.listLocal);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("https://backstage-backend.herokuapp.com/api/locales");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if(mToggle.onOptionsItemSelected(menuItem)){
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //Inicio logica traer JSON

    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {

                JSONArray locales =  new JSONArray(content);

                for(int i =0;i<locales.length(); i++){

                    JSONObject l = locales.getJSONObject(i);

                    arrayList.add(new Local(
                            l.getString("Name"),
                            l.getString("Direccion"),
                            l.getString("Calificacion"),
                            l.getString("Descripcion"),
                            l.getString("Precio"),
                            l.getString("Categoria"),
                            l.getString("Imagen")
                    ));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LocalListAdapter adapter = new LocalListAdapter(
                    getApplicationContext(), R.layout.list_locales, arrayList
            );
            localv.setAdapter(adapter);
        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    //RENDERIZAR VISTAS DEL MENU
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.eventos:
                Intent m = new Intent(MainLocal.this, MainActivity.class);
                startActivity(m);
                break;
            case R.id.locales:
                Intent a = new Intent(MainLocal.this, MainLocal.class);
                startActivity(a);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}



