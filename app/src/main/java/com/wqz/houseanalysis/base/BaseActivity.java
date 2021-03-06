package com.wqz.houseanalysis.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * Created by Wqz on 2016/12/23.
 */

abstract public class BaseActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        ButterKnife.bind(this);
        onInitLogic(savedInstanceState);
    }

    protected abstract int initLayoutId();
    protected void onInitLogic(Bundle savedInstanceState) {}


    public BaseApplication getBaseApplication()
    {
        return ((BaseApplication) getApplication());
    }

    public void setMainContainerFragment(BaseFragment fragment, int fragmentViewId)
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(fragmentViewId, fragment);
        fragmentTransaction.commit();
    }
}
