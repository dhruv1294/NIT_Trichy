package com.example.nittrichy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.Comments;
import com.example.nittrichy.Models.Chats;
import com.example.nittrichy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;

    Context context;
    ArrayList<Chats> messageList;
    FirebaseUser fUser;

    public MessageAdapter(Context context, ArrayList<Chats> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(context).inflate(R.layout.message_layout_right, parent, false);
            return new MessageAdapter.ViewHolder(v);
        }else{
            View v = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false);
            return new MessageAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chats chats = messageList.get(position);
        holder.showMessage.setText(chats.getMessage());


    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView showMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(messageList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
