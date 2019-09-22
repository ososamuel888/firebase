package com.cuna.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    Button multimedia;
    Button storage;
    Button realtimedb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        multimedia=findViewById(R.id.btnMultiMedia);
        storage=findViewById(R.id.btnStorage);
        realtimedb=findViewById(R.id.btnRealTime);


        multimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);

            }
        });


        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), Storage.class);
                startActivity(intent);

            }
        });

        realtimedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getApplicationContext(), realTimeActivity.class);
                startActivity(intent);

            }
        });

    }
}
