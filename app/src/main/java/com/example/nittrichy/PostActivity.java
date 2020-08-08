package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nittrichy.Notifications.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class PostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TextView welcomeUser;

    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    String user_id,username;
    private DatabaseReference UsersRef;
    String topic = "/topics/notification";
    static int previousState;
    Fragment fragment1,fragment2;
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
        setContentView(R.layout.activity_post);

        switch (previousState){
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomePostFragment()).commit();
                break;
            case 1:
                notice();
                break;
            case 2:
                news();
                break;
            case 3:
                places();
                break;
            case 4:
                contacts();
                break;


        }

       // setTitle("NIT TRICHY FEEDS");

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        setUpToolBar();
        UsersRef.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username = snapshot.child("name").getValue().toString();
                    welcomeUser.setText("Hello "+username+"!!");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("user",mAuth.getCurrentUser().getEmail().toString());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent intent = new Intent(PostActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        };
        navigationView = findViewById(R.id.navigation_view);
        welcomeUser = navigationView.getHeaderView(0).findViewById(R.id.welcomeUser);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                 switch (item.getItemId()){
                     case R.id.feedsNITT:
                         drawerLayout.closeDrawers();
                         feeds();
                         break;
                     case R.id.messageAdmin:
                         drawerLayout.closeDrawers();
                         previousState=0;
                         updateToken(FirebaseInstanceId.getInstance().getToken());

                         Intent intent = new Intent(PostActivity.this,MessageActivity.class);
                         intent.putExtra("user_id",user_id);
                         intent.putExtra("user_name",username);
                         startActivity(intent);
                         break;
                     case R.id.noticesNITT:
                         drawerLayout.closeDrawers();
                         notice();
                         break;
                     case R.id.newsNITT:
                         drawerLayout.closeDrawers();
                         news();
                         break;
                     case R.id.placesNITT:
                         drawerLayout.closeDrawers();
                         places();
                         break;
                     case R.id.contactsNITT:
                         drawerLayout.closeDrawers();
                        contacts();
                         break;


                 }
                return true;
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("notification").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("topic","subscribed");
                //Toast.makeText(PostActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Log.i("update","token");
        Token token1 = new Token(token);
        reference.child(user_id).setValue(token1);
    }

    private void setUpToolBar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
         moveTaskToBack(true);
        //super.onBackPressed();
    }
    private void feeds(){
        previousState=0;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomePostFragment()).commit();
        setTitle("NIT TRICHY FEEDS");
    }
    private void notice(){
        previousState=1;
        Bundle arg= new Bundle();
        arg.putString("fragment","notice");
        fragment1= new ViewNoticeFilesFragment();
        fragment1.setArguments(arg);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment1).commit();
        setTitle("NOTICES");
    }
    private void news(){
        previousState=2;

        Bundle arg1= new Bundle();
        arg1.putString("fragment","news");
        fragment2 = new ViewNoticeFilesFragment();
        fragment2.setArguments(arg1);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,fragment2).commit();
        setTitle("Press Releases");
    }
    private void places(){
        previousState=3;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new PlacesFragment()).commit();
        setTitle("Places:");
    }
    private void contacts(){
        previousState=4;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new ContactsFragment()).commit();
        setTitle("Contacts:");
    }

}
