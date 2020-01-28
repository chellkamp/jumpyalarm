package org.hellscode.jumpyalarm.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.google.android.material.switchmaterial.SwitchMaterial;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.data.AlarmEntity;
import org.hellscode.jumpyalarm.databinding.AlarmViewBinding;
import org.hellscode.jumpyalarm.ui.model.AlarmViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmView extends LinearLayout {

    private static final String _dateFormat = "yyyy-MM-dd HH:mm";
    private static final String _txtDateFormat = "hh:mm aa";

    AlarmViewBinding _binding;

    TextView _alarmTimeText;
    SwitchMaterial _enabledSwitch;

    private Date _date;

    public AlarmView(Context context) {
        super(context);
        init(context);
    }

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        applyCustomAttrs(attrs);
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        applyCustomAttrs(attrs);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
        applyCustomAttrs(attrs);
    }

    public void setViewModel(AlarmViewModel viewModel) {
        _binding.setViewModel(viewModel);
        _binding.invalidateAll();
    }

    public Date getDate() { return _date; }

    public void setDate(@NonNull Date date) {
        _date = date;
        _alarmTimeText.setText(new SimpleDateFormat(_txtDateFormat, Locale.US).format(_date));
    }

    @InverseBindingAdapter(attribute = "switchEnabled")
    public static boolean getSwitchEnabled(AlarmView view) {
        return view.getSwitchEnabled();
    }

    @BindingAdapter("switchEnabledAttrChanged")
    public static void setSwitchEnabledListeners(AlarmView view, final InverseBindingListener attrChange) {
        view._enabledSwitch.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        attrChange.onChange();
                    }
                }
        );
    }


    public boolean getSwitchEnabled() { return _enabledSwitch.isChecked(); }

    public void setSwitchEnabled(boolean switchEnabled) {
        boolean oldVal = _enabledSwitch.isChecked();

        if (oldVal != switchEnabled) {
            _enabledSwitch.setChecked(switchEnabled);
        }
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null) {
            Log.e("AlarmView", "Call to inflater service returned null.");
            System.exit(1);
        } else {

            _binding = DataBindingUtil.inflate(inflater, R.layout.alarm_view, this, true);

            //inflater.inflate(R.layout.alarm_view, this, true);

            _alarmTimeText = this.findViewById(R.id.alarmTimeText);
            _enabledSwitch = this.findViewById(R.id.enabledSwitch);
        }
    }

    private void applyCustomAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(
                attrs, R.styleable.AlarmView, 0, 0
        );

        try {
            String dateString = ta.getString(R.styleable.AlarmView_date);
            if (dateString != null) {
                Date d = new SimpleDateFormat(_dateFormat, Locale.US).parse(dateString);

                if (d != null) {
                    setDate(d);
                }
            }

            setSwitchEnabled(ta.getBoolean(R.styleable.AlarmView_switchEnabled, true));
        } catch(ParseException ex) {
            // do nothing
        }
        finally {
            ta.recycle();
        }
    }

}
