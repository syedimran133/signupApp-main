package com.diamond.future;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.diamond.future.model.AboutUsModel;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsAndConditionActivity extends AppCompatActivity {
    TextView tvDescripion, title;
    LinearLayout layoutHistory,layout_Video,layout_Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        initView();

    }

    private void initView() {
        layout_Setting = findViewById(R.id.layout_Setting);
        layout_Video = findViewById(R.id.layout_Video);
        layoutHistory = findViewById(R.id.layoutHistory);
        tvDescripion = findViewById(R.id.tvDescripion);

        title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("key"));
        if (getIntent().getStringExtra("key").equals("About Us")) {
            getAboutUs();
        } else if (getIntent().getStringExtra("key").equals("Terms And Conditions")) {
            getTermsConditions();
        } else if (getIntent().getStringExtra("key").equals("Privacy Policy")) {
            getPrivacy();
        }
        clickEvent();
    }

    private void clickEvent() {
        layoutHistory.setOnClickListener(V->{
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtra("name","history");
            startActivity(intent);
            finish();
        });
        layout_Video.setOnClickListener(V->{
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtra("name","layout_Video");
            startActivity(intent);
            finish();
        });
        layout_Setting.setOnClickListener(V->{
            Intent intent=new Intent(this,MainActivity.class);
            intent.putExtra("name","layout_Setting");
            startActivity(intent);
            finish();
        });
    }

    private void getAboutUs() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getAboutus().enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        if(response.body().getTitle()!=null)
                        title.setText(response.body().getTitle());
                        if(response.body().getDescription()!=null)
                        tvDescripion.setText(Html.fromHtml(response.body().getDescription()));
                    } else
                        Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void getPrivacy() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getPrivacyPolicy().enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        if(response.body().getTitle()!=null)
                            title.setText(response.body().getTitle());
                        if(response.body().getDescription()!=null)
                            tvDescripion.setText(Html.fromHtml(response.body().getDescription()));
                    } else
                        Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void getTermsConditions() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getTermsAndCondition().enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        if(response.body().getTitle()!=null)
                            title.setText(response.body().getTitle());
                        if(response.body().getDescription()!=null)
                            tvDescripion.setText(Html.fromHtml(response.body().getDescription()));
                    } else
                        Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                Toast.makeText(TermsAndConditionActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    public void Back(View view) {
        onBackPressed();
    }
}