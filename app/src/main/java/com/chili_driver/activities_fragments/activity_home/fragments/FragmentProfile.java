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
import com.chili_driver.databinding.FragmentProfileBinding;
import com.chili_driver.models.RestaurantSettingModel;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;


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



}
