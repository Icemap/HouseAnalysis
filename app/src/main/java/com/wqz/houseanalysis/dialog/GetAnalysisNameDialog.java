package com.wqz.houseanalysis.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.utils.StringUtils;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Wqz on 2018/3/14 0014.
 */

public class GetAnalysisNameDialog extends Dialog
{
    private Context context;

    Boolean isNum = false;
    StatusListener defaultListener;

    @BindView(R.id.met_transfer_num)
    MaterialEditText metTransferNum;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public GetAnalysisNameDialog(@NonNull Context context)
    {
        super(context, R.style.TransDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorTrans);
        setContentView(R.layout.dialog_analysis_name);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);

        onInit();
    }

    private void onInit()
    {
        defaultListener = new StatusListener() {
            @Override
            public void onDimiss()
            {
            }

            @Override
            public void onConfirm(String analysisName)
            {
            }
        };
    }

    @OnClick(R.id.tv_confirm)
    public void onConfirm()
    {
        defaultListener.onConfirm(metTransferNum.getText().toString());
        this.dismiss();
    }

    @OnClick(R.id.v_close)
    public void onClose()
    {
        defaultListener.onDimiss();
        this.dismiss();
    }

    public interface StatusListener
    {
        void onDimiss();
        void onConfirm(String analysisName);
    }

    public void setStatusListener(GetAnalysisNameDialog.StatusListener listener)
    {
        this.defaultListener = listener;
    }
}
