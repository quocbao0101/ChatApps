package com.example.chatapps.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapps.Adapter.UserAdapter;
import com.example.chatapps.Models.Chatlist;
import com.example.chatapps.Models.Users;
import com.example.chatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class chat extends Fragment {
    private RecyclerView recyclerView_Chat;
    private UserAdapter userAdapter;
    private List<Users> mListUser_Chat;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private List<Chatlist> ListChat_User;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);

        recyclerView_Chat = view.findViewById(R.id.recycler_view_chatfragment);
        recyclerView_Chat.setHasFixedSize(true);
        recyclerView_Chat.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ListChat_User = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListChat_User.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    ListChat_User.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListChat_User.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if(chat.getSender().equals(firebaseUser.getUid()))
                    {
                        ListChat_User.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(firebaseUser.getUid()))
                    {
                        ListChat_User.add(chat.getSender());
                    }
                }
                docChat();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */


        return view;

    }

    private void chatList()
    {
        mListUser_Chat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListUser_Chat.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Users users = dataSnapshot.getValue(Users.class);
                    for(Chatlist chatlist: ListChat_User)
                    {
                        if(users.getUserId().equals(chatlist.getId()))
                        {
                            mListUser_Chat.add(users);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mListUser_Chat,true);
                recyclerView_Chat.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    private void docChat()
    {
        mListUser_Chat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {

                mListUser_Chat.clear();
                for (DataSnapshot dataSnapshot1: snapshot.getChildren())
                {
                    Users users = dataSnapshot1.getValue(Users.class);
                    for(String userid: ListChat_User)
                    {
                        if (users.getUserId().equals(userid))
                        {
                            if(mListUser_Chat.size() != 0)
                            {
                                for(Users users1 : mListUser_Chat)
                                {
                                    if(!users.getUserId().equals(users1.getUserId()))
                                    {
                                        mListUser_Chat.add(users);
                                    }
                                }
                            }
                            else {
                                mListUser_Chat.add(users);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(),mListUser_Chat,true);
                recyclerView_Chat.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */

}