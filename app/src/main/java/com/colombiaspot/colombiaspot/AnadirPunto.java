package com.colombiaspot.colombiaspot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AnadirPunto extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference storageRef;
    private String coordenadasLatLang = "";
    private Uri mImageUri = null;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private static final int PLACE_PICKER_REQUEST = 10;


    EditText etNombre;
    EditText etDescripcion;
    Spinner spinner;
    EditText etImagen;
    TextView tvCoordenadas;
    Button btnCoordenadas;
    Button btnEnviar;
    Button btnImagen;

    String rSpinner = "";
    String urlImagen = "https://firebasestorage.googleapis.com/v0/b/colombiaspot2-alt.appspot.com/o/Images%2Fno-imagen.png?alt=media&token=16a448bc-3728-4c5f-a591-3ab448a47a39";
    ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_punto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("puntos");
        storage = FirebaseStorage.getInstance();

        etNombre = (EditText)findViewById(R.id.etNombre);
        etDescripcion = (EditText)findViewById(R.id.etDescripcion);
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AnadirPunto.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tipos));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        mProgressDialog = new ProgressDialog(AnadirPunto.this);
        btnCoordenadas = (Button)findViewById(R.id.btnCoordeanadas);
        tvCoordenadas = (TextView)findViewById(R.id.tvCoordenadas);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        btnImagen =(Button)findViewById(R.id.btnImagen);




        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = myRef.push().getKey();
                String nombre = etNombre.getText().toString().trim();
                String descripcion = etDescripcion.getText().toString().trim();
                String tipo = rSpinner;
                String imagen = urlImagen;
                String coordenadas = coordenadasLatLang;

                if (nombre.isEmpty()) {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Debe completar Nombre", Toast.LENGTH_LONG);
                    toast1.show();
                    return;
                }

                if (descripcion.isEmpty()) {
                    Toast toast2 = Toast.makeText(getApplicationContext(), "Debe completar Descripción", Toast.LENGTH_LONG);
                    toast2.show();
                    return;
                }

                if (coordenadas.isEmpty()) {
                    Toast toast3 = Toast.makeText(getApplicationContext(), "Debe añadir Coordenadas", Toast.LENGTH_LONG);
                    toast3.show();
                    return;
                }



                Puntos spot = new Puntos(id, nombre, descripcion, tipo, imagen, coordenadas);

                myRef.child(id).setValue(spot);
                Toast toast = Toast.makeText(getApplicationContext(), "Punto añadido exitósamente", Toast.LENGTH_LONG);
                toast.show();

                //Limpia los campos
                etNombre.getText().clear();
                etDescripcion.getText().clear();
                tvCoordenadas.setText("");
                spinner.setSelection(0);

            }
        });

        btnCoordenadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AnadirPunto.this),PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }
            }
        });



        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else
                {
                    abrirGaleria();
                }
            }
        });

        // Create a storage reference from our app
        storageRef = storage.getReference();

    }


    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    abrirGaleria();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    //After Selecting image from gallery image will directly uploaded to Firebase Database
    //and Image will Show in Image View
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 2:
                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

                    mImageUri = data.getData();
                    //user_image.setImageURI(mImageUri);
                    StorageReference filePath = storageRef.child("Images").child(mImageUri.getLastPathSegment());

                    mProgressDialog.setMessage("Subiendo Imagen....");
                    mProgressDialog.show();

                    filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error
                            String uriStr = downloadUri.toString();
                            urlImagen = uriStr;
                            // myRef.child("imagen").setValue(downloadUri.toString());


                            Toast.makeText(getApplicationContext(), "Imagen subida!", Toast.LENGTH_SHORT).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
                break;
            case 10:
                if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){

                    Place place = PlacePicker.getPlace(AnadirPunto.this, data);
                    String direccion = place.getAddress().toString();

                    Double lat = place.getLatLng().latitude;
                    Double lng = place.getLatLng().longitude;
                    String latLong = String.valueOf(lat) + ","+ String.valueOf(lng);

                    tvCoordenadas.setText(direccion);
                    coordenadasLatLang = latLong;

                }
        }


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sTipo = parent.getItemAtPosition(position).toString();
        rSpinner = sTipo;


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
