package com.sunil.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_patient<userdata> extends AppCompatActivity {

    EditText fullName,editTextPhone,password,email;
    Spinner spinner;
    Button button;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DatabaseReference databaseReference;
    RecyclerView recyclerview;
    userAdapter userAdapter;
    ArrayList<User> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);





        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Value");
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        spinner=findViewById(R.id.spinner);
      //  spinner =spinner.setOnItemSelectedListener(this);


        fullName = findViewById(R.id.editTextTextPersonName);
        email = findViewById(R.id.editTextTextPersonName2);
        editTextPhone = findViewById(R.id.editTextPhone);
        password = findViewById(R.id.editTextTextPassword2);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkfield(fullName);
                checkfield(email);
                checkfield(editTextPhone);
                checkfield(password);

                if(valid){
                    fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user=fAuth.getCurrentUser();
                            Toast.makeText(add_patient.this,"Patient Added",Toast.LENGTH_SHORT).show();
                            assert user != null;
                            DocumentReference df= fStore.collection("User").document(user.getUid());
                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("fullName",fullName.getText().toString());
                            userInfo.put("email",email.getText().toString());
                            userInfo.put("editTextPhone",editTextPhone.getText().toString());
                            userInfo.put("isUser","1");

                            df.set(userInfo);
                            startActivity(new Intent(getApplicationContext(),admin.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_patient.this,"Failed to Add Patient",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.spinnerList, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String data = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean checkfield(EditText textField) {
        if(textField.getText().toString().isEmpty())
        {
            textField.setError("Error");
            valid=false;
        }
        else {
            valid=true;
        }
        return valid;

    }

}