package com.example.fleeker;

import android.app.Activity;
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

import com.example.fleeker.model.Users;
import com.example.fleeker.model.chats_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Home extends Fragment {
    public ImageView hamberger;
    Activity context;
    ArrayList<chats_model> chat_list;
    RecyclerView chatRV;
    chats_adapter chats_adapter;

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        chatRV = view.findViewById(R.id.chat_rv);
        chat_list = new ArrayList<>();
        ArrayList<String> mylist = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    mylist.add(dataSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chat_list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);
                    if(user.getUser_username().equals(FirebaseAuth.getInstance().getUid())){
                        continue;
                    }
                    if(mylist.contains(user.getUser_username())) {
                        chats_model chats_model = new chats_model(user.getUser_profilePic(), user.getUser_name(), user.getUser_email(), user.getUser_username(), user.getUser_usernameReal());
                        chat_list.add(chats_model);
                    }
                }
                chats_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chats_adapter = new chats_adapter(chat_list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        chatRV.setLayoutManager(linearLayoutManager);
        chatRV.setNestedScrollingEnabled(true);
        chatRV.setAdapter(chats_adapter);

        return view;
    }
}
