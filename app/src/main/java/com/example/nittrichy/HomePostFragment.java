package com.example.nittrichy;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.nittrichy.Adapters.FeedPostAdapter;
import com.example.nittrichy.Models.FeedPost;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomePostFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<FeedPost> post_list;
    private DatabaseReference mdatabasePosts;
    public FeedPostAdapter recyclerAdapter;
    public static Boolean likeChecker = false;
    ProgressBar progressBar;



    public HomePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home_post, container, false);
       progressBar = view.findViewById(R.id.progress);
       progressBar.setVisibility(View.VISIBLE);


       recyclerView = view.findViewById(R.id.feedsList);
        post_list = new ArrayList<>();
      recyclerAdapter =  new FeedPostAdapter(getActivity(),getActivity(),post_list,0);
       LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        mdatabasePosts = FirebaseDatabase.getInstance().getReference().child("Posts");
        mdatabasePosts.keepSynced(true);

        //Query firstQuery = mdatabasePosts.limitToLast(3);
                mdatabasePosts.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               post_list.removeAll(post_list);
               progressBar.setVisibility(View.GONE);
               for(DataSnapshot child : snapshot.getChildren()){
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


       return view;
    }

}
