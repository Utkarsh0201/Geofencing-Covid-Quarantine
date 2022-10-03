package com.sunil.geofencing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class patient_list extends AppCompatActivity {
    RecyclerView recyclerview;
    FirebaseFirestore fStore;
    userAdapter userAdapter;
    ArrayList<udata> l;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_patient_list);
        recyclerview = findViewById(R.id.recycleview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        l = new ArrayList<udata>();
        userAdapter = new userAdapter(this,l);
        recyclerview.setAdapter(userAdapter);

        fStore.collection("User").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> lists= queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : lists){
                        udata u = d.toObject(udata.class);
                        l.add(u);
                    }
                    userAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}