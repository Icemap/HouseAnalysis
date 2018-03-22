package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.LianJiaHouseAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class ListLianJiaHouseActivity extends BaseActivity
{
    @BindView(R.id.rv_lianjia_house_list)
    RecyclerView rvLianJiaHouseList;

    LianJiaHouseAdapter lianJiaHouseAdapter;
    List<LianJiaHouseBean> houseBeanList;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_list_house;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter()
    {
        rvLianJiaHouseList.setHasFixedSize(true);
        rvLianJiaHouseList.setLayoutManager(new LinearLayoutManager(this));

        lianJiaHouseAdapter = new LianJiaHouseAdapter(null);
        lianJiaHouseAdapter.openLoadAnimation(SCALEIN);
        lianJiaHouseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BaseApplication.getInstances().setCurrentUrl(houseBeanList.get(position).getContentUrl());
                startActivity(new Intent(ListLianJiaHouseActivity.this, WebActivity.class));
            }
        });
        lianJiaHouseAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvLianJiaHouseList.getParent());
        rvLianJiaHouseList.setAdapter(lianJiaHouseAdapter);
        loadData();
    }

    private void loadData()
    {
        OkHttpUtils
                .post()
                .url(UrlUtils.DATA_LIANJIA)
                .addParams("address", BaseApplication.getInstances().getCurrentAddress().getName())
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id)
                    {

                    }

                    @Override
                    public void onResponse(String response, int id)
                    {
                        houseBeanList = new Gson().fromJson(response,
                                new TypeToken<List<LianJiaHouseBean>>(){}.getType());
                        lianJiaHouseAdapter.setNewData(houseBeanList);
                    }
                });
    }
}
