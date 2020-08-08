package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.nittrichy.Adapters.FeedPostAdapter;
import com.example.nittrichy.Models.FeedPost;
import com.example.nittrichy.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class PostAdminActivity extends AppCompatActivity {

    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private RecyclerView recyclerView;
    private ArrayList<FeedPost> post_list;
    private DatabaseReference mdatabasePosts;
    public static FeedPostAdapter recyclerAdapter;
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.post_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            mAuth.signOut();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_admin);
        mAuth = FirebaseAuth.getInstance();



        setUpToolBar();

//        Log.i("user",mAuth.getCurrentUser().getEmail().toString());
        floatingActionButton = findViewById(R.id.floatingActionButton);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(PostAdminActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        };

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostAdminActivity.this,AddPostActivity.class);
                startActivity(intent);
            }
        });

        recyclerView =findViewById(R.id.feedsListAdmin);
        post_list = new ArrayList<>();
        recyclerAdapter =  new FeedPostAdapter(this,PostAdminActivity.this,post_list,1);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        mdatabasePosts = FirebaseDatabase.getInstance().getReference().child("Posts");
        mdatabasePosts.keepSynced(true);
        mdatabasePosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post_list.removeAll(post_list);
                for(DataSnapshot child : snapshot.getChildren()){
                   //
                    FeedPost postItem = new FeedPost(child.getValue(FeedPost.class).getTitle(),child.getValue(FeedPost.class).getDesc(),child.getValue(FeedPost.class).getImage(),child.getValue(FeedPost.class).getTime(),child.getValue(FeedPost.class).getKey());
                    //FeedPost postItem = child.getValue(FeedPost.class);
                    post_list.add(postItem);
                    recyclerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.feedsNITT:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.messageAdmin:
                        drawerLayout.closeDrawers();
                        updateToken(FirebaseInstanceId.getInstance().getToken());
                        Intent intent = new Intent(PostAdminActivity.this,UserChatsActivity.class);

                        startActivity(intent);
                        break;
                    case R.id.noticesNITT:
                        drawerLayout.closeDrawers();
                        Intent intent1 = new Intent(PostAdminActivity.this,NoticePdfAdminActivity.class);
                        intent1.putExtra("activity","notice");
                        startActivity(intent1);

                        break;
                    case R.id.newsNITT:
                        drawerLayout.closeDrawers();
                        Intent intent2 = new Intent(PostAdminActivity.this,NoticePdfAdminActivity.class);
                        intent2.putExtra("activity","news");
                        startActivity(intent2);

                }
                return true;
            }
        });

       FirebaseMessaging.getInstance().unsubscribeFromTopic("notification").addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               //Toast.makeText(PostAdminActivity.this, "Unsubscribed", Toast.LENGTH_SHORT).show();
           }
       });

    }
    public void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(mAuth.getCurrentUser().getUid()).setValue(token1);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }
    private void setUpToolBar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

}
