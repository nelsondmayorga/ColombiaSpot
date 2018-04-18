package com.colombiaspot.colombiaspot;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Equipo on 11/12/2017.
 */

public class ComentariosAdaptador extends RecyclerView.Adapter<ComentariosAdaptador.ComentariosViewHolder> {

    ArrayList<Comentario> comentarios;
    private Context context;

    public ComentariosAdaptador(Context context,ArrayList<Comentario> comentarios){
        this.comentarios = comentarios;
        this.context = context;
    }

    @Override
    public ComentariosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comentario, parent, false);
        return new ComentariosViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ComentariosViewHolder comentarioHolder, int position) {
        Comentario comentario = comentarios.get(position);

        comentarioHolder.tvNombreUsuario.setText(comentario.getUsuario());
        comentarioHolder.tvComentario.setText(comentario.getMensaje());
        //comentarioHolder.ivFotoUsuario.setImageResource(comentario.getFoto());


        /*Uri uri = Uri.parse(comentario.getFoto());
        Context context = comentarioHolder.ivFotoUsuario.getContext();
        Picasso.with(context).load(uri).into(comentarioHolder.ivFotoUsuario);*/

        Picasso.with(context).load(comentario.getFoto()).into(comentarioHolder.ivFotoUsuario);
    }

    @Override
    public int getItemCount() {
        int numero = comentarios.size();
        Log.i("SIZE DE LA LISTA: ", String.valueOf(numero));
        return comentarios.size();

    }

    public static class ComentariosViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreUsuario;
        ImageView ivFotoUsuario;
        TextView tvComentario;

        public ComentariosViewHolder(View itemView) {
            super(itemView);
            tvNombreUsuario = (TextView) itemView.findViewById(R.id.tvNombreUsuario);
            ivFotoUsuario = (ImageView) itemView.findViewById(R.id.ivFotoUsuario);
            tvComentario = (TextView) itemView.findViewById(R.id.tvComentario);


        }
    }

}
