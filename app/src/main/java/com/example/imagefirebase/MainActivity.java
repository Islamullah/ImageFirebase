package com.example.imagefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
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
        progressBar.setVisibility(View.GONE);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                downloadWithByte();
//                  loadWithGlide();
                downloadWithUrl();
            }
        });



    }

    private void downloadWithUrl() {

        final StorageReference imageRef = storageReference.child("images/scan0001.jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                String newurl = null;

                URL url = null;
                try {
                    url = new URL(uri.toString());
                    newurl = url.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(newurl, imageView);
                imageLoadAsyncTask.execute();
//
//                String SUri = uri.getPath().toString();
//                imageView.setImageBitmap(bitmap);
//                Toast.makeText(getApplicationContext(),SUri,Toast.LENGTH_LONG).show();
            }
        });

    }


    /*private void downloadWithUrl() {

        storageReference.child("images/scan0001.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                Toast.makeText(getApplicationContext(),"URL succcessfuly got",Toast.LENGTH_SHORT).show();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver() , Uri.parse(paths));



//                Bitmap bitmap = BitmapFactory.ur
                imageView.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
*/
    /*public void loadWithGlide() {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        // ImageView in your Activity
//        ImageView imageView = findViewById(R.id.imageView);

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this *//* context *//*)
                .load(storageReference)
                .into(imageView);
        // [END storage_load_with_glide]
    }*/

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
                Toast.makeText(getApplicationContext(),"Fail",Toast.LENGTH_LONG).show();
            }
        });

    }

    private class ImageLoadAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }

        public ImageLoadAsyncTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;


        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }

}

