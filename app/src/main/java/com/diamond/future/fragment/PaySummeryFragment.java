package com.diamond.future.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.diamond.Security;
import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.diamond.future.SendingPaymentActivity;
import com.diamond.future.model.UserProfile;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.android.billingclient.api.BillingClient.SkuType.INAPP;
import static com.diamond.future.fragment.DateFragment.uploadingModel;


public class PaySummeryFragment extends Fragment  {
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
    private BillingClient billingClient;
    int month=0;

    View v;
    TextView title, tvInformation;
    ImageView ivBack;
    LinearLayout llNext;
    VideoView videoView;
    PreManager preManager;
    String newDate, newTime, newhr, newmin;
    String FINAL_VIDEO_PATH;
   public static File streamablefile;
    File f;
    String dest = "";
    float uploadsize = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pay_summery_fragment, container, false);
        initView();
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        month=0;
        long timestamp1 = System.currentTimeMillis();
        Toast.makeText(getContext(),"ffff",Toast.LENGTH_LONG).show();
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
        int smonth=c1.get(Calendar.MONTH);
        //  double yrs = remainingTime / (365 * 24 * 60 * 60 * 1000);
        Log.e("totaltime",timestamp1+"="+localSelectedDate1+"="+mYearnow+"="+remainingTime1);
        long divingnum=2592000000l;
       /* if (mYearnow == 0) {
            month =(int)((double) remainingTime1 /(double)divingnum );
            if(month<=2){
                Log.e("month",month+"="+mYearnow);
            }else if(month>2&&month<=6){
                Log.e("month",month+"="+mYearnow);
            }else if(month>6){
                Log.e("month",month+"="+mYearnow);
            }
        } else if (mYearnow <= 2) {
            PRODUCT_ID = PRODUCT_ID1;
            amountToPay = "$ 2";
        } else if (mYearnow > 2 && mYearnow <= 5) {
            PRODUCT_ID = PRODUCT_ID2;
            amountToPay = "$ 10";
        } else if (mYearnow > 5 && mYearnow <= 10) {
            PRODUCT_ID = PRODUCT_ID3;
            amountToPay = "$ 20";
        } else if (mYearnow > 10 && mYearnow <= 50) {
            PRODUCT_ID = PRODUCT_ID4;
            amountToPay = "$ 100";
        } else if (mYearnow > 50 && mYearnow <= 100) {
            PRODUCT_ID = PRODUCT_ID5;
            amountToPay = "$ 200";
        } else {
            PRODUCT_ID = PRODUCT_ID5;
            amountToPay = "$ 200";
        }*/
        if (mYearnow == 0) {
            month =(int)((double) remainingTime1 /(double)divingnum );
            if(month<=2){
                Log.e("month",month+"="+mYearnow);
                PRODUCT_ID = PRODUCT_ID1;
                amountToPay = "$ 0.99";
            }else if(month>2&&month<=6){
                Log.e("month",month+"="+mYearnow);
                PRODUCT_ID = PRODUCT_ID2;
                amountToPay = "$ 2.99";
            }else if(month>6){
                Log.e("month",month+"="+mYearnow);
                PRODUCT_ID = PRODUCT_ID3;
                amountToPay = "$ 4.99";
            }
        } else if (mYearnow>= 1&&mYearnow<3) {
            PRODUCT_ID = PRODUCT_ID4;
            amountToPay = "$ 19.99";
        } else if (mYearnow>= 3&&mYearnow<10) {
            PRODUCT_ID = PRODUCT_ID5;
            amountToPay = "$ 49.99";
        } else if (mYearnow>= 10&&mYearnow<20) {
            PRODUCT_ID = PRODUCT_ID6;
            amountToPay = "$ 99.99";
        }else if (mYearnow>=20) {
            PRODUCT_ID = PRODUCT_ID7;
            amountToPay = "$ 199.99";
        }
        Log.e("productid", PRODUCT_ID);
        preManager = new PreManager(getContext());
        //todo: inapp purchase
     /*   billingClient = BillingClient.newBuilder(getContext())
                .enablePendingPurchases().setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Purchase.PurchasesResult queryPurchase = billingClient.queryPurchases(INAPP);
                    List<Purchase> queryPurchases = queryPurchase.getPurchasesList();
                    if (queryPurchases != null && queryPurchases.size() > 0) {
                        handlePurchases(queryPurchases);
                    }
                    //if purchase list is empty that means item is not purchased
                    //Or purchase is refunded or canceled
                    else {
                        savePurchaseValueToPref(false);
                    }
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
//item Purchased
        if (getPurchaseValueFromPref()) {
            //purchaseButton.setVisibility(View.GONE);
            //purchaseStatus.setText("Purchase Status : Purchased");
            // Toast.makeText(getContext(), "Purchase Status : Purchased", Toast.LENGTH_SHORT).show();
        }
//item not Purchased
        else {
            //purchaseButton.setVisibility(View.VISIBLE);
            // purchaseStatus.setText("Purchase Status : Not Purchased");
            // Toast.makeText(getContext(), "Purchase Status : Not Purchased", Toast.LENGTH_SHORT).show();

        }*/
        videoView = v.findViewById(R.id.videoView);
        ivBack = v.findViewById(R.id.ivBack);
        title = v.findViewById(R.id.title);
        llNext = v.findViewById(R.id.llNext);
        tvInformation = v.findViewById(R.id.tvInformation);
        final MediaController mediacontroller = new MediaController(getContext());
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
                "Receiver : " + uploadingModel.getReciver_email1() + "\n\n" +
                "Receiver : " + uploadingModel.getReciver_name2() + "\n\n" +
                "Receiver : " + uploadingModel.getReciver_number3() + "\n\n" +
                size + "Mb = " + amountToPay;
        tvInformation.setText(information);
        title.setText("Pay Summary");
        //todo:compressor
        //   if (uploadingModel.isUploadState() == false) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Compressing..");
        progressDialog.create();
        progressDialog.show();

        VideoCompressor.INSTANCE.start(f.getPath(), streamablefile.getPath(), new CompressionListener() {
            @Override
            public void onStart() {
                Log.e("onStart", "onStart");

            }

            @Override
            public void onSuccess() {
                progressDialog.dismiss();
                try {
                    // File f = new File(uploadingModel.getVideouri().getPath());
                    uploadsize = streamablefile.length() / (1024 * 1024);
                } catch (Exception e) {
                    uploadsize = 0;
                }
                Log.e("sucess", "sucess"+uploadsize);
                Toast.makeText(getContext(), "Sucess", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(@NotNull String s) {
                Toast.makeText(getContext(), "Failed To Compress", Toast.LENGTH_LONG);
                progressDialog.dismiss();
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
            ((MainActivity) getContext()).setFrag("receiveremail");
        });
        //todo:Purchase product on next click
        llNext.setOnClickListener(V -> {
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
                month =(int)((double) remainingTime1 /(double)divingnum );
                if(month<=2){
                    Log.e("month",month+"="+mYearnow);
                    PRODUCT_ID = PRODUCT_ID1;
                }else if(month>2&&month<=6){
                    Log.e("month",month+"="+mYearnow);
                    PRODUCT_ID = PRODUCT_ID2;
                }else if(month>6){
                    Log.e("month",month+"="+mYearnow);
                    PRODUCT_ID = PRODUCT_ID3;
                }
            } else if (mYearnow>= 1&&mYearnow<3) {
                PRODUCT_ID = PRODUCT_ID4;
            } else if (mYearnow>= 3&&mYearnow<10) {
                PRODUCT_ID = PRODUCT_ID5;
            } else if (mYearnow>= 10&&mYearnow<20) {
                PRODUCT_ID = PRODUCT_ID6;
            }else if (mYearnow>=20) {
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
            Log.e("productid", PRODUCT_ID);
            Intent intent=new Intent(getContext(), SendingPaymentActivity.class);
            intent.putExtra("productid",PRODUCT_ID);
            intent.putExtra("uploadsize",uploadsize+"");
            startActivity(intent);
           // purchaseclickmethod();
            // showDeleteDialog();
        });

    }

   /* private void purchaseclickmethod() {
        if (billingClient.isReady()) {
            initiatePurchase();
        }
        //else reconnect service
        else {
            billingClient = BillingClient.newBuilder(getContext()).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase();
                    } else {
                        Log.e("error", billingResult + "");
                        Toast.makeText(getContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                }
            });
        }
    }

    private SharedPreferences getPreferenceObject() {
        return getActivity().getSharedPreferences(PREF_FILE, 0);
    }

    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getActivity().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private boolean getPurchaseValueFromPref() {
        return getPreferenceObject().getBoolean(PURCHASE_KEY, false);
    }

    private void savePurchaseValueToPref(boolean value) {
        getPreferenceEditObject().putBoolean(PURCHASE_KEY, value).commit();
    }

    private void initiatePurchase() {
        List<String> skuList = new ArrayList<>();
        skuList.add(PRODUCT_ID);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(INAPP);
        Log.e("skulist", skuList.toString());
        SkuDetailsParams skuParams = SkuDetailsParams.newBuilder().setType(BillingClient.SkuType.INAPP).setSkusList(skuList).build();

        billingClient.querySkuDetailsAsync(skuParams,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow(getActivity(), flowParams);


                            } else {
//try to add item/product id "purchase" inside managed product in google play console
                                Toast.makeText(getActivity(), "Purchase Item not Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),
                                    " Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDeleteDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_dialog, viewGroup, false);
        TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
        TextView tvDescripion = dialogView.findViewById(R.id.tvDescripion);
        TextView tvOk = dialogView.findViewById(R.id.tvOk);
        tvDescripion.setText("Thank you for your payment.Your video have  successfully sent.");
        // builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
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
       *//* DateTimeFormatter f = new DateTimeFormatterBuilder().appendPattern("d/M/yy h:mm a").toFormatter();
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

        }*//*
        String time = uploadingModel.getTimeToUpload();
        String[] timeParts = time.split(":");
        Long total = (Long.parseLong(timeParts[0]) * (60000 * 60)) + (Long.parseLong(timeParts[1]) * 60000) + date.getTime();
        Log.e("tagmili", total + "-" + time);

        *//*int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        Long newgmtTotal=total+offset;
        Log.e("newgmtTotal", newgmtTotal+"");*//*

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(sdf.format(new Date(total)));

        SimpleDateFormat dfnew = new SimpleDateFormat("hh:mm a MM/dd/yyyy");
        dfnew.setTimeZone(TimeZone.getTimeZone("GMT"));
        String result = dfnew.format(total);
        System.out.println(result);
        tvDescripion.setText("Thank you for your payment.Your video have successfully sent.Selected GMT  Time is: " + result + ".Compress File Size is :" + uploadsize + "MB");

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
            if (ampm.equals("PM")) {
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
                if (ampm.equals("PM")) {
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
                    if (ampm.equals("PM")) {
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
                        if (ampm.equals("PM")) {
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
                    Toast.makeText(getContext(), "Please select proper date time", Toast.LENGTH_LONG).show();
                }


            }
        });
        alertDialog.show();

    }
*/
    private String getPath(Uri uri) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void getUploadingFile(String newDate, String newTime) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        File file = null;

        RequestApi requestAPI = RetroConfig.getClient(getContext());
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
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), "10.20");
        RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), "25.612677");
        RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "85.158875");
        RequestBody price_token = RequestBody.create(MediaType.parse("multipart/form-data"), "jhenewknek8903");
        RequestBody reciverEmailId1 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_email1());
        RequestBody reciverEmailId2 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_name2());
        RequestBody reciverEmailId3 = RequestBody.create(MediaType.parse("multipart/form-data"), uploadingModel.getReciver_number3());
        RequestBody reciveTime = RequestBody.create(MediaType.parse("multipart/form-data"), newTime);
        RequestBody reciveDate = RequestBody.create(MediaType.parse("multipart/form-data"), newDate);
        RequestBody timezone = RequestBody.create(MediaType.parse("multipart/form-data"), TimeZone.getDefault().getID()+"");

        requestAPI.uploadVideo(fileToUpload, price,latitude,longitude, price_token, reciverEmailId1, reciverEmailId2, reciverEmailId3, reciveTime, devid, reciveDate,timezone, preManager.getAuthToken())
                .enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                ((MainActivity) getContext()).setFrag("video");
                                // Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_LONG).show();

                        } else if (response.code() == 413) {
                            Toast.makeText(getContext(), "Please crop the selected Image.It is too large too upload", Toast.LENGTH_LONG).show();
                        } else
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();
                        Log.e("Login Failed ", " " + response.code());

                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        Log.e("Login Api ERROR ", t.getMessage());
                        Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        progressDialog.dismiss();
                    }
                });
    }

  /*  @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
//if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases);
        }
//if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(INAPP);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if (alreadyPurchases != null) {
                handlePurchases(alreadyPurchases);
            }
        }
//if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(getContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show();
        }
// Handle any other error msgs
        else {
            Toast.makeText(getContext(), "Error " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void handlePurchases(List<Purchase> purchases) {
        for (Purchase purchase : purchases) {
//if item is purchased
            if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
// Invalid purchase
// show error to user
                    Toast.makeText(getContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
// else purchase is valid
//if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
//else item is purchased and also acknowledged
                else {
// Grant entitlement to the user on item purchase
// restart activity
                    if (!getPurchaseValueFromPref()) {
                        showDeleteDialog();
                        savePurchaseValueToPref(true);
                        Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        // getActivity().recreate();
                    }
                }
            }
//if purchase is pending
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                Toast.makeText(getContext(),
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
//if purchase is unknown
            else if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                savePurchaseValueToPref(false);
                // purchaseStatus.setText("Purchase Status : Not Purchased");
                // purchaseButton.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//if purchase is acknowledged
// Grant entitlement to the user. and restart activity
                showDeleteDialog();
                savePurchaseValueToPref(true);
                Toast.makeText(getContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                // getActivity().recreate();
            }
        }
    };

    *//**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     *//*
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
// To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiRbrbAYge1qt1lFVepkiWDEooNW2bEGvzxjtSfLaelLi6mbVWU2uICHSf71VBlWqawbefDU/iSoVkd67/B7w7V7dn/zjVneuIJ/35ZRupdFB7HHJeca+WIbpinN68UVKiBwD+KHhO9u14BUzlXhwNMHBjjgheGiLp6ec8SHDgUFDIUtdtzsdtNuhlnqZMbCuHWW246IZ6ZyUyrvfHt2XZkAsWysHXMGD4/KjZDuWwshOw9cIlw6PZCd3/hN+sBZ0hv3SYaAy90qqEJp0Uh/Nce/bLiuZbuYGUoAvolEYtdpwRPn3iggVrQICELTaW+TjMLU/NJouxOi2/siFMmawjQIDAQAB";
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }*/
}
