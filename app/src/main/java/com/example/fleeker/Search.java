package com.example.fleeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.fleeker.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {
    private SearchView searchView;
    ArrayList<Users> allItemList;
    RecyclerView search_rv;
    ArrayList<Users> filteredList;
    Search_Adapter search_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_bar);
        search_rv = findViewById(R.id.search_rv);
        searchView.clearFocus();

        allItemList = new ArrayList<>();
        filteredList = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allItemList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())){
                        continue;
                    }
                    Users user = dataSnapshot.getValue(Users.class);
                    allItemList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        search_adapter = new Search_Adapter(this);
        search_rv.setLayoutManager(new LinearLayoutManager(this));
        search_rv.setNestedScrollingEnabled(true);
        search_rv.setAdapter(search_adapter);



    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterList(String newText) {
        filteredList.clear();
        if(!newText.equals("")) {
            for (Users item : allItemList) {
                if (item.getUser_usernameReal().toLowerCase().contains(newText.toLowerCase()) || item.getUser_name().toLowerCase().contains(newText.toLowerCase()) || item.getUser_username().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
                else{
                    filteredList.remove(item);
                }
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found!", Toast.LENGTH_SHORT).show();
        }else {
            search_adapter.setFilterdList(filteredList);
        }


    }
}