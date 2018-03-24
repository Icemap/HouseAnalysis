package com.wqz.houseanalysis.activity;

import android.os.Bundle;
import android.widget.RadioButton;

import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseImmersiveActivity;
import com.wqz.houseanalysis.fragment.AddressListFragment;
import com.wqz.houseanalysis.fragment.AddressMapFragment;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class MainActivity extends BaseActivity
{
    @BindView(R.id.radio_address_map)
    RadioButton radioMap;
    @BindView(R.id.radio_list_map)
    RadioButton radioList;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        switchToMapFragment();
    }

    @OnCheckedChanged(R.id.radio_address_map)
    public void switchToMapFragment()
    {
        if(!radioMap.isChecked()) return;
        setMainContainerFragment(new AddressMapFragment(), R.id.rl_container_fragment);
    }

    @OnCheckedChanged(R.id.radio_list_map)
    public void switchToListFragment()
    {
        if(!radioList.isChecked()) return;
        setMainContainerFragment(new AddressListFragment(), R.id.rl_container_fragment);
    }
}
