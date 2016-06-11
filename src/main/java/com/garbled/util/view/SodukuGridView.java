package com.garbled.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Garbled on 3/18/2016.
 */
public class SodukuGridView extends GridView {
    public SodukuGridView(Context context) {
        super(context);
    }

    public SodukuGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SodukuGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
