package com.ar.novopayapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ar.novopayapp.R;
import com.ar.novopayapp.modals.DebitedAmount;
import com.ar.novopayapp.modals.DebitedModal;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class SmsMoneySpendAdapter extends RecyclerView.Adapter<SmsMoneySpendAdapter.MyViewHolder> {
    private List<DebitedModal> list;
    private static final int CHART_ANIM_DURATION = 2000;

    public void addAll(List<DebitedModal> spendList) {
        initList(spendList);
    }

    private void initList(List<DebitedModal> spendList) {
        if (spendList == null) return;
        if (list == null)
            list = new ArrayList<>();

        list.addAll(spendList);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        BarChart chart;

        private MyViewHolder(View view) {
            super(view);
            init(view);
        }

        private void init(View view) {
            date = view.findViewById(R.id.date);
            chart = view.findViewById(R.id.chartView);
        }

        private void bindView(int position) {
            final DebitedModal item = list.get(position);
            date.setText(item.getHeader());
            chart.setData(generateDataBar(item.getDebitedAmountList()));
            chart.animateY(CHART_ANIM_DURATION);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public SmsMoneySpendAdapter(List<DebitedModal> list) {
        initList(list);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.bindView(position);
    }

    private BarData generateDataBar(List<DebitedAmount> amountList) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (DebitedAmount amountObj : amountList) {
            entries.add(new BarEntry(Integer.valueOf(amountObj.getDay()), (int) amountObj.getTotalAmountInDay()));
        }

        BarDataSet d = new BarDataSet(entries, "");
        d.setColors(ColorTemplate.COLORFUL_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}