package com.example.fleeker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Notification extends Fragment {

    public Notification() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        ((ImageView)view.findViewById(R.id.search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Search.class));
            }
        });

        ((ImageView)view.findViewById(R.id.LinkRequestFrag)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.requestFrag,new request_notification_Fragment()).commit();
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireActivity());
                bottomSheetDialog.setContentView(R.layout.fragment_request_notification_);
                Request_Adapter request_adapter;
                ArrayList<String> req_list;
                RecyclerView req_rv;

                req_list = new ArrayList<>();
                req_rv = bottomSheetDialog.findViewById(R.id.req_rv);
                request_adapter = new Request_Adapter(getActivity(), req_list);
                assert req_rv != null;

                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("LinkRequest").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        req_list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            req_list.add(dataSnapshot.getKey());
                        }
                        request_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                req_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                req_rv.setNestedScrollingEnabled(true);
                req_rv.setAdapter(request_adapter);

                bottomSheetDialog.show();

            }

        });



        return view;
    }
}