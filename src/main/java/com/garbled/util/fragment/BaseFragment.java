package com.garbled.util.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garbled.util.activity.BaseActivity;

/**
 * Created by Garbled on 2016/5/29.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity activity;
    //初始化视图
    protected abstract void initView(View view, Bundle saveInstanceState);
    //获取布局文件
    protected abstract int getLayoutId();

    protected BaseActivity getHoldingActivity(){
        return activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        initView(view,savedInstanceState);
        return view;
    }
}
