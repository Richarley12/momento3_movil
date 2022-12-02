package com.example.cevicheriaalexander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UsuariosActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText jetcorreo, jetclave,jetcedula ;
    String correo,cedula, clave, cedula_id;
    CheckBox jcbactivo;
    byte sw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        jetcedula=findViewById(R.id.edtCedula);
        jetcorreo=findViewById(R.id.edtMail);
        jetclave=findViewById(R.id.edtPassword);
        jcbactivo=findViewById(R.id.checkBox);
        sw=0;

    }
    public  void Consultar(View view){
        cedula= jetcedula.getText().toString();
        correo=jetcorreo.getText().toString();
        clave=jetclave.getText().toString();
        if (cedula.isEmpty()){
            Toast.makeText(this, "Debe ingresar su cedula", Toast.LENGTH_SHORT).show();
            jetcedula.requestFocus();
        }else{
            db.collection("Usuarios")
                    .whereEqualTo("cedula",cedula)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                sw=1;
                                for (QueryDocumentSnapshot document: task.getResult()){
                                    cedula_id= document.getId();
                                    jetcedula.setText( document.getString("cedula"));
                                    jetcorreo.setText( document.getString("correo"));
                                    jetclave.setText( document.getString("clave"));
                                    jcbactivo.setChecked(document.getBoolean("activo"));
                                }
                            }
                            else {
                                Toast.makeText(UsuariosActivity.this, "Documento no encontrado", Toast.LENGTH_SHORT).show();
                                jetcedula.requestFocus();
                            }
                        }
                    });
           }

        }
    public void Modificar (View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcedula.requestFocus();
        } else {
            cedula= jetcedula.getText().toString();
            correo = jetcorreo.getText().toString();
            clave = jetclave.getText().toString();
            if (cedula.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("cedula", cedula);
                user.put("correo", correo);
                user.put("clave", clave);
                user.put("activo", true);
                db.collection("Usuarios").document(cedula_id)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void documentReference) {
                                Toast.makeText(UsuariosActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                                Limpiar_campos();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UsuariosActivity.this, "Error actualizando datos", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
    public void Anular (View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para anular debe primero consultar", Toast.LENGTH_SHORT).show();
            jetcedula.requestFocus();
        } else {
            cedula= jetcedula.getText().toString();
            correo = jetcorreo.getText().toString();
            clave = jetclave.getText().toString();
            if (cedula.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Todos los datos son requeridos", Toast.LENGTH_SHORT).show();
            } else {
            Map<String, Object> user = new HashMap<>();
            user.put("cedula", cedula);
            user.put("correo", correo);
            user.put("clave", clave);
            user.put("activo", false);
            db.collection("Usuarios").document(cedula_id)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void documentReference) {
                            Toast.makeText(UsuariosActivity.this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                            Limpiar_campos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UsuariosActivity.this, "Error actualizando datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }}
    }

    public void Cancelar(View view){
        Limpiar_campos();
    }
    private void Limpiar_campos() {
        jetcedula.setText("");
        jetcorreo.setText("");
        jetclave.setText("");
        jetcedula.requestFocus();
        sw=0;
    }
    public void Iniciarsesion(View view) {
        Intent Iniciar= new Intent(UsuariosActivity.this,MainActivity.class);
        startActivity(Iniciar);
    }

    }






