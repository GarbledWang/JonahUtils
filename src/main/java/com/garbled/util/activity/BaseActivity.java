package com.garbled.util.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.garbled.util.fragment.BaseFragment;

/**
 * Created by Garbled on 2016/5/14.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 布局的id
     * @return 返回布局Id
     */
    protected abstract int getContentViewId();

    /**
     * Fragment的id
     * @return
     */
    protected abstract int getFragmentId();

    /**
     * 替换Framgent 以及加到回退栈
     * @param baseFragment
     */
    protected void addFragment(BaseFragment baseFragment){
        getSupportFragmentManager().beginTransaction()
                .replace(getFragmentId(),baseFragment,baseFragment.getClass().getSimpleName())
                .addToBackStack(baseFragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    /**
     * 移除Fragment
     */
    protected void removeFragment(){
        if (getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStack();
        }else {
            finish();
        }
    }

    /**
     * 返回键的处理
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
