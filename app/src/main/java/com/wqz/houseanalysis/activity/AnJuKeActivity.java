package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.AnJuKeAdapter;
import com.wqz.houseanalysis.adapter.LianJiaHouseAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.base.BaseApplication;
import com.wqz.houseanalysis.bean.AnJuKeHouseBean;
import com.wqz.houseanalysis.bean.LianJiaHouseBean;
import com.wqz.houseanalysis.utils.UrlUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class AnJuKeActivity extends BaseActivity
{
    @BindView(R.id.rv_anjuke_house_list)
    RecyclerView rvAnJuKeHouseList;

    AnJuKeAdapter anJuKeHouseAdapter;
    List<AnJuKeHouseBean> houseBeanList;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_an_ju_ke;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void initAdapter()
    {
        rvAnJuKeHouseList.setHasFixedSize(true);
        rvAnJuKeHouseList.setLayoutManager(new LinearLayoutManager(this));

        anJuKeHouseAdapter = new AnJuKeAdapter(null);
        anJuKeHouseAdapter.openLoadAnimation();
        anJuKeHouseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BaseApplication.getInstances().setCurrentUrl(houseBeanList.get(position).getContenturl());
                startActivity(new Intent(AnJuKeActivity.this, WebActivity.class));
            }
        });
        anJuKeHouseAdapter.setEmptyView(R.layout.loading_view, (ViewGroup) rvAnJuKeHouseList.getParent());
        rvAnJuKeHouseList.setAdapter(anJuKeHouseAdapter);
        loadData();
    }

    private void loadData()
    {
        OkHttpUtils
                .post()
                .url(UrlUtils.DATA_ANJUKE)
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
                                new TypeToken<List<AnJuKeHouseBean>>(){}.getType());
                        anJuKeHouseAdapter.setNewData(houseBeanList);
                    }
                });
    }
}
