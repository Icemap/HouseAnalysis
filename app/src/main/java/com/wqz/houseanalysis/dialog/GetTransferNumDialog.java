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

public class GetTransferNumDialog extends Dialog
{
    private Context context;

    Boolean isNum = false;
    StatusListener defaultListener;

    @BindDrawable(R.drawable.confirm_bg)
    Drawable confirmBg;
    @BindDrawable(R.drawable.confirm_trans_bg)
    Drawable confirmTransBg;

    @BindView(R.id.met_transfer_num)
    MaterialEditText metTransferNum;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public GetTransferNumDialog(@NonNull Context context)
    {
        super(context, R.style.TransDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorTrans);
        setContentView(R.layout.dialog_transfer_num);
        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);

        onInit();
    }

    private void onInit()
    {
        metTransferNum.addTextChangedListener(textWatcher);
        defaultListener = new GetTransferNumDialog.StatusListener()
        {
            @Override
            public void onDimiss()
            {
            }

            @Override
            public void onConfirm(Integer mins)
            {
            }
        };
    }

    TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            String s = editable.toString();
            if(StringUtils.isNumeric(s) && !isNum)
            {
                changeConfirmAnim("确认", confirmBg);
                isNum = true;
            }
            else if(!StringUtils.isNumeric(s) && isNum)
            {
                changeConfirmAnim("填个整数", confirmTransBg);
                isNum = false;
            }
        }
    };

    @OnClick(R.id.tv_confirm)
    public void onConfirm()
    {
        if(!isNum) return;
        defaultListener.onConfirm(Integer.parseInt(metTransferNum.getText().toString()));
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
        void onConfirm(Integer transferNum);
    }

    public void setStatusListener(GetTransferNumDialog.StatusListener listener)
    {
        this.defaultListener = listener;
    }

    private void changeConfirmAnim(final String newData, final Drawable drawable)
    {
        final ObjectAnimator showMainAnim = ObjectAnimator.ofFloat(tvConfirm,
                "rotationX", 0f, -90f);
        showMainAnim.setDuration(200);

        showMainAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                tvConfirm.setText(newData);
                tvConfirm.setBackground(drawable);
                ObjectAnimator hideMainAnim = ObjectAnimator.ofFloat(tvConfirm,
                        "rotationX", 90f, 0f);
                hideMainAnim.setDuration(200);
                hideMainAnim.start();
            }
        });

        showMainAnim.start();
    }
}
