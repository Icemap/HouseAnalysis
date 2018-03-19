package com.wqz.houseanalysis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wqz.houseanalysis.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by 51667 on 2018/3/19.
 */

public class ParamSetDialog extends Dialog
{
    Boolean isTotalPriceCkecked = false;
    Boolean isUnitPriceCkecked = false;
    Boolean isRoomNumCkecked = false;
    Boolean isBuildTimeCkecked = false;

    @BindView(R.id.sb_total_price)
    SeekBar sbTotalPrice;
    @BindView(R.id.sb_unit_price)
    SeekBar sbUnitPrice;
    @BindView(R.id.sb_room_num)
    SeekBar sbRoomNum;
    @BindView(R.id.sb_build_time)
    SeekBar sbBuildTime;

    @BindView(R.id.tv_result_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_result_unit_price)
    TextView tvUnitPrice;
    @BindView(R.id.tv_result_room_num)
    TextView tvRoomNum;
    @BindView(R.id.tv_result_build_time)
    TextView tvBuildTime;

    ParamSetDialog.StatusListener defaultListener;

    private Context context;

    public ParamSetDialog(@NonNull Context context)
    {
        super(context, R.style.TransDialogTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.colorTrans);
        setContentView(R.layout.dialog_param_set);
        ButterKnife.bind(this);

        onInit();
    }

    private void onInit()
    {
        defaultListener = new StatusListener()
        {
            @Override
            public void onConfirm(Map<String, String> paramMap)
            {

            }
        };
        sbTotalPrice.setOnSeekBarChangeListener(seekBarChangeListener);
        sbUnitPrice.setOnSeekBarChangeListener(seekBarChangeListener);
        sbRoomNum.setOnSeekBarChangeListener(seekBarChangeListener);
        sbBuildTime.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b)
        {
            setTextByChange();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
        }
    };


    private void setTextByChange()
    {
        if(isTotalPriceCkecked)
            tvTotalPrice.setText("总价：" + (sbTotalPrice.getProgress() + 50) + "万");
        else
            tvTotalPrice.setText("总价:不限制");

        if(isUnitPriceCkecked)
            tvUnitPrice.setText("单价：" + (sbUnitPrice.getProgress() + 10000) + "元");
        else
            tvUnitPrice.setText("单价:不限制");

        if(isRoomNumCkecked)
            tvRoomNum.setText("房型：" + (sbRoomNum.getProgress() + 1) + "室");
        else
            tvRoomNum.setText("房型:不限制");

        if(isBuildTimeCkecked)
            tvBuildTime.setText("建造时间：" + (sbBuildTime.getProgress() + 1980) + "年");
        else
            tvBuildTime.setText("建造时间:不限制");
    }

    @OnClick(R.id.btn_confirm)
    public void btnClicked()
    {
        Map<String, String> map = new HashMap<>();
        if(isTotalPriceCkecked)
            map.put("price", (sbTotalPrice.getProgress() + 50) + "");
        if(isUnitPriceCkecked)
            map.put("unitPrice", (sbUnitPrice.getProgress() + 10000) + "");
        if(isRoomNumCkecked)
            map.put("roomNum", (sbRoomNum.getProgress() + 1) + "");
        if(isBuildTimeCkecked)
            map.put("buildTime", (sbBuildTime.getProgress() + 1980) + "");
        defaultListener.onConfirm(map);
        this.dismiss();
    }

    @OnCheckedChanged(R.id.cb_total_price)
    public void onTotalCheckedChanged()
    {
        isTotalPriceCkecked = !isTotalPriceCkecked;
        setTextByChange();
    }

    @OnCheckedChanged(R.id.cb_unit_price)
    public void onUnitCheckedChanged()
    {
        isUnitPriceCkecked = !isUnitPriceCkecked;
        setTextByChange();
    }

    @OnCheckedChanged(R.id.cb_room_num)
    public void onRoomCheckedChanged()
    {
        isRoomNumCkecked = !isRoomNumCkecked;
        setTextByChange();
    }

    @OnCheckedChanged(R.id.cb_build_time)
    public void onTimeCheckedChanged()
    {
        isBuildTimeCkecked = !isBuildTimeCkecked;
        setTextByChange();
    }

    public interface StatusListener
    {
        void onConfirm(Map<String, String> paramMap);
    }

    public void setStatusListener(StatusListener listener)
    {
        this.defaultListener = listener;
    }
}
