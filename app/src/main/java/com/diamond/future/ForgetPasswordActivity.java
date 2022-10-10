package com.diamond.future;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.diamond.future.model.LoginModel;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
EditText  etPhOrEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        etPhOrEmail=findViewById(R.id.etPhOrEmail);
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void SEND(View view) {
        if (!isValidEmail(etPhOrEmail.getText().toString())) {
            Toast.makeText(ForgetPasswordActivity.this, "Enter valid email ", Toast.LENGTH_SHORT).show();

        }else
        doPassword();
    }

    private void doPassword() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getEmailVerify(etPhOrEmail.getText().toString()).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if(response.body().isSuccess()==1)
                        Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    else  Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(ForgetPasswordActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
    public void back(View view) {
        onBackPressed();
    }
}