package com.example.fleeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    Animation top, bottom;
    ImageView imageView;
    TextView textView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        top = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        imageView.setAnimation(top);
        textView.setAnimation(bottom);

        Intent intent = new Intent(this,Login.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getShared = getSharedPreferences("user", MODE_PRIVATE);
                String eml = getShared.getString("Email", "NA");
                String pas = getShared.getString("Pass", "NA");

                auth.signInWithEmailAndPassword(eml, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Intent loginToMainIntent = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(loginToMainIntent);
                                finishAffinity();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            startActivity(intent);
                            finish();
                        }
                    }
                });


            }
        }, 4000);

    }
}