package mudit.com.myproject.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


import mudit.com.myproject.MainActivity;
import mudit.com.myproject.R;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    LinearLayout llLoginActivity;
    AnimationDrawable animationDrawable;
    EditText etPassword;
    Button btLogin,btSignUp;
    AutoCompleteTextView actvEmail;
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Billabong.ttf");
        ((TextView)findViewById(R.id.tvInstagram)).setTypeface(typeface);
        llLoginActivity=(LinearLayout)findViewById(R.id.llLoginActivity);
//        llLoginActivity.setBackgroundResource(R.drawable.login_animation_list);
        animationDrawable=(AnimationDrawable) llLoginActivity.getBackground();
        animationDrawable.start();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(1000);
        etPassword=(EditText)findViewById(R.id.etPassword);
        actvEmail=(AutoCompleteTextView)findViewById(R.id.actvEmail);
        btLogin=(Button)findViewById(R.id.btLogin);
        auth=FirebaseAuth.getInstance();
        btLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signInWithEmailAndPassword(actvEmail.getText().toString().trim(),etPassword.getText().toString().trim())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(LoginActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        ((Button)findViewById(R.id.btnSignUp)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
        if (auth!= null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}

