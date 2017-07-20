package mudit.com.myproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mudit.com.myproject.Activities.OnetoOneChat;
import mudit.com.myproject.DatabseInfo.Message;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

/**
 * Created by admin on 7/8/2017.
 */

public class MessageActivityAdapter extends RecyclerView.Adapter<MessageActivityAdapter.MessageViewHolder>{
    ArrayList<Message> messages;
    Context context;
    ArrayList<String> userId;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    public MessageActivityAdapter(ArrayList<Message> messages, Context context,ArrayList<String> userId) {
        this.messages = messages;
        this.context = context;
        this.userId=userId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(R.layout.list_item_chat,parent,false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, int position) {
        final Message message=messages.get(position);
        final String userid=userId.get(position);
        databaseReference.child("users").child(userid).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                holder.tvSenderName.setText(user.getUsername());
                holder.tvMessageBody.setText(message.getMessage());
                if(message.getStatus().equals("Not Seen"))
                    holder.tvNew.setText("New");
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(context, OnetoOneChat.class);
                        i.putExtra("userId",userid);
                        context.startActivity(i);
                    }
                });
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

                }
                });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderName,tvMessageBody,tvNew;
        View rootView;
        public MessageViewHolder(View itemView) {
            super(itemView);
            tvSenderName=(TextView)itemView.findViewById(R.id.tvSenderName);
            tvMessageBody= (TextView) itemView.findViewById(R.id.tvMessageBody);
            tvNew=(TextView)itemView.findViewById(R.id.tvNew);
            rootView=itemView;
        }
    }
}
