package org.hellscode.jumpyalarm.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.hellscode.jumpyalarm.R;

/**
 * Holds a list of timers.  This is just a placeholder at this point.
 */
public class TimerListFragment extends Fragment {
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.timer_tab, container, false);
    }
}
