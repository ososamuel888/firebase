package com.cuna.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import modelosdatos.*;

public class realTimeActivity extends AppCompatActivity implements View.OnClickListener{

    //variables design
    Spinner Grupos, Materias;
    Button Guardar, Actualizar, Eliminar;
    EditText Nombres;
    RecyclerView recyclerView;


    //variables conexion noSQL
    FirebaseDatabase firebaseDatabase;
    DatabaseReference modelClass;
    String idSelect;


    String[] gruposAr={"TI-701","AG-701","GE-701","ME-701","IN-701"};
    String[] materiasAr={"CALCULO II","ECUACIONES DIF","COLORIMETRIA","COMUNICACION ASERTIVA",
            "ARQUITECTURA DE DATOS"};

    public ArrayList<Model> list= new ArrayList<>();
    public MyRecycleViewHolder myRecycleViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);

        firebaseDatabase=FirebaseDatabase.getInstance();
        modelClass=firebaseDatabase.getReference("Model");

        //asigna Spinner
        Grupos= findViewById(R.id.spinnerGrupo);
        Materias=findViewById(R.id.spinnerMateria);
        //asigna Button
        Guardar=findViewById(R.id.buttonGuardar);
        Eliminar=findViewById(R.id.buttonEliminar);
        Actualizar=findViewById(R.id.buttonActualizar);
        //asigna EditText
        Nombres=findViewById(R.id.editNombres);
        //asigna Recycler
        recyclerView=findViewById(R.id.recycler_view);

        /*update recycler view*/
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //llenar spinner
        Grupos.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,gruposAr));

        Materias.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,materiasAr));

        Guardar.setOnClickListener(this);
        Actualizar.setOnClickListener(this);
        Eliminar.setOnClickListener(this);

        getDataFromFirebase();
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.buttonGuardar:

                addNode();

                break;


            case R.id.buttonActualizar:

                updateNode(idSelect);

                break;


            case R.id.buttonEliminar:

                /* add auto delete my recycler view holder*/
                deleteNode(idSelect);

                break;

        }

    }

    public void addNode(){

        //agregar nodos
   String DatosGrupos= Grupos.getSelectedItem().toString();
   String DatosMaterias= Materias.getSelectedItem().toString();
   String DatosNombres= Nombres.getText().toString().trim();

   if(DatosNombres.isEmpty()){

       //mostramos mensaje de error si el campo esta vacio
       Nombres.setError("El campo esta vacio");
       Nombres.setFocusable(true);

   }else{

       /*agregamos datos a firebase consultamos la base donde
       * se agregan los datos*/
       String idDatabase=modelClass.push().getKey();
       /*instancia del modelo de datos para guardar
       la informacion*/
        Model myActivity= new Model(idDatabase,DatosGrupos, DatosMaterias, DatosNombres);
        /*gaurdamos a la base de datos*/
       modelClass.child("Lectures").child(idDatabase).setValue(myActivity);
       Toast.makeText(getApplicationContext(),"AGREGADO CORRECTAMENTE",Toast.LENGTH_SHORT).show();


        }


    }

    /*consultar datos de la base de datos de realtime almacenada en firebase*/
    public void getDataFromFirebase(){

        modelClass.child("Lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    /*procesar la informacion que recolectamos de firebase*/
                    /*dos parametros dividido por : el izquierdo es la variable, derecho es de donde
                    sale la variable*/

                    list.clear();

                    for(DataSnapshot ds: dataSnapshot.getChildren()){

                        String id=ds.child("id").getValue().toString();
                        String activity= ds.child("activity").getValue().toString();
                        String group= ds.child("group").getValue().toString();
                        String lecture= ds.child("lecture").getValue().toString();

                        list.add(new Model(id,group,lecture,activity));

                    }

                    /*llenar el recycler view*/
                    myRecycleViewHolder= new MyRecycleViewHolder(list);
                    recyclerView.setAdapter(myRecycleViewHolder);

                    myRecycleViewHolder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //toast to show selected id
                            Toast.makeText(getApplicationContext(),
                                    list.get(recyclerView.getChildAdapterPosition(view)).getId(),
                                    Toast.LENGTH_SHORT).show();

                            //get id of selected item
                            idSelect=list.get(recyclerView.getChildAdapterPosition(view)).getId();

                            //add info to text edits

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                /*cancel*/

            }
        });

    }


    /*delete node*/
    public void deleteNode(String id){

        modelClass.child("Lectures").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(getApplicationContext(),"Se elimino el registro correctamente",
                        Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"No se pudo eliminar el registro",
                        Toast.LENGTH_SHORT).show();

            }
        });

    }



    public void updateNode(String id){

        Map<String,Object> dataMap= new HashMap<>();
        dataMap.put("activity",Nombres.getText().toString());
        dataMap.put("group",Grupos.getSelectedItem().toString());
        dataMap.put("lecture",Materias.getSelectedItem().toString());

        modelClass.child("Lectures").child(id).updateChildren(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getApplicationContext(),"Se actualizo correctamente",
                        Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(),"No se pudo actualizar el registro",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }

}
