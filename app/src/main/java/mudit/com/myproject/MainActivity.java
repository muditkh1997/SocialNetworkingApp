package mudit.com.myproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import mudit.com.myproject.Activities.LoginActivity;
import mudit.com.myproject.Adapters.MyFragmentPageAdapter;

public class MainActivity extends AppCompatActivity {
    ViewPager mviewPager;
    private TabLayout allTabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allTabs=(TabLayout)findViewById(R.id.tabs);
        mviewPager=(ViewPager)findViewById(R.id.viewPager);
        mviewPager.setOffscreenPageLimit(4);
        mviewPager.setAdapter(new MyFragmentPageAdapter(getSupportFragmentManager()));
        allTabs.setupWithViewPager(mviewPager);
        allTabs.getTabAt(0).setIcon(R.drawable.camera);
        allTabs.getTabAt(1).setIcon(R.drawable.newsfeed);
        allTabs.getTabAt(2).setIcon(R.drawable.chat);
        allTabs.getTabAt(3).setIcon(R.drawable.myprofile);
//        addPost=(ImageButton)findViewById(R.id.imgAddPost);
//        addPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this,NewPostActivity.class));
//            }
//        });
//        ((ImageButton)findViewById(R.id.imgMessage)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MessageActivity.class));
//            }
//        });
        mviewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
