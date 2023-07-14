package com.example.fleeker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fleeker.model.PostDB;
import com.example.fleeker.model.Users;
import com.example.fleeker.model.feeds_model;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Feed extends Fragment {
    ArrayList<feeds_model> feeds_model_list;
    RecyclerView feed_rv;

    public Feed() {
    }

    feeds_adapter feeds_adapter;
    ArrayList<String> mylinks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);



        feeds_model_list = new ArrayList<>();
        feed_rv = view.findViewById(R.id.feed_rv);
        mylinks = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("user_profilePic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.getValue(String.class)).into((ImageView)view.findViewById(R.id.story_self_profile));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getValue().equals("true")){
                        mylinks.add(dataSnapshot.getKey());
                    }else{
                        continue;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    feeds_model_list.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PostDB postDB = dataSnapshot.getValue(PostDB.class);
                        postDB.setPostID(dataSnapshot.getKey());
                        feeds_model feeds_model1 = new feeds_model();
                        feeds_model1.setPostDescription(postDB.getCreatepostDescription());
                        feeds_model1.setPostImage(postDB.getCreatepostImage());
                        feeds_model1.setCommentCount(postDB.getCommentCount());
                        feeds_model1.setLikesCount(postDB.getLikesCount());
                        feeds_model1.setTimedate(postDB.getPostdatetime());
                        feeds_model1.setFeedPostID(dataSnapshot.getKey());
                        if(mylinks.contains(postDB.getPostedByID())||postDB.getPostedByID().equals(FirebaseAuth.getInstance().getUid())) {
                            FirebaseDatabase.getInstance().getReference("user").child(postDB.getPostedByID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users user = snapshot.getValue(Users.class);
                                    feeds_model1.setFeedName(user.getUser_name());
                                    feeds_model1.setFeedProfile(user.getUser_profilePic());
                                    feeds_model1.setVerified(user.getVerified());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            feeds_model_list.add(feeds_model1);
                        }else{
                            continue;
                        }


                    }
                    feeds_adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        feeds_adapter = new feeds_adapter(feeds_model_list,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        feed_rv.setNestedScrollingEnabled(true);
        feed_rv.setLayoutManager(linearLayoutManager);
        feed_rv.setAdapter(feeds_adapter);

        ((ImageView)view.findViewById(R.id.addfeed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToAddFeed = new Intent(getActivity(), PostActivity.class);
                startActivity(goToAddFeed);

            }
        });



        return view;
    }
}