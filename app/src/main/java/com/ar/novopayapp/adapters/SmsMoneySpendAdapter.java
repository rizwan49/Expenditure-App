package com.ar.novopayapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ar.novopayapp.R;
import com.ar.novopayapp.Utils.Util;
import com.ar.novopayapp.db.Sms;

import java.util.ArrayList;
import java.util.List;

public class SmsMoneySpendAdapter extends RecyclerView.Adapter<SmsMoneySpendAdapter.MyViewHolder> {
    private List<Sms> list;

    public void addAll(List<Sms> spendList) {
        if (spendList == null) return;
        if (list == null)
            list = new ArrayList<>();
        else
            list.clear();
        list.addAll(spendList);
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, money;

        public MyViewHolder(View view) {
            super(view);
            init(view);
            setupStyle();
        }


        private void setupStyle() {
            //date.setTypeface(FontUtils.getThin(), BOLD);
            //temperature.setTypeface(FontUtils.getThin());
//            condition.setTypeface(FontUtils.getThin());
        }

        private void init(View view) {
            date = view.findViewById(R.id.date);
            money = view.findViewById(R.id.money);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public SmsMoneySpendAdapter(List<Sms> list) {
        if (list == null)
            this.list = new ArrayList<>();
        else {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Sms item = list.get(position);
        holder.date.setText(Util.getDatePrefix(item.getTimeStamp()));
        holder.money.setText(String.valueOf(item.getDebited()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}