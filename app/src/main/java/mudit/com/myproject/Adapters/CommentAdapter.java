package mudit.com.myproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mudit.com.myproject.Activities.UserProfile;
import mudit.com.myproject.DatabseInfo.Comment;
import mudit.com.myproject.R;

/**
 * Created by admin on 7/9/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    ArrayList<Comment> comments;
    Context context;

    public CommentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(R.layout.list_item_comment,parent,false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment comment=comments.get(position);
        holder.tvCommentAuthor.setText(comment.getAuthor());
        holder.tvCommentBody.setText(comment.getText());
        holder.tvCommentAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,UserProfile.class);
                i.putExtra("userId",comment.getUid());
                i.putExtra("userName",comment.getAuthor());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvCommentAuthor,tvCommentBody;
        public CommentViewHolder(View itemView) {
            super(itemView);
            tvCommentAuthor=(TextView)itemView.findViewById(R.id.tvCommentAutor);
            tvCommentBody=(TextView)itemView.findViewById(R.id.tvCommentBody);
        }
    }
}
