package mudit.com.myproject.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mudit.com.myproject.Adapters.ContactsAdapter;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

public class ContactsActivity extends AppCompatActivity {
    ArrayList<User> users=new ArrayList<>();
    ArrayList<String> userid=new ArrayList<>();
    RecyclerView rvContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Friends");
        rvContacts=(RecyclerView)findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        final ContactsAdapter contactsAdapter=new ContactsAdapter(users,this,userid);
        rvContacts.setAdapter(contactsAdapter);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
        String authUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("Friends").child(authUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key=dataSnapshot.getKey();
                User user=dataSnapshot.getValue(User.class);
                users.add(user);
                userid.add(key);
                Log.d("abcde", "onChildAdded: "+ key+ "  "+user.getUsername());
                contactsAdapter.notifyDataSetChanged();
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
