package mudit.com.myproject.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import mudit.com.myproject.Activities.UserProfile;
import mudit.com.myproject.Fragments.CameraFragment;
import mudit.com.myproject.Fragments.MessageFragment;
import mudit.com.myproject.Fragments.RecentPostFragment;
import mudit.com.myproject.Fragments.UserProfileFragment;

/**
 * Created by admin on 7/7/2017.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    Fragment[] fragments;

    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        fragments=new Fragment[]{new CameraFragment(),new RecentPostFragment(),new MessageFragment(),new UserProfileFragment()};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
