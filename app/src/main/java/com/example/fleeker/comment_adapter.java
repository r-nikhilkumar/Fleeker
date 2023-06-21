package com.example.fleeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleeker.databinding.CommentSampleLayoutBinding;
import com.example.fleeker.model.Users;
import com.example.fleeker.model.comment_model;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class comment_adapter extends RecyclerView.Adapter<comment_adapter.viewholder> {
    Context context;
    ArrayList<comment_model> list;

    public comment_adapter(Context context, ArrayList<comment_model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        comment_model model = list.get(position);
        holder.binding.time.setText(TimeAgo.using(model.getTime()));
        FirebaseDatabase.getInstance().getReference("user").child(model.getCommentedBy()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                Picasso.get().load(user.getUser_profilePic()).into(holder.binding.profile);
                holder.binding.comment.setText(user.getUser_name()+"- "+model.getComment());
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
        CommentSampleLayoutBinding binding;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleLayoutBinding.bind(itemView);

        }
    }
}
