package com.diamond.future;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.diamond.future.model.UserProfile;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    Uri imagePath;
    public static final int REQUEST_IMAGE = 100;
    CircleImageView imgProfile;
    EditText etBio;
    TextInputEditText edit_FullName, edit_UserName;
    PreManager preManager;
    private int GALLERY = 1, CAMERA = 2;
    ArrayList<InputStream> img_array = new ArrayList<>();
    InputStream is;
    String FINAL_VIDEO_PATH = "";
    String encodedImage = "";
    CheckBox cb;
    String loc = "No";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imgProfile = findViewById(R.id.imgProfile);
        etBio = findViewById(R.id.etBio);
        edit_FullName = findViewById(R.id.edit_FullName);
        edit_UserName = findViewById(R.id.edit_UserName);
        preManager = new PreManager(this);
        loc = preManager.getLOCATION();
        cb = findViewById(R.id.location_cb);
        if (loc.equalsIgnoreCase("Yes")) {
            cb.setChecked(true);
            loc="Yes";
        } else {
            cb.setChecked(false);
            loc="No";
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    loc = "Yes";
                } else {
                    loc = "No";
                }
            }
        });

        getUserDetails();
    }

    private void getUserDetails() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(this);

        requestAPI.getEditProfile(preManager.getAuthToken()).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus() == 1) {
                        // Toast.makeText(ProfileActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        if (response.body().getUserProfile() != null && !response.body().getUserProfile().equals("")) {
                            Glide.with(ProfileActivity.this).load(response.body().getUserProfile().getUser_profilepic())
                                    .into(imgProfile);
                            edit_UserName.setText(response.body().getUserProfile().getUser_name());
                            edit_FullName.setText(response.body().getUserProfile().getUser_name());
                            etBio.setText(response.body().getUserProfile().getAboutus());
                            if (response.body().getUserProfile().getLocation().equalsIgnoreCase("Yes")) {
                                cb.setChecked(true);
                            } else {
                                cb.setChecked(false);
                            }
                        }
                    } else
                        Toast.makeText(ProfileActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    public void UPdateProfile(View view) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            openPicker();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    private void openPicker() {
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.gallery_layout, viewGroup, false);
        LinearLayout layout_1 = dialogView.findViewById(R.id.layout_1);
        LinearLayout layout_2 = dialogView.findViewById(R.id.layout_2);
        // builder.setCancelable(false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
                alertDialog.dismiss();
            }
        });
        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFromCamera();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }

    public void choosePhotoFromGallary() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
      /*  Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);*/

    }

    private void takePhotoFromCamera() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                FINAL_VIDEO_PATH = resultUri.getPath();
                Log.w("FINAL_VIDEO_PATH", FINAL_VIDEO_PATH);
              /*  is = getContentResolver().openInputStream(data.getData());
                img_array.add(is);*/
                //  imgProfile.setImageBitmap(bitmap);
                Glide.with(ProfileActivity.this).load(resultUri)
                        .into(imgProfile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
       /* if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                    imagePath = tempUri;
                    FINAL_VIDEO_PATH = getRealPathFromURI_API19(getApplicationContext(), contentURI);
                    Log.w("FINAL_VIDEO_PATH", FINAL_VIDEO_PATH);
                    is = getContentResolver().openInputStream(data.getData());
                    img_array.add(is);
                  //  imgProfile.setImageBitmap(bitmap);
                    Glide.with(ProfileActivity.this).load(contentURI)
                            .into(imgProfile);
                    encodedImage = encodeTobase64(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
           // imgProfile.setImageBitmap(thumbnail);

            Uri contentURI = data.getData();


            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                Glide.with(ProfileActivity.this).load(tempUri)
                        .into(imgProfile);
                imagePath = tempUri;
                FINAL_VIDEO_PATH = getRealPathFromURI_API19(getApplicationContext(), contentURI);
                Log.w("FINAL_VIDEO_PATH", FINAL_VIDEO_PATH);
                is = getContentResolver().openInputStream(tempUri);
                img_array.add(is);
                encodedImage = encodeTobase64(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }*/


    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkFolder();


                } else {
                    //code for deny
                    checkAgain();
                }
                break;
        }
    }

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            androidx.appcompat.app.AlertDialog.Builder alertBuilder = new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            androidx.appcompat.app.AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }


    public void saveProfile(View view) {
        saveToServer();
    }

    private void saveToServer() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        //File file = null;

        RequestApi requestAPI = RetroConfig.getClient(this);
        MultipartBody.Part part = null;

        File file = new File(FINAL_VIDEO_PATH);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = null;
        if (!FINAL_VIDEO_PATH.equals(""))
            fileToUpload = MultipartBody.Part.createFormData("profilePic", file.getName(), requestBody);
        RequestBody user_name = RequestBody.create(MediaType.parse("multipart/form-data"), edit_UserName.getText().toString());
        RequestBody user_phonenumber = null;
        if (preManager.getphNumber().equalsIgnoreCase("")) {
            user_phonenumber = RequestBody.create(MediaType.parse("multipart/form-data"), "No Data");
        } else {
            user_phonenumber = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getphNumber());
        }
        RequestBody aboutus = RequestBody.create(MediaType.parse("multipart/form-data"), "No Data");
        RequestBody devid = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getDeviceId());
        RequestBody devname = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getDeviceName());
        RequestBody location = RequestBody.create(MediaType.parse("multipart/form-data"), loc);
        RequestBody devversion = RequestBody.create(MediaType.parse("multipart/form-data"), preManager.getDeviceVerson());
        requestAPI.setEditProfile(fileToUpload, user_name, user_phonenumber, devid, devname, devversion, aboutus, location, preManager.getAuthToken())
                .enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(ProfileActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                                preManager.setLOCATION(loc);
                            } else
                                Toast.makeText(ProfileActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();

                        }
                        if (response.code() == 413) {
                            Toast.makeText(ProfileActivity.this, "Please crop the selected Image.It is too large too upload", Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                        Log.e("Login Failed ", " " + response.code());

                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        // Toast.makeText(ProfileActivity.this, "Something Went Wrong  ", Toast.LENGTH_SHORT).show();
                        finish();
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