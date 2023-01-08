package com.diamond.future;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.diamond.future.fragment.DateFragment;
import com.diamond.future.fragment.HistoryFragment;
import com.diamond.future.fragment.ReceiverEmailFragment;
import com.diamond.future.fragment.SendingVideoFragment;
import com.diamond.future.fragment.SettingFragment;
import com.diamond.future.fragment.TimeFragment;
import com.diamond.future.fragment.VideoFragment;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
    LinearLayout layoutHistory, layout_Video, layout_Setting;
    ImageView imgHistory, img_Video, img_Setting;
    TextView txt_History, txt_Video, txt_Setting;
    public static Uri uri;
    SendingPaymentActivity sendingPaymentActivity;
    public interface OnItemClickListener {

        void onItemClick(String name);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        uri= Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.final_render_earth);
        initView();
    }

    private void initView() {
        sendingPaymentActivity=new SendingPaymentActivity(new OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                setFrag(name);
            }
        });
        imgHistory = findViewById(R.id.imgHistory);
        img_Video = findViewById(R.id.img_Video);
        img_Setting = findViewById(R.id.img_Setting);

        txt_History = findViewById(R.id.txt_History);
        txt_Video = findViewById(R.id.txt_Video);
        txt_Setting = findViewById(R.id.txt_Setting);

        layoutHistory = findViewById(R.id.layoutHistory);
        layout_Video = findViewById(R.id.layout_Video);
        layout_Setting = findViewById(R.id.layout_Setting);


        imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_History.setTextColor(this.getResources().getColor(R.color.steal));

        img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_Video.setTextColor(this.getResources().getColor(R.color.white));

        img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
        txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
        if(getIntent().getStringExtra("name")!=null){
            if(getIntent().getStringExtra("name").equals("history")) {
                pushFragment(new HistoryFragment(), "history");
                imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_History.setTextColor(this.getResources().getColor(R.color.white));

                img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Video.setTextColor(this.getResources().getColor(R.color.steal));

                img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
            }
            else  if(getIntent().getStringExtra("name").equals("layout_Video")) {
                pushFragment(new VideoFragment(), "video");
                imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_History.setTextColor(this.getResources().getColor(R.color.steal));

                img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Video.setTextColor(this.getResources().getColor(R.color.white));

                img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
            }
            else  if(getIntent().getStringExtra("name").equals("layout_Setting")){
                pushFragment(new SettingFragment(), "setting");
                imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_History.setTextColor(this.getResources().getColor(R.color.steal));

                img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Video.setTextColor(this.getResources().getColor(R.color.steal));

                img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Setting.setTextColor(this.getResources().getColor(R.color.white));
            }
            else  if(getIntent().getStringExtra("name").equals("receiveremail")){
                pushFragment(new ReceiverEmailFragment(), "receiveremail");
                imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_History.setTextColor(this.getResources().getColor(R.color.steal));

                img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Video.setTextColor(this.getResources().getColor(R.color.white));

                img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
                txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
            }


        }else {
            pushFragment(new VideoFragment(), "video");
        }
        clickevent();
    }

    private void clickevent() {
        layoutHistory.setOnClickListener(V -> {
            pushFragment(new HistoryFragment(), "history");
            imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_History.setTextColor(this.getResources().getColor(R.color.white));

            img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Video.setTextColor(this.getResources().getColor(R.color.steal));

            img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
        });
        layout_Video.setOnClickListener(V -> {
            pushFragment(new VideoFragment(), "video");
            imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_History.setTextColor(this.getResources().getColor(R.color.steal));

            img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Video.setTextColor(this.getResources().getColor(R.color.white));

            img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Setting.setTextColor(this.getResources().getColor(R.color.steal));
        });
        layout_Setting.setOnClickListener(V -> {
            pushFragment(new SettingFragment(), "setting");
            imgHistory.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_History.setTextColor(this.getResources().getColor(R.color.steal));

            img_Video.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.steal), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Video.setTextColor(this.getResources().getColor(R.color.steal));

            img_Setting.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
            txt_Setting.setTextColor(this.getResources().getColor(R.color.white));
        });
    }

    public boolean pushFragment(Fragment fragment, String tag) {
        //   FrameLayout viewPager_old = (FrameLayout)findViewById(R.id.viewPager_old);
        //    viewPager_old.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.GONE);


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(R.anim.feed_in, R.anim.feed_out)
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    //.addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        VideoFragment homeFragment = (VideoFragment) getSupportFragmentManager().findFragmentByTag("video");
        SendingVideoFragment sendingVideoFragment = (SendingVideoFragment) getSupportFragmentManager().findFragmentByTag("sendingvideo");
       // pushFragment(new VideoFragment(), "video");

        if (homeFragment != null && homeFragment.isVisible()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please click back again to exit");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

        }else if(sendingVideoFragment!=null && sendingVideoFragment.isVisible()){
            if(SendingVideoFragment.player!=null){
                Log.e("sendingvideo","sendingvideo");
                SendingVideoFragment.player.stop();
                SendingVideoFragment.player.release();
            }
            fragmentManager.popBackStack();
        }
        else
        fragmentManager.popBackStack();
    }


    public void setFrag(String type) {
        if(type.equals("datefrag"))
        pushFragment(new DateFragment(), "datefrag");
        else if(type.equals("timeFrag")){
            pushFragment(new TimeFragment(), "timeFrag");
        }
        else if(type.equals("receiveremail")){
            pushFragment(new ReceiverEmailFragment(), "receiveremail");
        }
        else if(type.equals("paysummery")){
           // pushFragment(new PaySummeryFragment(), "paysummery");
            Intent intent=new Intent(this,SendingPaymentActivity.class);
            startActivity(intent);
        }
        else if(type.equals("video")){
            pushFragment(new VideoFragment(), "video");
        }
        else if(type.equals("sendingvideo")){
            pushFragment(new SendingVideoFragment(), "sendingvideo");
        }
        else if(type.equals("history")){
            pushFragment(new HistoryFragment(), "history");
        }
    }
}