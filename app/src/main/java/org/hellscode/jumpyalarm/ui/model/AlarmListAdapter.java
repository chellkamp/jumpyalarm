package org.hellscode.jumpyalarm.ui.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.hellscode.jumpyalarm.ui.view.AlarmView;

import java.util.ArrayList;

public class AlarmListAdapter extends BaseAdapter {

    private SQLiteDatabase _db;
    private final Object _itemLock = new Object();
    private ArrayList<AlarmViewModel> _items = new ArrayList<>();

    /**
     * Constructor
     * @param db database connection
     */
    public AlarmListAdapter(@NonNull SQLiteDatabase db) {
        super();
        _db = db;
    }

    /**
     * Get underlying items
     * @return list of items
     */
    public ArrayList<AlarmViewModel> getItems()
    {
        ArrayList<AlarmViewModel> itemListCopy;

        synchronized (_itemLock) {
            itemListCopy = new ArrayList<>(_items);
        }

        return itemListCopy;
    }

    /**
     * set the item list
     * @param items items
     */
    public void setItems(@NonNull ArrayList<AlarmViewModel> items) {
        synchronized (_itemLock) {
            _items.clear();
            _items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void appendItem(@NonNull AlarmViewModel item) {
        synchronized (_itemLock) {
            _items.add(item);
        }
        notifyDataSetChanged();
    }

    public void removeItem(@NonNull AlarmViewModel item) {
        synchronized (_itemLock) {
            _items.remove(item);
        }
        notifyDataSetChanged();
    }

    /**
     * Get number of items in list.
     * @return item count
     */
    @Override
    public int getCount() {
        int count;
        synchronized (_itemLock) {
            count = _items.size();
        }
        return count;
    }

    /**
     * Get the item at the specified position.
     * @param position index
     * @return item
     */
    @Override
    public Object getItem(int position) {
        AlarmViewModel entry = null;

        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                entry = _items.get(position);
            }
        }

        return entry;
    }

    /**
     * Gets a unique ID for an item
     * @param position index of the item
     * @return ID number
     */
    @Override
    public long getItemId(int position) {
        long retVal = -1;

        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                retVal = _items.get(position).getID();
            }
        }

        return retVal;
    }

    /**
     * Get a view for the item at a specific position
     * @param position index of item
     * @param convertView cached old view
     * @param viewGroup group to attach new view to
     * @return view
     */
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup viewGroup) {

        Context c = viewGroup.getContext();
        AlarmView retVal = new AlarmView(c);

        synchronized (_itemLock) {
            if (position >= 0 && position < _items.size()) {
                AlarmViewModel item = _items.get(position);
                retVal.setViewModel(item);
            }
        }
        return retVal;
    }

}
