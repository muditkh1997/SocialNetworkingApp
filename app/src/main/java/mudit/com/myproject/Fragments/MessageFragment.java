package mudit.com.myproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mudit.com.myproject.Activities.ContactsActivity;
import mudit.com.myproject.Activities.MessageActivity;
import mudit.com.myproject.Adapters.MessageActivityAdapter;
import mudit.com.myproject.DatabseInfo.Message;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;


public class MessageFragment extends Fragment {
    ArrayList<Message> Readmessages=new ArrayList<>();
    ArrayList<Message> Unreadmessages=new ArrayList<>();
    ArrayList<Message> messages=new ArrayList<>();
    ArrayList<String> userId=new ArrayList<>();
    ImageButton imgContacts;
    final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    Message message;
    RecyclerView rvChat;
    public static final String TAG="Fragment";
    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View rootView=inflater.inflate(R.layout.activity_message, container, false);
        imgContacts=(ImageButton)rootView.findViewById(R.id.imgContacts);
        rvChat=(RecyclerView)rootView.findViewById(R.id.rvNewMessages);
        final MessageActivityAdapter messageActivityAdapter=new MessageActivityAdapter(messages,getActivity(),userId);
        rvChat.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                startActivity(new Intent(getActivity(),ContactsActivity.class));
            }
        });


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: 2");
    }
}
