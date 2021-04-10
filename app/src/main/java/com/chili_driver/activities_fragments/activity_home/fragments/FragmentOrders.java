package com.chili_driver.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.HomeActivity;
import com.chili_driver.adapters.OrderAdapter;
import com.chili_driver.databinding.DialogScanBinding;
import com.chili_driver.databinding.FragmentOrderBinding;
import com.chili_driver.models.MyOrderDataModel;
import com.chili_driver.models.OrderCountModel;
import com.chili_driver.models.OrderModel;
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

//binding.progBar.setVisibility(View.GONE);
        binding.swipeRefresh.setOnRefreshListener(this::getordercount);
        getordercount();
        binding.llQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrder();
            }
        });
    }

    public void CreateDialogAlert(Context context, SingleOrderModel singleOrderModel) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .create();

        DialogScanBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_scan, null, false);
        binding.setModel(singleOrderModel.getData());
        // binding.getRoot().setBackground(activity.getResources().getDrawable(R.drawable.small_rounded_fullwhite));
        binding.imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                skipOrder(singleOrderModel.getData());

            }
        });
        binding.btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                getordercount();
                //getOrders();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void getOrders() {
        try {
            if (userModel == null) {
                binding.swipeRefresh.setRefreshing(false);
                binding.tvNoData.setVisibility(View.VISIBLE);
                binding.progBar.setVisibility(View.GONE);
                return;
            }
            orderModelList.clear();
            adapter.notifyDataSetChanged();
            binding.progBar.setVisibility(View.VISIBLE);
            Api.getService(Tags.base_url)
                    .getMyOrder("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), "preparing", "desc")
                    .enqueue(new Callback<MyOrderDataModel>() {
                        @Override
                        public void onResponse(Call<MyOrderDataModel> call, Response<MyOrderDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    if (response.body().getData().size() > 0) {
                                        orderModelList.clear();
                                        orderModelList.addAll(response.body().getData());
                                        adapter.notifyDataSetChanged();
                                        binding.tvNoData.setVisibility(View.GONE);
                                    } else {
                                        orderModelList.clear();
                                        adapter.notifyDataSetChanged();
                                        binding.tvNoData.setVisibility(View.VISIBLE);

                                    }
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    public void getordercount() {
        try {

            Api.getService(Tags.base_url)
                    .getOrdersCount("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                    .enqueue(new Callback<OrderCountModel>() {
                        @Override
                        public void onResponse(Call<OrderCountModel> call, Response<OrderCountModel> response) {
                            //  binding.progBar.setVisibility(View.GONE);
                            //binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    Log.e("lkdkkd", response.body().getData().getCurrent_orders_count() + "");
                                    binding.setModel(response.body().getData());
                                    getOrders();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
//                                binding.progBar.setVisibility(View.GONE);
//                                binding.swipeRefresh.setRefreshing(false);

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
                        public void onFailure(Call<OrderCountModel> call, Throwable t) {
                            try {
                                // binding.swipeRefresh.setRefreshing(false);

                                //binding.progBar.setVisibility(View.GONE);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    private void sendOrder() {


        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sendOrder("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "")
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                CreateDialogAlert(activity, response.body());
                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(activity, getString(R.string.resnofound), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 412) {
                                Toast.makeText(activity, getString(R.string.complete_setting), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

    private void skipOrder(OrderModel orderModel) {


        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .skipOrder("Bearer " + userModel.getData().getToken(), orderModel.getId() + "", orderModel.getBarcode_code(), orderModel.getBarcode_image())
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                //  CreateDialogAlert(activity, response.body());
                                Toast.makeText(activity, getString(R.string.delte_suc), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(activity, getString(R.string.order_id_invaild), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 420) {
                                Toast.makeText(activity, getString(R.string.bar_code_invaild), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 421) {
                                Toast.makeText(activity, getString(R.string.bar_code_image_invaild), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
    public void finishOrder(OrderModel orderModel) {


        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .finishOrder("Bearer " + userModel.getData().getToken(), orderModel.getId() + "")
                .enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(Call<SingleOrderModel> call, Response<SingleOrderModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                getordercount();
                             //   getOrders();
                                //  CreateDialogAlert(activity, response.body());
                            //    Toast.makeText(activity, getString(R.string.delte_suc), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 404) {
                                Toast.makeText(activity, getString(R.string.order_id_invaild), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 420) {
                                Toast.makeText(activity, getString(R.string.bar_code_invaild), Toast.LENGTH_SHORT).show();

                            } else if (response.body().getStatus() == 421) {
                                Toast.makeText(activity, getString(R.string.bar_code_image_invaild), Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }

                            try {
                                Log.e("error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleOrderModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("msg_category_error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }

}
