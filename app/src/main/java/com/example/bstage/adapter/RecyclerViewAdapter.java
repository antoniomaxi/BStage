package com.example.bstage.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bstage.activities.EventoActivity;
import com.example.bstage.models.Evento;
import com.example.bstage.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    private List<Evento> mData, filterList;
    private Context mContext;
    RequestOptions option;

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

    public RecyclerViewAdapter(Context mContext, List<Evento> mData) {

        this.mContext = mContext;
        this.mData = mData;
        this.filterList = mData;

        //Opcion de request de Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

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
                i.putExtra("evento_acumulador", mData.get(viewHolder.getAdapterPosition()).getAcumulador());

                mContext.startActivity(i);

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){


        Evento evento = filterList.get(position);
        holder.tv_name.setText(evento.getName());
        holder.tv_categoria.setText(evento.getCategoria());
        holder.tv_calificacion.setText(evento.getCalificacion());
        holder.tv_precio.setText(evento.getPrecio());

        //Load image de internet y colocarla en ImageView usando Glide
        Glide.with(mContext).load(mData.get(position).getImagen()).apply(option).into(holder.img_ImgEvento);

    }

    /*
    public void filter(final String s){

        new Thread(new Runnable() {
            @Override
            public void run() {
                filterList.clear();

                if(TextUtils.isEmpty(s)){

                    filterList.addAll(mData);
                }

                else{

                    for(Evento evento: mData){

                        if(evento.getName().toLowerCase().contains(s.toLowerCase()) ||
                                evento.getCategoria().toLowerCase().contains(s.toLowerCase())){

                            filterList.add(evento);
                        }
                    }
                }

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }*/

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    //Busqueda
    @Override
    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){

                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    filterList = new ArrayList<>();
                    filterList = mData;
                }
                else{
                    List<Evento> listF = new ArrayList<>();

                    for(Evento evento : mData){

                        if(evento.getName().toLowerCase().contains(charString.toLowerCase())){
                            listF.add(evento);
                        }
                    }
                    filterList = listF;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults){
                filterList = (ArrayList<Evento>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




}
