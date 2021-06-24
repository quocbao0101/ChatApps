package com.example.chatapps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapps.MessageActivity;
import com.example.chatapps.Models.Chat;
import com.example.chatapps.Models.Users;
import com.example.chatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{
    private FirebaseDatabase userinfoDatabase;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Context mContext;
    private List<Users> mUsers;
    private boolean ischat;
    String theLastMess;
    String currentUserID;

    public UserAdapter(Context mContext,List<Users> mUsers,boolean ischat)
    {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        Users users = mUsers.get(position);
        holder.item_username.setText(users.getUserName());
        if(users.getimageURL().equals("default"))
        {
            holder.item_profile.setImageResource(R.mipmap.icon_avatar_foreground);
        }
        else {
            Glide.with(mContext).load(users.getimageURL()).into(holder.item_profile);
        }
        if(ischat)
        {
            lastMessage(users.getUserId(),holder.lastmess);
        }
        else holder.lastmess.setVisibility(View.GONE);
        if(ischat)
        {
            if(users.getStatus().equals("online"))
            {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }
            else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userID",users.getUserId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (mUsers != null) {
            return mUsers.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView item_username;
        public ImageView item_profile;
        private CircleImageView img_on;
        private CircleImageView img_off;
        private TextView lastmess;
        public UserViewHolder(View itemView) {
            super(itemView);

            item_username = itemView.findViewById(R.id.item_username);
            item_profile = itemView.findViewById(R.id.item_image_URL);
            img_off = itemView.findViewById(R.id.status_off);
            img_on = itemView.findViewById(R.id.status_onl);
            lastmess = itemView.findViewById(R.id.lastMessage);
        }
    }
    private void lastMessage(final String userID,final TextView lastmess)
    {
        theLastMess = "default";
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            currentUserID = currentUser.getUid();
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(currentUserID) && chat.getSender().equals(userID)
                    || chat.getReceiver().equals(userID) && chat.getSender().equals(currentUserID))
                    {
                        theLastMess = chat.getMessage();
                    }
                }
                switch (theLastMess)
                {
                    case "default":
                        lastmess.setText("Không có tin nhắn");
                        break;

                    default:
                        lastmess.setText(theLastMess);
                        break;
                }
                theLastMess = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
