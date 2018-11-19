package com.ar.novopayapp;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.ar.novopayapp.utils.EventBusAction;
import com.ar.novopayapp.adapters.SmsMoneySpendAdapter;
import com.ar.novopayapp.modals.DebitedModal;
import com.ar.novopayapp.utils.ViewDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/***
 * steps :
 * 1. checking and asking sms read permission
 *    i) permission denied :
 *        a) again asking permission : if no then showing alert dialog with message and pressing "ok" button closing app;
 *    ii) permission granted:
 *        a) sending event through EventBus permission granted
 *
 * 2. init MainActivityViewModel
 *    i) MainActivityViewModel invoke IntentService to read all the sms;
 *    ii) as service send acknowledgement of success
 *    iii) fetch grouped recordList using MainActivityViewModel from ROOM Persistence
 *    iv) if fetchedList is : not null
 *         a) load it into recyclerView
 *         b) if null then showing empty screen.
 */
public class MainActivity extends BaseActivity {

    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    @BindView(R.id.progressBarView)
    View progressBarView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private LinearLayoutManager layoutManager;
    private SmsMoneySpendAdapter adapter;
    private MainActivityViewModel viewModel;

    private List<DebitedModal> spendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        super.checkAndAskReadSMSPermission();
    }

    /***
     * as sms read permission granted this method get invoke with the help of EventBus
     * 1.
     */
    private void bindViewModel() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                viewModel.doFetchQuery();
                spendList = viewModel.getAllDebitedList();
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


    /***
     *
     * @param list help to show and hide the view
     */
    private void handleView(List<DebitedModal> list) {
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
        Log.d(TAG, "EventBus:" + type);
        switch (type) {
            case EventBusAction.SYNC_SUCCESS:
                hideViews(progressBarView);
                bindViewModel();
                Log.d(TAG, "msg synced");
                break;
            case EventBusAction.PERMISSION_DENIED:
                permissionDenied();
                break;
            case EventBusAction.PERMISSION_GRANTED:
                hideViews(emptyView, recyclerView);
                showViews(progressBarView);
                permissionGranted();
                break;
            case EventBusAction.FORCE_PERMISSION:
                onBackPressed();
                break;
            default:
                if (Character.isDigit(type.charAt(0))) {
                    progressBar.setProgress(Integer.parseInt(type));
                }
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

    private void setupRecyclerViews() {
        adapter = new SmsMoneySpendAdapter(viewModel.getAllDebitedList());
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


    public void permissionDenied() {
        super.checkAndAskReadSMSPermission();
    }

    /***
     * setting MainActivityViewModel
     *
     */
    public void permissionGranted() {
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setupRecyclerViews();
        if (viewModel.getAllDebitedList() != null)
            handleView(viewModel.getAllDebitedList());
    }

}

