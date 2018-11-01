package com.example.bstage.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bstage.models.Local;
import com.example.bstage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LocalListAdapter extends ArrayAdapter<Local> {

    ArrayList<Local> locales;
    Context context;
    int resource;

    public LocalListAdapter(Context context, int resource, ArrayList<Local> locales) {
        super(context, resource, locales);

        this.locales = locales;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_locales, null, true);

        }

        Local local = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ImgEvento);
        Picasso.get().load(local.getImagen()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        txtName.setText(local.getName());

        TextView txtCalificacion = (TextView) convertView.findViewById(R.id.calificacion);
        txtCalificacion.setText(local.getCalificacion());

        TextView txtPrecio = (TextView) convertView.findViewById(R.id.precio);
        txtPrecio.setText(local.getPrecio());

        TextView txtCategoria = (TextView) convertView.findViewById(R.id.categoria);
        txtCategoria.setText(local.getCategoria());

        return convertView;

    }
}
