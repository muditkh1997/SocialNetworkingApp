package mudit.com.myproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import mudit.com.myproject.DatabseInfo.Post;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.DatabseInfo.UserInfo;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

public class NewPostActivity extends AppCompatActivity {
    ImageButton imgCapture,imgGallery;
    ImageView imgPicture;
    EditText etBody;
    String key;
    String postUri="none";
    public static final int GALLERY_INTENT=2;
    ProgressDialog progressDialog;
    StorageReference sr;
    DatabaseReference mdatabase;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Post");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        etBody=(EditText)findViewById(R.id.etBody);
        imgCapture=(ImageButton)findViewById(R.id.imgCapture);
        imgGallery=(ImageButton)findViewById(R.id.imgGallery);
        imgPicture=(ImageView)findViewById(R.id.imgPicture);
        progressDialog=new ProgressDialog(this);
        mdatabase= FirebaseDatabase.getInstance().getReference();
        key=mdatabase.child("Posts").push().getKey();
        imgCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,GALLERY_INTENT);
//                Toast.makeText(NewPostActivity.this,"Picture Attached",Toast.LENGTH_SHORT).show();
            }
        });
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,GALLERY_INTENT);
//                Toast.makeText(NewPostActivity.this,"Picture Attached",Toast.LENGTH_SHORT).show();
            }
        });
        ((Button)findViewById(R.id.btAddPost)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etBody.getText().toString()) && postUri.equals("none")) {
                    Toast.makeText(NewPostActivity.this, "Nothing to post.", Toast.LENGTH_SHORT).show();
                    return;
                }
                final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                mdatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user1=dataSnapshot.getValue(User.class);
                        Post post=new Post(user.getUid(),etBody.getText().toString(),user1.getUsername(),0,postUri);
                        Log.d("abcd", "onClick: "+post.getUid()+ "  "+ post.getBody());
                        mdatabase.child("Posts").child(key).setValue(post);
//                ArrayList<String> starUids=new ArrayList<String>();
//                starUids.add("eJD8MfqPrIMKLVRxvtzq0G7sl1G2");
//                mdatabase.child("Posts").setValue(starUids);
                        mdatabase.child("User-Posts").child(user.getUid()).child(key).setValue(post);
                        startActivity(new Intent(NewPostActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== GALLERY_INTENT && resultCode==RESULT_OK){
            progressDialog.setMessage("Uploading ... ");
            progressDialog.show();
            Uri uri=data.getData();
            sr= FirebaseStorage.getInstance().getReference();
            sr.child("Posts-Pictures").child(key).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri=taskSnapshot.getDownloadUrl();
                    Log.d("abcde", "onSuccess: "+downloadUri);
                    Picasso.with(NewPostActivity.this).load(downloadUri).fit().centerInside().into(imgPicture);
                    postUri=downloadUri.toString();
                    Toast.makeText(NewPostActivity.this,"Picture is attached",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewPostActivity.this,"Uploading Failed",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
