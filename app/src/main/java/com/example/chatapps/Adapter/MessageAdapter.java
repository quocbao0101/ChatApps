package com.example.chatapps.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapps.Models.Chat;
import com.example.chatapps.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter{
    private final int  MSG_TYPE_LEFT = 0;
    private final int  MSG_TYPE_RIGHT = 1;
    private Context mContext;
    FirebaseUser firebaseUser;
    private List<Chat> ListChat;
    String imageURL;

    public MessageAdapter(Context mContext,List<Chat> ListChat,String imageURL)
    {
        this.mContext = mContext;
        this.ListChat = ListChat;
        this.imageURL = imageURL;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new ReceiverViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Chat chat = ListChat.get(position);
        if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).sender_mes.setText(chat.getMessage());
        }
        else {
            ((ReceiverViewHolder) holder).receiver_mes.setText(chat.getMessage());
            if(imageURL.equals("default")) {
                ((ReceiverViewHolder) holder).receiver_avatar.setImageResource(R.mipmap.icon_avatar_foreground);
            }
            else Glide.with(mContext).load(imageURL).into(((ReceiverViewHolder)holder).receiver_avatar);
        }

        if(holder.getClass() == SenderViewHolder.class)
        {
            if(position == ListChat.size()-1)
            {
                if(chat.isDaxem())
                {
                    ((SenderViewHolder)holder).txtdaxem.setText("Đã xem");
                }
                else ((SenderViewHolder)holder).txtdaxem.setText("");
            }
            else ((SenderViewHolder)holder).txtdaxem.setVisibility(View.GONE);
        }
        else
        {
            if(position == ListChat.size()-1)
            {
                if(chat.isDaxem())
                {
                    ((ReceiverViewHolder)holder).txtseen.setText("Đã xem");
                }
                else ((ReceiverViewHolder)holder).txtseen.setText("");
            }
            else ((ReceiverViewHolder)holder).txtseen.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount()
    {
        if (ListChat != null) {
            return ListChat.size();
        }
        return 0;
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder
    {
        TextView sender_mes;
        TextView txtdaxem;
        public SenderViewHolder(@NonNull View view)
        {
            super(view);
            sender_mes = view.findViewById(R.id.sender_body);
            txtdaxem = view.findViewById(R.id.txt_seen);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtseen;
        TextView receiver_mes;
        ImageView receiver_avatar;
        public ReceiverViewHolder(@NonNull View itemview)
        {
            super(itemview);
            receiver_mes = itemview.findViewById(R.id.message_body);
            receiver_avatar = itemview.findViewById(R.id.avatar);
            txtseen = itemview.findViewById(R.id.txt_seen);
        }
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(ListChat.get(position).getSender().equals(firebaseUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else return MSG_TYPE_LEFT;

    }
}