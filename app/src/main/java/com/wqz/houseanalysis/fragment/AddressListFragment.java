package com.wqz.houseanalysis.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.activity.AnJuKeActivity;
import com.wqz.houseanalysis.activity.ListLianJiaHouseActivity;
import com.wqz.houseanalysis.adapter.AddressAdapter;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.base.BaseFragment;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.utils.SortUtils;
import com.wqz.houseanalysis.utils.StatusUtils;

import butterknife.BindView;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class AddressListFragment extends BaseFragment
{
    @BindView(R.id.rv_address_list)
    RecyclerView rvAddressList;

    AddressAdapter addressAdapter;

    @Override
    protected int initLayoutId()
    {
        return R.layout.fragment_address_list;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter()
    {
        rvAddressList.setHasFixedSize(true);
        rvAddressList.setLayoutManager(new LinearLayoutManager(getActivity()));

        addressAdapter = new AddressAdapter(null);
        addressAdapter.openLoadAnimation(SCALEIN);
        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                AddressBean addressBean = StatusUtils.getActiveStatus().addressList.get(position);
                BaseApplication.getInstances().setCurrentAddress(addressBean);
                jumpToActivity(addressBean);
            }
        });
        addressAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvAddressList.getParent());
        rvAddressList.setAdapter(addressAdapter);

        addressAdapter.setNewData(SortUtils.getSortedByAddress());
    }

    private void jumpToActivity(AddressBean addressBean)
    {
        switch (addressBean.getSrc())
        {
            case "AnJuKe":
                startActivity(new Intent(getActivity(), AnJuKeActivity.class));
                break;
            case "LianJia":
                startActivity(new Intent(getActivity(), ListLianJiaHouseActivity.class));
                break;
        }
    }
}
