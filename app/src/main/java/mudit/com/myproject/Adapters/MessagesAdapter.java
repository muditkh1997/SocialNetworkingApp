package mudit.com.myproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mudit.com.myproject.DatabseInfo.Message;
import mudit.com.myproject.R;

/**
 * Created by admin on 7/8/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>{
    ArrayList<Message> messages;
    Context context;

    public MessagesAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getName().equals("You"))
            return 0;
        return 1;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int LayoutType;
        if(viewType==0)
            LayoutType=R.layout.list_item_message2;
        else
            LayoutType=R.layout.list_item_message1;
        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(LayoutType,parent,false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message=messages.get(position);
//        holder.tvName.setText(message.getName());
        holder.tvMessage.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName;
        TextView tvMessage;
        public MessageViewHolder(View itemView) {
            super(itemView);
//            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvMessage=(TextView)itemView.findViewById(R.id.tvMessage);
        }
    }
}
