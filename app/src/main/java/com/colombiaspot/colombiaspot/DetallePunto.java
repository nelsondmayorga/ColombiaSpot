package com.colombiaspot.colombiaspot;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetallePunto extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    private DatabaseReference comentarioRef;
    Context contexto = this;
    String username;
    Uri personPhoto;
    ArrayList<Comentario> comentarios;
    ComentariosAdaptador adaptador;
    RecyclerView listaComentarios;
    TextView noComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_punto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // [START initialize_database_ref]
        comentarioRef = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("puntos");

        TextView tvNombre = (TextView)findViewById(R.id.tvNombre);
        ImageView ivFoto = (ImageView)findViewById(R.id.ivFoto);
        TextView tvTipo = (TextView)findViewById(R.id.tvTipo);
        TextView tvDescripcion = (TextView)findViewById(R.id.tvDescripcion);
        final EditText etComentar = (EditText)findViewById(R.id.etComentar);
        Button btnComentar = (Button)findViewById(R.id.btnComentar);
        noComentarios = (TextView) findViewById(R.id.noComentarios);

        //Datos de la sesión iniciada de Google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            username = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            personPhoto = acct.getPhotoUrl();
        }

        //Obtener datos del intent
        Bundle extras = getIntent().getExtras();
        final String id = extras.getString("id");
        String nombre = extras.getString("nombre");
        String descripcion = extras.getString("descripcion");
        String tipo = extras.getString("tipo");
        String imagen = extras.getString("imagen");



        //Setear información del punto
        tvNombre.setText(nombre);
        Picasso.with(contexto).load(imagen).into(ivFoto);
        tvTipo.setText(tipo);
        tvDescripcion.setText(descripcion);


        //Añadir comentario a Firebase
        btnComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idComentario = myRef.child(id).child("comentarios").push().getKey();
                String comentario = etComentar.getText().toString();
                myRef.child(id).child("comentarios").child(idComentario).child("id").setValue(idComentario);
                myRef.child(id).child("comentarios").child(idComentario).child("mensaje").setValue(comentario);
                myRef.child(id).child("comentarios").child(idComentario).child("usuario").setValue(username);
                myRef.child(id).child("comentarios").child(idComentario).child("foto").setValue(String.valueOf(personPhoto));

                etComentar.getText().clear();
            }
        });



        //Llenar y mostrar Recyclerview
        comentarios = new ArrayList<>();
        listaComentarios = (RecyclerView)findViewById(R.id.rvComentarios);
        listaComentarios.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaComentarios.setLayoutManager(llm);


        inicializarAdaptador();
        checkComentarios();






        //actualizarLista();
        comentarioRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot s2 : dataSnapshot.child(id).child("comentarios").getChildren()){
                    Comentario comment = s2.getValue(Comentario.class);
                    comentarios.add(comment);
                }
                //comentarios.add(dataSnapshot.child(id).child("comentarios").child("-L0Ba1y7rU9Fc9IexSrh").getValue(Comentario.class));


                adaptador.notifyDataSetChanged();
                checkComentarios();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {



                comentarios.clear();
                for (DataSnapshot s2 : dataSnapshot.child(id).child("comentarios").getChildren()){
                    Comentario comment = s2.getValue(Comentario.class);
                    comentarios.add(comment);
                }


                adaptador.notifyDataSetChanged();
                checkComentarios();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void inicializarAdaptador(){
        adaptador = new ComentariosAdaptador(this, comentarios);
        listaComentarios.setAdapter(adaptador);
    }

    public void checkComentarios(){
        if (comentarios.size() == 0){
            listaComentarios.setVisibility(View.INVISIBLE);
            noComentarios.setVisibility(View.VISIBLE);
        }
        else{
            listaComentarios.setVisibility(View.VISIBLE);
            noComentarios.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return(super.onOptionsItemSelected(item));
    }




}
