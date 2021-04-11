package com.chili_driver.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.fragments.FragmentOrders;
import com.chili_driver.activities_fragments.activity_previous_order.PreviousOrderActivity;
import com.chili_driver.databinding.OrderRowBinding;
import com.chili_driver.models.OrderModel;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private AppCompatActivity activity;
    public OrderAdapter(List<OrderModel> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        activity = (AppCompatActivity) context;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
        return new MyHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        OrderModel orderModel = list.get(position);
        myHolder.binding.setModel(orderModel);
        myHolder.binding.tvClientDistance.setText(String.format(Locale.ENGLISH,"%.2f %s",orderModel.getClient_distance(),context.getString(R.string.km)));
        myHolder.binding.tvMarketDistance.setText(String.format(Locale.ENGLISH,"%.2f %s",orderModel.getMarket_distance(),context.getString(R.string.km)));

        myHolder.itemView.setOnClickListener(v -> {
            OrderModel  model = list.get(holder.getAdapterPosition());

            if (fragment!=null){
                if (fragment instanceof FragmentOrders){
                    FragmentOrders fragmentOrders = (FragmentOrders) fragment;
                    fragmentOrders.setItemData(model);
                }
            }else {
                if (activity instanceof PreviousOrderActivity){
                    PreviousOrderActivity previousOrderActivity = (PreviousOrderActivity) activity;
                    previousOrderActivity.seItemData(model);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        private OrderRowBinding binding;

        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }


}
