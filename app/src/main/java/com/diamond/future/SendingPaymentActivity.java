package com.diamond.future;

import static com.diamond.future.fragment.DateFragment.uploadingModel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.diamond.MyTimeUtils;
import com.diamond.Security;
import com.diamond.future.model.UserProfile;
import com.diamond.future.service.AppLocationService;
import com.diamond.future.utility.Constant;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendingPaymentActivity extends AppCompatActivity implements IAPHelper.IAPHelperListener {

    LinearLayout layoutHistory, layout_Video, layout_Setting;
    private String TAG = MainActivity.class.getSimpleName();
    public static MainActivity.OnItemClickListener mItemClickListener;
    String newDate, newTime, newhr, newmin;
    IAPHelper iapHelper;
    float uploadsize = 0;
    PreManager preManager;
    Context context;
    HashMap<String, SkuDetails> skuDetailsHashMap = new HashMap<>();
    //For non_consumable tag "nc" is used at start
    //final String COIN = "purchasefor2months";
    String COIN = "purchasefor2months";
    String TEST = "purchasefor2months"; //This id can be used for testing purpose
    private List<String> skuList = Arrays.asList(COIN, TEST);

    private SharedPreferences pref;
    private String SETTINGS = "saved_settings";
    private String SETTINGS_COINS = "saved_coins";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    AppLocationService appLocationService;
    double latitude1 = 0.0;
    double longitude1 = 0.0;
    //todo: inapp purchase
    public static final String PREF_FILE = "FuturePref";
    public static final String PURCHASE_KEY = "purchasefor2yrs";
    public static String PRODUCT_ID = "purchasefor2months";
    /*    public static final String PRODUCT_ID1 = "purchasefor2yrs";
        public static final String PRODUCT_ID2 = "purchase2to5yrs";
        public static final String PRODUCT_ID3 = "purchase5to10yrs";
        public static final String PRODUCT_ID4 = "purchase10to50yrs";
        public static final String PRODUCT_ID5 = "purchase50to100yrs";*/
    public static final String PRODUCT_ID1 = "purchasefor2months";
    public static final String PRODUCT_ID2 = "purchaseupto6months";
    public static final String PRODUCT_ID3 = "purchaseupto1years";
    public static final String PRODUCT_ID4 = "purchaseupto3yrs";
    public static final String PRODUCT_ID5 = "purchaseupto10yrs";
    public static final String PRODUCT_ID6 = "purchaseupto20yrs";
    public static final String PRODUCT_ID7 = "purchaseupto100yrs";
    public String amountToPay = "$ 0.99";
    public String amountToPaysend = "0.99";
    private BillingClient billingClient;
    int month = 0;

    View v;
    TextView title, tvInformation;
    ImageView ivBack;
    LinearLayout llNext;
    VideoView videoView;
    String FINAL_VIDEO_PATH;
    public static File streamablefile;
    File f;
    String dest = "";


    private Integer totalCoins;
    public static boolean paysucess = false;

    public SendingPaymentActivity() {

    }

    public SendingPaymentActivity(MainActivity.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_payment);
        ButterKnife.bind(this);
        if (Constant.Free.equalsIgnoreCase("Yes")) {
            PRODUCT_ID = PRODUCT_ID1;
            amountToPay = "Free";
            amountToPaysend = "Free";
        }
       /* COIN = getIntent().getStringExtra("productid");
        TEST = getIntent().getStringExtra("productid");
        Log.e("productsending",TEST);
        skuList = Arrays.asList(COIN, TEST);*/
        preManager = new PreManager(this);
        // iapHelper = new IAPHelper(this, this, skuList);
        context = this;
        //Get previous coins from shared pref data
        pref = getSharedPreferences(SETTINGS, 0);
        totalCoins = pref.getInt(SETTINGS_COINS, 0);
        appLocationService = new AppLocationService(context, getActivitys());
        getloc();
        initView();
        //  tvtotalCoin.setText(String.format(Locale.getDefault(), "%d", totalCoins));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        month = 0;
        long timestamp1 = System.currentTimeMillis();
        DateTimeFormatter selectedDF1 = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern("dd-MM-yyyy H:m")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        LocalDate localdateselected1 = LocalDate.parse(uploadingModel.getDate() + " " + uploadingModel.getTimeToUpload(), selectedDF1);

        Date date1 = new Date();
        date1 = Date.from(localdateselected1.atStartOfDay(ZoneId.systemDefault()).toInstant());
        long localSelectedDate1 = date1.getTime();

        long remainingTime1 = localSelectedDate1 - timestamp1;
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(remainingTime1);
        int mYearnow = c1.get(Calendar.YEAR) - 1970;
        int smonth = c1.get(Calendar.MONTH);
        //  double yrs = remainingTime / (365 * 24 * 60 * 60 * 1000);
        Log.e("totaltime", timestamp1 + "=" + localSelectedDate1 + "=" + mYearnow + "=" + remainingTime1);
        long divingnum = 2592000000l;
        if (mYearnow == 0) {
            month = (int) ((double) remainingTime1 / (double) divingnum);
            if (month < 2) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID1;
                amountToPay = "$ 0.99";
                amountToPaysend = "0.99";
            } else if (month >= 2 && month < 6) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID2;
                amountToPay = "$ 2.99";
                amountToPaysend = "2.99";
            } else if (month >= 6) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID3;
                amountToPay = "$ 4.99";
                amountToPaysend = "4.99";

            }
        } else if (mYearnow > 1 && mYearnow < 3) {
            PRODUCT_ID = PRODUCT_ID4;
            amountToPay = "$ 19.99";
            amountToPaysend = "19.99";
        } else if (mYearnow >= 3 && mYearnow < 10) {
            PRODUCT_ID = PRODUCT_ID5;
            amountToPay = "$ 49.99";
            amountToPaysend = "49.99";
        } else if (mYearnow >= 10 && mYearnow < 20) {
            PRODUCT_ID = PRODUCT_ID6;
            amountToPay = "$ 99.99";
            amountToPaysend = "99.99";
        } else if (mYearnow >= 20) {
            PRODUCT_ID = PRODUCT_ID7;
            amountToPay = "$ 199.99";
            amountToPaysend = "199.99";
        }
        Log.e("productid", PRODUCT_ID);
        Log.e("timezone", TimeZone.getDefault().getID() + "");
        preManager = new PreManager(this);
        videoView = findViewById(R.id.videoView);
        ivBack = findViewById(R.id.ivBack);
        title = findViewById(R.id.title);
        llNext = findViewById(R.id.llNext);
        tvInformation = findViewById(R.id.tvInformation);
        layout_Setting = findViewById(R.id.layout_Setting);
        layout_Video = findViewById(R.id.layout_Video);
        layoutHistory = findViewById(R.id.layoutHistory);
        final MediaController mediacontroller = new MediaController(this);
        mediacontroller.setAnchorView(videoView);


        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(uploadingModel.getVideouri());
        videoView.requestFocus();
        videoView.start();
        long size;
        try {
            // File f = new File(uploadingModel.getVideouri().getPath());
            if (uploadingModel.isUploadState() == true) {
                FINAL_VIDEO_PATH = uploadingModel.getVideouri().getPath();
            } else FINAL_VIDEO_PATH = getPath(uploadingModel.getVideouri());
            // streamablefile=new File(FINAL_VIDEO_PATH);
            f = new File(FINAL_VIDEO_PATH);
            size = f.length() / (1024 * 1024);
        } catch (Exception e) {
            size = 0;
        }

        try {
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(folder, System.currentTimeMillis() + ".mp4");
            if (!file.exists()) {
                file.createNewFile();
//                Log.e("try", getUriForFile(getContext(), "com.diamond.future.fileprovider", file) + "");
                streamablefile = file;

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("exep", "ex" + "");

        }
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Toast.makeText(getContext(), "Video over", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });
        DateTimeFormatter df = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern("dd-MM-yyyy")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        LocalDate localdate = LocalDate.parse(uploadingModel.getDate(), df);
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("MMM dd, yy");
        String newdate = localdate.format(f2);
        //MMM dd, yy
        String information = "Date: " + newdate + "\n\n" +
                "Time: " + uploadingModel.getTime() + "\n\n" +
                "Receiver 1: " + uploadingModel.getReciver_email1() + "\n\n" +
                "Receiver 2: " + uploadingModel.getReciver_name2()+ "\n\n" +
                "Receiver 3: " + uploadingModel.getReciver_number3() + "\n\n" +
                size + "Mb = " + amountToPay;
        tvInformation.setText(information);
        title.setText("Pay Summary");
        //todo:compressor
        //   if (uploadingModel.isUploadState() == false) {
        ProgressDialog progressDialog1 = new ProgressDialog(this);
        progressDialog1.setMessage("Compressing..");
        progressDialog1.create();
        progressDialog1.show();

        VideoCompressor.INSTANCE.start(f.getPath(), streamablefile.getPath(), new CompressionListener() {
            @Override
            public void onStart() {
                Log.e("onStart", "onStart");

            }

            @Override
            public void onSuccess() {
                try {
                    progressDialog1.dismiss();
                } catch (Exception e) {
                }
                try {
                    // File f = new File(uploadingModel.getVideouri().getPath());
                    uploadsize = streamablefile.length() / (1024 * 1024);
                } catch (Exception e) {
                    uploadsize = 0;
                }
                //showDeleteDialog();
                Log.e("sucess", "sucess" + uploadsize);
                // Toast.makeText(this, "Sucess", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(@NotNull String s) {
                //  Toast.makeText(getContext(), "Failed To Compress", Toast.LENGTH_LONG);
                try{
                progressDialog1.dismiss();}catch(Exception e){}
                Log.e("onFailure", "onFailure");
            }

            @Override
            public void onProgress(float v) {
                Log.e("onProgress", "onProgress");

            }

            @Override
            public void onCancelled() {
                Log.e("onCancelled", "onCancelled");
            }
        }, VideoQuality.LOW, false, false);
        //   }

        ivBack.setOnClickListener(V -> {
            // ((MainActivity) getContext()).setFrag("receiveremail");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "receiveremail");
            startActivity(intent);
            finish();
//            ((MainActivity) getApplicationContext()).setFrag("receiveremail");
        });
        long timestamp = System.currentTimeMillis();
        DateTimeFormatter selectedDF = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern("dd-MM-yyyy H:m")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        LocalDate localdateselected = LocalDate.parse(uploadingModel.getDate() + " " + uploadingModel.getTimeToUpload(), selectedDF);

        Date date = new Date();
        date = Date.from(localdateselected.atStartOfDay(ZoneId.systemDefault()).toInstant());
        long localSelectedDate = date.getTime();

        long remainingTime = localSelectedDate - timestamp;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(remainingTime);
        int mYear = c.get(Calendar.YEAR) - 1970;
        //  double yrs = remainingTime / (365 * 24 * 60 * 60 * 1000);
        Log.e("yrs", "timestamp" + timestamp + "localSelectedDate" + localSelectedDate + "remainingTime" + remainingTime + "/" + (365 * 24 * 60 * 60 * 1000) + "====" + mYear);
        if (mYearnow == 0) {
            month = (int) ((double) remainingTime1 / (double) divingnum);
            if (month <= 2) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID1;
            } else if (month > 2 && month <= 6) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID2;
            } else if (month > 6) {
                Log.e("month", month + "=" + mYearnow);
                PRODUCT_ID = PRODUCT_ID3;
            }
        } else if (mYearnow >= 1 && mYearnow < 3) {
            PRODUCT_ID = PRODUCT_ID4;
        } else if (mYearnow >= 3 && mYearnow < 10) {
            PRODUCT_ID = PRODUCT_ID5;
        } else if (mYearnow >= 10 && mYearnow < 20) {
            PRODUCT_ID = PRODUCT_ID6;
        } else if (mYearnow >= 20) {
            PRODUCT_ID = PRODUCT_ID7;
        }



            /*if (mYear <= 2) {
                PRODUCT_ID = PRODUCT_ID1;
            } else if (mYear > 2 && mYear <= 5) {
                PRODUCT_ID = PRODUCT_ID2;
            } else if (mYear > 5 && mYear <= 10) {
                PRODUCT_ID = PRODUCT_ID3;
            } else if (mYear > 10 && mYear <= 50) {
                PRODUCT_ID = PRODUCT_ID4;
            } else if (mYear > 50 && mYear <= 100) {
                PRODUCT_ID = PRODUCT_ID5;
            } else PRODUCT_ID = PRODUCT_ID5;*/


        clikevent();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clikevent() {
        layoutHistory.setOnClickListener(V -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "history");
            startActivity(intent);
            finish();
        });
        layout_Video.setOnClickListener(V -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "layout_Video");
            startActivity(intent);
            finish();
        });
        layout_Setting.setOnClickListener(V -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", "layout_Setting");
            startActivity(intent);
            finish();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDeleteDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_dialog, viewGroup, false);
        TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
        TextView tvDescripion = dialogView.findViewById(R.id.tvDescripion);
        TextView tvOk = dialogView.findViewById(R.id.tvOk);
        tvDescripion.setText("Thank you for your payment.Your video have  successfully sent.");
        // builder.setCancelable(false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        tvCancel.setVisibility(View.GONE);
        DateTimeFormatter df = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern("dd-MM-yyyy H:m")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        LocalDate localdate = LocalDate.parse(uploadingModel.getDate() + " " + uploadingModel.getTimeToUpload(), df);

        Date date = new Date();
        date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        System.out.println(date); //
        DateFormat gmtFormat = new SimpleDateFormat();
        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        gmtFormat.setTimeZone(gmtTime);

        Log.e("GMT Time: ", gmtFormat.format(date) + "");
        /* DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("d/M/yy h:mm a").toFormatter();
        DateTimeFormatter f1 = new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy h:mm a").toFormatter();
        DateTimeFormatter f5 = new DateTimeFormatterBuilder().appendPattern("M/dd/yy h:mm a").toFormatter();
        LocalDateTime parsedDate;
        try {
            parsedDate = LocalDateTime.parse(gmtFormat.format(date) + "", f);

        } catch (Exception e) {
            try {
                parsedDate = LocalDateTime.parse(gmtFormat.format(date) + "", f1);

            } catch (Exception e1) {
                parsedDate = LocalDateTime.parse(gmtFormat.format(date) + "", f5);

            }

        }*/
        String time = uploadingModel.getTimeToUpload();
        String[] timeParts = time.split(":");
        Long total = (Long.parseLong(timeParts[0]) * (60000 * 60)) + (Long.parseLong(timeParts[1]) * 60000) + date.getTime();
        Log.e("tagmili", total + "-" + time);

        /*int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        Long newgmtTotal=total+offset;
        Log.e("newgmtTotal", newgmtTotal+"");*/

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(new Date(total)));

        SimpleDateFormat dfnew = new SimpleDateFormat("hh:mm a MM/dd/yyyy");
        dfnew.setTimeZone(TimeZone.getTimeZone("GMT"));
        String result = dfnew.format(total);
        System.out.println(result);
       /* tvDescripion.setText("Thank you for your payment.Your video have successfully sent.Selected GMT  Time is: "
                + result + ".Compress File Size is :" + uploadsize + "MB");*/

        DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("d/M/yy hh:mm a").toFormatter();
        DateTimeFormatter f1 = new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy hh:mm a").toFormatter();
        DateTimeFormatter f5 = new DateTimeFormatterBuilder().appendPattern("M/d/yy hh:mm a").toFormatter();
        DateTimeFormatter fg = new DateTimeFormatterBuilder().appendPattern("hh:mm a MM/dd/yyyy").toFormatter();
        LocalDateTime parsedDate;

        try {
            parsedDate = LocalDateTime.parse(result, f5);
            Log.e("parsedate", parsedDate.getYear() + "-" + parsedDate.getMonth() + "-" + parsedDate.getDayOfMonth() + "");
            DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter f3 = DateTimeFormatter.ofPattern("mm");
            DateTimeFormatter f4 = DateTimeFormatter.ofPattern("a");
            DateTimeFormatter f6 = DateTimeFormatter.ofPattern("h");

            newDate = parsedDate.format(f2);
            //newTime = parsedDate.format(f3);
            newhr = parsedDate.format(f6);
            newmin = parsedDate.format(f3);
            String ampm = parsedDate.format(f4) + "";
            int totalhr;
            if (ampm.equals("PM") || ampm.equals("pm")) {
                totalhr = 12 + Integer.parseInt(newhr);
                newhr = totalhr + "";
            }
            newTime = newhr + ":" + newmin;
            Log.e("Send Date: ", newDate + "");
            Log.e("Send Time: ", newTime + "");
            Log.e("AmPm: ", parsedDate.format(f4) + "");

        } catch (Exception e) {
            try {
                parsedDate = LocalDateTime.parse(result + "", f);
                Log.e("parsedate", parsedDate.getYear() + "-" + parsedDate.getMonth() + "-" + parsedDate.getDayOfMonth() + "");
                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter f3 = DateTimeFormatter.ofPattern("mm");
                DateTimeFormatter f4 = DateTimeFormatter.ofPattern("a");
                DateTimeFormatter f6 = DateTimeFormatter.ofPattern("h");

                newDate = parsedDate.format(f2);
                //newTime = parsedDate.format(f3);
                newhr = parsedDate.format(f6);
                newmin = parsedDate.format(f3);
                String ampm = parsedDate.format(f4) + "";
                int totalhr;
                if (ampm.equals("PM") || ampm.equals("pm")) {
                    totalhr = 12 + Integer.parseInt(newhr);
                    newhr = totalhr + "";
                }
                newTime = newhr + ":" + newmin;
                Log.e("Send Date: ", newDate + "");
                Log.e("Send Time: ", newTime + "");
                Log.e("AmPm: ", parsedDate.format(f4) + "");

            } catch (Exception e1) {
                try {
                    parsedDate = LocalDateTime.parse(result + "", f);
                    Log.e("parsedate", parsedDate.getYear() + "-" + parsedDate.getMonth() + "-" + parsedDate.getDayOfMonth() + "");
                    DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter f3 = DateTimeFormatter.ofPattern("mm");
                    DateTimeFormatter f4 = DateTimeFormatter.ofPattern("a");
                    DateTimeFormatter f6 = DateTimeFormatter.ofPattern("h");

                    newDate = parsedDate.format(f2);
                    //newTime = parsedDate.format(f3);
                    newhr = parsedDate.format(f6);
                    newmin = parsedDate.format(f3);
                    String ampm = parsedDate.format(f4) + "";
                    int totalhr;
                    if (ampm.equals("PM") || ampm.equals("pm")) {
                        totalhr = 12 + Integer.parseInt(newhr);
                        newhr = totalhr + "";
                    }
                    newTime = newhr + ":" + newmin;
                    Log.e("Send Date: ", newDate + "");
                    Log.e("Send Time: ", newTime + "");
                    Log.e("AmPm: ", parsedDate.format(f4) + "");

                } catch (Exception e2) {
                    try {
                        parsedDate = LocalDateTime.parse(result + "", fg);
                        Log.e("parsedate", parsedDate.getYear() + "-" + parsedDate.getMonth() + "-" + parsedDate.getDayOfMonth() + "");
                        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter f3 = DateTimeFormatter.ofPattern("mm");
                        DateTimeFormatter f4 = DateTimeFormatter.ofPattern("a");
                        DateTimeFormatter f6 = DateTimeFormatter.ofPattern("h");

                        newDate = parsedDate.format(f2);
                        //newTime = parsedDate.format(f3);
                        newhr = parsedDate.format(f6);
                        newmin = parsedDate.format(f3);
                        String ampm = parsedDate.format(f4) + "";
                        int totalhr;
                        if (ampm.equals("PM") || ampm.equals("pm")) {
                            totalhr = 12 + Integer.parseInt(newhr);
                            newhr = totalhr + "";
                        }
                        newTime = newhr + ":" + newmin;
                        Log.e("Send Date: ", newDate + "");
                        Log.e("Send Time: ", newTime + "");
                        Log.e("AmPm: ", parsedDate.format(f4) + "");

                    } catch (Exception e3) {
                        e3.printStackTrace();
                        newDate = null;
                        newTime = null;
                    }
                }
            }

        }
        String[] arr = MyTimeUtils.getGMTTimeString(uploadingModel.getDate() + "|" + uploadingModel.getTime());
        newDate = arr[0];
        newTime = arr[1];
        String[] corrent = MyTimeUtils.getTime2();
        result = corrent[1] + " " + corrent[0];
        tvDescripion.setText("Thank you for your payment.Your video have successfully sent.Selected GMT  Time is: "
                + result + "Send Date:" + newDate + "SendTime:" + newTime + ".Compress File Size is :" + uploadsize + "MB");
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                if (newDate != null && newTime != null) {
                    getUploadingFile(newDate, newTime);
                } else {
                    Toast.makeText(SendingPaymentActivity.this, "Please select proper date time", Toast.LENGTH_LONG).show();
                }


            }
        });
        //if (!((Activity) context).isFinishing()) {
            alertDialog.show();
    /*    } else {
            getUploadingFile(newDate, newTime);
        }*/


    }

    private String getPath(Uri uri) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getUploadingFile(String newDate, String newTime) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();
        } catch (Exception e) {
        }
        try {

            File file = null;
            PreManager preManager = new PreManager(context);
            String locationOn = preManager.getLOCATION();
            RequestApi requestAPI = RetroConfig.getClient(this);
            MultipartBody.Part part = null;
            String FINAL_VIDEO_PATH;
            if (uploadingModel.isUploadState() == true) {
                FINAL_VIDEO_PATH = uploadingModel.getVideouri().getPath();
                // file = new File(FINAL_VIDEO_PATH);
                file = streamablefile;
            } else {
                FINAL_VIDEO_PATH = getPath(uploadingModel.getVideouri());
                file = streamablefile;
            }


            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = null;
            if (!FINAL_VIDEO_PATH.equals(""))
                fileToUpload = MultipartBody.Part.createFormData("video", file.getName(), requestBody);
            RequestBody devid = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getDeviceId());
            RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), amountToPaysend);
            RequestBody latitude = null;
            RequestBody longitude = null;
            if (preManager.getLOCATION().equalsIgnoreCase("Yes")) {
                latitude = RequestBody.create(MediaType.parse("multipart/form-data"), "" + latitude1);
                longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "" + longitude1);
            } else {
                latitude = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            }
            RequestBody price_token = RequestBody.create(MediaType.parse("multipart/form-data"), "jhenewknek8903");
            RequestBody reciverEmailId1 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_email1());
            RequestBody reciverEmailId2 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_name2());
            RequestBody reciverEmailId3 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_number3());
            RequestBody reciveTime = RequestBody.create(MediaType.parse("multipart/form-data"), newTime);
            RequestBody reciveDate = RequestBody.create(MediaType.parse("multipart/form-data"), newDate);
            RequestBody timezone = RequestBody.create(MediaType.parse("multipart/form-data"), TimeZone.getDefault().getID() + "");

            ProgressDialog finalProgressDialog = progressDialog;
            requestAPI.uploadVideo(fileToUpload, price, latitude, longitude, price_token, reciverEmailId1, reciverEmailId2, reciverEmailId3, reciveTime, devid, reciveDate, timezone, preManager.getAuthToken())
                    .enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            if (response.code() == 200) {
                                if (response.body().getStatus() == 1) {
                                    Intent intent = new Intent(SendingPaymentActivity.this, MainActivity.class);
                                    intent.putExtra("name", "layout_Video");
                                    startActivity(intent);
                                    // ((MainActivity) context).setFrag("video");
                                    // Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                                } else
                                    Toast.makeText(SendingPaymentActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();

                            } else if (response.code() == 413) {
                                Toast.makeText(SendingPaymentActivity.this, "Please crop the selected Image.It is too large too upload", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(SendingPaymentActivity.this, response.message(), Toast.LENGTH_LONG).show();
                            try {
                                finalProgressDialog.dismiss();
                            } catch (Exception e) {
                            }
                            Log.e("Login Failed ", " " + response.code());

                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            Log.e("Login Api ERROR ", t.getMessage());
                            Toast.makeText(SendingPaymentActivity.this, t.getMessage() + "", Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                            try {
                                finalProgressDialog.dismiss();
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("Login Api ERROR ", e.getMessage());
            Toast.makeText(SendingPaymentActivity.this, e.getMessage() + "", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            try {
                progressDialog.dismiss();
            } catch (Exception e1) {
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        COIN = PRODUCT_ID;
        TEST = PRODUCT_ID;
        skuList = Arrays.asList(COIN, TEST);
        iapHelper = new IAPHelper(this, this, skuList);
        Log.e("finalproductid", PRODUCT_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void consume(View view) {
        if (amountToPay.equalsIgnoreCase("Free")) {
            showDeleteDialog();
        } else {
            launch(TEST);
        }
    }

    private void launch(String sku) {
        if (!skuDetailsHashMap.isEmpty())
            iapHelper.launchBillingFLow(skuDetailsHashMap.get(sku));
    }


    @Override
    public void onSkuListResponse(HashMap<String, SkuDetails> skuDetails) {
        skuDetailsHashMap = skuDetails;
    }

    @Override
    public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {
        if (purchasedItems != null) {
            for (Purchase purchase : purchasedItems) {
                //Update UI and backend according to purchased items if required
                // Like in this project I am updating UI for purchased items
                String sku = purchase.getProducts().get(0);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPurchaseCompleted(Purchase purchase) {
        // Toast.makeText(getApplicationContext(), "Purchase Successful", Toast.LENGTH_LONG).show();
        updatePurchase(purchase);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePurchase(Purchase purchase) {
        String sku = purchase.getProducts().get(0);
        //  Toast.makeText(this, "Sucess" + COIN, Toast.LENGTH_LONG).show();
        showDeleteDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iapHelper != null)
            iapHelper.endConnection();
    }

    public void getloc() {
        try {
            if (checkAndRequestPermissions()) {
                Location nwLocation = appLocationService
                        .getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null) {
                    latitude1 = nwLocation.getLatitude();
                    longitude1 = nwLocation.getLongitude();
                    //Toast.makeText(context,""+latitude1+"\n"+longitude1,Toast.LENGTH_LONG).show();
                    //AppPrefrence.getInstance().setUserAddMoneyData(AppSingle.getInstance().getActivity().getUserJson(address.getPremises()+" ,"+address.getSubLocality(),address.getLocality(),address.getPostalCode(),address.getAdminArea(),address.getCountryName(),AppSingle.getInstance().getActivity().getIMEI(),AppSingle.getInstance().getActivity().getLocalIpAddress()));
                } else {
                    //showSettingsAlert("NETWORK");
                }
            } else {

            }
        } catch (Exception e) {
        }
    }

    public boolean checkAndRequestPermissions() {
        try {
            int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int readPhoneState = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            int profilephoto = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> listPermissionsNeeded = new ArrayList<>();
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (profilephoto != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivitys(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }

    public Activity getActivitys() {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
