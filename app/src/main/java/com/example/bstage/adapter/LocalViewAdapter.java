package com.example.bstage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.example.bstage.R;
import com.example.bstage.activities.LocalActivity;
import com.example.bstage.models.Local;

import java.util.ArrayList;
import java.util.List;

public class LocalViewAdapter  extends  RecyclerView.Adapter<LocalViewAdapter.MyViewHolder> implements Filterable {

    private Context mContext;
    private List<Local> mData, mFilteredList;
    RequestOptions option;

    public LocalViewAdapter(Context mContext, List<Local> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mFilteredList = mData;

        //Pedir option para Glide

        option = new RequestOptions().centerCrop().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.list_locales, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);

        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mContext, LocalActivity.class);
                i.putExtra("_id", mData.get(viewHolder.getAdapterPosition()).get_id());
                i.putExtra("local_name", mData.get(viewHolder.getAdapterPosition()).getName());
                i.putExtra("local_descripcion", mData.get(viewHolder.getAdapterPosition()).getDescripcion());
                i.putExtra("local_categoria", mData.get(viewHolder.getAdapterPosition()).getCategoria());
                i.putExtra("local_precio", mData.get(viewHolder.getAdapterPosition()).getPrecio());
                i.putExtra("local_calificacion", mData.get(viewHolder.getAdapterPosition()).getCalificacion());
                i.putExtra("local_direccion", mData.get(viewHolder.getAdapterPosition()).getDireccion());
                i.putExtra("local_img", mData.get(viewHolder.getAdapterPosition()).getImagen());
                i.putExtra("local_contador", mData.get(viewHolder.getAdapterPosition()).getContador());
                i.putExtra("local_acumulador", mData.get(viewHolder.getAdapterPosition()).getAcumulador());

                mContext.startActivity(i);
            }
        });

        return viewHolder;
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

        return mFilteredList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name;
        TextView tv_categoria;
        TextView tv_calificacion;
        TextView tv_precio;
        ImageView img_ImgLocal;
        LinearLayout view_container;


        public MyViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container_local);
            tv_name = itemView.findViewById(R.id.local_name);
            tv_categoria = itemView.findViewById(R.id.local_categoria);
            tv_calificacion = itemView.findViewById(R.id.local_calificacion);
            tv_precio = itemView.findViewById(R.id.local_precio);
            img_ImgLocal = itemView.findViewById(R.id.ImgLocal);
        }
    }

    //Busqueda
    @Override
    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){

                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mFilteredList = mData;
                }
                else{
                    List<Local> filteredList = new ArrayList<>();

                    for(Local local : mData){

                        if(local.getName().toLowerCase().contains(charString)){
                            filteredList.add(local);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults){
                mFilteredList = (ArrayList<Local>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
