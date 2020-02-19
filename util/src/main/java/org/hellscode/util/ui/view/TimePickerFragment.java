package org.hellscode.util.ui.view;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    /**
     * Provides an entry point for the dialog to return its info to
     */
    public interface TimePickerCallback {
        void onTimeSet(long lookupId, int hourOfDay, int minute);
    }

    public static final String PARENT_TAG = "tag";
    public static final String LOOKUP_ID = "lookupid";
    public static final String HOUROFDAY = "hourofday";
    public static final String MINUTE = "minute";

    @Override
    public @NonNull Dialog onCreateDialog(Bundle savedInstanceState) {
        String parentTag = null;
        long lookupId = -1;

        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Bundle args = getArguments();

        if(args != null) {
            parentTag = args.getString(PARENT_TAG);
            lookupId = args.getLong(LOOKUP_ID, lookupId);
            hourOfDay = args.getInt(HOUROFDAY, hourOfDay);
            minute = args.getInt(MINUTE, minute);
        }

        Listener l = new Listener(this, parentTag, lookupId);

        return new TimePickerDialog(getActivity(), l, hourOfDay, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    private static class Listener implements TimePickerDialog.OnTimeSetListener {
        private Fragment _owner;
        private String _parentTag;
        private long _lookupId;

        Listener(Fragment owner, String parentTag, long lookupId) {
            _owner = owner;
            _parentTag = parentTag;
            _lookupId = lookupId;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            FragmentManager fm  = _owner != null ? _owner.getFragmentManager() : null;
            Fragment f = fm != null ? fm.findFragmentByTag(_parentTag) : null;
            TimePickerCallback tpc = null;
            if (f instanceof TimePickerCallback) {
                tpc = (TimePickerCallback) f;
            }

            if (tpc != null) {
                tpc.onTimeSet(_lookupId, hourOfDay, minute);
            }

        }
    }

}
