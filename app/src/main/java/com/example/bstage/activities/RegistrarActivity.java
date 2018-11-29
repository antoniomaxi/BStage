package com.example.bstage.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.bstage.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
import java.util.UUID;

import retrofit2.http.Url;


public class RegistrarActivity extends AppCompatActivity {

    Button btnSignUp;
    EditText nombre, username, email, password;
    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;
    String urlImagen,url2;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        nombre = (EditText) findViewById(R.id.nombre);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        ImgUserPhoto = findViewById(R.id.reqUserPhoto);

        //firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= 22){

                    checkAndRequestForPremission();
                }
                else{
                    openGallery();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();
                Intent i = new Intent(RegistrarActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void uploadImage() {

        final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        uploadTask = ref.putFile(pickedImgUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    urlImagen = downloadUri.toString();
                    Log.i("URL", "if urlImagen "+urlImagen);
                    //make POST request
                    new PostDataTask().execute("http://backstage-backend.herokuapp.com/api/usuarios");
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void openGallery() {

        //TODO: open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPremission() {

        if(ContextCompat.checkSelfPermission(RegistrarActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(RegistrarActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(RegistrarActivity.this, "Por favor acepta los permisos requeridos", Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(RegistrarActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        }
        else{
            openGallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if((resultCode == RESULT_OK) && (requestCode == REQUESCODE) && (data != null)){

            //The user has successfully picked an image

            //We need to save its reference to a Uri variable

            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }

    class PostDataTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(RegistrarActivity.this);
            progressDialog.setMessage("Registrando...");
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
                dataToSend.put("Nombre", nombre.getText().toString());
                dataToSend.put("Username", username.getText().toString());
                dataToSend.put("Correo", email.getText().toString());
                dataToSend.put("Clave", password.getText().toString());
                dataToSend.put("Admin", false);
                Log.i("URL2", "url: "+urlImagen);
                dataToSend.put("Imagen", urlImagen);

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
