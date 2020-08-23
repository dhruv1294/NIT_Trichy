package com.example.nittrichy.Adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.CommentsActivity;
import com.example.nittrichy.EditPostActivity;
import com.example.nittrichy.Models.FeedPost;
import com.example.nittrichy.Notifications.AlarmService;
import com.example.nittrichy.Notifications.AlertReciever;
import com.example.nittrichy.PostActivity;
import com.example.nittrichy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.nittrichy.HomePostFragment.bookMark;
import static com.example.nittrichy.HomePostFragment.likeChecker;



public class FeedPostAdapter extends RecyclerView.Adapter<FeedPostAdapter.ViewHolder> {
    Context context;
    ArrayList<FeedPost> feedPosts;
    Activity a;
    int likeCount=0;
    int admin;
    String currentUserId;
    public static DatabaseReference mdatabaseLikes;
    public static DatabaseReference mDatabaseBookmark;
    PostActivity postActivity;

    public FeedPostAdapter(Context context, Activity a, ArrayList<FeedPost> feedPosts,int admin){
     this.context = context;
     this.feedPosts = feedPosts;
     this.a =a;
     this.admin = admin;
    }


    @NonNull
    @Override
    public FeedPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid()!=null) {
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        mdatabaseLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseBookmark = FirebaseDatabase.getInstance().getReference().child("Bookmarks");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedPostAdapter.ViewHolder holder, final int position) {
        final String postKey = feedPosts.get(position).getKey();
        holder.menuButton.setVisibility(View.INVISIBLE);

        holder.postTitle.setText(feedPosts.get(position).getTitle());
        holder.postCard.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale));

        holder.postTime.setText(feedPosts.get(position).getTime());
        holder.postDescription.setText(feedPosts.get(position).getDesc());
        Picasso.with(context).load(feedPosts.get(position).getImage()).into(holder.postImage);
        setLikeButtonStatus(postKey,holder);
        setBookmarkButtonStatus(postKey,holder);

        holder.postLikeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeChecker = true;
                mdatabaseLikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(likeChecker.equals(true)){
                            if(currentUserId!=null) {
                                if (snapshot.child(postKey).hasChild(currentUserId)) {
                                    mdatabaseLikes.child(postKey).child(currentUserId).removeValue();


                                    likeChecker = false;
                                } else {
                                    mdatabaseLikes.child(postKey).child(currentUserId).setValue(true);
                                    likeChecker = false;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsIntent = new Intent(context, CommentsActivity.class);
                commentsIntent.putExtra("PostKey",postKey);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(a,v.findViewById(R.id.postCommentButton),"CommentButton");
                context.startActivity(commentsIntent,optionsCompat.toBundle());
            }
        });

        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookMark = true;
                mDatabaseBookmark.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(bookMark.equals(true)){
                            if(currentUserId!=null) {
                                if (snapshot.child(postKey).hasChild(currentUserId)) {
                                    mDatabaseBookmark.child(postKey).child(currentUserId).removeValue();
                                    Intent intent = new Intent(context, AlarmService.class);

                                    context.stopService(intent);

                                    bookMark = false;
                                } else {
                                    mDatabaseBookmark.child(postKey).child(currentUserId).setValue(true);
                                    String date = feedPosts.get(position).getDeadlineDate();
                                    String time = feedPosts.get(position).getDeadlineTime();
                                    if(date.equals("nil") || time.equals("nil") ){
                                        Log.i("no","deadline");
                                    }else {
                                        Log.i("date and Time",date + " " + time);

                                        String[] date1 = date.split("/");
                                        String[] time1 = time.split(":");
                                        Calendar c = Calendar.getInstance();
                                        c.set(Integer.parseInt(date1[2])-10, Integer.parseInt(date1[1]), Integer.parseInt(date1[0]), Integer.parseInt(time1[0]), Integer.parseInt(time1[1]));
                                        c.set(Calendar.YEAR,Integer.parseInt(date1[2]));
                                        c.set(Calendar.MONTH,Integer.parseInt(date1[1]));
                                        c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(date1[0])-1);
                                        c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time1[0]));
                                        c.set(Calendar.MINUTE,Integer.parseInt(time1[1]));
                                        long millis = c.getTimeInMillis();
                                        //postActivity.setNotification(c);
                                        Intent intent = new Intent(context, AlarmService.class);
                                        intent.putExtra("millis",millis);
                                        intent.putExtra("date",date1);
                                        intent.putExtra("time",time1);
                                        intent.putExtra("title",feedPosts.get(position).getTitle());
                                        ContextCompat.startForegroundService(context,intent);
                                    }

                                    bookMark = false;
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


         if(admin==1){
             holder.menuButton.setVisibility(View.VISIBLE);
             holder.menuButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(context, EditPostActivity.class);
                     intent.putExtra("PostKey", postKey);
                     context.startActivity(intent);
                 }
             });
         }


    }
    public void setLikeButtonStatus(final String postkey,@NonNull final FeedPostAdapter.ViewHolder holder){
        mdatabaseLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(currentUserId!=null) {
                    if (snapshot.child(postkey).hasChild(currentUserId)) {
                        likeCount = (int) snapshot.child(postkey).getChildrenCount();
                        holder.postLikeImage.setImageResource(R.drawable.ic_favorite_accent_24dp);
                        holder.postLikeCount.setText(Integer.toString(likeCount) + " Likes");
                    } else {
                        likeCount = (int) snapshot.child(postkey).getChildrenCount();
                        holder.postLikeImage.setImageResource(R.drawable.ic_favorite_grey_24dp);
                        holder.postLikeCount.setText(Integer.toString(likeCount) + " Likes");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setBookmarkButtonStatus(final String postkey,@NonNull final FeedPostAdapter.ViewHolder holder){
        mDatabaseBookmark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(currentUserId!=null){
                    if(snapshot.child(postkey).hasChild(currentUserId)){
                        holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    }else{
                        holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return feedPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView postTitle,postDescription,postTime,postLikeCount;
        ImageView postImage,postLikeImage,commentButton,bookmarkButton;
        ImageButton menuButton;
        CardView postCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.feedPostTitle);
            postDescription = itemView.findViewById(R.id.feedPostDescription);
            postTime = itemView.findViewById(R.id.feedPostDate);
            postImage = itemView.findViewById(R.id.feedPostImage);
            postLikeImage =itemView.findViewById(R.id.feedPostLike);
            postLikeCount = itemView.findViewById(R.id.postLikeCount);
            commentButton = itemView.findViewById(R.id.postCommentButton);
            menuButton = itemView.findViewById(R.id.feedMenuAdmin);
            postCard = itemView.findViewById(R.id.postcard);
            bookmarkButton = itemView.findViewById(R.id.bookmarkPost);

        }
    }
}
