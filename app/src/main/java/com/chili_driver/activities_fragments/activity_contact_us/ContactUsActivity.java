package com.chili_driver.activities_fragments.activity_contact_us;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.chili_driver.R;
import com.chili_driver.databinding.ActivityContactUsBinding;
import com.chili_driver.language.Language;
import com.chili_driver.models.ContactUsModel;
import com.chili_driver.models.UserModel;
import com.chili_driver.preferences.Preferences;

import io.paperdb.Paper;

public class ContactUsActivity extends AppCompatActivity {
    private ActivityContactUsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private ContactUsModel contactUsModel;
    private String lang = "ar";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, "ar"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        binding.setModel(userModel);
        contactUsModel = new ContactUsModel();
        if (userModel != null) {
         /* contactUsModel.setName(userModel.getData().getName());
            contactUsModel.setEmail(userModel.getData().getEmail());
            contactUsModel.setPhone(userModel.getData().getPhone());*/
        }

        binding.setContactModel(contactUsModel);
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);
        binding.btnSend.setOnClickListener(view -> {
            if (contactUsModel.isDataValid(this)) {
                contactUs();
            }
        });
        binding.llBack.setOnClickListener(view -> finish());
    }

    private void contactUs() {
       /* ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .contactUs(contactUsModel.getName(), contactUsModel.getEmail(), contactUsModel.getPhone(), contactUsModel.getMessage())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            Toast.makeText(ContactUsActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                                Toast.makeText(ContactUsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ContactUsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ContactUsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ContactUsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });*/
    }
}