package com.diamond.future.fragment;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.diamond.future.LoginActivity;
import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.diamond.future.model.ItemsSender;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.diamond.future.fragment.HistoryFragment.videoItem;


public class SendingVideoFragment extends Fragment implements Player.EventListener {
    View v;
   public static SimpleExoPlayer player;
    PlayerView playerView;
    TextView title, tvInformation;
    ImageView ivBack;
    LinearLayout llNext;
    VideoView videoView;
    ImageView ivShare, ivDownload,ivDelete;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,

    };
    RelativeLayout relativeLayoutMainLayout;
    private static final int PERMISSION_REQ_CODE = 1 << 4;
    String videourl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.showing_video_fragment, container, false);
        initView();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player.stop();
        }
    }

    private void initView() {
        relativeLayoutMainLayout = v.findViewById(R.id.mainLayout);
        playerView = v.findViewById(R.id.simpleExoPlayerView);
        ivDelete = v.findViewById(R.id.ivDelete);
        videoView = v.findViewById(R.id.videoView);
        ivDownload = v.findViewById(R.id.ivDownload);
        ivShare = v.findViewById(R.id.ivShare);
        ivBack = v.findViewById(R.id.ivBack);
        tvInformation = v.findViewById(R.id.tvInformation);
        final MediaController mediacontroller = new MediaController(getContext());
        mediacontroller.setAnchorView(videoView);
        videourl = videoItem.getUpload_video();
        initialize(videoItem.getUpload_video());
        playerView.getVideoSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        relativeLayoutMainLayout.setOnClickListener(V -> {
            playerView.performClick();
        });
        ivDelete.setOnClickListener(V->{
            getDeleteDialog();
        });
      /*  videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(Uri.parse(videoItem.getUpload_video()));
        videoView.requestFocus();
        videoView.start();

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
        });*/
        long sizeInMb = Long.parseLong(videoItem.getUpload_size()) / (1024 * 1024);
        String information = "Name: " + videoItem.getSenderName() + "\n\n" +
                "Date: " + videoItem.getReciveDate() + "\n\n" +
                "Time: " + videoItem.getReciveTime() + "\n\n" +
                "Size " + sizeInMb + "mb" + "\n\n" +
                "Price:$ " + videoItem.getPrice() + "\n\n";
        tvInformation.setText(information);
        ivBack.setOnClickListener(V -> {
            if (player != null) {
                player.release();
                player.stop();
            }
            ((MainActivity) getContext()).setFrag("history");
        });
        ivShare.setOnClickListener(V -> {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            /*This will be the actual content you wish you share.*/
            /*The type of the content is text, obviously.*/
            intent.setType("text/plain");
            /*Applying information Subject and Body.*/
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Video message " + videoItem.getUpload_video());
            /*Fire!*/
            startActivity(Intent.createChooser(intent, "Video Message"));
        });
        ivDownload.setOnClickListener(V -> {

            checkPermission();

        });
    }
    private void getDeleteDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_dialog, viewGroup, false);
        TextView tvCancel = dialogView.findViewById(R.id.tvCancel);
        TextView tvDescripion = dialogView.findViewById(R.id.tvDescripion);
        TextView tvOk = dialogView.findViewById(R.id.tvOk);
        tvDescripion.setText("Are you sure you want to delete this item.");
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        tvCancel.setVisibility(View.VISIBLE);
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
                getDeleteItem();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getDeleteItem() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        PreManager preManager=new PreManager(getContext());
        RequestApi requestAPI = RetroConfig.getClient(getContext());
        requestAPI.getdeleteItems(preManager.getAuthToken(),videoItem.getUploadId()+"").enqueue(new Callback<ItemsSender>() {
            @Override
            public void onResponse(Call<ItemsSender> call, Response<ItemsSender> response) {
                if (response.code() == 200) {
                    if(response.body().getStatus()==1) {
                        Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                        ivBack.performClick();
                    }else if(response.body().getStatus()==0){
                        if(response.body().getData().equals("token Id not vaild")){
                            Intent intent=new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else  Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();

                    }
                    else  Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<ItemsSender> call, Throwable t) {

                Toast.makeText(getContext(), "Data not Found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    void initialize(String videourln) {
        Uri uri = Uri.parse(videourln);
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        play();
    }

    public void play() {
        /*    currentViewPlayingPosition = HomeProductAdater.VideoViewHolder.this.getAdapterPosition();
            initializedVideo();*/
        if (player != null) {
            player.seekTo(0L);
            player.play();
        }
        Log.e("Video is Playing ", " " + player.isPlaying());

    }

    @Override
    public void onStart() {
        super.onStart();
        initialize(videourl);
        //playIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        //playIcon.setVisibility(View.VISIBLE);
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

            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(videoItem.getUpload_video());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("My Video File");
            request.setDescription("Future  Video Download");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            try {
                if (downloadManager != null) {
                    request.setDestinationUri(getUri());
                    downloadManager.enqueue(request);
                }
                Toast.makeText(getContext(), "Video Saved in  Gallery Successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(), "Internal Storage Issue", Toast.LENGTH_SHORT).show();
                Log.e("tagexception",e+"");
            }

          /*  File myFile;
            try {
                myFile = new File(new File(UtilNew.getItemDir()), videoItem.getSenderName() + ".mp4");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                    new DownloadFileTask(videoItem.getUpload_video(), myFile).execute();

                } else {
                    Toast.makeText(getContext(), "Video Saved in  Gallery Successfully", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues(3);
                    values.put(MediaStore.Video.Media.TITLE, "My video title");
                    values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                    values.put(MediaStore.Video.Media.DATA, myFile.getAbsolutePath());
                    getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } else {
            requestPermissions();
        }
    }

    Uri getUri() {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver contentResolver = getContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, videoItem.getSenderName() + ".mp4");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/*");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            // If you downloaded to a specific folder inside "Downloads" folder
            contentValues.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + File.separator + "Temp");
            Uri uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            Log.e("qtag", uri + "");
            return uri;
        } else */{

            try {
                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(folder, videoItem.getSenderName() + ".mp4");
                if (!file.exists()) {
                    file.createNewFile();
                    Log.e("try", getUriForFile(getContext(), "com.diamond.future.fileprovider", file) + "");
                    return getUriForFile(getContext(), "com.diamond.future.fileprovider", file);


                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("exep", "ex" + "");

                return null;

            }
        }
        Log.e("lowest", "ex" + "");

        return null;
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
                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(videoItem.getUpload_video());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("My Video File");
                request.setDescription("Future  Video Download");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(true);
                request.setDestinationUri(getUri());
                downloadManager.enqueue(request);
                Toast.makeText(getContext(), "Video Saved in  Gallery Successfully", Toast.LENGTH_SHORT).show();
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

    class DownloadFileTask extends AsyncTask<Void, Void, String> {

        String url;
        File file;

        public DownloadFileTask(String url, File file) {
            this.url = url;
            this.file = file;
        }

        @Override
        protected String doInBackground(Void... voids) {
            downloadFile(url, file);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(videoItem.getUpload_video());
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("My Video File");
            request.setDescription("Future  Video Download");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationUri(getUri());
            downloadManager.enqueue(request);
            Toast.makeText(getContext(), "Video Saved in  Gallery Successfully", Toast.LENGTH_SHORT).show();
        }

    }

    public void downloadFile(String fileURL, File directory) {
        int count;
        try {
            URL url = new URL(fileURL);
            URLConnection conection = url.openConnection();
            conection.connect();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(directory);
            byte data[] = new byte[1024];
            long total = 0;
            int lenghtOfFile = conection.getContentLength();
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
                int progress = (int) ((total * 100) / lenghtOfFile);

            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
