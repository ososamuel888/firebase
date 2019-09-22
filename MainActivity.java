package com.cuna.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText emailID, password;
    Button btnSignUp;
    SignInButton signingo;
    TextView tvSignUp;
    FirebaseAuth mfirebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //google signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //obejto de conexion de firebase
        mfirebaseAuth= FirebaseAuth.getInstance();

        //variables asignados al diseño de la interafaz
        emailID=findViewById(R.id.email1);
        password=findViewById(R.id.password1);
        btnSignUp=findViewById(R.id.signin);
        tvSignUp=findViewById(R.id.changeLogin);
        signingo=findViewById(R.id.sign_in_button);

        signingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });

        //al dar click al boton Sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email= emailID.getText().toString().trim();
                String pwd= password.getText().toString().trim();

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

                        Toast.makeText(MainActivity.this,"Fields are empty", Toast.LENGTH_SHORT).show();
                        return;

                        //ambos campos llenos
                    }else if(!(email.isEmpty() && pwd.isEmpty())){

                        //creacion de usuario con datos introducidos
                        mfirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            //nos dira si se ejecuta o no
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //maensaje enviado si algo falla
                                if(!task.isSuccessful()){

                                    if(task.getException()instanceof FirebaseAuthUserCollisionException){

                                        Toast.makeText(getApplicationContext(), "Ya existe este " +
                                                        "usuario",
                                                Toast.LENGTH_SHORT).show();

                                    }else{

                                        Toast.makeText(getApplicationContext(),"Error al " +
                                                "ejecutar", Toast.LENGTH_SHORT).show();

                                    }
                                    Toast.makeText(MainActivity.this,"Signup unsuccesful",Toast.LENGTH_SHORT).show();

                                }else{

                                    //mensaje enviado si se registro de manera correcta
                                    Toast.makeText(MainActivity.this,"Signup Succesful!!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                                }

                            }
                        });

                    }else{

                        Toast.makeText(MainActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();

                    }

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onStart(){

        super.onStart();
        FirebaseUser currentUser= mfirebaseAuth.getCurrentUser();

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }




    }

}
