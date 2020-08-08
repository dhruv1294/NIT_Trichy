package com.example.nittrichy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.Comments;
import com.example.nittrichy.R;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    Context context;
    ArrayList <Comments> commentList;

    public CommentsAdapter(Context context, ArrayList<Comments> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.all_comments_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
     holder.userName.setText(commentList.get(position).getUsername());
     holder.commentText.setText(commentList.get(position).getComment());
        holder.commentDate.setText(commentList.get(position).getDate());
        holder.commentTime.setText("Time:"+ commentList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName,commentDate,commentTime,commentText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.comment_username);
            commentDate = itemView.findViewById(R.id.comment_date);
            commentTime = itemView.findViewById(R.id.comment_time);
            commentText = itemView.findViewById(R.id.comment_text);
        }
    }


}
