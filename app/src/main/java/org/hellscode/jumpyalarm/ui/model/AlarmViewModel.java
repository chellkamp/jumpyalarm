package org.hellscode.jumpyalarm.ui.model;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.LifecycleOwner;

import org.hellscode.jumpyalarm.data.AlarmEntity;

import java.util.Date;

/**
 * View model for an alarm's settings
 */
public class AlarmViewModel extends BaseObservable {

    private LifecycleOwner _lifecycleOwner;
    private SQLiteDatabase _db;
    private AlarmEntity _entity;

    private Runnable _userSelectTimeAction;
    private final Object _userSelectTimeActionLock = new Object();

    /**
     * Constructor
     * @param lifecycleOwner lifecycle owner for the live data members
     * @param db database connection
     * @param entity alarm database entity
     */
    public AlarmViewModel(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull SQLiteDatabase db,
            @NonNull AlarmEntity entity) {
        _lifecycleOwner = lifecycleOwner;
        _db = db;
        _entity = entity;
    }

    public LifecycleOwner getLifecycleOwner() { return _lifecycleOwner;}

    /**
     * ID property.  Read-only.
     * @return ID of entry
     */
    @Bindable
    public long getID() { return _entity.get_id(); }

    /**
     * enabled property
     * @return alarm enabled
     */
    @Bindable
    public synchronized boolean getEnabled() {
        return _entity.getEnabled();
    }

    public synchronized void setEnabled(boolean enabled) {
        boolean oldVal = _entity.getEnabled();
        if (oldVal != enabled) {
            _entity.setEnabled(enabled);
            notifyPropertyChanged(BR.enabled);
            saveToDB();
        }
    }

    /**
     * "on or after" property
     * @return date/time combo of alarm time and "no earlier than" date to start alarm
     */
    @Bindable
    public synchronized Date getOnOrAfter() { return _entity.getOnOrAfter(); }

    public synchronized void setOnOrAfter(Date onOrAfter) {
        Date oldVal = _entity.getOnOrAfter();
        if (!oldVal.equals(onOrAfter)) {
            _entity.setOnOrAfter(onOrAfter);
            notifyPropertyChanged(BR.onOrAfter);
            saveToDB();
        }
    }

    /**
     * repeat property
     * @return whether alarm is repeating
     */
    @Bindable
    public synchronized boolean getRepeat() {
        return _entity.getRepeat();
    }

    public synchronized void setRepeat(boolean repeat) {
        boolean oldVal = _entity.getRepeat();
        if (oldVal != repeat) {
            _entity.setRepeat(repeat);
            notifyPropertyChanged(BR.repeat);
            saveToDB();
        }
    }

    /**
     * daysOfWeek property
     * @return days of week that alarm is valid
     */
    @Bindable
    public synchronized byte getDaysOfWeek() { return _entity.getDaysOfWeek(); }

    public synchronized void setDaysOfWeek(byte days) {
        byte oldVal = _entity.getDaysOfWeek();
        if (oldVal != days) {
            _entity.setDaysOfWeek(days);
            notifyPropertyChanged(BR.daysOfWeek);
            saveToDB();
        }
    }

    /**
     * label property
     * @return label for alarm
     */
    @Bindable
    public synchronized String getLabel() {
        return _entity.getLabel();
    }

    public synchronized void setLabel(String label) {
        String oldVal = _entity.getLabel();
        boolean changed = true;
        if (oldVal != null && label != null) {
            if (oldVal.equals(label)) {
                changed = false;
            }
        } else if (oldVal == null && label == null) {
            changed = false;
        }
        if (changed) {
            _entity.setLabel(label);
            notifyPropertyChanged(BR.label);
            saveToDB();
        }
    }

    public void runUserSelectTimeAction() {
        synchronized (_userSelectTimeActionLock) {
            if (_userSelectTimeAction != null) {
                _userSelectTimeAction.run();
            }
        }
    }

    public synchronized void setUserSelectTimeAction(Runnable action) {
        synchronized (_userSelectTimeActionLock) {
            if (_userSelectTimeAction != action) {
                _userSelectTimeAction = action;
            }
        }
    }

    private void saveToDB() {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        _entity.persistToDB(_db);
                    }
                }
        );
        t.start();
    }
}
