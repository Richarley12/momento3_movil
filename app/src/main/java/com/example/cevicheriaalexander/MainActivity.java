package com.example.cevicheriaalexander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText jetcorreo, jetclave ;
    String correo, clave,codigo_id,tmpcorreo,tmpclave;
    Boolean activo;
    TextView tmptxtNuevoUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jetcorreo=findViewById(R.id.etcorreo);
        jetclave=findViewById(R.id.etcontraseña);
        tmptxtNuevoUsuario=findViewById(R.id.txtNuevoUsuario);
        tmptxtNuevoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registrarse();
            }
        });
    }


    private void Registrarse() {
        Intent intuusuario= new Intent(MainActivity.this,RegistroActivity.class);
        startActivity(intuusuario);
    }

    private void Sesioniniciada(){
        Intent sesion= new Intent(MainActivity.this,UsuariosActivity.class);
        startActivity(sesion);
    }

    public void Ingresar(View view) {
        correo = jetcorreo.getText().toString();
        clave = jetclave.getText().toString();
        if (correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Ingrese todos los datos", Toast.LENGTH_SHORT).show();
            jetcorreo.requestFocus();
        }
    else {
        db.collection("Usuarios")
                .whereEqualTo("correo",correo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                codigo_id = document.getId();
                                tmpcorreo = document.getString("correo");
                                tmpclave = document.getString("clave");
                                activo= document.getBoolean("activo");
                                if (tmpclave.equals(clave)) {
                                    if(activo.equals(false)) {
                                        Toast.makeText(MainActivity.this, "El usuario está inactivo", Toast.LENGTH_SHORT).show();
                                        jetcorreo.requestFocus();
                                    }
                                    else{
                                    Toast.makeText(MainActivity.this, "sesion iniciada", Toast.LENGTH_SHORT).show();
                                        Sesioniniciada();
                                }
                                }else {
                                    Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                                    jetcorreo.requestFocus();
                                }
                            }
                        }
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Usuario no Encontrado", Toast.LENGTH_SHORT).show();
                                    jetcorreo.requestFocus();
                                }
                            });
                            //Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });



        }
    }
}