package com.example.fleeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fleeker.model.msgModelclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatWin extends AppCompatActivity {
    public static String senderImg,receiverIImg;
    String senderRoom, receiverRoom;
    EditText textmsg;
    ArrayList<msgModelclass> msgList;
    RecyclerView chat_msg_rv;
    messageAdapter msgAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_win);
        Picasso.get().load(getIntent().getStringExtra("profileChat")).into((ImageView)findViewById(R.id.chatProfileIn));
        ((TextView)findViewById(R.id.nameChatIn)).setText(getIntent().getStringExtra("nameChat"));
        textmsg = findViewById(R.id.typeComment);
        chat_msg_rv = findViewById(R.id.comment_rv);

        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("user_profilePic").getValue().toString();
                receiverIImg = getIntent().getStringExtra("profileChat");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        msgList = new ArrayList<>();

        senderRoom = FirebaseAuth.getInstance().getUid()+getIntent().getStringExtra("uidChat");
        receiverRoom = getIntent().getStringExtra("uidChat")+FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    msgModelclass msgModel = dataSnapshot.getValue(msgModelclass.class);
                    msgList.add(msgModel);
                }
                msgAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        msgAdapter = new messageAdapter(ChatWin.this, msgList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chat_msg_rv.setNestedScrollingEnabled(true);
        chat_msg_rv.setLayoutManager(linearLayoutManager);
        chat_msg_rv.setAdapter(msgAdapter);




        ((ImageView)findViewById(R.id.sendComment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = textmsg.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatWin.this, "Message can't be invalid!", Toast.LENGTH_SHORT).show();
                    return;
                }
                textmsg.setText("");
                Date date = new Date();
                msgModelclass messagess = new msgModelclass(message,FirebaseAuth.getInstance().getUid(),date.getTime());

                FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                            }
                        });
            }
        });




    }
}