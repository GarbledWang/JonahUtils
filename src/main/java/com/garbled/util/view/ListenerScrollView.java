package com.garbled.util.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 带有滚动监听的ScrollView
 * Created by Garbled on 2016/4/11.
 */
public class ListenerScrollView extends ScrollView {

    //用来当手指离开ScrollView Scrollview还在滚动的时候确定Y的位置
    private int lastScrollY;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int scrollY = ListenerScrollView.this.getScrollY();
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScroll(scrollY);
            }

        }
    };



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }


    public ListenerScrollView(Context context) {
        super(context);
    }

    public ListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);

        void onScroll(float y);//y轴的监听
    }

    private OnScrollChangedListener mOnScrollChangedListener;
    public void setOnScrollChangedListener(OnScrollChangedListener listener) {
        mOnScrollChangedListener = listener;
    }
}