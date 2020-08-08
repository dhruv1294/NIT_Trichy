package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nittrichy.Adapters.CommentsAdapter;
import com.example.nittrichy.Adapters.FeedPostAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView commentsList;
    private ImageButton sendCommentButton;
    private ArrayList<Comments> comments;
    private CommentsAdapter commentsAdapter;
    private EditText commentInputText;
    private String postKey,currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        postKey = getIntent().getStringExtra("PostKey");
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UsersRef.keepSynced(true);
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Comments");
        PostsRef.keepSynced(true);
        commentsList = findViewById(R.id.commentsRecyclerView);
        comments = new ArrayList<>();
        commentsAdapter =  new CommentsAdapter(this,comments);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
       // mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        commentsList.setLayoutManager(mLayoutManager);
        commentsList.setAdapter(commentsAdapter);
        sendCommentButton = findViewById(R.id.sendCommentButton);
        commentInputText = findViewById(R.id.commentInput);
        commentsList.setHasFixedSize(true);
        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.removeAll(comments);
                for(DataSnapshot child : snapshot.getChildren()){
                    Comments commentItem = new Comments(child.getValue(Comments.class).getComment(),child.getValue(Comments.class).getDate(),child.getValue(Comments.class).getTime(),child.getValue(Comments.class).getUsername());
                    comments.add(commentItem);
                    commentsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersRef.child("Admins").child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            ValidateComment("Admin");
                            commentInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                UsersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            String userName = snapshot.child("name").getValue().toString();
                         //   Toast.makeText(CommentsActivity.this, userName+"commented", Toast.LENGTH_SHORT).show();
                            ValidateComment(userName);
                            commentInputText.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }






    private void ValidateComment(final String name){
        String commentText = commentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText)){
            Toast.makeText(this, "Please write text....", Toast.LENGTH_SHORT).show();
        }else{
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-YYYY");
            final String saveCurrentDate = currentDate.format(calFordDate.getTime());

            Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM");
            final String saveCurrentTime = currentTime.format(calFordDate.getTime());

            final String randomKey = currentUserId + saveCurrentDate + saveCurrentTime;

            HashMap commentsMap = new HashMap();

            commentsMap.put("comment",commentText);
            commentsMap.put("date",saveCurrentDate);
            commentsMap.put("time",saveCurrentTime);
            commentsMap.put("username",name);
            PostsRef.push().updateChildren(commentsMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CommentsActivity.this, name + " Commented Successfully...", Toast.LENGTH_SHORT).show();
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                commentsAdapter.notifyDataSetChanged();
                                Log.i("size",Integer.toString(comments.size()));

                                imm.hideSoftInputFromWindow(CommentsActivity.this.getCurrentFocus().getWindowToken(), 0);
                            }else{
                                Toast.makeText(CommentsActivity.this, "Error! Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
