package com.example.cevicheriaalexander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText jetcedula, jetcorreo, jetclave ;
    String cedula, correo, clave;
    byte sw;
    CheckBox jcbactivo;
    Boolean activo;
    Button volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        jetcedula=findViewById(R.id.edtCedula);
        jetcorreo=findViewById(R.id.edtMail);
        jetclave=findViewById(R.id.edtPassword);
        volver=findViewById(R.id.btnIniciarSesion);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iniciarsesion();
            }
        });
    }

    private void Iniciarsesion() {
        Intent Iniciar= new Intent(RegistroActivity.this,MainActivity.class);
        startActivity(Iniciar);
    }

    public void Guardar(View view){
        cedula=jetcedula.getText().toString();
        correo=jetcorreo.getText().toString();
        clave=jetclave.getText().toString();
        if (cedula.isEmpty()||correo.isEmpty()||clave.isEmpty()){
            Toast.makeText(this, "Digite todos los datos", Toast.LENGTH_SHORT).show();
            jetcedula.requestFocus();
        } else{
            Map<String,Object> user=new HashMap<>();
            user.put("cedula",cedula);
            user.put("correo",correo);
            user.put("clave",clave);
            user.put("activo",true);

    db.collection("Usuarios")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(RegistroActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                    Limpiar_campos();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegistroActivity.this, "Error en registro", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void Limpiar_campos() {
        jetcedula.setText("");
        jetcorreo.setText("");
        jetclave.setText("");
        jetcedula.requestFocus();
        sw=0;
    }

}