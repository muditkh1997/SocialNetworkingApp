package mudit.com.myproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mudit.com.myproject.Adapters.RecentPostAdapter;
import mudit.com.myproject.DatabseInfo.Post;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

public class UserProfile extends AppCompatActivity {
    ImageButton imgFriend;
    ImageButton imgMessage;
    ImageView imgProfilePicture;
    RecyclerView rvUserPosts;
    ArrayList<Post> posts=new ArrayList<>();
    ArrayList<String> postId=new ArrayList<>();
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    StorageReference sr;
    public static final int GALLERY_INTENT=2;
    String userId,userName;
    int flag;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        imgFriend=(ImageButton)findViewById(R.id.imgFriend);
        imgMessage=(ImageButton)findViewById(R.id.imgMessage);
        imgProfilePicture=(ImageView)findViewById(R.id.imgProfilePicture);
        progressDialog=new ProgressDialog(this);
        Intent intent=getIntent();
        userName=intent.getStringExtra("userName");
        ((TextView)findViewById(R.id.tvUserName)).setText(userName);
        userId=intent.getStringExtra("userId");
        if(userName.equals("")){
            final FirebaseUser auth= FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference db=FirebaseDatabase.getInstance().getReference();
            userId=auth.getUid();
            db.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    userName=user.getUsername();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        rvUserPosts=(RecyclerView)findViewById(R.id.rvUserPosts);
        setTitle(userName);
        sr=FirebaseStorage.getInstance().getReference();
        sr.child("Profile-Pictures").child(userId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Picasso.with(UserProfile.this).load(uri).fit().centerCrop().into(imgProfilePicture);
                imgProfilePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(UserProfile.this, ViewImage.class);
                        intent.putExtra("uri",uri.toString());
                        startActivity(intent);
                    }
                });
            }
        });

        final RecentPostAdapter recentPostAdapter=new RecentPostAdapter(posts,UserProfile.this,postId);
        rvUserPosts.setLayoutManager(new LinearLayoutManager(UserProfile.this));
        rvUserPosts.setAdapter(recentPostAdapter);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final String authUserId=user.getUid();
        final String authUserEmail=user.getEmail();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Log.d("abcd", "onCreate: "+userId+"  "+authUserId);
        if(!userId.equals(authUserId))
        {
            imgFriend.setBackground(getResources().getDrawable(R.drawable.addfriends));
            flag=0;
            databaseReference.child("Friends").child(authUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        User user1=snapshot.getValue(User.class);
                        if(user1.getEmail()==authUserEmail){
                            flag=1;
                            imgFriend.setBackground(getResources().getDrawable(R.drawable.friends));
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            imgMessage.setBackground(getResources().getDrawable(R.drawable.message));


            imgFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag==0){
                        //add Friend
                        databaseReference.child("users").child(userId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user1=dataSnapshot.getValue(User.class);
                                databaseReference.child("Friends").child(authUserId).child(userId).setValue(user1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        databaseReference.child("users").child(authUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user1=dataSnapshot.getValue(User.class);
                                databaseReference.child("Friends").child(userId).child(authUserId).setValue(user1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        imgFriend.setBackground(getResources().getDrawable(R.drawable.friends));
                        flag=1;
                    }
                    else
                    {
                        //remove friend
                        databaseReference.child("Friends").child(authUserId).child(userId).removeValue();
                        databaseReference.child("Friends").child(userId).child(authUserId).removeValue();
//                        databaseReference.child("users").child(userId).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                User user1=dataSnapshot.getValue(User.class);
//                                databaseReference.child("Friends").child(authUserId).child(userId).removeValue();
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                        databaseReference.child("users").child(authUserId).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                User user1=dataSnapshot.getValue(User.class);
//                                databaseReference.child("Friends").child(userId).child(authUserId).removeValue();
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
                        imgFriend.setBackground(getResources().getDrawable(R.drawable.addfriends));
                        flag=0;
                    }

                }
            });
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(UserProfile.this,OnetoOneChat.class);
                    i.putExtra("userId",userId);
                    startActivity(i);
                }
            });
        }
        else
        {
            imgFriend.setBackground(getResources().getDrawable(R.drawable.capture));
            imgMessage.setBackground(getResources().getDrawable(R.drawable.gallery));
            imgFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,GALLERY_INTENT);
                }
            });
            imgMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Intent.ACTION_PICK);
                    i.setType("image/*");
                    startActivityForResult(i,GALLERY_INTENT);
                }
            });



        }
//        databaseReference.child("User-Posts").child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    String postid=snapshot.getKey();
//                    Post post=snapshot.getValue(Post.class);
//                    Log.d("abcd", "onDataChange: "+ post.getBody());
//                    posts.add(post);
//                    postId.add(postid);
//                }
//                recentPostAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.child("User-Posts").child(userId).limitToLast(1000).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String postid=dataSnapshot.getKey();
                Post post=dataSnapshot.getValue(Post.class);
                Log.d("abcd", "onDataChange: "+ post.getBody());
                posts.add(0,post);
                postId.add(postid);
                recentPostAdapter.notifyDataSetChanged();
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

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setMessage("Updating ... ");
            progressDialog.show();
            Uri uri=data.getData();
            sr= FirebaseStorage.getInstance().getReference();
            sr.child("Profile-Pictures").child(userId).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    Log.d("abcde", "onSuccess: "+downloadUri);
                    Picasso.with(UserProfile.this).load(downloadUri).fit().centerInside().into(imgProfilePicture);
//                    databaseReference.child("users").child(userId).child("profileUri").setValue(downloadUri);
                    Toast.makeText(UserProfile.this,"Profile picture updated",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this,"Uploading Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }}

}
