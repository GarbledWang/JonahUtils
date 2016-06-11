package com.garbled.util.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.garbled.util.fragment.BaseFragment;
import com.person.jonahutil.R;

/**
 * Created by Garbled on 2016/5/29.
 */
public abstract class AppActivity extends BaseActivity {

    /**
     * 第一个Fragment
     * @return
     */
    protected abstract BaseFragment getFirstFragment();

    /**
     * 获取Intent
     * @param intent
     */
    protected void handleIntent(Intent intent){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        if (getIntent() != null){
            handleIntent(getIntent());
        }//避免重复添加Fragment
        if (getSupportFragmentManager().getFragments() != null){
            BaseFragment baseFragment = getFirstFragment();
            if (baseFragment != null) {
                addFragment(baseFragment);
            }
        }

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentId() {
        return R.id.fragment_container;
    }
}
