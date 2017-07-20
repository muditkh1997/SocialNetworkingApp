package mudit.com.myproject.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mudit.com.myproject.Adapters.MessageActivityAdapter;
import mudit.com.myproject.DatabseInfo.Message;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

public class MessageActivity extends AppCompatActivity {
    ArrayList<Message> Readmessages=new ArrayList<>();
    ArrayList<Message> Unreadmessages=new ArrayList<>();
    ArrayList<Message> messages=new ArrayList<>();
    ArrayList<String> userId=new ArrayList<>();
    ImageButton imgContacts;
    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    Message message;
    RecyclerView rvChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        imgContacts=(ImageButton)findViewById(R.id.imgContacts);
        rvChat=(RecyclerView)findViewById(R.id.rvNewMessages);
        final MessageActivityAdapter messageActivityAdapter=new MessageActivityAdapter(messages,this,userId);
        rvChat.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        rvChat.setAdapter(messageActivityAdapter);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final String authUserId=auth.getCurrentUser().getUid();
        final DatabaseReference dr=databaseReference;
//        databaseReference.child("Messages").child(authUserId).orderByKey().orderByKey().orderByChild("status").equalTo("Not Seen").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Message message=dataSnapshot.getValue(Message.class);
//                messages.add(message);
//                messageActivityAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        databaseReference.child("Messages").child(authUserId).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String userId=dataSnapshot.getValue().toString();
                final String key=dataSnapshot.getKey();
//                Log.d("abcde", "onChildAdded: "+ key+"         " +  userId);
//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    message=snapshot.getValue(Message.class);
//                    Log.d("abcde", "onChildAdded: "+message.getMessage());
//                }

                dr.child("Messages").child(authUserId).child(key).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("abcde", "onDataChange: "+dataSnapshot.getValue().toString());
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            message=snapshot.getValue(Message.class);}
                        Log.d("abcde", "onDataChange: "+message.getMessage());
                        if(message.getStatus().equals("Not Seen"))
                            messages.add(message);
                        else
                            messages.add(message);
                        userId.add(key);
                        messageActivityAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
        imgContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,ContactsActivity.class));
            }
        });
    }

}
