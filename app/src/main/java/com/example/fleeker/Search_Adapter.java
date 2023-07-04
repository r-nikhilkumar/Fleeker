package com.example.fleeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleeker.databinding.SearchSampleLayoutBinding;
import com.example.fleeker.model.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.viewholder> {

    Context context;
    ArrayList<Users> list;

    public Search_Adapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setFilterdList(ArrayList<Users> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_sample_layout, parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Users user = list.get(position);
        holder.binding.searchName.setText(user.getUser_name());
        Picasso.get().load(user.getUser_profilePic()).into(holder.binding.searchProfile);
        holder.binding.searchUsername.setText(user.getUser_usernameReal());
        try {
            if (user.getVerified().equals("true")) {
                holder.binding.searchVerify.setVisibility(View.VISIBLE);
            } else {
                holder.binding.searchVerify.setVisibility(View.GONE);
            }
        }catch (NullPointerException ignored){}

        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Objects.equals(snapshot.child(Objects.requireNonNull(user.getUser_username())).getValue(), "true")){
                    holder.binding.searchLinkbtn.setText("Linked");
                    holder.binding.searchLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.outline_check_24,0);
                }else if(Objects.equals(snapshot.child(Objects.requireNonNull(user.getUser_username())).getValue(), "requested")){
                    holder.binding.searchLinkbtn.setText("requested");
                    holder.binding.searchLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.outline_check_24,0);
                }
                else{
                    holder.binding.searchLinkbtn.setText("Link");
                    holder.binding.searchLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_add_24,0);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.binding.searchLinkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users userss = snapshot.getValue(Users.class);
                        if(Objects.equals(snapshot.child("Links").child(Objects.requireNonNull(user.getUser_username())).getValue(), "true")){
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").child(user.getUser_username()).setValue(null);
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("linkCount").setValue(userss.getLinkCount()-1);
                            FirebaseDatabase.getInstance().getReference("user").child(user.getUser_username()).child("LinkRequest").child(FirebaseAuth.getInstance().getUid()).setValue(null);

                            holder.binding.searchLinkbtn.setText("Link");
                            holder.binding.searchLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_add_24,0);
                        }else{
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").child(user.getUser_username()).setValue("requested");
//                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("linkCount").setValue(userss.getLinkCount()+1);
                            FirebaseDatabase.getInstance().getReference("user").child(user.getUser_username()).child("LinkRequest").child(FirebaseAuth.getInstance().getUid()).setValue("true");

                            holder.binding.searchLinkbtn.setText("Requested");
                            holder.binding.searchLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.outline_check_24,0);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        SearchSampleLayoutBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = SearchSampleLayoutBinding.bind(itemView);
        }
    }
}
