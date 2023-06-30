package com.example.fleeker;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeker.model.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Account extends Fragment {
    ImageView logout;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Activity context;
    public static Account newInstance() {
        return new Account();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        logout = view.findViewById(R.id.logout);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(auth.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                assert user != null;
                ((TextView)view.findViewById(R.id.createpost_name)).setText(user.getUser_name());
                ((TextView)view.findViewById(R.id.emailText)).setText(user.getUser_email());
                ((TextView)view.findViewById(R.id.usernameRealText)).setText(user.getUser_usernameReal());
                ((TextView)view.findViewById(R.id.userIDText)).setText(user.getUser_username());
                Picasso.get().load(user.getUser_profilePic()).into((CircleImageView)view.findViewById(R.id.profile_image));
                ((TextView)view.findViewById(R.id.postcount)).setText(user.getPostCount()+"");
                ((TextView)view.findViewById(R.id.linkcount)).setText(user.getLinkCount()+"");
                try {
                    if (user.getVerified().equals("true")) {
                        ((ImageView) view.findViewById(R.id.profileVerify)).setVisibility(View.VISIBLE);
                    } else {
                        ((ImageView) view.findViewById(R.id.profileVerify)).setVisibility(View.GONE);
                    }
                }catch (NullPointerException ignored){}


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        view.findViewById(R.id.editprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoacSetting = new Intent(getActivity(), AccountSetting.class);
                startActivity(gotoacSetting);
            }
        });

        return view;
    }

    private void SignOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent gotologin = new Intent(getActivity(), Login.class);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Email", "NA");
                editor.putString("Pass", "NA");
                editor.apply();
                getActivity().startActivity(gotologin);
                getActivity().finishAffinity();
            }
        });
    }

}