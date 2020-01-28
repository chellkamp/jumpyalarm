package org.hellscode.jumpyalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import org.hellscode.jumpyalarm.data.DatabaseHelper;
import org.hellscode.jumpyalarm.ui.view.AlarmListFragment;
import org.hellscode.jumpyalarm.ui.view.TimerListFragment;
import org.hellscode.util.ui.DialogUtil;
import org.hellscode.util.ui.model.TabConfigInfo;
import org.hellscode.util.ui.view.TabLayoutUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper.load(getApplicationContext());

        Resources res = getResources();

        TabLayout tabLayout = findViewById(R.id.mainTab);
        ViewPager tabPager = findViewById(R.id.mainPager);

        TabConfigInfo[] tabConfig = new TabConfigInfo[] {
                new TabConfigInfo(
                        res.getString(R.string.alarm_tab_title),
                        ResourcesCompat.getDrawable(res, R.drawable.outline_alarm_black_18, null),
                        new TabConfigInfo.FragmentFactory() {
                            @Override
                            public Fragment create() {
                                return new AlarmListFragment();
                            }
                        }
                ),
                new TabConfigInfo(
                        res.getString(R.string.timer_tab_title),
                        ResourcesCompat.getDrawable(res, R.drawable.baseline_hourglass_empty_black_18, null),
                        new TabConfigInfo.FragmentFactory() {
                            @Override
                            public Fragment create() {
                                return new TimerListFragment();
                            }
                        }
                )
        };

        // Connect tab layout to pageview using config info
        TabLayoutUtil.setupTabLayoutAndViewPager(tabLayout, tabPager,
                getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, tabConfig);
    }

    @Override
    protected void onDestroy() {
        DatabaseHelper.recycle();

        super.onDestroy();
    }
}
