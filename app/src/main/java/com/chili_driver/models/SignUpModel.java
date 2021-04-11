package com.chili_driver.models;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.chili_driver.BR;
import com.chili_driver.R;


public class SignUpModel extends BaseObservable {
    private String name;
    private String image_url;
    private String phone;
    private String email;
    private String password;

    public ObservableField<String> error_name = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();

    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public boolean isDataValid(Context context) {
        if (!name.trim().isEmpty() &&
                !phone.trim().isEmpty() &&
                !email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !email.trim().isEmpty()
                && !password.isEmpty() && password.length() >= 6

        ) {
            error_phone.set(null);
            error_name.set(null);
            error_email.set(null);
            error_password.set(null);

            return true;
        } else {
            if (name.isEmpty()) {
                error_name.set(context.getString(R.string.field_required));

            } else {
                error_name.set(null);

            }

            if (phone.isEmpty()) {
                error_phone.set(context.getString(R.string.field_required));

            } else {
                error_phone.set(null);

            }

            if (email.isEmpty()) {
                error_email.set(context.getString(R.string.field_required));

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_name.set(null);

            }
            if (password.trim().isEmpty()) {
                error_password.set(context.getString(R.string.field_required));
            } else if (password.trim().length() < 6) {
                error_password.set(context.getString(R.string.pass_short));

            } else {
                error_password.set(null);

            }
            return false;
        }
    }

    public SignUpModel() {
        setName("");
        setPhone("");
        setEmail("");
        setPassword("");

    }


    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);

    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
