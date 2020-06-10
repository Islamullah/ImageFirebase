package com.example.imagefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    ImageView  imageView;
    Button  button;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.image1);
        button = (Button)findViewById(R.id.button1);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadWithByte();

            }
        });



    }

    //download image with byte[]
    public void downloadWithByte(){

        final StorageReference imageRef1 = storageReference.child("images/scan0001.jpg");
        long MAXBYTES = 1024*1024;
        imageRef1.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG);
            }
        });

    }




}