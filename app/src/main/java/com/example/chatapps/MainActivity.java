package com.example.chatapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.chatapps.Adapter.ViewPayerAdapter;
import com.example.chatapps.Fragment.chat;
import com.example.chatapps.Fragment.profile;
import com.example.chatapps.Fragment.users;
import com.example.chatapps.Models.Chat;
import com.example.chatapps.Models.Users;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        anhxa();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUserName());
                if(users.getimageURL().equals("default"))
                {
                    profile_image.setImageResource(R.mipmap.ic_launcher_foreground);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(users.getimageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ViewPayerAdapter viewPayerAdapter = new ViewPayerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
                int unread = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isDaxem())
                    {
                        unread++;
                    }
                }
                if(unread == 0)
                {
                    viewPayerAdapter.addFragment(new chat(),"Chats");
                }
                else {viewPayerAdapter.addFragment(new chat(),"("+unread+") Chats"); }
                viewPayerAdapter.addFragment(new users(),"Người dùng");
                viewPayerAdapter.addFragment(new profile(),"Profile");

                viewPager.setAdapter(viewPayerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void anhxa()
    {
        profile_image = (CircleImageView)findViewById(R.id.profile_image);
        username = (TextView)findViewById(R.id.main_username);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dangxuat:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
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
        trangthai("offline");
    }
}