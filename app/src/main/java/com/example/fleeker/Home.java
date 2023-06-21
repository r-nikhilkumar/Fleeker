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
        FirebaseDatabase.getInstance().getReference().child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Users user = dataSnapshot.getValue(Users.class);
                        if(user.getUser_username().equals(FirebaseAuth.getInstance().getUid())){
                            continue;
                        }
                        chats_model chats_model = new chats_model(user.getUser_profilePic(),user.getUser_name(),user.getUser_email(),user.getUser_username());
                        chat_list.add(chats_model);
                    }
                    chats_adapter.notifyDataSetChanged();
                }

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
