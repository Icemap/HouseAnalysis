package com.wqz.houseanalysis.base;

import android.app.Activity;
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
        onInitLogic();
    }

    protected abstract int initLayoutId();
    protected void onInitLogic() {}

    public BaseApplication getBaseApplication()
    {
        return ((BaseApplication) getApplication());
    }
}
