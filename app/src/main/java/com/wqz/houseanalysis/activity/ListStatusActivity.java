package com.wqz.houseanalysis.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.StatusAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.utils.ListUtils;
import com.wqz.houseanalysis.utils.StatusUtils;

import java.util.List;

import butterknife.BindView;

import static com.chad.library.adapter.base.BaseQuickAdapter.SLIDEIN_RIGHT;

public class ListStatusActivity extends BaseActivity
{
    @BindView(R.id.rv_status_list)
    RecyclerView rvAddressList;

    private StatusAdapter mStatusAdapter;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_list;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        initAdapter();
    }

    private void initAdapter()
    {
        rvAddressList.setHasFixedSize(true);
        rvAddressList.setLayoutManager(new LinearLayoutManager(this));

        mStatusAdapter = new StatusAdapter(null);
        mStatusAdapter.openLoadAnimation(SLIDEIN_RIGHT);
        mStatusAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                ListUtils.setAllNodeStatusUnactive(statusList);
                statusList.get(position).active = true;
                StatusUtils.setStatusList(statusList);
                mStatusAdapter.setNewData(statusList);
            }
        });

        mStatusAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.status_delete:
                        List<AddressActiveStatus> statusList = StatusUtils.getStatusList();
                        ListUtils.setAllNodeStatusUnactive(statusList);
                        AddressActiveStatus status = statusList.get(position);
                        statusList.remove(position);
                        if(status.active && statusList.size() != 0)
                        {
                            status = statusList.get(0);
                            status.active = true;
                            statusList.set(0, status);
                        }
                        StatusUtils.setStatusList(statusList);
                        mStatusAdapter.setNewData(statusList);
                        break;
                }
            }
        });
        mStatusAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvAddressList.getParent());
        rvAddressList.setAdapter(mStatusAdapter);

        mStatusAdapter.setNewData(StatusUtils.getStatusList());
    }
}
