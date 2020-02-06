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

    private final Object _memberLock = new Object();

    private boolean _showDetails;

    private Runnable _userSelectTimeAction;
    private final Object _userSelectTimeActionLock = new Object();

    private Runnable _onShowDetailsChanged;
    private final Object _onShowDetailsChangedLock = new Object();


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
    public long getID() {
        long retVal;
        synchronized (_memberLock) {
            retVal = _entity.get_id();
        }
        return retVal;
    }

    /**
     * enabled property
     * @return alarm enabled
     */
    @Bindable
    public boolean getEnabled() {
        boolean retVal;
        synchronized (_memberLock) {
            retVal =_entity.getEnabled();
        }
        return retVal;
    }

    public void setEnabled(boolean enabled) {
        boolean changed;
        synchronized (_memberLock) {
            boolean oldVal = _entity.getEnabled();
            changed = oldVal != enabled;
            if (changed) {
                _entity.setEnabled(enabled);
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.enabled);
            saveToDB();
        }
    }

    /**
     * "on or after" property
     * @return date/time combo of alarm time and "no earlier than" date to start alarm
     */
    @Bindable
    public Date getOnOrAfter() {
        Date retVal;
        synchronized (_memberLock) {
            retVal = _entity.getOnOrAfter();
        }
        return retVal;
    }

    public void setOnOrAfter(Date onOrAfter) {
        boolean changed;
        synchronized (_memberLock) {
            Date oldVal = _entity.getOnOrAfter();
            changed = !oldVal.equals(onOrAfter);
            if (changed) {
                _entity.setOnOrAfter(onOrAfter);
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.onOrAfter);
            saveToDB();
        }
    }

    /**
     * repeat property
     * @return whether alarm is repeating
     */
    @Bindable
    public boolean getRepeat() {
        boolean retVal;
        synchronized (_memberLock) {
            retVal = _entity.getRepeat();
        }
        return retVal;
    }

    public void setRepeat(boolean repeat) {
        boolean changed;
        synchronized (_memberLock) {
            boolean oldVal = _entity.getRepeat();
            changed = oldVal != repeat;
            if (changed) {
                _entity.setRepeat(repeat);
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.repeat);
            saveToDB();
        }
    }

    /**
     * daysOfWeek property
     * @return days of week that alarm is valid
     */
    @Bindable
    public byte getDaysOfWeek() {
        byte retVal;
        synchronized (_memberLock) {
            retVal = _entity.getDaysOfWeek();
        }
        return retVal;
    }

    public void setDaysOfWeek(byte days) {
        boolean changed;
        synchronized (_memberLock) {
            byte oldVal = _entity.getDaysOfWeek();
            changed = oldVal != days;
            if (changed) {
                _entity.setDaysOfWeek(days);
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.daysOfWeek);
            saveToDB();
        }
    }

    /**
     * label property
     * @return label for alarm
     */
    @Bindable
    public String getLabel() {
        String retVal;
        synchronized (_memberLock) {
            retVal = _entity.getLabel();
        }
        return retVal;
    }

    public void setLabel(String label) {
        boolean changed = true;
        synchronized (_memberLock) {
            String oldVal = _entity.getLabel();
            if (oldVal != null && label != null) {
                if (oldVal.equals(label)) {
                    changed = false;
                }
            } else if (oldVal == null && label == null) {
                changed = false;
            }
            if (changed) {
                _entity.setLabel(label);
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.label);
            saveToDB();
        }
    }

    public void runUserSelectTimeAction() {
        Runnable action;
        synchronized (_userSelectTimeActionLock) {
            action = _userSelectTimeAction;
        }

        if (action != null) {
            action.run();
        }
    }

    public void setUserSelectTimeAction(Runnable action) {
        synchronized (_userSelectTimeActionLock) {
            if (_userSelectTimeAction != action) {
                _userSelectTimeAction = action;
            }
        }
    }

    /**
     * 'Show Details" property
     * @return true if details should be shown; false otherwise
     */
    @Bindable
    public boolean getShowDetails() {
        boolean retVal;
        synchronized (_memberLock) {
            retVal = _showDetails;
        }
        return retVal;
    }

    public void setShowDetails(boolean showDetails) {
        boolean changed;
        synchronized (_memberLock) {
            changed = _showDetails != showDetails;
            if (changed) {
                _showDetails = showDetails;
            }
        }

        if (changed) {
            notifyPropertyChanged(BR.showDetails);
            runOnShowDetailsChanged();
        }
    }

    private void runOnShowDetailsChanged() {
        Runnable action;
        synchronized (_onShowDetailsChangedLock) {
            action = _onShowDetailsChanged;
        }

        if (action != null) {
            action.run();
        }
    }

    public void setOnShowDetailsChanged(Runnable action) {
        synchronized (_onShowDetailsChangedLock) {
            if (_onShowDetailsChanged != action) {
                _onShowDetailsChanged = action;
            }
        }
    }

    private void saveToDB() {
        Thread t = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        synchronized (_memberLock) {
                            _entity.persistToDB(_db);
                        }
                    }
                }
        );
        t.start();
    }
}
