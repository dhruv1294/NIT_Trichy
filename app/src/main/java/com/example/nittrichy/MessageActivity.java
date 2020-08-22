package com.example.nittrichy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nittrichy.Adapters.MessageAdapter;
import com.example.nittrichy.Models.Chats;
import com.example.nittrichy.Models.Users;
import com.example.nittrichy.Notifications.Client;
import com.example.nittrichy.Notifications.Data;
import com.example.nittrichy.Notifications.MyResponse;
import com.example.nittrichy.Notifications.Sender;
import com.example.nittrichy.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageButton sendMessageButton ;
    private EditText userMessageInput;
    private RecyclerView messageList;

    private String username,userid,senderId,recieverId;
    private static String AdminId;
    private DatabaseReference AdminRef;
    private FirebaseAuth mAuth;
    MessageAdapter messageAdapter;
    ArrayList<Chats> mchat;
    RecyclerView recyclerView;
    ApiService apiService;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        InitializeFields();
        mAuth = FirebaseAuth.getInstance();
        username = getIntent().getStringExtra("user_name");

        userid = getIntent().getStringExtra("user_id");
        AdminRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Admins");
        AdminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    AdminId = child.getKey();
                    Log.i("adminId",AdminId);
                    if(mAuth.getCurrentUser().getUid().equals(AdminId)){
                        toolbar.setTitle(username);

                        senderId = AdminId;
                        recieverId=userid;
                        readMessage(senderId,recieverId);
                    }else{
                        senderId = mAuth.getCurrentUser().getUid();
                        recieverId = AdminId;
                        readMessage(senderId,recieverId);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Bundle bundle = getIntent().getExtras();
        if(userid==null){
            Log.i("user",mAuth.getCurrentUser().getUid());
            userid = bundle.getString("userid");
            Log.i("userid",userid);
            String path;
            if(userid.equals(AdminId)){
                Log.i("userid","adminid");
                path = "Users/Admins";

            }else{
                Log.i("userid","else");

                path ="Users";
            }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path).child(userid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        if (userid.equals(AdminId)) {
                            username = "Admin";
                        } else {
                            username = snapshot.getValue(Users.class).getName();
                        }

                        readMessage(userid, mAuth.getCurrentUser().getUid());
                        getSupportActionBar().setTitle(username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        recyclerView = findViewById(R.id.messagesListUsers);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mchat = new ArrayList<>();
        messageAdapter = new MessageAdapter(MessageActivity.this,mchat);
        recyclerView.setAdapter(messageAdapter);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);



        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readMessage(senderId,recieverId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String message = userMessageInput.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(MessageActivity.this, "Please enter a message first...", Toast.LENGTH_SHORT).show();
                }else {
                    SendMessage(senderId, recieverId,message);
                    userMessageInput.setText("");
                }
            }
        });
    }

    private void InitializeFields() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sendMessageButton = findViewById(R.id.sendMessageButton);

        userMessageInput = findViewById(R.id.inputMessage);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void SendMessage(String sender, final String reciever, String mess){
        DatabaseReference chats = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("reciever",reciever);
        hashMap.put("message",mess);
        chats.child("Chats").push().setValue(hashMap);
        messageAdapter.notifyDataSetChanged();
        final DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference().child("ChatList").child(recieverId).child(senderId);
        chatsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatsRef.child("id").setValue(senderId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String path;
        if(mAuth.getCurrentUser().getUid().equals(AdminId)){
            path = "Users/Admins";
        }else{
            path ="Users";
        }


        final String msg = mess;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path).child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                if(users!=null) {
                    if (notify) {
                        Log.i("reached","here");
                        Log.i("reciever",reciever);
                        Log.i("sender",users.getName());
                        sendNotification(reciever, users.getName(), msg);
                    }
                    notify = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendNotification(final String reciever , final String username , final String message ){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(reciever);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()){
                    Token token = child.getValue(Token.class);
                    Log.i("snapshot","reached");
                    Data data = new Data(mAuth.getCurrentUser().getUid(),R.mipmap.ic_launcher2,username+": " + message,"New Message",reciever);
                    Sender sender = new Sender(data , token.getToken());
                    apiService.sendNotificattion(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    Log.i("msg","delivered");


                                            //Toast.makeText(MessageActivity.this, "Delivered", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readMessage(final String myId, final String userid){

        DatabaseReference ChatRef = FirebaseDatabase.getInstance().getReference().child("Chats");
        ChatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();

                for(DataSnapshot child : snapshot.getChildren()){
                    Chats chat = child.getValue(Chats.class);
                    if(chat.getReciever().equals(myId) && chat.getSender().equals(userid) || chat.getSender().equals(myId) && chat.getReciever().equals(userid)){
                        mchat.add(chat);
                        messageAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
