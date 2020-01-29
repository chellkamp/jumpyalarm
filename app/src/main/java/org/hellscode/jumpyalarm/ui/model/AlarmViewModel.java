package org.hellscode.jumpyalarm.ui.model;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.hellscode.jumpyalarm.data.AlarmEntity;

import java.util.Date;

/**
 * View model for an alarm's settings
 */
public class AlarmViewModel {

    private LifecycleOwner _lifecycleOwner;
    private SQLiteDatabase _db;
    private AlarmEntity _entity;

    private MutableLiveData<Boolean> _enabled = new MutableLiveData<>();
    private MutableLiveData<Date> _firstAlarm = new MutableLiveData<>();
    private MutableLiveData<Boolean> _repeat = new MutableLiveData<>();
    private MutableLiveData<Byte> _daysOfWeek = new MutableLiveData<>();
    private MutableLiveData<String> _label = new MutableLiveData<>();

    /**
     * Constructor
     * @param lifecycleOwner lifecycle owner for the live data members
     * @param db database connection
     * @param entity alarm database entity
     */
    AlarmViewModel(
            @NonNull LifecycleOwner lifecycleOwner,
            @NonNull SQLiteDatabase db,
            @NonNull AlarmEntity entity) {
        _lifecycleOwner = lifecycleOwner;
        _db = db;
        _entity = entity;

        // tie members to entity
        _enabled.setValue(_entity.getEnabled());
        _enabled.observe(_lifecycleOwner,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        boolean oldVal = _entity.getEnabled();
                        if (oldVal != aBoolean) {
                            _entity.setEnabled(aBoolean);

                            saveToDB();
                        }

                    }
                });

        _firstAlarm.setValue(_entity.getFirstAlarm());
        _firstAlarm.observe(_lifecycleOwner,
                new Observer<Date>() {
                    @Override
                    public void onChanged(Date date) {
                        Date oldVal = _entity.getFirstAlarm();
                        if (!oldVal.equals(date)) {
                            _entity.setFirstAlarm(date);
                            saveToDB();
                        }
                    }
                });

        _repeat.setValue(_entity.getRepeat());
        _repeat.observe(_lifecycleOwner,
                new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        boolean oldVal = _entity.getRepeat();
                        if (oldVal != aBoolean) {
                            _entity.setRepeat(aBoolean);
                            saveToDB();
                        }
                    }
                });

        _daysOfWeek.setValue(_entity.getDaysOfWeek());
        _daysOfWeek.observe(_lifecycleOwner,
                new Observer<Byte>() {
                    @Override
                    public void onChanged(Byte aByte) {
                        byte oldVal = _entity.getDaysOfWeek();
                        if (oldVal != aByte) {
                            _entity.setDaysOfWeek(aByte);
                            saveToDB();
                        }
                    }
                });

        _label.setValue(_entity.getLabel());
        _label.observe(_lifecycleOwner,
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        String oldVal = _entity.getLabel();
                        boolean changed = true;
                        if (oldVal != null && s != null) {
                            if (oldVal.equals(s)) {
                                changed = false;
                            }
                        } else if (oldVal == null && s == null) {
                            changed = false;
                        }
                        if (changed) {
                            _entity.setLabel(s);
                            saveToDB();
                        }
                    }
                });

    }

    /**
     * enabled property
     * @return alarm enabled
     */
    public MutableLiveData<Boolean> getEnabled() {
        return _enabled;
    }

    /**
     * firstAlarm property
     * @return date/time of first scheduled alarm
     */
    public MutableLiveData<Date> getFirstAlarm() {
        return _firstAlarm;
    }

    /**
     * repeat property
     * @return whether alarm is repeating
     */
    public MutableLiveData<Boolean> getRepeat() {
        return _repeat;
    }

    /**
     * daysOfWeek property
     * @return days of week that alarm is valid
     */
    public MutableLiveData<Byte> getDaysOfWeek() {
        return _daysOfWeek;
    }

    /**
     * label property
     * @return label for alarm
     */
    public MutableLiveData<String> getLabel() {
        return _label;
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
