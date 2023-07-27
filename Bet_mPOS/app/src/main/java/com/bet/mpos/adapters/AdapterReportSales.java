package com.bet.mpos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bet.mpos.BetApp;
import com.bet.mpos.R;

import com.bet.mpos.objects.ReportSalesData;
import com.bet.mpos.util.Functions;


import java.util.ArrayList;


public class AdapterReportSales extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private OnItemClickListener listener;

    private ArrayList<ReportSalesData> list;
    public AdapterReportSales(ArrayList<ReportSalesData> reportSalesData) {
        list = reportSalesData;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, ReportSalesData item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_report_sales, parent, false);

            return new ViewHolderReportSales(view);
        }else if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_my_sales, parent, false);
            return new ViewHolderReportSalesHeader(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        ReportSalesData sale = list.get(position);
//        System.out.println(sale.toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null &&  sale.getTransaction_type() != -1) {
                    listener.onItemClick(holder.getAdapterPosition(), sale);
                }
            }
        });
        if(holder instanceof ViewHolderReportSales && position != 0) {

            ViewHolderReportSales salesViewHolder = (ViewHolderReportSales) holder;
            if (sale.getTransaction_type() == -1) {
                salesViewHolder.layoutDM.setVisibility(View.VISIBLE);
                salesViewHolder.layoutSale.setVisibility(View.INVISIBLE);
                salesViewHolder.tvDayMonth.setText(sale.getEntry_date());
            } else {
                salesViewHolder.layoutDM.setVisibility(View.INVISIBLE);
                salesViewHolder.layoutSale.setVisibility(View.VISIBLE);

                salesViewHolder.tvSaleValue.setText(Functions.int_to_real(sale.getValue_net()));
                salesViewHolder.tvSaleHour.setText(sale.getHour());

                switch (sale.getStatus()){
                    case 1:
                        salesViewHolder.tvSaleStatus.setBackground(BetApp.getAppContext().getDrawable(R.drawable.border_green));
                        salesViewHolder.tvSaleStatus.setText("Aprovado");
                        break;
                    case 2:
                        salesViewHolder.tvSaleStatus.setBackground(BetApp.getAppContext().getDrawable(R.drawable.border_inactive));
                        salesViewHolder.tvSaleStatus.setText("Estorno");
                        break;
                    case 3:
                        salesViewHolder.tvSaleStatus.setBackground(BetApp.getAppContext().getDrawable(R.drawable.border_orange));
                        salesViewHolder.tvSaleStatus.setText("Falha");
                        break;
                }

                switch (sale.getTransaction_type()) {
                    case 2:
                        salesViewHolder.tvTypeSale.setText("Crédito");
                        salesViewHolder.ivReportSaleType.setImageDrawable(BetApp.getAppContext().getDrawable(R.drawable.credito));
                        break;
                    case 1:
                        salesViewHolder.tvTypeSale.setText("Debito");
                        salesViewHolder.ivReportSaleType.setImageDrawable(BetApp.getAppContext().getDrawable(R.drawable.debito));
                        break;
                    case 3:
                        salesViewHolder.tvTypeSale.setText("Pix");
                        salesViewHolder.ivReportSaleType.setImageDrawable(BetApp.getAppContext().getDrawable(R.drawable.ic_icone_pix));
                }
            }
        }else {
            if(holder instanceof ViewHolderReportSalesHeader) {
                ViewHolderReportSalesHeader headerViewHolder = (ViewHolderReportSalesHeader) holder;

                headerViewHolder.tvPeriod.setText(sale.getPeriod());
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }
}

class ViewHolderReportSales extends RecyclerView.ViewHolder{

    ConstraintLayout layoutDM, layoutSale;
    TextView tvTypeSale, tvSaleHour, tvSaleValue, tvSaleStatus, tvDayMonth;
    ImageView ivReportSaleType;

    public ViewHolderReportSales(@NonNull View itemView){
        super(itemView);

        layoutDM = itemView.findViewById(R.id.layout_day_month);
        layoutSale = itemView.findViewById(R.id.layout_sale);

        tvTypeSale = itemView.findViewById(R.id.tv_type_sale);
        tvSaleHour = itemView.findViewById(R.id.tv_sale_hour);
        tvSaleValue = itemView.findViewById(R.id.tv_sale_value);
        tvSaleStatus = itemView.findViewById(R.id.tv_sale_status);
        tvDayMonth = itemView.findViewById(R.id.tv_day_month);

        ivReportSaleType = itemView.findViewById(R.id.iv_report_sale_type);
    }

}

class ViewHolderReportSalesHeader extends RecyclerView.ViewHolder{

    TextView tvPeriod;

    public ViewHolderReportSalesHeader(@NonNull View itemView){
        super(itemView);

        tvPeriod = itemView.findViewById(R.id.tv_header_my_sales_period);
    }

}



