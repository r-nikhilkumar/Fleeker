package com.example.fleeker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fleeker.model.chats_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class chats_adapter extends RecyclerView.Adapter<chats_adapter.viewholder> {
    ArrayList<chats_model> chat_list;
    Context context;

    public chats_adapter(ArrayList<chats_model> chat_list, Context context) {
        this.chat_list = chat_list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chats_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public int getItemCount() {
        return chat_list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        chats_model chats_model = chat_list.get(position);
        holder.name.setText(chats_model.getName());
        holder.usrnm.setText(chats_model.getUsername());
        try {
            if (chats_model.getVerified().equals("true")) {
                holder.verified.setVisibility(View.VISIBLE);
            } else {
                holder.verified.setVisibility(View.GONE);
            }
        }catch (NullPointerException ignored){}
        Picasso.get().load(chats_model.getProfileImage()).into(holder.chatProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatWin.class);
                intent.putExtra("profileChat",chats_model.getProfileImage());
                intent.putExtra("nameChat", chats_model.getName());
                intent.putExtra("uidChat", chats_model.getUid());
                intent.putExtra("verified", chats_model.getVerified());
                context.startActivity(intent);
            }
        });
    }

    public class viewholder extends RecyclerView.ViewHolder{
        ImageView chatProfile, verified;
        TextView name, usrnm;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            chatProfile = itemView.findViewById(R.id.chatProfile);
            name = itemView.findViewById(R.id.chat_name);
            usrnm = itemView.findViewById(R.id.chat_username);
            verified = itemView.findViewById(R.id.chatVerify);
        }
    }
}
