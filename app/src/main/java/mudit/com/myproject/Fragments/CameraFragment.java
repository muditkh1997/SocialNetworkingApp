package mudit.com.myproject.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import mudit.com.myproject.Activities.NewPostActivity;
import mudit.com.myproject.DatabseInfo.Post;
import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

import static android.app.Activity.RESULT_OK;


public class CameraFragment extends Fragment {


    public CameraFragment() {
        // Required empty public constructor
    }

    public static final int GALLERY_INTENT=2;
    ProgressDialog progressDialog;
    StorageReference sr;
    DatabaseReference mdatabase;
    String key;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
            onStart1();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_camera, container, false);

        return rootView;
    }

    public void onStart1() {
        super.onStart();
        progressDialog=new ProgressDialog(getActivity());
        mdatabase= FirebaseDatabase.getInstance().getReference();
        sr=FirebaseStorage.getInstance().getReference();
        key=mdatabase.child("Posts").push().getKey();
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,GALLERY_INTENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    final String postUri = downloadUri.toString();
                    final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    mdatabase.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user1=dataSnapshot.getValue(User.class);
                            Post post=new Post(user.getUid(),"",user1.getUsername(),0,postUri);
                            Log.d("abcd", "onClick: "+post.getUid()+ "  "+ post.getBody());
                            mdatabase.child("Posts").child(key).setValue(post);
//                ArrayList<String> starUids=new ArrayList<String>();
//                starUids.add("eJD8MfqPrIMKLVRxvtzq0G7sl1G2");
//                mdatabase.child("Posts").setValue(starUids);
                            mdatabase.child("User-Posts").child(user.getUid()).child(key).setValue(post);
                            progressDialog.dismiss();
                            startActivity(new Intent(getActivity(),MainActivity.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            });
        }}

    @Override
    public void onResume() {
        super.onResume();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
