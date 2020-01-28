package org.hellscode.util.ui.view;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.hellscode.util.ui.model.TabConfigInfo;

public class TabLayoutUtil {
    public static void setupTabLayoutAndViewPager(
            @NonNull TabLayout layout,
            @NonNull ViewPager pager,
            @NonNull FragmentManager fm,
            int behavior,
            @NonNull TabConfigInfo[] config) {
        FragmentConfigPagerAdapter pagerAdapter = new FragmentConfigPagerAdapter(
                fm,
                behavior,
                config);
        pager.setAdapter(pagerAdapter);
        layout.setupWithViewPager(pager);

        // setup tab icons.  This has to be done after setupWithViewPager()
        for (int i = 0; i < config.length; ++i) {
            TabLayout.Tab t = layout.getTabAt(i);
            Drawable icon = config[i].getTabIcon();

            if (t != null && icon != null) {
                t.setIcon(config[i].getTabIcon());
            }
        }

    }
}
