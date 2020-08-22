package com.example.nittrichy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.MessageActivity;
import com.example.nittrichy.Models.Chats;
import com.example.nittrichy.Models.Users;
import com.example.nittrichy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Users> mUsers;
    String theLastMessage;

    public UsersAdapter(Context context, ArrayList<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getName());
        lastMessage(users.getuserid(),holder.lastMsg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("user_name",users.getName());
                intent.putExtra("user_id",users.getuserid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView lastMsg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernameChat);
            lastMsg = itemView.findViewById(R.id.last_msg);
        }
    }

    private void lastMessage(final String userid , final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    Chats chat = child.getValue(Chats.class);
                    if(chat.getReciever().equals(fUser.getUid()) && chat.getSender().equals(userid)){
                        theLastMessage = chat.getMessage();
                        last_msg.setText(theLastMessage);
                        last_msg.setTextColor(Color.GREEN);

                    }else if(chat.getReciever().equals(userid) && chat.getSender().equals(fUser.getUid())){
                        theLastMessage = chat.getMessage();
                        last_msg.setText(theLastMessage);
                        last_msg.setTextColor(Color.BLACK);
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_msg.setText("");
                        break;
                        default:
                            last_msg.setText(theLastMessage);
                            break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
