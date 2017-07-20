package mudit.com.myproject.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mudit.com.myproject.Activities.NewPostActivity;
import mudit.com.myproject.Adapters.RecentPostAdapter;
import mudit.com.myproject.DatabseInfo.Post;
import mudit.com.myproject.DialogBox.CustomDialog;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

public class RecentPostFragment extends Fragment {



    public RecentPostFragment() {
        // Required empty public constructor
    }

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference mdatabase;
    RecyclerView rvRecentPosts;
    ArrayList<Post> posts=new ArrayList<Post>();
    ArrayList<String> postId=new ArrayList<>();
    ImageButton imgNewPost;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        progressDialog=new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading Data ...");
//        progressDialog.show();
        final CustomDialog cdd=new CustomDialog(getActivity(),getActivity());
        cdd.show();
        final View rootView=inflater.inflate(R.layout.fragment_recent_post, container, false);
        rvRecentPosts=(RecyclerView)rootView.findViewById(R.id.rvRecentPosts);
        final RecentPostAdapter recentPostAdapter=new RecentPostAdapter(posts,getActivity(),postId);
        rvRecentPosts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRecentPosts.setAdapter(recentPostAdapter);
        imgNewPost=(ImageButton)rootView.findViewById(R.id.imgNewPost);
        imgNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),NewPostActivity.class));
            }
        });
        mdatabase=FirebaseDatabase.getInstance().getReference();
//        Query postsQuery=mdatabase.child("posts");
//        Log.d("MA", "onCreateView: "+postsQuery);
//        mdatabase.child("Posts").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    String postid=snapshot.getKey();
//                    Post post=snapshot.getValue(Post.class);
//                    Log.d("abcd", "onDataChange: "+ post.getBody());
//                    posts.add(post);
//                    postId.add(postid);
//                }
//                Log.d("abcd", "onCreateView: "+posts.size());
//
//
//                recentPostAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        mdatabase.child("Posts").limitToLast(1000).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String postid=dataSnapshot.getKey();
                    Post post=dataSnapshot.getValue(Post.class);
                    Log.d("abcd", "onDataChange: "+ post.getBody());
                    posts.add(0,post);
                    postId.add(postid);
                recentPostAdapter.notifyDataSetChanged();
//                progressDialog.dismiss();
                cdd.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                progressDialog.dismiss();
//            }
//        }, 5000);
        return rootView;
    }

}
