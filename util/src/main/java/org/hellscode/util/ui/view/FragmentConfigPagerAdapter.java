package org.hellscode.util.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.hellscode.util.ui.model.TabConfigInfo;

class FragmentConfigPagerAdapter extends FragmentPagerAdapter {

    private TabConfigInfo[] _config;
    private Fragment[] _fragments;

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

        // build fragment list
        _fragments = new Fragment[_config.length];
        for(int i = 0; i < _config.length; ++i) {
            _fragments[i] = _config[i].getFragmentCreator().create();
        }
    }

    /**
     * Get the page title for a given position
     * @param position item index
     * @return title or null
     */
    public CharSequence getPageTitle (int position) {
        return _config[position].getTabName();
    }

    /**
     * Get the fragment for a given position
     * @param position item index
     * @return fragment
     */
    @Override
    public @NonNull Fragment getItem(int position) {
        return _fragments[position];
    }

    @Override
    public int getCount() { return _fragments.length; }
}
