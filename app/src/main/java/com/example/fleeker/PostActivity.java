package com.example.fleeker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeker.model.PostDB;
import com.example.fleeker.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    EditText postDesc;
    Button postbtn;
    ImageView postImage;
    Uri ImageURI;
    String postdesc;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get().load(user.getUser_profilePic()).into((ImageView)findViewById(R.id.create_post_profile));
                ((TextView)findViewById(R.id.createpost_name)).setText(user.getUser_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        progressDialog = new ProgressDialog(this);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("in progress...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        postDesc = findViewById(R.id.create_postdiscription);
        postbtn = findViewById(R.id.postbtn);
        postImage = findViewById(R.id.postimage);


        TextWatcher postText = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!postDesc.getText().toString().isEmpty()){
                    postbtn.setEnabled(true);
                    postbtn.setBackgroundColor(getResources().getColor(R.color.adon4));
                    postbtn.setTextColor(getResources().getColor(R.color.white));
                }else{
                    postbtn.setEnabled(false);
                    postbtn.setBackgroundColor(getResources().getColor(R.color.greybut));
                    postbtn.setTextColor(getResources().getColor(R.color.greybuttext));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        postDesc.addTextChangedListener(postText);

        ((ImageView)findViewById(R.id.addimg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                postdesc = postDesc.getText().toString();
                if(postdesc.isEmpty()){
                    postdesc = null;
                }
                if(ImageURI!=null) {
                    StorageReference storage = FirebaseStorage.getInstance().getReference("post").child(FirebaseAuth.getInstance().getUid()).child(new Date().getTime() + "");
                    storage.putFile(ImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    PostDB postDB = new PostDB(uri.toString(), postdesc, FirebaseAuth.getInstance().getUid(), new Date().getTime(), 0, 0);
                                    FirebaseDatabase.getInstance().getReference("post").push().setValue(postDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    Users user = snapshot.getValue(Users.class);
                                                    FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("postCount").setValue(user.getPostCount()+1);

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            Toast.makeText(PostActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                                            finishAffinity();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else{
                    PostDB postDB = new PostDB(null, postdesc, FirebaseAuth.getInstance().getUid(), new Date().getTime(), 0, 0);
                    FirebaseDatabase.getInstance().getReference("post").push().setValue(postDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users user = snapshot.getValue(Users.class);
                                    FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("postCount").setValue(user.getPostCount()+1);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Toast.makeText(PostActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(PostActivity.this, MainActivity.class));
                            finishAffinity();
                        }
                    });
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(data!=null){
                ImageURI = data.getData();
                postImage.setImageURI(ImageURI);
                postImage.setVisibility(View.VISIBLE);
                postbtn.setEnabled(true);
                postbtn.setBackgroundColor(getResources().getColor(R.color.adon4));
                postbtn.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }
}