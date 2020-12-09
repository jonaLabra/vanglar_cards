package com.example.vanlgar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView profile;
    private TextView Headername,Headerkey,Headerstate,Headertown,Headersection;
    private TextView Name,Key,State,Town,Section;
    private static final int IMAGE_CODE_REQUEST = 1;
    private static final int PICK_CAMERA_REQUEST = 2;
    final int REQUEST_CODE_ASK_PERMISSION = 111;
    Map<String, Map<String, String>> mapCards;
    Map<String, Map<String, String>> mapCardsCompany;
    Uri uri;
    FirebaseDatabase database;
    DatabaseReference reference,referenceCard;
    private StorageReference mStorageRef;
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){
        solicitarPermisos();
        profile = findViewById(R.id.circleImageViewProfile);
        Headername = findViewById(R.id.headerName);
        Headerkey = findViewById(R.id.headerKey);
        Headerstate = findViewById(R.id.headerState);
        Headertown = findViewById(R.id.headerTown);
        Headersection = findViewById(R.id.headerSection);
        Name = findViewById(R.id.Name);
        Key = findViewById(R.id.key);
        State = findViewById(R.id.state);
        Town = findViewById(R.id.town);
        Section = findViewById(R.id.section);
        database = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Cards");
        reference = database.getReference("cards").child("fuerza_mexico_demo");
        referenceCard = database.getReference("usuario").child("cardsCompany").child("fuerza_mexico_demo");
        ProfilePicture();
        DataCard();
        DataCardCompany();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });

    }
    private void ProfilePicture(){
        referenceCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String img = snapshot.child("photoUrl").getValue().toString();
                    Glide
                            .with(getApplicationContext())
                            .load(img)
                            .thumbnail(0.5f)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DataCard(){
        reference.child("headers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mapCards = (Map<String, Map<String, String>>) snapshot.getValue();
                    if(mapCards != null) {
                        for (Map.Entry<String, Map<String, String>> card : mapCards.entrySet()) {
                            Log.d("Message Map", card.getKey());
                        }
                    }

                    Log.d("Key Firebase headers", snapshot.child("name").getKey());
                    String headerName = snapshot.child("name").getValue().toString();
                    String headerKey = snapshot.child("elector_key").getValue().toString();
                    String headerState = snapshot.child("state").getValue().toString();
                    String headerSection = snapshot.child("section").getValue().toString();
                    String headerTown = snapshot.child("town").getValue().toString();
                    Headername.setText(headerName);
                    Headerkey.setText(headerKey);
                    Headerstate.setText(headerState);
                    Headertown.setText(headerTown);
                    Headersection.setText(headerSection);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DataCardCompany(){
        referenceCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                   mapCardsCompany = (Map<String, Map<String, String>>) snapshot.getValue();

                    String name = snapshot.child("name").getValue().toString();
                    String key = snapshot.child("elector_key").getValue().toString();
                    String state = snapshot.child("state").getValue().toString();
                    String section = snapshot.child("section").getValue().toString();
                    String town = snapshot.child("town").getValue().toString();

                    Name.setText(name);
                    Key.setText(key);
                    State.setText(state);
                    Town.setText(town);
                    Section.setText(section);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void OptionsImage(){
        ImageButton photo,galery;
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.options_image, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        photo = view.findViewById(R.id.photo);
        galery = view.findViewById(R.id.galery);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
                dialog.dismiss();

            }
        });
        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirImagen();
                dialog.dismiss();
            }
        });

    }

    public String GetFileExtension (Uri uri) {
        ContentResolver contentResolver = getContentResolver ();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton ();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void abrirImagen(){
        Intent intent = new Intent ();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser (intent, "Seleccionar imagen"),IMAGE_CODE_REQUEST);
    }
    public void abrirCamara(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,PICK_CAMERA_REQUEST);
    }
    void Dialog(){
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Cambiar imagen de perfil");
        builder.setCancelable(false);
        builder.setMessage("Desea cambiar de imagen?");
        builder.setIcon(R.drawable.add_photo_icon);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OptionsImage();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }
        });
        builder.show();
    }
     void UpdateImage(){
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + GetFileExtension(uri));
        final UploadTask mUploadTask =  fileReference.putFile(uri);
         mUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                     @Override
                     public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                         if (!task.isSuccessful()){
                             throw task.getException();
                         }
                         return fileReference.getDownloadUrl();
                     }
                 }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                     @Override
                     public void onComplete(@NonNull Task<Uri> task) {
                         if (task.isSuccessful()) {
                             uri = task.getResult();
                             referenceCard.child("photoUrl").setValue(uri.toString());
                             Toast.makeText(MainActivity.this, "Imagen de perfil actualizada", Toast.LENGTH_LONG).show();
                             Log.d("BD", "Firebase Complete");
                         }
                     }
                 });
             }
         });
     }
     public Uri getImageUri(Context context,Bitmap bitmap){
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
         String PATH = MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"Camera",null);
         return Uri.parse(PATH);
     }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void solicitarPermisos() {
        int permiso = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permiso != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CODE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            Log.d("Gale", uri.toString());
            try{
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                UpdateImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        if (requestCode == PICK_CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap b = (Bitmap)  data.getExtras().get("data");
            uri = getImageUri(getApplicationContext(),b);
            Log.d("Camara", uri.toString());
            profile.setImageBitmap(b);
            UpdateImage();
        }
        else {
            Log.d("TAG Error", "Error");
        }
    }

}
