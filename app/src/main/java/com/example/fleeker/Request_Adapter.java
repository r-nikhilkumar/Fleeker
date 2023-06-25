package com.example.fleeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleeker.databinding.RequestSampleLayoutBinding;
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

public class Request_Adapter extends RecyclerView.Adapter<Request_Adapter.viewholder>{
    Context context;
    ArrayList<String> list;

    public Request_Adapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_sample_layout, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
            String lst = list.get(position);

            FirebaseDatabase.getInstance().getReference("user").child(lst).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users user = snapshot.getValue(Users.class);
                    holder.binding.requestName.setText(user.getUser_name());
                    holder.binding.requestUsername.setText(user.getUser_usernameReal());
                    Picasso.get().load(user.getUser_profilePic()).into(holder.binding.requestProfile);


                    holder.binding.requestLinkbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Users userss = snapshot.getValue(Users.class);
                                    if ((Objects.equals(snapshot.child("Links").child(Objects.requireNonNull(user.getUser_username())).getValue(), "true"))) {
                                        Toast.makeText(context, "Already your friend!", Toast.LENGTH_SHORT).show();
                                        holder.binding.requestLinkbtn.setText("Linked");
                                        holder.binding.requestLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_check_24, 0);
                                        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("LinkRequest").child(user.getUser_username()).setValue(null);
                                    }else{

                                        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("Links").child(user.getUser_username()).setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                assert userss != null;
                                                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("linkCount").setValue(userss.getLinkCount() + 1);
                                                FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).child("LinkRequest").child(user.getUser_username()).setValue(null);
                                            }
                                        });
                                        holder.binding.requestLinkbtn.setText("Linked");
                                        holder.binding.requestLinkbtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.outline_check_24, 0);

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
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        RequestSampleLayoutBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = RequestSampleLayoutBinding.bind(itemView);
        }
    }
}
