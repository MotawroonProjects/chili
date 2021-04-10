package com.chili_driver.activities_fragments.activity_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
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


import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.HomeActivity;
import com.chili_driver.activities_fragments.activity_sign_up.SignUpActivity;
import com.chili_driver.databinding.DialogDelivryTimeBinding;
import com.chili_driver.databinding.DialogOrderNumBinding;
import com.chili_driver.databinding.FragmentProfileBinding;
import com.chili_driver.models.RestaurantSettingModel;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;
import com.chili_driver.remote.Api;
import com.chili_driver.share.Common;
import com.chili_driver.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private Preferences preferences;
    final static private String Tag = "info";
    private UserModel userModel;
    private String lang;
    private RestaurantSettingModel settingmodel;

    public static FragmentProfile newInstance() {

        return new FragmentProfile();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {

        preferences = Preferences.getInstance();
        activity = (HomeActivity) getActivity();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel);
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        binding.tvedit.setPaintFlags(binding.tvedit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (lang.equals("ar")) {
            binding.tvlang.setText("EN");

        } else {
            binding.tvlang.setText("عربى");

        }
        binding.flEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SignUpActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.fllogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.logout();
            }
        });
        binding.fllanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lang.equals("ar")) {
                    activity.refreshActivity("en");
                } else {
                    activity.refreshActivity("ar");

                }
            }
        });
        binding.flEdittime.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                createTimeDialogAlert();
            }
        });
        binding.llrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNumDialogAlert();
            }
        });
        getRestaurntSetting();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (preferences != null) {
                userModel = preferences.getUserData(activity);
                binding.setModel(userModel);
            }
        }
    }

    public void getRestaurntSetting() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getRestaurantSetting("Bearer " + userModel.getData().getToken(), userModel.getData().getId())
                    .enqueue(new Callback<RestaurantSettingModel>() {
                        @Override
                        public void onResponse(Call<RestaurantSettingModel> call, Response<RestaurantSettingModel> response) {
                            //  binding.progBar.setVisibility(View.GONE);
                            //binding.swipeRefresh.setRefreshing(false);
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    //   Log.e("lkdkkd", response.body().getData().getCurrent_orders_count() + "");
                                    settingmodel = response.body();
                                    binding.setSetmodel(response.body().getData());
                                    //getOrders();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
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
                        public void onFailure(Call<RestaurantSettingModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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

    private void createTimeDialogAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogDelivryTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_delivry_time, null, false);
        List<String> times = new ArrayList<>();
        times.add(getString(R.string.min1));
        times.add(getString(R.string.min2));
        times.add(getString(R.string.min3));
        times.add(getString(R.string.min4));
        times.add(getString(R.string.min5));
        times.add(getString(R.string.min6));
        times.add(getString(R.string.min7));
        times.add(getString(R.string.min8));
        times.add(getString(R.string.min9));
        times.add(getString(R.string.min10));
        times.add(getString(R.string.min11));
        times.add(getString(R.string.min12));
        times.add(getString(R.string.min13));
        times.add(getString(R.string.min14));
        times.add(getString(R.string.min15));
        times.add(getString(R.string.min16));
        times.add(getString(R.string.min17));
        times.add(getString(R.string.min18));
        times.add(getString(R.string.min19));
        times.add(getString(R.string.min20));
        times.add(getString(R.string.min21));
        times.add(getString(R.string.min22));
        times.add(getString(R.string.min23));
        times.add(getString(R.string.min24));
        times.add(getString(R.string.min25));
        times.add(getString(R.string.min26));
        times.add(getString(R.string.min27));
        times.add(getString(R.string.min28));
        times.add(getString(R.string.min29));
        times.add(getString(R.string.min30));
        times.add(getString(R.string.min31));
        times.add(getString(R.string.min32));
        times.add(getString(R.string.min33));
        times.add(getString(R.string.min34));
        times.add(getString(R.string.min35));
        times.add(getString(R.string.min36));
        times.add(getString(R.string.min37));
        times.add(getString(R.string.min38));
        times.add(getString(R.string.min39));
        times.add(getString(R.string.min40));
        times.add(getString(R.string.min41));
        times.add(getString(R.string.min42));
        times.add(getString(R.string.min43));
        times.add(getString(R.string.min44));
        times.add(getString(R.string.min45));
        times.add(getString(R.string.min46));
        times.add(getString(R.string.min47));
        times.add(getString(R.string.min48));
        times.add(getString(R.string.min49));
        times.add(getString(R.string.min50));
        times.add(getString(R.string.min51));
        times.add(getString(R.string.min52));
        times.add(getString(R.string.min53));
        times.add(getString(R.string.min54));
        times.add(getString(R.string.min55));
        times.add(getString(R.string.min56));
        times.add(getString(R.string.min57));
        times.add(getString(R.string.min58));
        times.add(getString(R.string.min59));
        times.add(getString(R.string.min60));


        String[] values = new String[times.size()];

        binding.picker.setMinValue(0);
        binding.picker.setMaxValue(times.size() - 1);
        binding.picker.setDisplayedValues(times.toArray(values));
        binding.picker.setValue(1);
        binding.imageUp.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() - 1);
        });

        binding.imageDown.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() + 1);
        });


        binding.btnOk.setOnClickListener(v ->
                {
                    changeRestaurntTime(((binding.picker.getValue()+1 ) + "").replaceAll("Minute","").replaceAll("دقيقه",""));
                    //  time = binding.picker.getValue() + 1;
                    dialog.dismiss();

                }
        );

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }

    public void changeRestaurntTime(String time) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .changeRestaurantTime("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), time, settingmodel.getData().getOrder_first_num() + "")
                    .enqueue(new Callback<RestaurantSettingModel>() {
                        @Override
                        public void onResponse(Call<RestaurantSettingModel> call, Response<RestaurantSettingModel> response) {
                            //  binding.progBar.setVisibility(View.GONE);
                            //binding.swipeRefresh.setRefreshing(false);
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    //   Log.e("lkdkkd", response.body().getData().getCurrent_orders_count() + "");
                                    settingmodel = response.body();
                                    binding.setSetmodel(response.body().getData());
                                    //getOrders();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
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
                        public void onFailure(Call<RestaurantSettingModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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
    public void changeordernum(String num) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .changeRestaurantTime("Bearer " + userModel.getData().getToken(), userModel.getData().getId(), settingmodel.getData().getOrder_time_preparing()+"", num)
                    .enqueue(new Callback<RestaurantSettingModel>() {
                        @Override
                        public void onResponse(Call<RestaurantSettingModel> call, Response<RestaurantSettingModel> response) {
                            //  binding.progBar.setVisibility(View.GONE);
                            //binding.swipeRefresh.setRefreshing(false);
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {

                                if (response.body().getStatus() == 200) {
                                    //   Log.e("lkdkkd", response.body().getData().getCurrent_orders_count() + "");
                                    settingmodel = response.body();
                                    binding.setSetmodel(response.body().getData());
                                    //getOrders();
                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
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
                        public void onFailure(Call<RestaurantSettingModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
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

    private void createNumDialogAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogOrderNumBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_order_num, null, false);
        List<String> times = new ArrayList<>();
        times.add(getString(R.string.zero));
        times.add(getString(R.string.one));
        times.add(getString(R.string.two));
        times.add(getString(R.string.three));
        times.add(getString(R.string.four));
        times.add(getString(R.string.five));
        times.add(getString(R.string.six));
        times.add(getString(R.string.seven));
        times.add(getString(R.string.eignt));
        times.add(getString(R.string.nine));
        times.add(getString(R.string.ten));
        times.add(getString(R.string.eleven));
        times.add(getString(R.string.twelefe));
        times.add(getString(R.string.thriteen));
        times.add(getString(R.string.fourteen));
        times.add(getString(R.string.fiftheen));
        times.add(getString(R.string.sixteen));
        times.add(getString(R.string.sevteen));
        times.add(getString(R.string.eighteen));
        times.add(getString(R.string.ninteen));
        times.add(getString(R.string.tewenty));
        times.add(getString(R.string.twone));
        times.add(getString(R.string.twtwo));
        times.add(getString(R.string.twthree));
        times.add(getString(R.string.twfour));
        times.add(getString(R.string.twfive));
        times.add(getString(R.string.twsix));
        times.add(getString(R.string.twseven));
        times.add(getString(R.string.tweignt));
        times.add(getString(R.string.twnine));
        times.add(getString(R.string.thirty));
        times.add(getString(R.string.thirtyone));
        times.add(getString(R.string.thirtytwo));
        times.add(getString(R.string.thirtythree));
        times.add(getString(R.string.thirtyfour));
        times.add(getString(R.string.thirtyfif));
        times.add(getString(R.string.thirtysix));
        times.add(getString(R.string.thirtysev));
        times.add(getString(R.string.thirtyeigh));
        times.add(getString(R.string.thirtynin));
        times.add(getString(R.string.fourty));
        times.add(getString(R.string.fourtyone));
        times.add(getString(R.string.fourtytwo));
        times.add(getString(R.string.fourtythree));
        times.add(getString(R.string.fourtyfour));
        times.add(getString(R.string.fourtyfive));
        times.add(getString(R.string.fourtysix));
        times.add(getString(R.string.fourtyseven));
        times.add(getString(R.string.fourtyeignt));
        times.add(getString(R.string.fourtynine));
        times.add(getString(R.string.fifthy));
        times.add(getString(R.string.fifthyone));
        times.add(getString(R.string.fifthytwo));
        times.add(getString(R.string.fifthythree));
        times.add(getString(R.string.fifthyfour));
        times.add(getString(R.string.fifthyfif));
        times.add(getString(R.string.fifthysix));
        times.add(getString(R.string.fifthysev));
        times.add(getString(R.string.fifthyeigh));
        times.add(getString(R.string.fifthynin));
        times.add(getString(R.string.sixthy));

        String[] values = new String[times.size()];

        binding.picker.setMinValue(0);
        binding.picker.setMaxValue(times.size() - 1);
        binding.picker.setDisplayedValues(times.toArray(values));
        binding.picker.setValue(1);
        binding.imageUp.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() - 1);
        });

        binding.imageDown.setOnClickListener(v -> {
            binding.picker.setValue(binding.picker.getValue() + 1);
        });


        binding.btnOk.setOnClickListener(v ->
                {
                    changeordernum((binding.picker.getValue()) + "");
                    //  time = binding.picker.getValue() + 1;
                    dialog.dismiss();

                }
        );

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss()

        );
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }


}
