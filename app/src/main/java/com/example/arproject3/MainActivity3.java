package com.example.arproject3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity {

    String asset = "http://3569e58bb3d3.ngrok.io/Andy.gltf";
    CollectionReference colref= FirebaseFirestore.getInstance().collection("patients");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog dialog;
    ProgressBar progressBar;
    int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        progressBar=findViewById(R.id.progressBar);
        PatientParent.index=0;
        PatientParent.list=new ArrayList<>();
        PatientParent.models=new ArrayList<>();
        colref.orderBy("model").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressBar.setVisibility(View.VISIBLE);
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    String name = queryDocumentSnapshot.get("name").toString();
                    String date = queryDocumentSnapshot.get("date").toString();
                    String scanType = queryDocumentSnapshot.get("scanType").toString();
                    String model = queryDocumentSnapshot.get("model").toString();
                    String age = queryDocumentSnapshot.get("age").toString();
                    String time = queryDocumentSnapshot.get("time").toString();
                    Patient patient=new Patient(name,age,time,model,scanType,date);
                    PatientParent.list.add(patient);

                }

                switchActivity();

//                StringBuilder sb=new StringBuilder();
//                sb.append("Patient Name: "+list.get(0).name+"\n");
//                sb.append("Patient Age: "+list.get(0).age+"\n");
//                sb.append("Scan Type: "+list.get(0).scanType+"\n");
//                sb.append("Date of Scan: "+list.get(0).date+"\n");
//                sb.append("Time of Scan: "+list.get(0).time+"\n");
//                index++;
//                patientInfo.setText(sb.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void switchActivity() {
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent=new Intent(MainActivity3.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}