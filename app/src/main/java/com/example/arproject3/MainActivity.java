package com.example.arproject3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    String asset = "http://3569e58bb3d3.ngrok.io/Andy.gltf";
    CollectionReference colref = FirebaseFirestore.getInstance().collection("patients");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog dialog;
    StorageReference child = storage.getReference("3D_Objects/");
    WeakReference<MainActivity> weakActivity = new WeakReference<>(this);
    ProgressBar progressBar;
    int index = 0;
    Button button;
    ArrayList<Patient> list = new ArrayList<>();
    TextView patientInfo;
    ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(this, MainActivity2.class);
//        startActivity(intent);
        progressBar=findViewById(R.id.progressBar2);
        TextView tv = findViewById(R.id.tv);
        button = findViewById(R.id.button);
        Patient patient=null;
        patient=PatientParent.list.get(PatientParent.index);
        StringBuilder sb=new StringBuilder();
        sb.append("Patient Name: "+patient.name+"\n");
        sb.append("Patient Age: "+patient.age+"\n");
        sb.append("Scan Type: "+patient.scanType+"\n");
        sb.append("Date of Scan: "+patient.date+"\n");
        sb.append("Time of Scan: "+patient.time+"\n");
        String model=patient.model;
        PatientParent.index++;
        patientInfo=findViewById(R.id.patientInfo);
        patientInfo.setText(sb.toString());
        StorageReference modelRef = storage.getReference().child("3D_Objects/"+model+".glb");
        try {
            File file = File.createTempFile("out"+String.valueOf(PatientParent.index), "glb");
//            Toast.makeText(this,file.getAbsolutePath(),Toast.LENGTH_LONG).show();
            modelRef.getFile(file).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }).
                    addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            progressBar.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                            arFragment.setOnTapArPlaneListener(
                                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                                        Toast.makeText(MainActivity.this, "On Tap Called", Toast.LENGTH_LONG).show();
                                        Anchor anchor = hitResult.createAnchor();
                                        ModelRenderable.builder()
                                                .setSource(
                                                        MainActivity.this, RenderableSource.builder().setSource(MainActivity.this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
                                                                .build())
                                                .setRegistryId(asset)
                                                .build().
                                                thenAccept(
                                                        modelRenderable -> {
                                                            addToModel(anchor, modelRenderable);
                                                        })
                                                .exceptionally(
                                                        throwable -> {
                                                            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                                                            return null;
                                                        });
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Retrieval Failed", Toast.LENGTH_LONG).show();
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
        }


//        patientInfo.setText(sb.toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PatientParent.index<PatientParent.list.size()) {
                    finish();
                    startActivity(getIntent());
                }
            }
        });


            }
//        arFragment.setOnTapArPlaneListener(
//                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
//                    Toast.makeText(MainActivity.this,"On Tap Called",Toast.LENGTH_LONG).show();
//                    Anchor anchor = hitResult.createAnchor();
//                    try {
//                        ModelRenderable.builder()
//                                .setSource(
//                                        MainActivity.this, RenderableSource.builder().setSource(MainActivity.this, Uri.parse("brain2.glb"), RenderableSource.SourceType.GLB)
//                                                .build()).setRegistryId(asset)
//                                .build().
//                                thenAccept(
//                                        modelRenderable -> {
//                                         addToModel(anchor, modelRenderable);
//                                        });
//                    }
//                    catch (Exception e)
//                    {
//                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).toString();
//                    }
//
//
//
//                });
//
//    }
    //                    AnchorNode anchorNode = new AnchorNode(anchor);
//                    anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//                    // Create the transformable model and add it to the anchor.
//                    TransformableNode model = new TransformableNode(arFragment.getTransformationSystem());
//                    model.setParent(anchorNode);
//                    model.setRenderable(renderable);
//                    model.select();


    private void addToModel(Anchor anchor, ModelRenderable modelRenderable) {
//        Toast.makeText(this,"AddToModel Called",Toast.LENGTH_LONG).show();
        Log.d("Main Activity","Unable to Render");
//        Camera camera = sceneView.getScene().getCamera();
//        camera.setLocalRotation(Quaternion.axisAngle(Vector3.right(), -30.0f));
        AnchorNode node=new AnchorNode(anchor);
        TransformableNode transformableNode=new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.getRotationController().setEnabled(true);
        transformableNode.getScaleController().setEnabled(true);
        transformableNode.setParent(node);
//        transformableNode.setRenderable(modelRenderable);
//        transformableNode.select();
//        transformableNode.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 1, 0f), 180));
       transformableNode.setLocalScale(new Vector3(0.1f, 0.1f, 0.1f));
        arFragment.getArSceneView().getScene().addChild(node);
        Node node1 = new Node();
        node1.setRenderable(modelRenderable);
        node1.setName("Node 1");
        node1.setParent(transformableNode);
        node1.setLocalRotation(Quaternion.lookRotation(Vector3.down(), Vector3.right()));
    }
}
