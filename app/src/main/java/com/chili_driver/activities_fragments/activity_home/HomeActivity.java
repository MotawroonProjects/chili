package com.chili_driver.activities_fragments.activity_home;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.iid.FirebaseInstanceId;
import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.fragments.FragmentOrders;
import com.chili_driver.activities_fragments.activity_home.fragments.FragmentProfile;
import com.chili_driver.activities_fragments.activity_login.LoginActivity;
import com.chili_driver.adapters.ViewPagerAdapter;
import com.chili_driver.databinding.ActivityHomeBinding;
import com.chili_driver.language.Language;

import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;
import com.chili_driver.remote.Api;
import com.chili_driver.share.Common;
import com.chili_driver.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private ViewPagerAdapter adapter;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        if(userModel!=null){
            updateFirebaseToken();
        }
        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();

        //  binding.tab.setupWithViewPager(binding.pager,false);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);

        binding.pager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.pager);


        binding.setModel(userModel);


        if (userModel != null) {
//            EventBus.getDefault().register(this);

        }


    }





    private void getNotificationCount() {

    }

    @Override
    public void onBackPressed() {

        finish();

    }


    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void addFragments_Titles() {
        fragmentList.add(FragmentOrders.newInstance());
        fragmentList.add(FragmentProfile.newInstance());


        titles.add(getString(R.string.orders));
        titles.add(getString(R.string.profile));


    }

    public void logout() {
        if (userModel != null) {
            ProgressDialog dialog = Common.createProgressDialog(HomeActivity.this, getString(R.string.wait));
            dialog.show();
            Api.getService(Tags.base_url).logout("Bearer " + userModel.getData().getToken(), userModel.getData().getId() + "", userModel.getData().getFirebaseToken()).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            if (manager != null) {
                                manager.cancel(Tags.not_tag, Tags.not_id);
                            }
                            preferences.clear(HomeActivity.this);
                            navigateToSignInActivity();
                        } else {
                            navigateToSignInActivity();
                        }
                    } else {
                        dialog.dismiss();
                        try {
                            Log.e("error", response.code() + "__" + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (response.code() == 500) {
                            Toast.makeText(HomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {

                    try {
                        dialog.dismiss();
                        if (t.getMessage() != null) {
                            Log.e("error", t.getMessage() + "__");


                            if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage() + "__");
                    }

                }
            });

        } else {
            navigateToSignInActivity();
        }
    }

    private void updateFirebaseToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        try {
                            Api.getService(Tags.base_url)
                                    .updatePhoneToken("Bearer " + userModel.getData().getToken(), token, userModel.getData().getId(), "android")
                                    .enqueue(new Callback<UserModel>() {
                                        @Override
                                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                if (response.body().getStatus() == 200) {
                                                    userModel.getData().setFirebaseToken(token);
                                                    preferences.create_update_userdata(HomeActivity.this, userModel);

                                                    Log.e("token", "updated successfully");
                                                }

                                            } else {
                                                try {

                                                    Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserModel> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());
                                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                        Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });

    }
    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
}
