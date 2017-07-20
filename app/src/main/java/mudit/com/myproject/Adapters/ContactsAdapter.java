package mudit.com.myproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mudit.com.myproject.Activities.OnetoOneChat;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.R;

/**
 * Created by admin on 7/9/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>{
    ArrayList<User> users;
    Context context;
    ArrayList<String> userId;

    public ContactsAdapter(ArrayList<User> users, Context context, ArrayList<String> userId) {
        this.users = users;
        this.context = context;
        this.userId = userId;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(R.layout.list_item_contacts,parent,false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        final User user=users.get(position);
        holder.tvContactName.setText(user.getUsername());
        holder.tvContactEmail.setText(user.getEmail());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, OnetoOneChat.class);
                i.putExtra("userId",userId.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView tvContactName,tvContactEmail;
        View rootView;
        public ContactViewHolder(View itemView) {
            super(itemView);
            tvContactName=(TextView)itemView.findViewById(R.id.tvContactName);
            tvContactEmail=(TextView)itemView.findViewById(R.id.tvContactEmail);
            rootView=itemView;
        }
    }
}
