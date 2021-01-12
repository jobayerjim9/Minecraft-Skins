package com.jobayerjim.minecraftskins.controller.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.listener.RefreshListener;
import com.jobayerjim.minecraftskins.ui.fragment.FavouriteFragment;
import com.jobayerjim.minecraftskins.ui.fragment.MainFragment;

public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    FragmentManager fm;
    public HomeViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.fm = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MainFragment(fm);
        } else if (position == 1) {
            return new FavouriteFragment(fm);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0) {
            return context.getString(R.string.main_tab);
        }
        else {
            return context.getString(R.string.favourite_tab);
        }
    }
}
