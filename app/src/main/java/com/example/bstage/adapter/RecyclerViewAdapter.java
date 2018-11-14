package com.example.bstage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.activities.EventoActivity;
import com.example.bstage.models.Evento;
import com.example.bstage.R;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Evento> mData;
    private Context mContext;
    RequestOptions option;

    public RecyclerViewAdapter(Context mContext, List<Evento> mData) {

        this.mContext = mContext;
        this.mData = mData;

        //Opcion de request de Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        holder.tv_name.setText(mData.get(position).getName());
        holder.tv_categoria.setText(mData.get(position).getCategoria());
        holder.tv_calificacion.setText(mData.get(position).getCalificacion());
        holder.tv_precio.setText(mData.get(position).getPrecio());

        //Load image de internet y colocarla en ImageView usando Glide
        Glide.with(mContext).load(mData.get(position).getImagen()).apply(option).into(holder.img_ImgEvento);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, EventoActivity.class);
                i.putExtra("evento_id", mData.get(viewHolder.getAdapterPosition()).get_id());
                i.putExtra("evento_name", mData.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("evento_productor", mData.get(viewHolder.getAdapterPosition()).getProductor());
                i.putExtra("evento_fecha", mData.get(viewHolder.getAdapterPosition()).getFecha());
                i.putExtra("evento_lugar", mData.get(viewHolder.getAdapterPosition()).getLugar());
                i.putExtra("evento_calificacion", mData.get(viewHolder.getAdapterPosition()).getCalificacion());
                i.putExtra("evento_descripcion", mData.get(viewHolder.getAdapterPosition()).getDescripcion());
                i.putExtra("evento_precio", mData.get(viewHolder.getAdapterPosition()).getPrecio());
                i.putExtra("evento_categoria", mData.get(viewHolder.getAdapterPosition()).getCategoria());
                i.putExtra("evento_imagen", mData.get(viewHolder.getAdapterPosition()).getImagen());
                i.putExtra("evento_contador", mData.get(viewHolder.getAdapterPosition()).getContador());

                mContext.startActivity(i);

            }
        });

        return viewHolder;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        TextView tv_calificacion;
        TextView tv_categoria;
        TextView tv_precio;
        ImageView img_ImgEvento;
        LinearLayout view_container;

        public MyViewHolder(View itemView){
            super(itemView);

            view_container = itemView.findViewById(R.id.container);
            tv_name = itemView.findViewById(R.id.evento_name);
            tv_precio = itemView.findViewById(R.id.precio);
            tv_categoria = itemView.findViewById(R.id.categoria);
            tv_calificacion = itemView.findViewById(R.id.calificacion);
            img_ImgEvento = itemView.findViewById(R.id.ImgEvento);
        }
    }


}
