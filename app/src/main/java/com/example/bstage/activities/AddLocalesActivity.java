package com.example.bstage.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddLocalesActivity extends AppCompatActivity {

    Button btnAñadir;
    EditText nombre, direccion, descripcion, precio, categoria, url_imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_locales);

        btnAñadir = findViewById(R.id.btnAddLocal);
        nombre = findViewById(R.id.nombreLocal);
        direccion = findViewById(R.id.direccionLocal);
        descripcion = findViewById(R.id.descripcionLocal);
        precio = findViewById(R.id.precioLocal);
        categoria = findViewById(R.id.categoriaLocal);
        url_imagen = findViewById(R.id.urlImagenLocal);

        btnAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(usuario.getToken()!=null) {

                    //make POST request
                    new PostDataTask().execute("http://backstage-backend.herokuapp.com/api/locales");

                    Intent i = new Intent(AddLocalesActivity.this, LocalMainActivity.class);
                    startActivity(i);

                }
                else{

                    Toast.makeText(AddLocalesActivity.this, "Disculpe, para agregar locales es necesario iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AddLocalesActivity.this);
            progressDialog.setMessage("Añadiendo Local...");
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
                dataToSend.put("Direccion", direccion.getText().toString());
                dataToSend.put("Calificacion", 0);
                dataToSend.put("Descripcion", descripcion.getText().toString());
                dataToSend.put("Precio", precio.getText().toString());
                dataToSend.put("Categoria", categoria.getText().toString());
                dataToSend.put("Imagen", url_imagen.getText().toString());
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
                urlConnection.setRequestProperty("authentication", usuario.getToken());// set header
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
