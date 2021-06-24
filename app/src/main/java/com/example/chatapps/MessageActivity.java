package com.example.chatapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapps.Adapter.MessageAdapter;
import com.example.chatapps.Models.Chat;
import com.example.chatapps.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView message_profile_image;
    TextView message_username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    ImageButton btnsend;
    RecyclerView message_view;
    EditText textsend;

    List<Chat> mListChat;
    Intent intent;

    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar message_toolbar = findViewById(R.id.message_toolbar);
        setSupportActionBar(message_toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        message_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        anhxa();
        intent = getIntent();

        String userid = intent.getStringExtra("userID");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String senderID = firebaseUser.getUid();

        // layout

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        message_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        message_view.setLayoutManager(linearLayoutManager);


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textsend.getText().toString();
                if(!msg.equals(""))
                {
                    sendMessage(senderID,userid,msg);
                }
                else Toast.makeText(MessageActivity.this,"Không được để trống ô chat",Toast.LENGTH_SHORT).show();
                textsend.setText("");
            }
        });
        // get username , image
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                message_username.setText(users.getUserName());
                if(users.getimageURL().equals("default"))
                {
                    message_profile_image.setImageResource(R.mipmap.icon_avatar_foreground);
                }
                else {
                    Glide.with(getApplicationContext()).load(users.getimageURL()).into(message_profile_image);
                }
                readMessage(senderID,userid,users.getimageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        daXem(userid);
    }
    public void anhxa()
    {
        message_profile_image = (CircleImageView) findViewById(R.id.message_profile_image);
        message_username = (TextView)findViewById(R.id.message_username);
        textsend = (EditText)findViewById(R.id.text_send);
        btnsend = (ImageButton)findViewById(R.id.btnsend);
        message_view = (RecyclerView)findViewById(R.id.recycler_view_message);

    }
    private void sendMessage(String sender,String receiver,String message)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final String userid = intent.getStringExtra("userID");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("daxem",false);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid()).child(userid);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });




    }
    private void readMessage(String myid,String userid,String imageurl)
    {
        mListChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                mListChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                        mListChat.add(chat);
                    }
                }
                MessageAdapter messageAdapter = new MessageAdapter(MessageActivity.this,mListChat,imageurl);
                message_view.setAdapter(messageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void daXem(String userid)
    {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid))
                    {
                        HashMap<String ,Object> hashMap = new HashMap<>();
                        hashMap.put("daxem",true);
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void trangthai(String tinhtrang)
    {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String ,Object> objectHashMap = new HashMap<>();
        objectHashMap.put("status", tinhtrang);
        reference.updateChildren(objectHashMap);
    }
    public void onResume()
    {
        super.onResume();
        trangthai("online");
    }
    public void onPause()
    {
        super.onPause();
        reference.removeEventListener(seenListener);
        trangthai("offline");
    }
}