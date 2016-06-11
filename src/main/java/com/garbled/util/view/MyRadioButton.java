package com.garbled.util.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

import com.soundgroup.okay.R;

/**
 * Created by Ringo on 16/3/14.
 */

public class MyRadioButton extends Button implements Checkable {

    private boolean checked = false;

    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        this.setBackgroundResource(checked ? R.mipmap.btn_selected : R.mipmap.btn_unselected);
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
