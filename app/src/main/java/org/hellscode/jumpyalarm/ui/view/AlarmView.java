package org.hellscode.jumpyalarm.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.databinding.AlarmViewBinding;
import org.hellscode.jumpyalarm.ui.model.AlarmViewModel;

/**
 * AlarmView compound control
 */
public class AlarmView extends LinearLayout {

    AlarmViewBinding _binding;

    /**
     * Constructor
     * @param context context
     */
    public AlarmView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributes
     */
    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Constructor
     * @param context constructor
     * @param attrs attributes
     * @param defStyleAttr default style attribute
     */
    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributes
     * @param defStyleAttr default style attribute
     * @param defStyleRes defautl style resource
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Set the underlying data object for this display
     * @param viewModel data
     */
    public void setViewModel(AlarmViewModel viewModel) {
        _binding.setViewModel(viewModel);
        _binding.setLifecycleOwner(viewModel.getLifecycleOwner());
        _binding.invalidateAll();
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null) {
            Log.e("AlarmView", "Call to inflater service returned null.");
            System.exit(1);
        } else {
            _binding = DataBindingUtil.inflate(inflater, R.layout.alarm_view, this, true);
        }
    }

}
