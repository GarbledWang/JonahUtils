package com.garbled.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundgroup.okay.R;
import com.soundgroup.okay.util.DensityUtil;

/**
 * Created by Ringo on 15/11/10.
 */
public class CartView extends ViewGroup {

    private Context mContext;
    private int number = 1;
    private int mTargetWidth;

    private ImageView mMinusImageButton;
    private TextView mNumberTextView;
    private ImageView mAddImageButton;

    private int mBackground;                // TextView背景资源
    private int mTextSize = 20;             // 字体大小, 默认为14sp
    private int mVerticalPadding = 4;       // TextView上下内边距, 默认为4dp
    private int mHorizontalPadding = 8;     // TextView左右内边距, 默认为8dp
    private int mViewSpace = 4;             // TextView左右Margin

    public CartView(Context context) {
        this(context, null);
    }

    public CartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (getChildCount() > 0) {
            throw new RuntimeException("CartView不允许有子元素.");
        }

        this.mContext = context;

        // 读取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CartView);
        mBackground = ta.getResourceId(R.styleable.CartView_textBackground, 0);
        mTextSize = ta.getDimensionPixelSize(R.styleable.CartView_textSize,
                DensityUtil.sp2px(context, mTextSize));
        mVerticalPadding = ta.getDimensionPixelSize(R.styleable
                .CartView_verticalPadding, DensityUtil.dp2px(context,
                mVerticalPadding));
        mHorizontalPadding = ta.getDimensionPixelSize(R.styleable
                .CartView_horizontalPadding, DensityUtil.dp2px(context,
                mHorizontalPadding));
        mViewSpace = ta.getDimensionPixelSize(R.styleable.CartView_viewSpace,
                mViewSpace);
        ta.recycle();

        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        mTargetWidth = (int) paint.measureText("00");
        initializeView();
    }

    // 初始化视图
    private void initializeView() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);

        mMinusImageButton = new ImageView(mContext);
        mMinusImageButton.setLayoutParams(params);
        mMinusImageButton.setImageResource(R.mipmap.btn_minus_grey);
        mMinusImageButton.setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding,
                mVerticalPadding);
        mMinusImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number > 1) {
                    number--;
                    numberChanged();
                }
            }
        });
        addView(mMinusImageButton);

        mNumberTextView = new TextView(mContext);
        LayoutParams editLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams
                .WRAP_CONTENT);
        mNumberTextView.setLayoutParams(editLayoutParams);
        mNumberTextView.setBackgroundResource(mBackground);
        mNumberTextView.setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding,
                mVerticalPadding);
        mNumberTextView.setGravity(Gravity.CENTER);
        mNumberTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mNumberTextView.setText(String.valueOf(number));
        addView(mNumberTextView);

        mAddImageButton = new ImageView(mContext);
        mAddImageButton.setLayoutParams(params);
        mAddImageButton.setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding,
                mVerticalPadding);
        mAddImageButton.setImageResource(R.mipmap.btn_plus_green);
        mAddImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number < 999) {
                    number++;
                    numberChanged();
                }
            }
        });
        addView(mAddImageButton);

        numberChanged();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int widthSum;   // 总宽度, 最终结果为自定义组件的宽度

        // 处理中间的TextView
        View middleView = getChildAt(1);
        int childHeight = middleView.getMeasuredHeight();
        int childWidth = middleView.getMeasuredWidth();
        widthSum = childWidth + mTargetWidth;
        middleView.measure(MeasureSpec.makeMeasureSpec(childWidth + mTargetWidth, MeasureSpec
                .EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY));

        // 处理左右ImageView，重新计算其尺寸
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (i == 1) continue;
            View child = getChildAt(i);
            int ms = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            child.measure(ms, ms);
            widthSum = widthSum + childHeight;
        }

        // 设置组件自身尺寸， 总宽度再加上两个间距(间距默认为0)
        setMeasuredDimension(widthSum + mViewSpace * 2, childHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int left = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            childView.layout(left, 0, left + childWidth, childHeight);
            left += childWidth;
            if (i != childCount - 1) {
                left += mViewSpace;
            }
        }
    }


    public void setNumber(int number) {
        this.number = number;
        numberChanged(false);
    }

    private void numberChanged() {
        numberChanged(true);
    }

    private void numberChanged(boolean isTriggerChanged) {
        mNumberTextView.setText(String.valueOf(number));
        if (mOnNumberChangeListener != null && isTriggerChanged) {
            mOnNumberChangeListener.onNumberChange(number);
        }
        if (number <= 1) {
            mMinusImageButton.setImageResource(R.mipmap.btn_minus_grey);
        } else {
            mMinusImageButton.setImageResource(R.mipmap.btn_minus_green);
        }
    }

    public int getNumber() {
        return this.number;
    }


    // ------------------- 数值更改回调接口 -------------------
    private OnNumberChangeListener mOnNumberChangeListener;

    public void setOnNumberChangeListener(OnNumberChangeListener listener) {
        mOnNumberChangeListener = listener;
    }

    public interface OnNumberChangeListener {
        void onNumberChange(int number);
    }
}