package com.example.bstage.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.renderscript.Float2;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import java.text.DecimalFormat;

public class EventoActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button btnCalificar;
    String id;
    String cont;
    float auxCal, viejo =0;
    String calificacion;
    String acumulador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        ratingBar = (RatingBar) findViewById(R.id.rating_eventos);
        btnCalificar = (Button) findViewById(R.id.btn_calificar);

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventoActivity.this, "Stars: " + (float) ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                auxCal = ratingBar.getRating();
                Log.e("btn", "aux es "+auxCal);
                ratingBar.setRating(0);

                new PutDataTask().execute("https://backstage-backend.herokuapp.com/api/eventos/"+id);
            }
        });

        //hide de default action bar
        getSupportActionBar().hide();

        //Recibe la data
        id = getIntent().getExtras().getString("evento_id");
        String name = getIntent().getExtras().getString("evento_name");
        String productor = getIntent().getExtras().getString("evento_productor");
        String fecha = getIntent().getExtras().getString("evento_fecha");
        String lugar = getIntent().getExtras().getString("evento_lugar");
        calificacion = getIntent().getExtras().getString("evento_calificacion");
        String descripcion = getIntent().getExtras().getString("evento_descripcion");
        String precio = getIntent().getExtras().getString("evento_precio");
        String categoria = getIntent().getExtras().getString("evento_categoria");
        String imagen = getIntent().getExtras().getString("evento_imagen");
        cont = getIntent().getExtras().getString("evento_contador");
        acumulador = getIntent().getExtras().getString("evento_acumulador");

        //ini views
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        TextView tv_name = findViewById(R.id.aa_evento_name);
        TextView tv_categoria = findViewById(R.id.aa_categoria);
        TextView tv_precio = findViewById(R.id.aa_precio);
        TextView tv_calificacion = findViewById(R.id.aa_calificacion);
        TextView tv_descripcion = findViewById(R.id.aa_descripcion);
        ImageView img = findViewById(R.id.aa_ImgEvento);

        //setting values to each view

        tv_name.setText(name);
        tv_categoria.setText(categoria);
        tv_precio.setText(precio);
        tv_calificacion.setText(calificacion);
        tv_descripcion.setText(descripcion);

        collapsingToolbarLayout.setTitle(name);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        //set image using Glide

        Glide.with(this).load(imagen).apply(requestOptions).into(img);

    }

    class PutDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(EventoActivity.this);
            progressDialog.setMessage("Updating data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return putData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            } catch (JSONException ex) {
                return "Data invalid !";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog != null) {
                progressDialog.dismiss();

            }
        }

        private String calificar(){

            float nuevo = auxCal;

            Log.e("btn", "nuevo "+nuevo);

            float acum = Float.valueOf(acumulador);
             acum = acum + nuevo;

            Log.e("btn", "viejo "+calificacion);

            int contNuevo = Integer.valueOf(cont);
            contNuevo++;

            Log.e("btn", "contador "+contNuevo);

            float promedio = acum/contNuevo;

            DecimalFormat formato1 = new DecimalFormat("#.00");

            Log.e("btn", "promedio "+promedio);

            String promedioF = String.valueOf(formato1.format(promedio));

            return promedioF;
        }

        private String contador(){

            int contNuevo = Integer.valueOf(cont);
            contNuevo++;

            return String.valueOf(contNuevo);
        }

        private String acumulador(){

            float nuevo = auxCal;
            float acum = Float.valueOf(acumulador);
            acum = acum + nuevo;

            return String.valueOf(acum);
        }

        private String putData(String urlPath) throws IOException, JSONException {

            BufferedWriter bufferedWriter = null;
            String result = null;

            try {
                //Create data to update
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("Calificacion", calificar());
                dataToSend.put("Contador", contador());
                dataToSend.put("Acumulador", acumulador());

                //Initialize and config request, then connect to server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
                urlConnection.connect();

                //Write data into server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //Check update successful or not
                if (urlConnection.getResponseCode() == 200) {
                    return "Update successfully !";
                } else {
                    return "Update failed !";
                }
            } finally {
                if(bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
        }
    }
}