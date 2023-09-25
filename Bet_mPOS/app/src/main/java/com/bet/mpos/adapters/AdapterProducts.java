package com.bet.mpos.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bet.mpos.R;
import com.bet.mpos.objects.BetItem;
import com.bet.mpos.util.DownloadImageFromInternet;
import com.bet.mpos.util.FunctionsK;

import java.util.ArrayList;

public class AdapterProducts extends RecyclerView.Adapter<viewHolderProducts>{

//    private ProductDescriptionDialog dialog;
    private Context context;
    private Bitmap image_pos_0, image_pos_1, image_pos_2;
    private final ArrayList<BetItem> list;
    private final ArrayList<BetItem> item_full;
    private Filter descriptionFilter ;

    private OnDrawClickListener drawListener;
    private OnBet1ClickListener bet1Listener;
    private OnBet2ClickListener bet2Listener;

    private boolean isClickEnabled = true;
    private long delay = 100;


    public interface OnDrawClickListener {
        void onClickDraw(BetItem ticket);
    }
    public interface OnBet1ClickListener {
        void onClicBet1(BetItem ticket);
    }
    public interface OnBet2ClickListener {
        void onClicBet2(BetItem ticket);
    }

    public void setOnDrawClickListener(OnDrawClickListener listener) {
        drawListener = listener;
    }
    public void setOnBet1ClickListener(OnBet1ClickListener listener) {
        bet1Listener = listener;
    }
    public void setOnBet2ClickListener(OnBet2ClickListener listener) {
        bet2Listener = listener;
    }

    public AdapterProducts(ArrayList<BetItem> list, Context context) {
        this.list = list;
        this.item_full = new ArrayList<BetItem> (list);
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolderProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_bet_items, parent, false);

            return new viewHolderProducts(view);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderProducts holder, int position) {

        buildLayout(holder , holder.getAdapterPosition());

        holder.btn_bet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("0");
                bet1Listener.onClicBet1(list.get(holder.getAdapterPosition()));
            }
        });
        holder.btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("1");
                drawListener.onClickDraw(list.get(holder.getAdapterPosition()));
            }
        });
        holder.btn_bet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("2");
                bet2Listener.onClicBet2(list.get(holder.getAdapterPosition()));
            }
        });
    }

    private void buildLayout(viewHolderProducts holder, int position) {
        BetItem item = list.get(position);

        holder.tv_day.setText(FunctionsK.Companion.convertDate4(item.getDate()));
        holder.tv_month.setText(FunctionsK.Companion.convertDate5(item.getDate())+".");

        holder.tv_name.setText(item.getTitle());
        holder.tv_city.setText(item.getLocation());
        holder.tv_date.setText(FunctionsK.Companion.convertDate3(item.getDate()));

        holder.tv_name_bet1.setText(item.getBet0().getName());
        holder.tv_name_bet2.setText(item.getBet2().getName());

        holder.tv_odd_bet1.setText(item.getBet0().getOdd());
        holder.tv_odd_draw.setText(item.getBet1().getOdd());
        holder.tv_odd_bet2.setText(item.getBet2().getOdd());

        //new DownloadImageFromInternet(holder.iv_bet1).downloadSVGImage(item.getBet0().getLogo());
        //new DownloadImageFromInternet(holder.iv_bet2).downloadSVGImage(item.getBet2().getLogo());

        new DownloadImageFromInternet(holder.iv_bet1).downloadImage(item.getBet0().getLogo());
        new DownloadImageFromInternet(holder.iv_bet2).downloadImage(item.getBet2().getLogo());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

class viewHolderProducts extends RecyclerView.ViewHolder {

    ConstraintLayout btn_bet1, btn_draw, btn_bet2;
    TextView tv_day, tv_month, tv_name, tv_city, tv_date;
    TextView tv_name_bet1, tv_name_bet2;
    ImageView iv_bet1, iv_bet2;
    TextView tv_odd_bet1, tv_odd_draw, tv_odd_bet2;

    public viewHolderProducts(@NonNull View itemView) {
        super(itemView);
        btn_bet1 = itemView.findViewById(R.id.btn_pos_0);
        btn_draw = itemView.findViewById(R.id.btn_pos_1);
        btn_bet2 = itemView.findViewById(R.id.btn_pos_2);

        tv_day = itemView.findViewById(R.id.tv_day);
        tv_month = itemView.findViewById(R.id.tv_month);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_city = itemView.findViewById(R.id.tv_city);
        tv_date = itemView.findViewById(R.id.tv_date);

        tv_name_bet1 = itemView.findViewById(R.id.tv_name_bet1);
        tv_name_bet2 = itemView.findViewById(R.id.tv_name_bet2);

        iv_bet1 = itemView.findViewById(R.id.iv_bet1);
        iv_bet2 = itemView.findViewById(R.id.iv_bet2);

        tv_odd_bet1 = itemView.findViewById(R.id.tv_odd_bet1);
        tv_odd_draw = itemView.findViewById(R.id.tv_odd_bet_draw);
        tv_odd_bet2 = itemView.findViewById(R.id.tv_odd_bet2);

    }
}