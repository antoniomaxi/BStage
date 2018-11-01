package com.example.bstage.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.R;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //hide de default action bar
        getSupportActionBar().hide();

        //Recibe la data
        String name = getIntent().getExtras().getString("evento_name");
        String productor = getIntent().getExtras().getString("evento_productor");
        String fecha = getIntent().getExtras().getString("evento_fecha");
        String lugar = getIntent().getExtras().getString("evento_lugar");
        String calificacion = getIntent().getExtras().getString("evento_calificacion");
        String descripcion = getIntent().getExtras().getString("evento_descripcion");
        String precio = getIntent().getExtras().getString("evento_precio");
        String categoria = getIntent().getExtras().getString("evento_categoria");
        String imagen = getIntent().getExtras().getString("evento_imagen");

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
}
