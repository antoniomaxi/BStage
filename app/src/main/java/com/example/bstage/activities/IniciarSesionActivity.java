package com.example.bstage.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bstage.R;
import com.example.bstage.Retrofit.IMyService;
import com.example.bstage.Retrofit.RetrofitClient;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class IniciarSesionActivity extends AppCompatActivity{

    Button btnRegistar, btnEntrar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    EditText et_correo, et_clave;

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
        btnRegistar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Intent i = new Intent(IniciarSesionActivity.this, RegistrarActivity.class);
                startActivity(i);
            }
        });

        //Init Service
        Retrofit retrofitClient = RetrofitClient.getInstances();
        iMyService = retrofitClient.create(IMyService.class);

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

        if(TextUtils.isEmpty(correo)){
            Toast.makeText(this, "Correo no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(clave)){
            Toast.makeText(this, "Clave no puede estar vacio", Toast.LENGTH_LONG).show();
            return;
        }

        compositeDisposable.add(iMyService.loginUser(correo, clave)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String response) throws Exception {
                Toast.makeText(IniciarSesionActivity.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
