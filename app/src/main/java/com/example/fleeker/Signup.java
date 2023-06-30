package com.example.fleeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fleeker.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {

    Button signUp;
    ImageView google_signup;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextInputEditText ftname, ltname, email, newPass,username;
    FirebaseAuth auth;
    CircleImageView profilePic;
    Uri imageURI;
    String imageuri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Intent signUpToLoginIntent = new Intent(this, Login.class);
        signUp = findViewById(R.id.signup_button);
        ftname = findViewById(R.id.firstnametext);
        ltname = findViewById(R.id.lastnametext);
        email = findViewById(R.id.emailIDtext);
        newPass = findViewById(R.id.signup_password_input);
        username = findViewById(R.id.usernametext);
        profilePic = findViewById(R.id.profilePic);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ftname.getText().toString()+" "+ltname.getText();
                String emailId = email.getText().toString();
                String newPassword = newPass.getText().toString();
                String usernm = username.getText().toString();

                auth.createUserWithEmailAndPassword(emailId,newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String user_id=task.getResult().getUser().getUid();
                            DatabaseReference databaseReference = database.getReference().child("user").child(user_id);
                            StorageReference storageReference = storage.getReference().child("upload").child(user_id);

                            if(imageURI!=null){
                                storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if(task.isSuccessful()){
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageuri = uri.toString();
                                                    String verified = "false";
                                                    Users users = new Users(name, imageuri, emailId, user_id,newPassword, usernm,verified);
                                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Intent intent = new Intent(Signup.this, Login.class);
                                                                Toast.makeText(Signup.this,"User Registered successfully!", Toast.LENGTH_SHORT).show();
                                                                startActivity(intent);
                                                                finishAffinity();
                                                            }else{
                                                                Toast.makeText(Signup.this,"Error in creating user", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }else{
                                String verified = "false";
                                imageuri = "https://firebasestorage.googleapis.com/v0/b/fleeker-64601.appspot.com/o/default.png?alt=media&token=f634647f-fdba-45ff-85cf-ae2c3db8f081";
                                Users users = new Users(name, imageuri, emailId, user_id,newPassword, usernm, verified);
                                databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(Signup.this,Login.class);
                                            Toast.makeText(Signup.this,"User Registered successfully!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            finishAffinity();
                                        }else{
                                            Toast.makeText(Signup.this,"Error in creating user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(Signup.this, Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        google_signup = findViewById(R.id.google_signup_image);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        google_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignIn();
            }
        });

//        profilePic set:
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
            }
        });


    }

    public void GoogleSignIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                sendInsideActivity();
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==100){
            if(data!=null){
                imageURI = data.getData();
                profilePic.setImageURI(imageURI);
            }
        }
    }

    private void sendInsideActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}