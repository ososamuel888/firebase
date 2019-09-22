package com.cuna.firebaselogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    String nomUsuario;
    Bundle usuarioNom;
    Intent intent;

    TextView mensaje;
    Button camera,video,audio;




    //save variables
    String CARPETA_RAIZ="misImagenesPrueba/";
    String RUTA_IMAGEN=CARPETA_RAIZ+ "misFotos";
    String path;
    String nombreFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //asignar el UI con las variables
        mensaje=findViewById(R.id.congratulations);
        camera=findViewById(R.id.launchCamera);
        video=findViewById(R.id.launchVideo);
        audio=findViewById(R.id.launchAudio);

        //obtiene los datos del intent pasado
        //intent=getIntent();
        //guarda los datos del intent en un bundle para extraer en variables individuales
        //usuarioNom=intent.getExtras();
        //guarda el dato usuario del bundle en la variable nomUsuario
       // nomUsuario=(String) usuarioNom.get("usuario");


        //set message
       // mensaje.setText("Welcome "+nomUsuario);







        //camera activation

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tomarFoto();

            }
        });

        //video activation

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirVideo();

            }
        });


        //audio activation
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirMusica();

            }
        });

    }

    public void abrirMusica(){

        path= Environment.getExternalStorageDirectory()+File.separator;
        File music=new File(path);
        Intent intent=new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(music));
        startActivityForResult(intent,10);

    }


    public void abrirVideo(){


        path= Environment.getExternalStorageDirectory()+File.separator;
        File video=new File(path);
        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(video));
        startActivityForResult(intent,20);



    }


    public void tomarFoto(){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED


                &&


                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED



                &&


                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)!=
                        PackageManager.PERMISSION_GRANTED){



                        ActivityCompat.requestPermissions(HomeActivity.this,
                                new String[]{Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                                            1);


                        return;


        }

        //arrays

       Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       startActivityForResult(intent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Date date=new Date();

        Bitmap picture=(Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream arrayOutputStream= new ByteArrayOutputStream();
        picture.compress(
                Bitmap.CompressFormat.PNG,0,arrayOutputStream);

        File file=
                new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES),
                        "nombre"+date.getTime()
                                +date.getHours()
                                +date.getMinutes()
                                +date.getSeconds()+".png");



        try{

            FileOutputStream fileOutputStream= new FileOutputStream(file);
            fileOutputStream.write(arrayOutputStream.toByteArray());

        }catch(FileNotFoundException e){

            e.printStackTrace();

        }catch(IOException e){

            e.printStackTrace();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }
}
