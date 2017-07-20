package mudit.com.myproject.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import mudit.com.myproject.DatabseInfo.User;
import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;
public class SignUpActivity extends AppCompatActivity {
    EditText etEmail,etPassword,etName;
    Button btSignUp;
    FirebaseAuth auth;
    LinearLayout llSignUpActivity;
    AnimationDrawable animationDrawable;
    DatabaseReference databaseReference;
    StorageReference sr;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Billabong.ttf");
        ((TextView)findViewById(R.id.tvInstagram)).setTypeface(typeface);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        etName=(EditText)findViewById(R.id.etName);
        btSignUp=(Button)findViewById(R.id.btSignUp);
        pd=new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        llSignUpActivity=(LinearLayout)findViewById(R.id.llSignUpActivity);
//        llLoginActivity.setBackgroundResource(R.drawable.login_animation_list);
        animationDrawable=(AnimationDrawable) llSignUpActivity.getBackground();
        animationDrawable.start();
        animationDrawable.setEnterFadeDuration(6000);
        animationDrawable.setExitFadeDuration(2000);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email,password,name;
                email=etEmail.getText().toString().trim();
                password=etPassword.getText().toString().trim();
                name=etName.getText().toString();
                Log.d("abcd", "onClick: "+password);
                Log.d("abcd", "onClick: "+email);
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this,"Enter Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignUpActivity.this,"Enter Password", Toast.LENGTH_SHORT).show();
                    return;}
                if(password.length()<6){
                    Toast.makeText(SignUpActivity.this,"Password should be at least 6 character long", Toast.LENGTH_SHORT).show();
                    return;}
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignUpActivity.this,"Enter Name", Toast.LENGTH_SHORT).show();
                    return;}
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.d("abcd", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this,"SignUp Failed. Check your credentials",Toast.LENGTH_SHORT).show();}
                        else
                        {   pd.setMessage("Creating account ... ");
                            pd.show();
                            FirebaseUser user=auth.getCurrentUser();
                            User user1=new User(name,email);
                            databaseReference= FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("users").child(user.getUid()).setValue(user1);
                            sr= FirebaseStorage.getInstance().getReference();
                            Uri uri = Uri.parse("android.resource://your.package.here/drawable/defaultprofilepicture");
                            sr.child("Profile-Pictures").child(user.getUid()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pd.dismiss();
                                }
                            });
                            Log.d("abcd", "onComplete: "+user.getUid());
                            Intent i=new Intent(SignUpActivity.this, MainActivity.class);
                            i.putExtra("UserEmail",user.getEmail());
                            startActivity(i);
                        }

                    }
                });
            }
        });
    }

}
