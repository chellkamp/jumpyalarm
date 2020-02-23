package org.hellscode.jumpyalarm.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.Observable;
import androidx.databinding.DataBindingUtil;

import org.hellscode.jumpyalarm.BR;
import org.hellscode.jumpyalarm.R;
import org.hellscode.jumpyalarm.databinding.AlarmViewBinding;
import org.hellscode.jumpyalarm.ui.model.AlarmViewModel;
import org.hellscode.util.ui.ErrorDialog;

/**
 * AlarmView compound control
 */
public class AlarmView extends LinearLayout {

    AlarmViewBinding _binding;

    Drawable _expandIcon;
    Drawable _collapseIcon;

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
    public void setViewModel(AlarmViewModel viewModel)
    {
        _binding.setViewModel(viewModel);
        _binding.invalidateAll();
    }

    private void init(Context context) {
        Resources baseRes = getResources();
        _expandIcon = ResourcesCompat.getDrawable(
                baseRes,
                R.drawable.baseline_keyboard_arrow_down_black_36,
                null);
        _collapseIcon = ResourcesCompat.getDrawable(
                baseRes,
                R.drawable.baseline_keyboard_arrow_up_black_36,
                null);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null) {
            ErrorDialog.handleUnrecoverableError(
                    getContext(),
                    "Call to inflater service returned null.",
                     null);
        } else {
            _binding = DataBindingUtil.inflate(inflater, R.layout.alarm_view, this, true);
        }
    }

}
