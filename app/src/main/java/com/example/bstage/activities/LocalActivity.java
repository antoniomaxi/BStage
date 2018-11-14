package com.example.bstage.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.R;

public class LocalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        //ocultando el actionBar

        getSupportActionBar().hide();

        //recibiendo la data

        String name = getIntent().getExtras().getString("local_name");
        String categoria = getIntent().getExtras().getString("local_categoria");
        String calificacion = getIntent().getExtras().getString("local_calificacion");
        String precio = getIntent().getExtras().getString("local_precio");
        String imagen = getIntent().getExtras().getString("local_img");
        String descripcion = getIntent().getExtras().getString("local_descripcion");
        String direccion = getIntent().getExtras().getString("local_direccion");

        //inciar vistas

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_local);
        collapsingToolbarLayout.setTitleEnabled(true);


        TextView tv_name = findViewById(R.id.aa_local_name);
        TextView tv_categoria = findViewById(R.id.aa_local_categoria);
        TextView tv_calificacion = findViewById(R.id.aa_local_calificacion);
        TextView tv_precio = findViewById(R.id.aa_local_precio);
        ImageView img = findViewById(R.id.aa_ImgLocal);
        TextView tv_descipcion = findViewById(R.id.aa_local_descripcion);

        //colocando los valores en cada vista

        tv_name.setText(name);
        tv_categoria.setText(categoria);
        tv_calificacion.setText(calificacion);
        tv_precio.setText(precio);
        tv_descipcion.setText(descripcion);

        collapsingToolbarLayout.setTitle(name);

        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
        //colocando la imagen usando Glide

        Glide.with(this).load(imagen).apply(requestOptions).into(img);

    }
}
