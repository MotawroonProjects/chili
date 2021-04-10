package com.chili_driver.activities_fragments.activity_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.chili_driver.R;
import com.chili_driver.activities_fragments.activity_home.HomeActivity;
import com.chili_driver.activities_fragments.activity_login.LoginActivity;
import com.chili_driver.databinding.ActivitySplashBinding;
import com.chili_driver.language.Language;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;
import com.chili_driver.tags.Tags;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Preferences preferences;
    private UserModel userModel;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();

        new Handler().postDelayed(()->{

            String session = preferences.getSession(SplashActivity.this);
            if (session.equals(Tags.session_login)) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);

                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        },3000);
    }
}