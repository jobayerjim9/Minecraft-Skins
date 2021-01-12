package com.jobayerjim.minecraftskins.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jobayerjim.minecraftskins.R;
import com.jobayerjim.minecraftskins.controller.adapter.HomeViewPagerAdapter;
import com.jobayerjim.minecraftskins.databinding.ActivityMainBinding;
import com.jobayerjim.minecraftskins.ui.fragment.FavouriteFragment;
import com.jobayerjim.minecraftskins.ui.fragment.MainFragment;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        try {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, new MainFragment(getSupportFragmentManager()), "main")
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack();
        Log.d("backStack", getSupportFragmentManager().getBackStackEntryCount() + "");
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            TabLayout.Tab tab = binding.homeTabLayout.getTabAt(0);
            tab.select();
        }
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
                if (binding.homeTabLayout.getSelectedTabPosition() == 0) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new MainFragment(getSupportFragmentManager()), "main")
                            .setReorderingAllowed(true)
                            .addToBackStack("main") // name can be null
                            .commit();
                } else if (binding.homeTabLayout.getSelectedTabPosition() == 1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FavouriteFragment(getSupportFragmentManager()), "favourite")
                            .setReorderingAllowed(true)
                            .addToBackStack("favorite") // name can be null
                            .commit();

                }
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