package com.example.bstage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bstage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.bstage.activities.IniciarSesionActivity.usuario;

public class AddEventosActivity extends AppCompatActivity{

    Button btnAñadir;
    EditText nombre, categoria, productor, fecha, lugar, descripcion, precio, url_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eventos);

        btnAñadir = findViewById(R.id.btnAddEvento);
        nombre = findViewById(R.id.nombreEvento);
        categoria = findViewById(R.id.categoriaEvento);
        productor = findViewById(R.id.productorEvento);
        fecha = findViewById(R.id.fechaEvento);
        lugar = findViewById(R.id.lugarEvento);
        descripcion = findViewById(R.id.descripcionEvento);
        precio = findViewById(R.id.precioEvento);
        url_imagen = findViewById(R.id.urlImagenEvento);

        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usuario.getToken()!=null) {

                    //make POST request
                    new PostDataTask().execute("http://backstage-backend.herokuapp.com/api/eventos");

                    Intent i = new Intent(AddEventosActivity.this, MainActivity.class);
                    startActivity(i);
                }

                else{

                    Toast.makeText(AddEventosActivity.this, "Disculpe, para agregar eventos es necesario iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AddEventosActivity.this);
            progressDialog.setMessage("Añadiendo Evento...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String postData(String urlPath) throws IOException, JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {
                //Create data to send to server
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("Name", nombre.getText().toString());
                dataToSend.put("Lugar", lugar.getText().toString());
                dataToSend.put("Fecha", fecha.getText().toString());
                dataToSend.put("Imagen", url_imagen.getText().toString());
                dataToSend.put("Productor", productor.getText().toString());
                dataToSend.put("Descripcion", descripcion.getText().toString());
                dataToSend.put("Precio", precio.getText().toString());
                dataToSend.put("Categoria", categoria.getText().toString());
                dataToSend.put("Calificacion", 0);
                dataToSend.put("Contador", 0);
                dataToSend.put("Acumulador", 0);

                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.setRequestProperty("authorization", usuario.getToken());// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }

            return result.toString();
        }
    }
}
