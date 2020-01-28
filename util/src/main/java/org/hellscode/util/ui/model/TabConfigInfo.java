package org.hellscode.util.ui.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabConfigInfo {
    public interface FragmentFactory {
        Fragment create();
    }

    private String _tabName;
    private Drawable _tabIcon;
    private FragmentFactory _fragmentCreator;

    public TabConfigInfo(
            @Nullable String tabName,
            @Nullable Drawable tabIcon,
            @NonNull FragmentFactory fragmentCreator) {
        _tabName = tabName;
        _tabIcon = tabIcon;
        _fragmentCreator = fragmentCreator;
    }

    public String getTabName() { return _tabName; }

    public Drawable getTabIcon() { return _tabIcon; }

    public FragmentFactory getFragmentCreator() { return _fragmentCreator; }
}
