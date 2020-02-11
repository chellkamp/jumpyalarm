package org.hellscode.util.ui.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
    /**
     * Provides an entry point for the dialog to return its info to
     */
    public interface DatePickerCallback {
        void onDateSet(int lookupId, int year, int month, int day);
    }

    public static final String PARENT_TAG = "tag";
    public static final String LOOKUP_ID = "lookupid";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY_OF_MONTH = "dayofmonth";

    @Override
    public @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        String parentTag = null;
        int lookupId = -1;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        Bundle args = getArguments();

        if(args != null) {
            parentTag = args.getString(PARENT_TAG);
            lookupId = args.getInt(LOOKUP_ID, lookupId);
            year = args.getInt(YEAR, year);
            month = args.getInt(MONTH, month);
            dayOfMonth = args.getInt(DAY_OF_MONTH, dayOfMonth);
        }

        DatePickerFragment.Listener l = new DatePickerFragment.Listener(this, parentTag, lookupId);

        return new DatePickerDialog(getActivity(), l, year, month, dayOfMonth);
    }

    private static class Listener implements DatePickerDialog.OnDateSetListener {
        private Fragment _owner;
        private String _parentTag;
        private int _lookupId;

        Listener(Fragment owner, String parentTag, int lookupId) {
            _owner = owner;
            _parentTag = parentTag;
            _lookupId = lookupId;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            FragmentManager fm  = _owner != null ? _owner.getFragmentManager() : null;
            Fragment f = fm != null ? fm.findFragmentByTag(_parentTag) : null;
            DatePickerFragment.DatePickerCallback dpc = null;
            if (f instanceof DatePickerFragment.DatePickerCallback) {
                dpc = (DatePickerFragment.DatePickerCallback) f;
            }

            if (dpc != null) {
                dpc.onDateSet(_lookupId, year, month, dayOfMonth);
            }
        }
    }

}
