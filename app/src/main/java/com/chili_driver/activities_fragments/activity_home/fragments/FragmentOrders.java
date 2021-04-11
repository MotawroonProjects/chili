package com.chili_driver.activities_fragments.activity_home.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.HomeActivity;
import com.chili_driver.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.chili_driver.adapters.OrderAdapter;
import com.chili_driver.databinding.FragmentOrderBinding;
import com.chili_driver.models.MyOrderDataModel;
import com.chili_driver.models.OrderCountModel;
import com.chili_driver.models.OrderModel;
import com.chili_driver.models.RestaurantSettingModel;
import com.chili_driver.models.SingleOrderModel;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;
import com.chili_driver.remote.Api;
import com.chili_driver.share.Common;
import com.chili_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentOrders extends Fragment {
    private HomeActivity activity;
    private FragmentOrderBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private List<OrderModel> orderModelList;
    private OrderAdapter adapter;
    private String type = "new";
    private Call<MyOrderDataModel> call;

    public static FragmentOrders newInstance() {

        return new FragmentOrders();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        orderModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        activity = (HomeActivity) getActivity();
        userModel = preferences.getUserData(activity);
        adapter = new OrderAdapter(orderModelList, activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        getOrders();

        binding.tvNew.setOnClickListener(v -> {
            binding.tvNew.setBackgroundResource(R.drawable.rounded_primary);
            binding.tvNew.setTextColor(ContextCompat.getColor(activity, R.color.white));

            binding.tvCurrent.setBackgroundResource(R.drawable.rounded_white);
            binding.tvCurrent.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            if (!type.equals("new")){
                type = "new";
                getOrders();
            }

        });

        binding.tvCurrent.setOnClickListener(v -> {
            binding.tvCurrent.setBackgroundResource(R.drawable.rounded_primary);
            binding.tvCurrent.setTextColor(ContextCompat.getColor(activity, R.color.white));

            binding.tvNew.setBackgroundResource(R.drawable.rounded_white);
            binding.tvNew.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

            if (!type.equals("current")){
                type = "current";
                getOrders();
            }



        });
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        binding.swipeRefresh.setOnRefreshListener(this::getOrders);
    }

    private void getOrders() {
        orderModelList.clear();
        adapter.notifyDataSetChanged();
        binding.tvNoData.setVisibility(View.GONE);
        binding.progBar.setVisibility(View.VISIBLE);
        if (type.equals("new")) {
            getNewOrders();
        } else {
            getCurrentOrders();
        }
    }

    private void getNewOrders() {

        if (call!=null){
            call.cancel();
        }
        Call<MyOrderDataModel> call = Api.getService(Tags.base_url)
                .getNewOrder(userModel.getId(), activity.user_lat, activity.user_lng);

        call.enqueue(new Callback<MyOrderDataModel>() {
            @Override
            public void onResponse(Call<MyOrderDataModel> call, Response<MyOrderDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    orderModelList.clear();
                    orderModelList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    if (orderModelList.size() > 0) {
                        binding.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.swipeRefresh.setRefreshing(false);

                    if (response.code() == 500) {
                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        try {

                            Log.e("error", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderDataModel> call, Throwable t) {
                try {
                    binding.swipeRefresh.setRefreshing(false);

                    binding.progBar.setVisibility(View.GONE);

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                        }else if (t.getMessage().toLowerCase().contains("socket")){

                        }else {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
    }

    private void getCurrentOrders() {
        if (call!=null){
            call.cancel();
        }

        call = Api.getService(Tags.base_url)
                .getCurrentOrder(userModel.getId(), activity.user_lat, activity.user_lng);

        call.enqueue(new Callback<MyOrderDataModel>() {
            @Override
            public void onResponse(Call<MyOrderDataModel> call, Response<MyOrderDataModel> response) {
                binding.progBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    orderModelList.clear();
                    orderModelList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    if (orderModelList.size() > 0) {
                        binding.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.swipeRefresh.setRefreshing(false);

                    if (response.code() == 500) {
                        Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        try {

                            Log.e("error", response.code() + "_" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyOrderDataModel> call, Throwable t) {
                try {
                    binding.swipeRefresh.setRefreshing(false);

                    binding.progBar.setVisibility(View.GONE);

                    if (t.getMessage() != null) {
                        Log.e("error", t.getMessage());
                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                        }else if (t.getMessage().toLowerCase().contains("socket")){

                        } else {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
    }

    public void setItemData(OrderModel model) {
        Intent intent = new Intent(activity, OrderDetailsActivity.class);
        intent.putExtra("order_id", model.getId());
        startActivityForResult(intent,100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100 && resultCode== Activity.RESULT_OK){
            getOrders();
        }
    }
}
