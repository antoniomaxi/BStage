package com.example.bstage.activities;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bstage.R;
import com.example.bstage.models.Evento;
import com.example.bstage.models.Usuario;
import com.google.gson.JsonParser;

import org.json.JSONArray;
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
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class IniciarSesionActivity extends AppCompatActivity {

    Button btnRegistar, btnEntrar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText et_correo, et_clave;
    public static Usuario usuario = new Usuario();

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        btnRegistar = (Button) findViewById(R.id.btnRegistrar);
        btnRegistar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(IniciarSesionActivity.this, RegistrarActivity.class);
                startActivity(i);
            }
        });

        //Init view
        et_correo = (EditText) findViewById(R.id.correo_login);
        et_clave = (EditText) findViewById(R.id.clave_login);

        btnEntrar = (Button) findViewById(R.id.entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(et_correo.getText().toString(), et_clave.getText().toString());

            }
        });

    }

    private void loginUser(String correo, String clave) {

        if (TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Correo no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(clave)) {
            Toast.makeText(this, "Clave no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        //make POST request
        new PostDataTask().execute("https://backstage-backend.herokuapp.com/api/signin");
    }

    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(IniciarSesionActivity.this);
            progressDialog.setMessage("Iniciando Sesion...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "error: "+ex;
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("err", "in post execute");

            try{
                Log.e("err", "in try");
                Log.e("err", "result "+result);
                JSONObject jsonObject = new JSONObject(result) ;

                if(jsonObject.getString("message").equals("A")){
                    Log.e("err", "in if of post execute");
                    usuario.set_id(jsonObject.getString("_id"));
                    usuario.setName(jsonObject.getString("Name"));
                    usuario.setToken(jsonObject.getString("token"));
                    usuario.setCorreo(et_correo.getText().toString());
                    usuario.setImagen(jsonObject.getString("Imagen"));
                    usuario.setAdmin(jsonObject.getBoolean("Admin"));

                    Log.e("img", ""+usuario.getImagen());
                    Toast.makeText(IniciarSesionActivity.this, "Bienvenido "+usuario.getName(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(IniciarSesionActivity.this, MainActivity.class);
                    startActivity(i);

                }else{
                    if(jsonObject.getString("message").equals("B")){

                        // Email o Contraseña incorrectos
                        Toast.makeText(IniciarSesionActivity.this, "Email o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        //Usuario no encontrado
                        Toast.makeText(IniciarSesionActivity.this, "Disculpe, debe registrarse primero.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                Log.e("err", "in catch");
                e.printStackTrace();
            }
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
                dataToSend.put("Correo", et_correo.getText().toString());
                dataToSend.put("Clave", et_clave.getText().toString());


                //Initialize and config request, then connect to server.
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(10000 /* milliseconds */);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);  //enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");// set header
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