package org.hellscode.jumpyalarm.ui.view;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.data.DatabaseHelper;
import org.hellscode.jumpyalarm.ui.model.AlarmListAdapter;
import org.hellscode.util.ui.DialogUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmListFragment extends Fragment {

    private DatabaseHelper _dbHelper;
    private SQLiteDatabase _database;

    private ListView _listView;
    private AlarmListAdapter _listAdapter;

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
                            _listAdapter = new AlarmListAdapter(getViewLifecycleOwner(), _database);
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
        ArrayList<AlarmEntity> data = AlarmEntity.loadAll(_database);
        _listAdapter.setItems(data);
    }
}
