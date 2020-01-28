package org.hellscode.util.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.hellscode.util.ui.model.TabConfigInfo;

class FragmentConfigPagerAdapter extends FragmentPagerAdapter {

    private TabConfigInfo[] _config;

    /**
     * Constructor
     * @param fm fragment manager
     * @param behavior behavior
     * @param config tab configuration
     */
    FragmentConfigPagerAdapter (
            @NonNull FragmentManager fm,
            int behavior,
            @NonNull TabConfigInfo[] config) {
        super(fm, behavior);
        _config = config;
    }

    public CharSequence getPageTitle (int position) {
        return _config[position].getTabName();
    }

    @Override
    public @NonNull Fragment getItem(int position) {
        return _config[position].getFragmentCreator().create();
    }

    @Override
    public int getCount() { return _config.length; }

}
