package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.MutilHouseAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AddressActiveStatus;
import com.wqz.houseanalysis.bean.AddressBean;
import com.wqz.houseanalysis.bean.AddressListBean;
import com.wqz.houseanalysis.bean.AnJuKeHouseBean;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.bean.MultiHouseBean;
import com.wqz.houseanalysis.bean.MultiHouseItemType;
import com.wqz.houseanalysis.bean.RequestHouseBean;
import com.wqz.houseanalysis.dialog.DownloadDialog;
import com.wqz.houseanalysis.dialog.ParamSetDialog;
import com.wqz.houseanalysis.utils.StatusUtils;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class ParamHouseActivity extends BaseActivity
{
    Map<String, String> paramMap;
    private MutilHouseAdapter mutilHouseAdapter;
    List<MultiHouseBean> multiHouseBeanList;
    DownloadDialog downloadDialog;

    @BindView(R.id.rv_param_house_list)
    RecyclerView rvParamHouse;
    @BindView(R.id.fab_param_house_setting)
    FloatingActionButton fabParamHouseSetting;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_param_house;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);
        rvParamHouse.setHasFixedSize(true);
        rvParamHouse.setLayoutManager(new LinearLayoutManager(this));
        mutilHouseAdapter = new MutilHouseAdapter(multiHouseBeanList);
        mutilHouseAdapter.openLoadAnimation(SCALEIN);
        mutilHouseAdapter.setEmptyView(R.layout.loading_net_view, (ViewGroup) rvParamHouse.getParent());
        rvParamHouse.setAdapter(mutilHouseAdapter);
        downloadDialog = new DownloadDialog(ParamHouseActivity.this);

        paramMap = new HashMap<>();
        paramMap.put("jsonAddressList",new Gson().toJson(getAddressList()));
        getData();
    }

    private void getData()
    {
        downloadDialog.show();
        OkHttpUtils
                .post()
                .url(UrlUtils.DATA_PARAM)
                .params(paramMap)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {
                        downloadDialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        RequestHouseBean requestHouseBean = new Gson().fromJson(response,RequestHouseBean.class);
                        buildAdapterData(requestHouseBean);
                        mutilHouseAdapter.setNewData(multiHouseBeanList);

                        mutilHouseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
                            {
                                MultiHouseBean item = multiHouseBeanList.get(position);
                                if(item.getItemType() == MultiHouseItemType.AnJuKe)
                                {
                                    BaseApplication.getInstances().setCurrentUrl(item.getAnJuKeHouseBean().getContenturl());
                                    startActivity(new Intent(ParamHouseActivity.this, WebActivity.class));
                                }
                                else if(item.getItemType() == MultiHouseItemType.LianJia)
                                {
                                    BaseApplication.getInstances().setCurrentUrl(item.getLianJiaHouseBean().getContentUrl());
                                    startActivity(new Intent(ParamHouseActivity.this, WebActivity.class));
                                }
                            }
                        });

                        downloadDialog.dismiss();
                    }
                });
    }

    private void buildAdapterData(RequestHouseBean requestData)
    {
        multiHouseBeanList = new ArrayList<>();
        for(LianJiaHouseBean lianJiaHouseBean : requestData.getLianjiaHouseDataList())
        {
            MultiHouseBean bean = new MultiHouseBean(MultiHouseItemType.LianJia);
            bean.setLianJiaHouseBean(lianJiaHouseBean);
            multiHouseBeanList.add(bean);
        }
        for(AnJuKeHouseBean anJuKeHouseBean : requestData.getAnjukeHouseDataList())
        {
            MultiHouseBean bean = new MultiHouseBean(MultiHouseItemType.AnJuKe);
            bean.setAnJuKeHouseBean(anJuKeHouseBean);
            multiHouseBeanList.add(bean);
        }
    }

    private AddressListBean getAddressList()
    {
        AddressListBean addressListBean = new AddressListBean();
        AddressActiveStatus addressActiveStatus = StatusUtils.getActiveStatus();
        for (AddressBean addressBean : addressActiveStatus.addressList)
        {
            if(addressBean.getSrc().equals("AnJuKe"))
                addressListBean.getAnjukeAddressList().add(addressBean.getName());
            else if(addressBean.getSrc().equals("LianJia"))
                addressListBean.getLianjiaAddressList().add(addressBean.getName());
        }
        return addressListBean;
    }

    @OnClick(R.id.fab_param_house_setting)
    public void onSetting()
    {
        ParamSetDialog dialog = new ParamSetDialog(ParamHouseActivity.this);
        dialog.show();
        dialog.setStatusListener(listener);
    }

    ParamSetDialog.StatusListener listener = new ParamSetDialog.StatusListener()
    {
        @Override
        public void onConfirm(Map<String, String> paramMap)
        {
            ParamHouseActivity.this.paramMap.putAll(paramMap);
            getData();
        }
    };
}
