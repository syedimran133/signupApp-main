package com.diamond.future.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.diamond.future.service.AppLocationService;
import com.gowtham.library.utils.LogMessage;
import com.gowtham.library.utils.TrimType;
import com.gowtham.library.utils.TrimVideo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.diamond.future.fragment.DateFragment.uploadingModel;
import static com.diamond.future.fragment.HistoryFragment.videoItem;


public class VideoFragment extends Fragment {
    View v;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    AppLocationService appLocationService;
    double latitude1 = 0.0;
    double longitude1 = 0.0;
    LinearLayout llUpload,llRecord;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private static final int PERMISSION_REQ_CODE = 1 << 4;
    boolean recordState=false;
    //Java
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    uploadingModel.setVideouri(uri);
                    Log.d("TAG", "Trimmed path:: " + uri);
                    ((MainActivity) getContext()).setFrag("datefrag");
                } else
                    LogMessage.v("videoTrimResultLauncher data is null");
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.video_fragment, container, false);
        appLocationService = new AppLocationService(getContext(),getActivitys());
        getloc();
        initView();
        checkPermission();
        return v;
    }
    private void checkPermission() {
        boolean granted = true;
        for (String per : PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }
        if (granted) {
            //showTermsPrivacyDialog();
            if(recordState) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5*60);
                if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, 101);
                }
            }
        } else {
            requestPermissions();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                if(recordState) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,5*60);
                    if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, 101);
                    }
                }
            } else {
            }
        }
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_REQ_CODE);
    }
    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                getContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
    private void initView() {
        llRecord=v.findViewById(R.id.llRecord);
        llUpload=v.findViewById(R.id.llUpload);
        llUpload.setOnClickListener(V->{
            recordState=false;
            uploadingModel.setUploadState(true);
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Video"),REQUEST_TAKE_GALLERY_VIDEO);
        });
        llRecord.setOnClickListener(V->{
            recordState=true;
            uploadingModel.setUploadState(false);
            checkPermission();

        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();
                TrimVideo.activity(String.valueOf(selectedImageUri))
//        .setCompressOption(new CompressOption()) //empty constructor for default compress option
                        .setHideSeekBar(true)
                        .setTrimType(TrimType.MIN_MAX_DURATION)
                        .setMinToMax(2, 60*5)
                        .start(this,startForResult);


            }else {
                Uri uri =data.getData();
                uploadingModel.setVideouri(uri);
                Log.d("TAG", "Trimmed path:: " + uri);
                ((MainActivity) getContext()).setFrag("datefrag");
            }
        } else if (resultCode == 101) {
            Uri uri =data.getData();
            uploadingModel.setVideouri(uri);
            Log.d("TAG", "Trimmed path:: " + uri);
            ((MainActivity) getContext()).setFrag("datefrag");
        }
    }
    public void getloc() {
        try {
            if (checkAndRequestPermissions()) {
                Location nwLocation = appLocationService
                        .getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null) {
                    latitude1 = nwLocation.getLatitude();
                    longitude1 = nwLocation.getLongitude();
                   // Toast.makeText(getContext(),""+latitude1+"\n"+longitude1,Toast.LENGTH_LONG).show();
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
            int locationPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            int readPhoneState = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE);
            int profilephoto = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

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
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
