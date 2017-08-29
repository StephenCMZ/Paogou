package com.stephen.paogou.mvp.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stephen.paogou.R;
import com.stephen.paogou.mvp.adapter.ImageAdapter;
import com.stephen.paogou.mvp.model.ImageModel;
import com.stephen.paogou.mvp.presenter.MainPresenter;
import com.stephen.paogou.utils.StringUtil;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemClickListener{

    private Boolean isExit = false;
    private MainPresenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<ImageModel> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        presenter.getList();
    }

    @Override
    public void onRefresh() {
        presenter.getList();
    }

    private void init(){

        presenter = new MainPresenter(this, this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        adapter = new ImageAdapter(imageList);
        adapter.setOnItemClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hidenLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        imageList = presenter.getImageList();
        adapter.setNewData(imageList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        List<String> imageUrls = new ArrayList<>();
        imageUrls.addAll(presenter.getImageUrls());
        if (position >= imageUrls.size()){
            showToast("未知错误");
            return;
        }

        new ImageViewer.Builder(this, imageUrls)
                .setStartPosition(position)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByDoubleClick();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exitByDoubleClick() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            showToast(StringUtil.getString(this, R.string.exit_msg));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.exit(0);
                }
            },500);
        }
    }

}
