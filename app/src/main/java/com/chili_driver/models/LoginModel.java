package com.chili_driver.models;

import android.content.Context;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import com.chili_driver.BR;
import com.chili_driver.R;


public class LoginModel extends BaseObservable {

    private String email;
    private String password;

    public ObservableField<String> error_email = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public boolean isDataValid(Context context) {


        if (!email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                password.trim().length() >= 6

        ) {
            error_email.set(null);
            error_password.set(null);

            return true;
        } else {

            if (email.trim().isEmpty()) {
                error_email.set(context.getString(R.string.field_required));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
                error_email.set(context.getString(R.string.inv_email));

            } else {
                error_email.set(null);

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

    public LoginModel() {
        setEmail("");
        setPassword("");
    }


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }
}
