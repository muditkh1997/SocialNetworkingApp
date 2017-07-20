package mudit.com.myproject.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mudit.com.myproject.Adapters.MessagesAdapter;
import mudit.com.myproject.DatabseInfo.Message;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

public class OnetoOneChat extends AppCompatActivity {
//    TextView tvName;
    RecyclerView rvChat;
    ArrayList<Message> messages=new ArrayList<>();
    EditText etMessage;
    Button btSend;
    final int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneto_one_chat);
//        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(tb);

        // Get the ActionBar here to configure the way it behaves.
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowCustomEnabled(true);
//        View view =getSupportActionBar().getCustomView();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_account_circle_40);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        tvName=(TextView)findViewById(R.id.tvName);
        etMessage=(EditText)findViewById(R.id.etMessage);
        btSend=(Button)findViewById(R.id.btSend);
        Intent intent=getIntent();
        final String userId=intent.getStringExtra("userId");
        rvChat=(RecyclerView)findViewById(R.id.rvChat);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);
        final MessagesAdapter messagesAdapter=new MessagesAdapter(messages,this);
        rvChat.setAdapter(messagesAdapter);
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(userId).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName=dataSnapshot.getValue().toString();
                setTitle(userName);
//                tvName.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseAuth auth=FirebaseAuth.getInstance();
        final String authUseId=auth.getCurrentUser().getUid();
//        databaseReference.child("Messages").child(authUseId).child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    Message message=snapshot.getValue(Message.class);
//                    Log.d("abcd", "onDataChange: "+message.getMessage());
//                    messages.add(message);
//                }
//                messagesAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.child("Messages").child(authUseId).child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Message message=dataSnapshot.getValue(Message.class);
                    dataSnapshot.getRef().child("status").setValue("Seen");
                    Log.d("abcd", "onDataChange: "+message.getMessage());
                    messages.add(message);


                messagesAdapter.notifyDataSetChanged();
                rvChat.scrollToPosition(messages.size()-1);
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
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=etMessage.getText().toString();
                if(TextUtils.isEmpty(message)){
                    Toast.makeText(OnetoOneChat.this,"Nothing to send",Toast.LENGTH_SHORT).show();
                    return;
                }
                databaseReference.child("users").child(authUseId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user=dataSnapshot.getValue(User.class);
                        String userName=user.getUsername();
                        Message message1=new Message("You",message,"Not Seen");
                        Message message2=new Message(userName,message,"Not Seen");
                       DatabaseReference mesgRef= databaseReference.child("Messages").child(authUseId).child(userId).push();
                        databaseReference.child("Messages").child(authUseId).child(userId).child(mesgRef.getKey()).setValue(message1);
                        databaseReference.child("Messages").child(userId).child(authUseId).child(mesgRef.getKey()).setValue(message2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                etMessage.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
