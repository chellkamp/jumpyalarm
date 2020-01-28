package org.hellscode.jumpyalarm.ui.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.ui.view.AlarmView;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {

    private LifecycleOwner _lifecycleOwner;
    private SQLiteDatabase _db;
    private final Object _itemLock = new Object();
    private ArrayList<AlarmEntity> _items = new ArrayList<>();

    public AlarmListAdapter(LifecycleOwner lifecycleOwner, SQLiteDatabase db) {
        super();
        _lifecycleOwner = lifecycleOwner;
        _db = db;
    }

    public ArrayList<AlarmEntity> getItems()
    {
        ArrayList<AlarmEntity> itemListCopy;

        synchronized (_itemLock) {
            itemListCopy = new ArrayList<>(_items);
        }

        return itemListCopy;
    }

    public void setItems(@NonNull ArrayList<AlarmEntity> items) {
        synchronized (_itemLock) {
            _items.clear();
            _items.addAll(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count;
        synchronized (_itemLock) {
            count = _items.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        AlarmEntity entry = null;

        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                entry = _items.get(position);
            }
        }

        return entry;
    }

    @Override
    public long getItemId(int position) {
        long retVal = -1;

        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                retVal = _items.get(position).get_id();
            }
        }

        return retVal;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {

        Context c = viewGroup.getContext();
        AlarmView retVal = new AlarmView(c);


        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                AlarmEntity item = _items.get(position);
                AlarmViewModel vm = new AlarmViewModel(_lifecycleOwner, _db, item);
                retVal.setViewModel(vm);
            }
        }
        return retVal;
    }
}
