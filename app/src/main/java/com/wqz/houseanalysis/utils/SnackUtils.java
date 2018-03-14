package com.wqz.houseanalysis.utils;

import android.app.ActionBar;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by 51667 on 2018/3/13.
 */

public class SnackUtils
{
    public static void makeSnackBar(View rootView, String text, int duration, final View getOutView)
    {
        Snackbar
                .make(rootView, text, duration)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>()
                {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event)
                    {
                        super.onDismissed(transientBottomBar, event);

                        getOutView.setY(getOutView.getY() + transientBottomBar.getView().getHeight());
                    }

                    @Override
                    public void onShown(Snackbar transientBottomBar)
                    {
                        super.onShown(transientBottomBar);
                        getOutView.setY(getOutView.getY() - transientBottomBar.getView().getHeight());
                    }
                })
                .show();
    }

    public static void makeSnackBar(View rootView, String text, int duration,
                                    final View getOutView, View.OnClickListener listener, String actionName)
    {
        Snackbar
                .make(rootView, text, duration)
                .setAction(actionName, listener)
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>()
                {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event)
                    {
                        super.onDismissed(transientBottomBar, event);

                        getOutView.setY(getOutView.getY() + transientBottomBar.getView().getHeight());
                    }

                    @Override
                    public void onShown(Snackbar transientBottomBar)
                    {
                        super.onShown(transientBottomBar);
                        getOutView.setY(getOutView.getY() - transientBottomBar.getView().getHeight());
                    }
                })
                .show();
    }
}
