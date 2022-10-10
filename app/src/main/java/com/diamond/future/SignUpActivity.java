package com.diamond.future;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.diamond.future.model.LoginModel;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText etName, etEmail, etPh, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPh = findViewById(R.id.etPh);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

    }

    public void back(View view) {
        onBackPressed();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void SEND(View view) {
        if (!isValidEmail(etEmail.getText().toString())) {
            Toast.makeText(SignUpActivity.this, "Enter valid email ", Toast.LENGTH_SHORT).show();

        }/* else if (etPh.getText().toString().length() != 10) {
            Toast.makeText(SignUpActivity.this, "Enter valid mobile number ", Toast.LENGTH_SHORT).show();

        }*/ else if (etPh.getText().toString().isEmpty()) {
            Toast.makeText(SignUpActivity.this, "Enter mobile number ", Toast.LENGTH_SHORT).show();

        }else {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    String refreshedToken = task.getResult();
                    Log.e("tokenforfcm", refreshedToken);
                    doSignup(refreshedToken);
                }
            });
        }
    }

    private void doSignup(String refreshedToken) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getSignup(etName.getText().toString(), etEmail.getText().toString(), etPh.getText().toString(),
                etPassword.getText().toString(), etConfirmPassword.getText().toString(), "Samsung", "V1",refreshedToken).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()==1) {
                        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignUpActivity.this, "Sign  up SuccessFully  ", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Something Went Wrong.Please fill all the mandatory fields or Password and confirm Password should be same  ", Toast.LENGTH_LONG).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
}