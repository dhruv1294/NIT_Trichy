package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.nittrichy.Adapters.UsersAdapter;
import com.example.nittrichy.Models.ChatList;
import com.example.nittrichy.Models.Chats;
import com.example.nittrichy.Models.Users;
import com.example.nittrichy.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserChatsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> UsersList;
    private ArrayList<Users> mUsers;
    DatabaseReference chatReference;
    FirebaseUser fuser;
    UsersAdapter usersAdapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chats);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Messages:");
        recyclerView = findViewById(R.id.chatList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //manager.setReverseLayout(true);
        //manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        fuser = FirebaseAuth.getInstance().getCurrentUser();


        UsersList = new ArrayList<>();
        mUsers = new ArrayList<>();


        chatReference = FirebaseDatabase.getInstance().getReference("Chats");
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UsersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chat = snapshot.getValue(Chats.class);

                    if(chat.getSender().equals(fuser.getUid())){
                        UsersList.add(chat.getReciever());
                    }
                    if(chat.getReciever().equals(fuser.getUid())){
                        UsersList.add(chat.getSender());
                    }
                }
                Log.i("size",Integer.toString(UsersList.size()));
                Log.i("first",UsersList.get(0));
                Collections.reverse(UsersList);
                Log.i("first after",UsersList.get(0));
                Set<String> hashSet = new HashSet<String>(UsersList);
                UsersList.clear();
                UsersList.addAll(hashSet);


                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());





    }


    public void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }


    private void readChats() {

        mUsers = new ArrayList<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);

                    for (String id : UsersList) {
                        if(user.getuserid()!=null) {
                            if (user.getuserid().equals(id)) {

                                mUsers.add(user);
                            }
                        }
                    }
                }
               usersAdapter = new UsersAdapter(UserChatsActivity.this, mUsers);
                recyclerView.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}




