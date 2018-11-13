package com.example.bstage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.R;
import com.example.bstage.models.Local;

import java.util.List;

public class LocalViewAdapter  extends  RecyclerView.Adapter<LocalViewAdapter.MyViewHolder>{

    private Context mContext;
    private List<Local> mData;
    RequestOptions option;

    public LocalViewAdapter(Context mContext, List<Local> mData) {
        this.mContext = mContext;
        this.mData = mData;

        //Pedir option para Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_locales, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_categoria.setText(mData.get(position).getCategoria());
        holder.tv_calificacion.setText(mData.get(position).getCalificacion());
        holder.tv_precio.setText(mData.get(position).getPrecio());

        //Cargar imagen de internet y colocarla en el ImageView usando Glide

        Glide.with(mContext).load(mData.get(position).getImagen()).apply(option).into(holder.img_ImgLocal);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        TextView tv_categoria;
        TextView tv_calificacion;
        TextView tv_precio;
        ImageView img_ImgLocal;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.local_name);
            tv_categoria = itemView.findViewById(R.id.local_categoria);
            tv_calificacion = itemView.findViewById(R.id.local_calificacion);
            tv_precio = itemView.findViewById(R.id.local_precio);
            img_ImgLocal = itemView.findViewById(R.id.ImgLocal);
        }
    }
}
