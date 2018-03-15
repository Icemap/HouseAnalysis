package com.wqz.houseanalysis.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.AddressAdapter;
import com.wqz.houseanalysis.adapter.StatusAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.StatusUtils;

import java.util.List;

import butterknife.BindView;

public class ListAddressActivity extends BaseActivity
{

    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;

    AddressAdapter addressAdapter;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_list_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter()
    {
        rvAddressList.setHasFixedSize(true);
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));

        addressAdapter = new AddressAdapter(null);
        addressAdapter.openLoadAnimation();
        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

            }
        });
        addressAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvAddressList.getParent());
        rvAddressList.setAdapter(addressAdapter);
        addressAdapter.setNewData(StatusUtils.getActiveStatus().addressList);
    }
}