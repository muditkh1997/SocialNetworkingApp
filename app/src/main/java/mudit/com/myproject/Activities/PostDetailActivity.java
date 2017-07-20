package mudit.com.myproject.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import mudit.com.myproject.Adapters.CommentAdapter;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

public class PostDetailActivity extends AppCompatActivity {
    TextView tvPostAutor,tvPostBody;
    EditText etComment;
    ImageView tvPostDetailPicture;
    Button btPostComment;
    RecyclerView rvComments;
    ArrayList<mudit.com.myproject.DatabseInfo.Comment> comments=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        Intent i=getIntent();
        final String postId=i.getStringExtra("postId");
        String authorName=i.getStringExtra("authorName");
        String messageBody=i.getStringExtra("messageBody");
        String postPictureUri=i.getStringExtra("postPictureUri");
        Log.d("abcde", "onCreate: "+authorName+"  "+messageBody);
        tvPostAutor=(TextView)findViewById(R.id.tvDetailPostAuthor);
        tvPostBody=(TextView)findViewById(R.id.tvDetailPostBody);
        etComment=(EditText)findViewById(R.id.etComment);
        btPostComment=(Button)findViewById(R.id.btPostComment);
        rvComments=(RecyclerView)findViewById(R.id.rvComments);
        tvPostDetailPicture=(ImageView)findViewById(R.id.tvDetailPostPicture);
        tvPostAutor.setText(authorName);
        tvPostBody.setText(messageBody);

        if(!postPictureUri.equals("none"))
            Picasso.with(this).load(Uri.parse(postPictureUri)).fit().centerInside().into(tvPostDetailPicture);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        final CommentAdapter commentAdapter=new CommentAdapter(comments,this);
        rvComments.setAdapter(commentAdapter);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        FirebaseUser auth= FirebaseAuth.getInstance().getCurrentUser();
        final String authUserId=auth.getUid();
        final User[] user = new User[1];
        databaseReference.child("users").child(authUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user[0] =dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentBody=etComment.getText().toString();
                if(TextUtils.isEmpty(commentBody)){
                    Toast.makeText(PostDetailActivity.this,"Nothing to post",Toast.LENGTH_SHORT).show();
                    return;
                }
                mudit.com.myproject.DatabseInfo.Comment comment=new mudit.com.myproject.DatabseInfo.Comment(authUserId,user[0].getUsername(),commentBody);
                String key=databaseReference.child("Posts-Comments").child(postId).push().getKey();
                databaseReference.child("Posts-Comments").child(postId).child(key).setValue(comment);
                etComment.setText("");
            }
        });
        databaseReference.child("Posts-Comments").child(postId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mudit.com.myproject.DatabseInfo.Comment comment=dataSnapshot.getValue(mudit.com.myproject.DatabseInfo.Comment.class);
                comments.add(comment);
                commentAdapter.notifyDataSetChanged();
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
}
