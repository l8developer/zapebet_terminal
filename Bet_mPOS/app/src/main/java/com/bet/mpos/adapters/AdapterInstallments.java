package com.bet.mpos.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bet.mpos.R;
import com.bet.mpos.objects.Installments;

import java.util.List;


public class AdapterInstallments extends BaseAdapter {

    private final List<Installments> list;
    private final Activity activity;


    public AdapterInstallments(List<Installments> installments, Activity activity) {
        this.list = installments;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Installments getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View Convertview, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.layout_installments, parent, false);
        Installments res = list.get(pos);

        TextView installment = (TextView) view.findViewById(R.id.tv_installment);
        TextView installment_value = (TextView) view.findViewById(R.id.tv_installment_value);
        TextView total_fees = (TextView) view.findViewById(R.id.tv_total_fees);

        installment.setText(res.getInstallment()+"x");
        installment_value.setText(res.getInstallmentValue());
        total_fees.setText(res.getTotalFees());

        return view;
    }

}




