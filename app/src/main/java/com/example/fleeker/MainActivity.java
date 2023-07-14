package com.example.fleeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment(new Home(),true);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
        Home home = new Home();
        Feed feed = new Feed();
        Notification notification = new Notification();
        Account accountFrag = new Account();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.menu_home) {
                    fragment = home;
                }
                else if (item.getItemId() == R.id.menu_feed) {
                    fragment = feed;
                }
                else if (item.getItemId() == R.id.menu_notification) {
                    fragment = notification;
                }else if(item.getItemId()==R.id.menu_account){
                    fragment = accountFrag;
                }
                return loadFragment(fragment,false);
            }
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            String name = account.getDisplayName();
            String email = account.getEmail();
        }

    }



    public boolean loadFragment(Fragment fragment, boolean flag){
        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (flag) {
                fragmentTransaction.add(R.id.fragmentContainer, fragment);
            } else {
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
            }
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }


}