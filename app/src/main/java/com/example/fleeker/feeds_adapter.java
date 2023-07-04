package com.example.fleeker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleeker.model.comment_model;
import com.example.fleeker.model.feeds_model;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class feeds_adapter extends RecyclerView.Adapter<feeds_adapter.viewholder>{
    ArrayList<feeds_model> list;
    Context context;



    public feeds_adapter(ArrayList<feeds_model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_layout,parent,false);

        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        feeds_model feeds_model = list.get(position);
        holder.feedName.setText(feeds_model.getFeedName());
        Picasso.get().load(feeds_model.getFeedProfile()).into(holder.feedProfile);
        if(feeds_model.getPostDescription()!=null){
            holder.postDesc.setText(feeds_model.getPostDescription());
            holder.postDesc.setVisibility(View.VISIBLE);
        }else{
            holder.postDesc.setVisibility(View.GONE);
        }
        if(feeds_model.getPostImage()!=null){
            Picasso.get().load(feeds_model.getPostImage()).into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        }else{
            holder.postImage.setVisibility(View.GONE);
        }
        holder.likescount.setText(""+feeds_model.getLikesCount());
        holder.commentcount.setText(""+feeds_model.getCommentCount());
        holder.time.setText(TimeAgo.using(feeds_model.getTimedate()));
        try {
            if (feeds_model.getVerified().equals("true")) {
                holder.verified.setVisibility(View.VISIBLE);
            } else {
                holder.verified.setVisibility(View.GONE);
            }
        }catch (NullPointerException ignored){}

        FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (Objects.equals(snapshot.child("likedBy").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).getValue(), "true")) {
                    holder.likescount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heartfilled,0,0,0);
                }else {
                    holder.likescount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_outlined,0,0,0);
                }
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });

        holder.likescount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Objects.equals(snapshot.child("likedBy").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).getValue(), "true")) {
                            FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("likesCount").setValue(Integer.parseInt(snapshot.child("likesCount").getValue().toString()) - 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.likescount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heart_outlined,0,0,0);
                                    HashMap<String, Object> liked = new HashMap<String, Object>();
                                    liked.put(FirebaseAuth.getInstance().getUid(), "false");
                                    FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("likedBy").updateChildren(liked);
                                }
                            });

                        }else {
                            FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("likesCount").setValue(Integer.parseInt(snapshot.child("likesCount").getValue().toString()) + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.likescount.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heartfilled,0,0,0);
                                    HashMap<String, Object> liked = new HashMap<String, Object>();
                                    liked.put(FirebaseAuth.getInstance().getUid(), "true");
                                    FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("likedBy").updateChildren(liked);

                                }
                            });
                        }
                        }

                        @Override
                        public void onCancelled (@NonNull DatabaseError error){

                        }
                });
            }
        });


//        commentBottomDialoge



        holder.commentcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.comment_layout);
                EditText typeComment;
                ImageView sendComment;
                RecyclerView comRv = bottomSheetDialog.findViewById(R.id.comment_rv);
                ArrayList<comment_model> comList = new ArrayList<>();
                typeComment = bottomSheetDialog.findViewById(R.id.typeComment);
                sendComment = bottomSheetDialog.findViewById(R.id.sendComment);
                comment_adapter commentAdapter = new comment_adapter(context,comList);


                FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("commentList").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        comList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            comment_model mdl = dataSnapshot.getValue(comment_model.class);
                            comList.add(mdl);
                        }
                        commentAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                comRv.setLayoutManager(linearLayoutManager);
                comRv.setAdapter(commentAdapter);

                sendComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        comment_model model = new comment_model(typeComment.getText().toString(), new Date().getTime(),FirebaseAuth.getInstance().getUid());
                        FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("commentList").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Commented!", Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        FirebaseDatabase.getInstance().getReference("post").child(feeds_model.getFeedPostID()).child("commentCount").setValue(Integer.parseInt(snapshot.child("commentCount").getValue().toString()) + 1);
                                        typeComment.setText("");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });
                    }
                });


                bottomSheetDialog.show();
            }
        });


    }

    void showBottomSheetDialog() {


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView feedName, postDesc, likescount, commentcount, time;
        ImageView feedProfile, postImage, verified;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            feedName = itemView.findViewById(R.id.feed_name);
            postDesc = itemView.findViewById(R.id.postDesc);
            feedProfile = itemView.findViewById(R.id.chatProfileIn);
            postImage = itemView.findViewById(R.id.postImage);
            likescount = itemView.findViewById(R.id.like_count);
            commentcount = itemView.findViewById(R.id.comment_count);
            time = itemView.findViewById(R.id.dateofposting);
            verified = itemView.findViewById(R.id.feedVerify);
        }
    }
}
