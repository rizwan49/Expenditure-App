package com.ar.novopayapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ar.novopayapp.Utils.EventBusAction;
import com.ar.novopayapp.adapters.SmsMoneySpendAdapter;
import com.ar.novopayapp.db.Sms;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private Unbinder unbinder;
    private LinearLayoutManager layoutManager;
    private SmsMoneySpendAdapter adapter;
    private SmsViewModel viewModel;

    private List<Sms> spendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        viewModel = ViewModelProviders.of(this).get(SmsViewModel.class);
        setupViews();
        handleView(viewModel.getList());
    }

    private void bindViewModel() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                viewModel.doFetchQuery();
                spendList = viewModel.getList();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(spendList);
                        handleView(spendList);
                    }
                });
            }
        });
        t.start();

//       when need to use LiveData enable and have to change return type;
//        data.observe(this, new Observer<List<Sms>>() {
//            @Override
//            public void onChanged(@Nullable List<Sms> sms) {
//
//            }
//        });
    }


    private void handleView(List<Sms> list) {
        if (list == null || list.size() == 0) {
            hideViews(recyclerView);
            showViews(emptyView);
        } else {
            showViews(recyclerView);
            hideViews(emptyView);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String type) {
        switch (type) {
            case EventBusAction.SYNC_SUCCESS:
                bindViewModel();
                //super.dismissDialog(R.string.please_wait_loading);
                Log.d(TAG, "msg synced");
        }

    }

    private void showViews(final View... views) {
        if (views == null) return;
        for (View v : views)
            v.setVisibility(View.VISIBLE);
    }

    private void hideViews(final View... views) {
        if (views == null) return;
        for (View v : views)
            v.setVisibility(View.GONE);
    }

    private void setupViews() {
        adapter = new SmsMoneySpendAdapter(viewModel.getList());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }
}

