package com.wqz.houseanalysis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.wqz.houseanalysis.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wqz on 2018/3/14 0014.
 */

public class DownloadDialog extends Dialog
{
    private Context context;
    private UIHandler uiHandler;

    @BindView(R.id.tv_download_active)
    TextView tvActive;

    public DownloadDialog(@NonNull Context context)
    {
        super(context, R.style.SelfDialogTheme);
        this.context = context;
        uiHandler = new UIHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorTrans);
        setContentView(R.layout.dialog_download);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);

        onInit();
    }

    private void onInit()
    {
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    String sTaskBaseCycle = ">-<";
    TimerTask task = new TimerTask()
    {
        @Override
        public void run()
        {
            Message message = new Message();
            uiHandler.sendMessage(message);
        }
    };

    public class UIHandler extends android.os.Handler
    {
        WeakReference<DownloadDialog> softReference;
        Integer pointNum = 0;

        public UIHandler(DownloadDialog dialog)
        {
            softReference = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            DownloadDialog context = softReference.get();
            if(context != null)
            {
                if(pointNum == 4)
                    pointNum = 0;

                String subStr = "";
                for(int i = 0;i < pointNum;i ++)
                {
                    subStr += ".";
                }
                pointNum ++;
                context.tvActive.setText(subStr + sTaskBaseCycle + subStr);
            }
        }
    }
}
