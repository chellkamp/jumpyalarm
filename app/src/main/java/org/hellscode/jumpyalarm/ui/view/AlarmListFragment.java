package org.hellscode.jumpyalarm.ui.view;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.data.DatabaseHelper;
import org.hellscode.jumpyalarm.ui.model.AlarmListAdapter;
import org.hellscode.jumpyalarm.ui.model.AlarmViewModel;
import org.hellscode.util.LoadWaitUtil;
import org.hellscode.util.ui.DialogUtil;
import org.hellscode.util.ui.view.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmListFragment extends Fragment
        implements TimePickerFragment.TimePickerCallback {

    private DatabaseHelper _dbHelper;
    private SQLiteDatabase _database;

    private ListView _listView;
    private AlarmListAdapter _listAdapter;

    private LoadWaitUtil _loadWait = new LoadWaitUtil();

    /**
     * Called to create the view for the fragment
     * @param inflater layout inflater
     * @param container container to attach to
     * @param savedInstanceState saved state
     * @return view
     */
    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        View retVal = inflater.inflate(R.layout.alarm_tab, container, false);

        _listView = retVal.findViewById(R.id.alarmList);

        initializeDatabase();

        return retVal;
    }

    private void initializeDatabase() {
        Activity activity = getActivity();

        if (activity == null) {
            Log.e("AlarmListFragment", "Call to getActivity() returned null.");
            System.exit(1);
        } else {

            // load database
            DialogUtil.runWaitDialog(activity, null, "loading...", null,
                    new Runnable() {
                        @Override
                        public void run() {
                            _dbHelper = DatabaseHelper.getInstance();
                            _database = _dbHelper.getWritableDatabase();
                            _listAdapter = new AlarmListAdapter(
                                    getViewLifecycleOwner(),
                                    _database);
                            refreshData();
                        }
                    },
                    new Runnable() {
                        @Override
                        public void run() {
                            _listView.setAdapter(_listAdapter);
                        }
                    }
            );
        }
    }

    private void refreshData() {
        _loadWait.performLoad(
                new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<AlarmEntity> entities = AlarmEntity.loadAll(_database);
                        ArrayList<AlarmViewModel> data = new ArrayList<>();

                        for (int i = 0; i < entities.size(); ++i) {
                            AlarmEntity entity = entities.get(i);
                            final AlarmViewModel vm = new AlarmViewModel(getViewLifecycleOwner(), _database, entity);
                            final int idx = i;

                            Runnable timeAction = new Runnable() {
                                @Override
                                public void run() {
                                    onTimeClick(idx, vm);
                                }
                            };

                            vm.setUserSelectTimeAction(timeAction);

                            data.add(vm);
                        }

                        _listAdapter.setItems(data);
                    }
                }
        );
    }

    private void onTimeClick(int lookupId, @NonNull AlarmViewModel vm) {

        Calendar c = Calendar.getInstance();
        c.setTime(vm.getOnOrAfter());

        Bundle b = new Bundle();
        b.putString(TimePickerFragment.PARENT_TAG, getTag());
        b.putInt(TimePickerFragment.LOOKUP_ID, lookupId);
        b.putInt(TimePickerFragment.HOUROFDAY, c.get(Calendar.HOUR_OF_DAY));
        b.putInt(TimePickerFragment.MINUTE, c.get(Calendar.MINUTE));

        TimePickerFragment dialog = new TimePickerFragment();
        dialog.setArguments(b);

        FragmentManager fm = getFragmentManager();

        if (fm != null) {
            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onTimeSet(final int lookupId, final int hourOfDay, final int minute) {
        _loadWait.waitForLoad(
                new Runnable() {
                    @Override
                    public void run() {
                        onTimeSetImpl(lookupId, hourOfDay, minute);
                    }
                }
        );
    }

    private void onTimeSetImpl(int lookupId, int hourOfDay, int minute) {
        // lookup id will be item index
        if (lookupId >= 0 && lookupId < _listAdapter.getCount()) {
            AlarmViewModel vm = (AlarmViewModel)_listAdapter.getItem(lookupId);
            if (vm != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(vm.getOnOrAfter());
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                vm.setOnOrAfter(c.getTime());
            }
        }
    }

}
