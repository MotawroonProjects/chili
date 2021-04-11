package com.chili_driver.activities_fragments.activity_previous_order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_order_details.OrderDetailsActivity;
import com.chili_driver.adapters.OrderAdapter;
import com.chili_driver.databinding.ActivityPreviousOrderBinding;
import com.chili_driver.language.Language;
import com.chili_driver.models.MyOrderDataModel;
import com.chili_driver.models.OrderModel;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;
import com.chili_driver.remote.Api;
import com.chili_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousOrderActivity extends AppCompatActivity {
    private ActivityPreviousOrderBinding binding;
    private String lang="ar";
    private Preferences preferences;
    private UserModel userModel;
    private double lat =0.0,lng=0.0;
    private List<OrderModel> orderModelList;
    private OrderAdapter adapter;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_order);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng", 0.0);

    }

    private void initView() {
        orderModelList = new ArrayList<>();

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);

        adapter = new OrderAdapter(orderModelList, this,null);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        binding.flBack.setOnClickListener(v -> {finish();});
        getPreviousOrders();
    }


    private void getPreviousOrders() {
        Api.getService(Tags.base_url)
                .getPreviousOrder(userModel.getId(),lat,lng)
                .enqueue(new Callback<MyOrderDataModel>() {
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
                                Toast.makeText(PreviousOrderActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(PreviousOrderActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(PreviousOrderActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PreviousOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void seItemData(OrderModel model) {
        Intent intent = new Intent(this, OrderDetailsActivity.class);
        intent.putExtra("order_id", model.getId());
        startActivity(intent);

    }
}