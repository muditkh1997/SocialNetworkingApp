package mudit.com.myproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mudit.com.myproject.Activities.PostDetailActivity;
import mudit.com.myproject.Activities.UserProfile;
import mudit.com.myproject.Activities.ViewImage;
import mudit.com.myproject.DatabseInfo.Post;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

/**
 * Created by admin on 7/7/2017.
 */

public class RecentPostAdapter extends RecyclerView.Adapter<RecentPostAdapter.RecentPostViewHolder>{
    ArrayList<Post> posts;
    Context context;
    ArrayList<String> postId;

    public RecentPostAdapter(ArrayList<Post> posts, Context context, ArrayList<String> postId) {
        this.posts = posts;
        this.context = context;
        this.postId = postId;
    }

    @Override
    public RecentPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView=li.inflate(R.layout.list_item_posts,parent,false);
        return new RecentPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentPostViewHolder holder, final int position) {

        final Post post=posts.get(position);
        holder.tvAuthor.setText(post.getAuthor());
        holder.tvBody.setText(post.getBody());
        holder.tvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,UserProfile.class);
                i.putExtra("userId",post.getUid());
                i.putExtra("userName",post.getAuthor());
                context.startActivity(i);
            }
        });
        holder.tvComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, PostDetailActivity.class);
                i.putExtra("postId",postId.get(position));
                i.putExtra("authorName",post.getAuthor());
                i.putExtra("messageBody",post.getBody());
                i.putExtra("postPictureUri",post.getUri());
                context.startActivity(i);
            }
        });
        if(!post.getUri().equals("none"))
        {
            Picasso.with(context).load(Uri.parse(post.getUri())).fit().centerInside().into(holder.imgPostPicture);
            holder.imgPostPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ViewImage.class);
                    intent.putExtra("uri",post.getUri());
                    context.startActivity(intent);
                }
            });
        }
        else
            holder.imgPostPicture.getLayoutParams().height=0;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class RecentPostViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor,tvBody;
        TextView tvAuthorEmail,tvComments;
        ImageView imgPostPicture;
        public RecentPostViewHolder(View itemView) {
            super(itemView);
            imgPostPicture=(ImageView)itemView.findViewById(R.id.imgPostPicture);
            tvAuthor=(TextView)itemView.findViewById(R.id.tvAuthor);
            tvBody=(TextView)itemView.findViewById(R.id.tvBody);
//            tvAuthorEmail=(TextView)itemView.findViewById(R.id.tvAuthorEmail);
            tvComments=(TextView)itemView.findViewById(R.id.tvComments);
        }
    }
}
