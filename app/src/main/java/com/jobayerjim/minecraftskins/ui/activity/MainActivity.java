package com.jobayerjim.minecraftskins.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.adapter.HomeViewPagerAdapter;
import com.jobayerjim.minecraftskins.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();

    }
    int currentPosition = 0;

    private void init() {

        ViewGroup tabStrip = (ViewGroup) binding.homeTabLayout.getChildAt(0);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View tabView = tabStrip.getChildAt(i);
            if (tabView != null) {
                int paddingStart = tabView.getPaddingStart();
                int paddingTop = tabView.getPaddingTop();
                int paddingEnd = tabView.getPaddingEnd();
                int paddingBottom = tabView.getPaddingBottom();
                ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_background));
                ViewCompat.setPaddingRelative(tabView, paddingStart, paddingTop, paddingEnd, paddingBottom);
            }
        }
        binding.homeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(binding.homeTabLayout.getSelectedTabPosition());
                if (tab.getText().toString().trim().equals(getString(R.string.main_tab))) {
//                    binding.setTabState(1);
                    ViewGroup tabStrip = (ViewGroup) binding.homeTabLayout.getChildAt(0);
                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
                        View tabView = tabStrip.getChildAt(i);
                        if (tabView != null) {
                            int paddingStart = tabView.getPaddingStart();
                            int paddingTop = tabView.getPaddingTop();
                            int paddingEnd = tabView.getPaddingEnd();
                            int paddingBottom = tabView.getPaddingBottom();
                            ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_background));
                            ViewCompat.setPaddingRelative(tabView, paddingStart, paddingTop, paddingEnd, paddingBottom);
                        }
                    }
                }
                else {
                    ViewGroup tabStrip = (ViewGroup) binding.homeTabLayout.getChildAt(0);
                    for (int i = 0; i < tabStrip.getChildCount(); i++) {
                        View tabView = tabStrip.getChildAt(i);
                        if (tabView != null) {
                            int paddingStart = tabView.getPaddingStart();
                            int paddingTop = tabView.getPaddingTop();
                            int paddingEnd = tabView.getPaddingEnd();
                            int paddingBottom = tabView.getPaddingBottom();
                            ViewCompat.setBackground(tabView, AppCompatResources.getDrawable(tabView.getContext(), R.drawable.tab_background_alter));
                            ViewCompat.setPaddingRelative(tabView, paddingStart, paddingTop, paddingEnd, paddingBottom);
                        }
                    }
//                    binding.setTabState(2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), this);
        binding.viewPager.setAdapter(adapter);
//        try {
//            // get input stream
//            InputStream ims = getAssets().open("images/skin_image_1.png");
//            // load image as Drawable
//            Drawable d = Drawable.createFromStream(ims, null);
//            // set image to ImageView
//            binding.testImageView.setImageDrawable(d);
//        }
//        catch(IOException ex) {
//            ex.printStackTrace();
//        }
    }


}