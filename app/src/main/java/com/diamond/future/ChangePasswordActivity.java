package com.diamond.future;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.diamond.future.model.LoginModel;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    TextInputEditText edit_password,edit_Confirmpassword;
    Button btnSave;
    PreManager preManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        preManager=new PreManager(this);
        initView();
    }

    private void initView() {
        edit_password=findViewById(R.id.edit_password);
        edit_Confirmpassword=findViewById(R.id.edit_Confirmpassword);
        btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(V->{
            if(edit_password.getText().toString().equals("")){
                edit_password.setError("Enter Password");
            }else if(edit_Confirmpassword.getText().toString().equals("")){
                edit_Confirmpassword.setError("Enter  Confirm Password");
            }if(edit_password.getText().toString().length()<6){
                Toast.makeText(ChangePasswordActivity.this, "Please use minimum 6 character password", Toast.LENGTH_LONG).show();
            }if(edit_Confirmpassword.getText().toString().length()<6){
                Toast.makeText(ChangePasswordActivity.this, "Please use minimum 6 character password", Toast.LENGTH_LONG).show();
            }else
            changePass();
        });
    }

    private void changePass() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);
        RequestBody old = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getPassword());
        RequestBody newpass = RequestBody.create(MediaType.parse("multipart/form-data"), edit_password.getText().toString());
        RequestBody confirm = RequestBody.create(MediaType.parse("multipart/form-data"), edit_Confirmpassword.getText().toString());
        requestAPI.getchangepass(old,newpass,confirm,preManager.getAuthToken()).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.code() == 200) {
                    if(response.body().isSuccess()==1) {
                        finish();
                        Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    else  Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }


    public void Cancel(View view) {
        onBackPressed();
    }
}