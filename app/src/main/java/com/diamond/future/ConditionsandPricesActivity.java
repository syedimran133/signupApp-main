package com.diamond.future;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.diamond.future.model.AboutUsModel;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConditionsandPricesActivity extends AppCompatActivity {
    TextView tvDescripion, title;
    LinearLayout layoutHistory,layout_Video,layout_Setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditionsand_prices);
        initView();
    }
    private void initView() {
        layout_Setting = findViewById(R.id.layout_Setting);
        layout_Video = findViewById(R.id.layout_Video);
        tvDescripion = findViewById(R.id.tvDescripion);
        layoutHistory = findViewById(R.id.layoutHistory);
        title = findViewById(R.id.title);
       // tvDescripion.setText(getString(R.string.conditions));
        clickEvent();
        getpriceInformation();
    }

    private void getpriceInformation() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getPriceSetting().enqueue(new Callback<AboutUsModel>() {
            @Override
            public void onResponse(Call<AboutUsModel> call, Response<AboutUsModel> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        if(response.body().getTitle()!=null)
                            title.setText(response.body().getTitle());
                        if(response.body().getDescription()!=null)
                            tvDescripion.setText(Html.fromHtml(response.body().getDescription()));
                    } else
                        Toast.makeText(ConditionsandPricesActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<AboutUsModel> call, Throwable t) {
                Toast.makeText(ConditionsandPricesActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
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

    public void Back(View view) {
        onBackPressed();
    }
}