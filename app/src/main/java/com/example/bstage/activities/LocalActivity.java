package com.example.bstage.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static com.example.bstage.activities.IniciarSesionActivity.usuario;

public class LocalActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button btnCalificar;
    String id;
    String cont;
    float auxCal;
    String calificacion;
    String acumulador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        //Calificando
        ratingBar = (RatingBar) findViewById(R.id.calficar_local);
        btnCalificar = (Button) findViewById(R.id.btn_calificar_local);

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LocalActivity.this, "Stars :"+ (float) ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                auxCal = ratingBar.getRating();
                ratingBar.setRating(0);

                new PutDataTask().execute("https://backstage-backend.herokuapp.com/api/locales/"+id);
            }
        });

        //ocultando el actionBar

        getSupportActionBar().hide();

        //recibiendo la data

        id = getIntent().getExtras().getString("_id");
        String name = getIntent().getExtras().getString("local_name");
        String categoria = getIntent().getExtras().getString("local_categoria");
        calificacion = getIntent().getExtras().getString("local_calificacion");
        String precio = getIntent().getExtras().getString("local_precio");
        String imagen = getIntent().getExtras().getString("local_img");
        String descripcion = getIntent().getExtras().getString("local_descripcion");
        String direccion = getIntent().getExtras().getString("local_direccion");
        cont = getIntent().getExtras().getString("local_contador");
        acumulador = getIntent().getExtras().getString("local_acumulador");

        //inciar vistas

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_local);
        collapsingToolbarLayout.setTitleEnabled(true);


        TextView tv_name = findViewById(R.id.aa_local_name);
        TextView tv_categoria = findViewById(R.id.aa_local_categoria);
        TextView tv_calificacion = findViewById(R.id.aa_local_calificacion);
        TextView tv_precio = findViewById(R.id.aa_local_precio);
        ImageView img = findViewById(R.id.aa_ImgLocal);
        TextView tv_descipcion = findViewById(R.id.aa_local_descripcion);
        TextView tv_direccion = findViewById(R.id.aa_local_direccion);

        //colocando los valores en cada vista

        tv_name.setText(name);
        tv_categoria.setText(categoria);
        tv_calificacion.setText(calificacion);
        tv_precio.setText(precio);
        tv_descipcion.setText(descripcion);
        tv_direccion.setText(direccion);

        collapsingToolbarLayout.setTitle(name);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        //colocando la imagen usando Glide

        Glide.with(this).load(imagen).apply(requestOptions).into(img);

    }

    class PutDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LocalActivity.this);
            progressDialog.setMessage("Calificando...");
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

            Log.e("btn", "acum "+acum);

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
                urlConnection.setRequestProperty("authorization", usuario.getToken());// set header
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
