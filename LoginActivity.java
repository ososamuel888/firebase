package com.cuna.firebaselogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignUp;
    TextView tvSignUp;
    FirebaseAuth mfirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mfirebaseAuth= FirebaseAuth.getInstance();

        emailID=findViewById(R.id.email2);
        password=findViewById(R.id.password2);
        btnSignUp=findViewById(R.id.login);
        tvSignUp=findViewById(R.id.changeSignin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email= emailID.getText().toString().trim();
                final String pwd= password.getText().toString().trim();

                //crear usuario en firebase
                //usuario vacio
                if(email.isEmpty()){

                    emailID.setError("please enter email id");
                    emailID.requestFocus();
                    return;

                    //contraseña vacia
                }else if(pwd.isEmpty()){

                    password.setError("please enter password");
                    password.requestFocus();
                    return;

                    //ambos campos vacios
                }else if(email.isEmpty() && pwd.isEmpty()){

                    Toast.makeText(LoginActivity.this,"Fields are empty", Toast.LENGTH_SHORT).show();
                    return;

                    //ambos campos llenos
                }else if(!(email.isEmpty() && pwd.isEmpty())){

                    //creacion de usuario con datos introducidos
                    mfirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(LoginActivity.this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        //nos dira si se ejecuta o no
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //maensaje enviado si algo falla
                            if(!task.isSuccessful()){

                                if(task.getException()instanceof FirebaseAuthInvalidUserException){

                                    Toast.makeText(getApplicationContext(), "No existe este usuario",
                                            Toast.LENGTH_SHORT).show();

                                }else{

                                    Toast.makeText(getApplicationContext(),"Error al ejecutar", Toast.LENGTH_SHORT).show();

                                }
                                Toast.makeText(LoginActivity.this,"Login unsuccesful",
                                        Toast.LENGTH_SHORT).show();

                            }else{

                                //mensaje enviado si se registro de manera correcta
                                Toast.makeText(LoginActivity.this,"Login Succesful!!!", Toast.LENGTH_SHORT).show();
                                //abre la actividad home al ingresar correctamente
                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                intent.putExtra("usuario",email);
                                startActivity(intent);


                            }

                        }
                    });

                }else{

                    Toast.makeText(LoginActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();

                }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

    }
}
