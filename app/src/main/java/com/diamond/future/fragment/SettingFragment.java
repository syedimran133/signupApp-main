package com.diamond.future.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.diamond.future.ChangePasswordActivity;
import com.diamond.future.ConditionsandPricesActivity;
import com.diamond.future.LoginActivity;
import com.diamond.future.ProfileActivity;
import com.diamond.future.R;
import com.diamond.future.TermsAndConditionActivity;
import com.diamond.future.model.UserProfile;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingFragment extends Fragment {
    View v;
    TextView tvChangePassword,tvConditionsPrices,tvProfileUpdate,tvAboutUs,tvPrivacyPolicy,tvTermsAndConditions,tvShareApp,tvDeleteAccount;
    Button btnLogout;
    PreManager preManager;
    SwitchCompat swNotification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.setting_fragment, container, false);

        initView();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(preManager.isFirsttime()) {
            if (preManager.isPushnotification()) {
                swNotification.setChecked(true);
            } else {
                swNotification.setChecked(false);

            }
        }else {
            preManager.setFirstTime(true);
            if(NotificationManagerCompat.from(getContext()).areNotificationsEnabled()){
                swNotification.setChecked(true);
                preManager.setPushnotification(true);
            }else {
                swNotification.setChecked(false);
                preManager.setPushnotification(false);


            }
        }

    }

    private void initView() {
        preManager=new PreManager(getContext());
        swNotification=v.findViewById(R.id.swNotification);
        tvConditionsPrices=v.findViewById(R.id.tvConditionsPrices);
        tvDeleteAccount=v.findViewById(R.id.tvDeleteAccount);
        tvShareApp=v.findViewById(R.id.tvShareApp);
        btnLogout=v.findViewById(R.id.btnLogout);
        tvChangePassword=v.findViewById(R.id.tvChangePassword);
        tvProfileUpdate=v.findViewById(R.id.tvProfileUpdate);
        tvAboutUs=v.findViewById(R.id.tvAboutUs);
        tvPrivacyPolicy=v.findViewById(R.id.tvPrivacyPolicy);
        tvTermsAndConditions=v.findViewById(R.id.tvTermsAndConditions);
       /* if(NotificationManagerCompat.from(getContext()).areNotificationsEnabled()){
            swNotification.setChecked(true);
        }else {
            swNotification.setChecked(false);

        }*/
        if(preManager.isPushnotification()){
            swNotification.setChecked(true);
        }else {
            swNotification.setChecked(false);
        }
        swNotification.setOnClickListener(V->{
          if( swNotification.isChecked()) {
            /*  Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
              intent.setData(uri);
              startActivity(intent);*/
              preManager.setPushnotification(true);
              Log.e("notification","on");
          }else {
             /* Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
              intent.setData(uri);
              startActivity(intent);*/
              Log.e("notification","off");
              preManager.setPushnotification(false);

          }
        });
        tvShareApp.setOnClickListener(V->{
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            //intent.putExtra(android.content.Intent.EXTRA_TEXT,  "please find out our app from play store link https://play.google.com/store/apps/details?id=com.diamond.future");
            intent.putExtra(android.content.Intent.EXTRA_TEXT,  " https://play.google.com/");
            getContext().startActivity(Intent.createChooser(intent, "Future Video"));
        });
        tvDeleteAccount.setOnClickListener(V->{
            showDeleteDialog();

        });
        tvConditionsPrices.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), ConditionsandPricesActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(V->{
            preManager.setLogin(false);
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        });
        tvChangePassword.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        tvProfileUpdate.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), ProfileActivity.class);
            startActivity(intent);
        });
        tvAboutUs.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), TermsAndConditionActivity.class);
            intent.putExtra("key","About Us");
            startActivity(intent);
        });
        tvTermsAndConditions.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), TermsAndConditionActivity.class);
            intent.putExtra("key","Terms And Conditions");
            startActivity(intent);
        });
        tvPrivacyPolicy.setOnClickListener(V->{
            Intent intent = new Intent(getContext(), TermsAndConditionActivity.class);
            intent.putExtra("key","Privacy Policy");
            startActivity(intent);
        });

    }

    private void showDeleteDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup =v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_dialog, viewGroup, false);
        TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
        TextView tvOk = dialogView.findViewById(R.id.tvOk);
        // builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                getDeleteAccount();
            }
        });
        alertDialog.show();

    }

    private void getDeleteAccount(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(getContext());

        requestAPI.deleteAccount(preManager.getAuthToken()).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        preManager.setLogin(false);
                        // Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                       Intent intent=new Intent(getContext(),LoginActivity.class);
                       startActivity(intent);
                       getActivity().finish();
                    } else
                        Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(getContext(), "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
}
