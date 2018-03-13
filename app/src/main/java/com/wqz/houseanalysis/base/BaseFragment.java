package com.wqz.houseanalysis.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/17 0017.
 */

public abstract class BaseFragment extends Fragment
{
    private View rootView;

    protected abstract int initLayoutId();
    protected void onInitLogic() {}
    public abstract Boolean isMarginLeft();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (null != rootView)
        {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent)
            {
                parent.removeView(rootView);
            }
        }
        else
        {
            rootView = inflater.inflate(initLayoutId(), container, false);
            ButterKnife.bind(this, rootView);
            onInitLogic();
        }
        return rootView;
    }
}
