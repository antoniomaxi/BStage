package com.example.bstage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventoListAdapter extends ArrayAdapter<Evento> {

    ArrayList<Evento> eventos;
    Context context;
    int resource;

    public EventoListAdapter(Context context, int resource, ArrayList<Evento> eventos) {
        super(context, resource, eventos);

        this.eventos = eventos;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null, true);

        }

        Evento evento = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.ImgEvento);
        Picasso.get().load(evento.getImagen()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.name);
        txtName.setText(evento.getName());

        TextView txtCalificacion = (TextView) convertView.findViewById(R.id.calificacion);
        txtCalificacion.setText(evento.getCalificacion());

        TextView txtPrecio = (TextView) convertView.findViewById(R.id.precio);
        txtPrecio.setText(evento.getPrecio());

        TextView txtCategoria = (TextView) convertView.findViewById(R.id.categoria);
        txtCategoria.setText(evento.getCategoria());

        return convertView;

    }
}
