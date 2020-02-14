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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.data.DatabaseHelper;
import org.hellscode.jumpyalarm.ui.model.AlarmListAdapter;
import org.hellscode.jumpyalarm.ui.model.AlarmViewModel;
import org.hellscode.util.LoadWaitUtil;
import org.hellscode.util.SimpleMethod;
import org.hellscode.util.SimpleVoidMethod;
import org.hellscode.util.ui.DialogUtil;
import org.hellscode.util.ui.view.DatePickerFragment;
import org.hellscode.util.ui.view.TextEditFragment;
import org.hellscode.util.ui.view.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmListFragment extends Fragment
        implements TimePickerFragment.TimePickerCallback,
        DatePickerFragment.DatePickerCallback,
        TextEditFragment.TextEditCallback{

    private DatabaseHelper _dbHelper;
    private SQLiteDatabase _database;

    private ListView _listView;
    private AlarmListAdapter _listAdapter;

    private LoadWaitUtil<Object, ArrayList<AlarmViewModel>> _loadWait =
            new LoadWaitUtil<>();

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

        FloatingActionButton addBtn = retVal.findViewById(R.id.add_button);

        if (addBtn != null) {
            addBtn.setOnClickListener(new AddClickListener());
        }

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
            new DialogUtil<Object, ArrayList<AlarmViewModel>>().
                    runWaitDialog(activity, null, "loading...", null,
                    new SimpleMethod<Object, ArrayList<AlarmViewModel>>() {
                        @Override
                        public ArrayList<AlarmViewModel> run(Object arg) {
                            _dbHelper = DatabaseHelper.getInstance();
                            _database = _dbHelper.getWritableDatabase();
                            _listAdapter = new AlarmListAdapter(_database);
                            return retrieveAlarmVMs();
                        }
                    }, null,
                    new SimpleVoidMethod<ArrayList<AlarmViewModel>>() {
                        @Override
                        public void run(ArrayList<AlarmViewModel> arg) {
                            _listView.setAdapter(_listAdapter);
                            _listAdapter.setItems(arg);
                        }
                    }
            );
        }
    }

    private ArrayList<AlarmViewModel> retrieveAlarmVMs() {
        return _loadWait.performLoad(
                new SimpleMethod<Object, ArrayList<AlarmViewModel>>() {
                    @Override
                    public ArrayList<AlarmViewModel> run(Object o) {
                        ArrayList<AlarmEntity> entities = AlarmEntity.loadAll(_database);
                        ArrayList<AlarmViewModel> data = new ArrayList<>();

                        for (int i = 0; i < entities.size(); ++i) {
                            AlarmEntity entity = entities.get(i);
                            final AlarmViewModel vm = new AlarmViewModel(_database, entity);
                            final int idx = i;

                            Runnable timeAction = new Runnable() {
                                @Override
                                public void run() {
                                    onTimeClick(idx, vm);
                                }
                            };

                            vm.setUserSelectTimeAction(timeAction);

                            Runnable dateAction = new Runnable() {
                                @Override
                                public void run() {
                                    onDateClick(idx, vm);
                                }
                            };

                            vm.setUserSelectDateAction(dateAction);

                            Runnable labelAction = new Runnable() {
                                @Override
                                public void run() {
                                    onLabelClick(idx, vm);
                                }
                            };

                            vm.setUserSelectLabelAction(labelAction);

                            vm.setOnShowDetailsChanged(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            onShowDetailsChange(vm);
                                        }
                                    }
                            );

                            data.add(vm);
                        }

                        return data;
                    }
                },
                null
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

    private void onDateClick(int lookupId, @NonNull AlarmViewModel vm) {

        Calendar c = Calendar.getInstance();
        c.setTime(vm.getOnOrAfter());

        Bundle b = new Bundle();
        b.putString(DatePickerFragment.PARENT_TAG, getTag());
        b.putInt(DatePickerFragment.LOOKUP_ID, lookupId);
        b.putInt(DatePickerFragment.YEAR, c.get(Calendar.YEAR));
        b.putInt(DatePickerFragment.MONTH, c.get(Calendar.MONTH));
        b.putInt(DatePickerFragment.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));

        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setArguments(b);

        FragmentManager fm = getFragmentManager();

        if (fm != null) {
            dialog.show(getFragmentManager(), null);
        }
    }

    private void onShowDetailsChange(AlarmViewModel vm) {

        if (vm != null && vm.getShowDetails()) {
            for(AlarmViewModel entry : _listAdapter.getItems()) {
                if (entry != vm) {
                    entry.setShowDetails(false);
                }
            }
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
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                vm.setOnOrAfter(c.getTime());
            }
        }
    }

    @Override
    public void onDateSet(final int lookupId, final int year, final int month, final int dayOfMonth) {
        _loadWait.waitForLoad(
                new Runnable() {
                    @Override
                    public void run() {
                        onDateSetImpl(lookupId, year, month, dayOfMonth);
                    }
                }
        );
    }

    private void onDateSetImpl(int lookupId, int year, int month, int dayOfMonth) {
        // lookup id will be item index
        if (lookupId >= 0 && lookupId < _listAdapter.getCount()) {
            AlarmViewModel vm = (AlarmViewModel)_listAdapter.getItem(lookupId);
            if (vm != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(vm.getOnOrAfter());
                c.set(year, month, dayOfMonth);
                vm.setOnOrAfter(c.getTime());
            }
        }
    }

    private void onLabelClick(int lookupId, @NonNull AlarmViewModel vm) {
        String label = vm.getLabel();

        Bundle b = new Bundle();
        b.putString(TextEditFragment.PARENT_TAG, getTag());
        b.putInt(TextEditFragment.LOOKUP_ID, lookupId);
        b.putString(TextEditFragment.TEXT, label);

        TextEditFragment dialog = new TextEditFragment();
        dialog.setArguments(b);

        FragmentManager fm = getFragmentManager();

        if (fm != null) {
            dialog.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onTextSet(final int lookupId, final String text) {
        _loadWait.waitForLoad(
                new Runnable() {
                    @Override
                    public void run() {
                        onTextSetImpl(lookupId, text);
                    }
                }
        );
    }

    private void onTextSetImpl(int lookupId, String text) {
        // lookup id will be item index
        if (lookupId >= 0 && lookupId < _listAdapter.getCount()) {
            AlarmViewModel vm = (AlarmViewModel)_listAdapter.getItem(lookupId);
            if (vm != null) {
                vm.setLabel(text);
            }
        }
    }

    private class AddClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            AlarmEntity newEntity = new AlarmEntity();
            Activity activity = getActivity();
            if (activity != null && _database != null) {
                newEntity.persistToDB(_database);
                // load database
                new DialogUtil<Object, ArrayList<AlarmViewModel>>().
                        runWaitDialog(activity, null, "refreshing...", null,
                                new SimpleMethod<Object, ArrayList<AlarmViewModel>>() {
                                    @Override
                                    public ArrayList<AlarmViewModel> run(Object arg) {
                                        return retrieveAlarmVMs();
                                    }
                                },
                                null,
                                new SimpleVoidMethod<ArrayList<AlarmViewModel>>() {
                                    @Override
                                    public void run(ArrayList<AlarmViewModel> arg) {
                                        _listAdapter.setItems(arg);
                                    }
                                }
                        );
            }
        }
    }
}
