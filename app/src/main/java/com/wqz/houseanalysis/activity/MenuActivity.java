package com.wqz.houseanalysis.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wqz.houseanalysis.R;
import com.wqz.houseanalysis.adapter.MenuAdapter;
import com.wqz.houseanalysis.base.BaseActivity;
import com.wqz.houseanalysis.bean.MenuBean;

import java.util.List;

import butterknife.BindView;

import static com.chad.library.adapter.base.BaseQuickAdapter.SCALEIN;

public class MenuActivity extends BaseActivity
{
    @BindView(R.id.list_view)
    RecyclerView rvMenu;

    MenuAdapter menuAdapter;
    List<MenuBean> menuBeans;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_menu;
    }

    @Override
    protected void onInitLogic(Bundle savedInstanceState)
    {
        super.onInitLogic(savedInstanceState);

        onInitAdapter();
    }

    private void onInitAdapter()
    {
        menuBeans = MenuBean.getAllMenus(this.getResources());
        rvMenu.setHasFixedSize(true);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));

        menuAdapter = new MenuAdapter(menuBeans);
        menuAdapter.openLoadAnimation(SCALEIN);
        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                try
                {
                    startActivity(new Intent(MenuActivity.this,
                            Class.forName(menuBeans.get(position).getListClass())));
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
        rvMenu.setAdapter(menuAdapter);
    }
}
