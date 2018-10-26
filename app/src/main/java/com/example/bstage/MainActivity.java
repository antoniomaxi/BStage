package com.example.bstage;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = (TextView) findViewById(R.id.txtvista);

        //Get//
        new GetDataTask().execute("http://backstage-backend.herokuapp.com/api/eventos");
    }


    class GetDataTask extends AsyncTask<String, Void, String> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            StringBuilder resultado = new StringBuilder();
            try {
                //Inicio y config de request

                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);// milisegundoos
                urlConnection.setConnectTimeout(10000);// milisegundoos
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");//header
                urlConnection.connect();
                Log.e("URL", "URL SI");


                //Lectura de la Data

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;


                while ((line = bufferedReader.readLine()) != null) {
                    resultado.append(line).append("\n");
                    System.out.println("while");
                }

            } catch (IOException ex) {
                return ex.getMessage();

            }

            return resultado.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            //Set Data

            mResult.setText(resultado);

            System.out.println("Resultado" + resultado);

            //cancelacion progressDialog

            if (progressDialog != null) {

                progressDialog.dismiss();
            }

        }
    }
}
