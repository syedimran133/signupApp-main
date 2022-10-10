package com.diamond.future.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.diamond.future.MainActivity;
import com.diamond.future.MapsMarkerActivity;
import com.diamond.future.R;
import com.diamond.future.SendingVideoActivity;
import com.diamond.future.model.ItemsSender;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.diamond.future.fragment.HistoryFragment.videoItem;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemsSender.Items> data;
    String baseUrl;
    long receivetimeinMiliSec = 0;

    public ItemsAdapter(Context context, ArrayList<ItemsSender.Items> bannerModules, String baseUrl) {
        this.context = context;
        this.data = bannerModules;
        this.baseUrl = baseUrl;
    }

    int currentPostion = 0;

    int getCurrentPostion() {
        return currentPostion;
    }

    public ItemsAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        Glide.with(context).load(data.get(i).getImagePath()).placeholder(R.drawable.sendinglogo).into(viewHolder.ivimage);
        //   viewHolder.tvId.setText(data.get(i).getId()+".");
        viewHolder.tvId.setText((i + 1) + ".");
        viewHolder.tvName.setText(data.get(i).getReceiverName());
        if (data.get(i).getAdd_datetime() != null && !data.get(i).getAdd_datetime().isEmpty()) {
            try{
            String serverTime = data.get(i).getAdd_datetime(); // GMT
            ZonedDateTime gmtTime = LocalDateTime.parse(serverTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
            LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newDate = localTime.format(f2);
            viewHolder.tvSendingDate.setText("" + newDate);}catch (Exception e){}
        }
        //Toast.makeText(context, data.get(i).getLatitude(), Toast.LENGTH_LONG).show();
        if (data.get(i).getReciveDate() != null && !data.get(i).getReciveDate().isEmpty()) {
            try {
                String serverTime = data.get(i).getReciveDate() + " " + data.get(i).getReciveTime(); // GMT
                ZonedDateTime gmtTime = LocalDateTime.parse(serverTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String newDate = localTime.format(f2);
                viewHolder.tvReceivingDate.setText("" + newDate);
                Log.e("localtime", localTime.getDayOfWeek() + "");

                long remainsitem = 0;
                long days = 0;
                long hr = 0;
                long minute = 0;
                long sec = 0;
                //remainsitem=localTime.getSecond()*1000;
                remainsitem = localTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Log.e("remainsitem", remainsitem + "");
                long timestamp = System.currentTimeMillis();
                Log.e("timestamp", timestamp + "");

                if (remainsitem > timestamp) {
                    remainsitem = remainsitem - timestamp;
                    if (remainsitem == 0) {
                        viewHolder.tvStatus.setText("Video Sent");
                    } else {
                        days = remainsitem / (24 * 60 * 60 * 1000);
                        long remafotrhr = (remainsitem % (24 * 60 * 60 * 1000));
                        hr = remafotrhr / (60 * 60 * 1000);
                        long remainsforminute = remafotrhr % (60 * 60 * 1000);
                        minute = remainsforminute / (60 * 1000);
                        long remainsforSec = remainsforminute % (60 * 1000);
                        sec = remainsforSec / 1000;
                        String status = "";
                        if (days != 0) {
                            status = status + days + " Days";
                        } else if (hr != 0) {
                            status = status + hr + "hr";
                        } else if (minute != 0) {
                            status = status + minute + "minute";
                        } else if (sec != 0) {
                            status = status + sec + "Sec";
                        }
                        viewHolder.tvStatus.setText(status + " Left");
                    }
                } else {
                    viewHolder.tvStatus.setText("Video Sent");
                }
            }catch (Exception e){}
        }
        // viewHolder.tvReceivingDate.setText(""+data.get(i).getReciveDate()+" "+data.get(i).getReciveTime());
        viewHolder.tvPrice.setText(data.get(i).getPrice() + "$");
        if (data.get(i).getReciverEmailId1() != null && !data.get(i).getReciverEmailId1().isEmpty())
            viewHolder.tvSenderEmaillone.setText(data.get(i).getReciverEmailId1());
        else viewHolder.tvSenderEmaillone.setText(".....");
        if (data.get(i).getUpload_size() != null) {
            Log.e("size", Integer.parseInt(data.get(i).getUpload_size()) + "");
            double sizeInMb = (Double.parseDouble(data.get(i).getUpload_size()) / (1024 * 1024));
            Log.e("sizedes", sizeInMb + "");
            viewHolder.tvSize.setText(String.format("%.2f", sizeInMb) + " Mb");

        }

        if (data.get(i).getReciverEmailId2() != null && !data.get(i).getReciverEmailId2().isEmpty())
            viewHolder.tvSenderEmailltwo.setText(data.get(i).getReciverEmailId2());
        else viewHolder.tvSenderEmailltwo.setText(".....");

        if (data.get(i).getReciverEmailId3() != null && !data.get(i).getReciverEmailId3().isEmpty())
            viewHolder.tvSenderEmaillthree.setText(data.get(i).getReciverEmailId3());
        else viewHolder.tvSenderEmaillthree.setText(".....");
        viewHolder.rlMain.setOnClickListener(V -> {
            if (data.get(i).getUpload_size() != null) {
                videoItem = data.get(i);
                Intent intent = new Intent(context, SendingVideoActivity.class);
                intent.putExtra("type", "sendItem");
                context.startActivity(intent);
                //((MainActivity) context).setFrag("sendingvideo");
            } else {
                Toast.makeText(context, "Video is corrupted", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivimage, ivMap;
        TextView tvId, tvName, tvStatus, tvSendingDate, tvReceivingDate, tvPrice, tvSenderEmaillone, tvSenderEmailltwo, tvSenderEmaillthree, tvSize;
        RelativeLayout rlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvId = itemView.findViewById(R.id.tvId);
            ivimage = itemView.findViewById(R.id.ivimage);
            tvName = itemView.findViewById(R.id.tvName);
            tvSendingDate = itemView.findViewById(R.id.tvSendingDate);
            tvReceivingDate = itemView.findViewById(R.id.tvReceivingDate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvSenderEmaillthree = itemView.findViewById(R.id.tvSenderEmaillthree);
            tvSenderEmailltwo = itemView.findViewById(R.id.tvSenderEmailltwo);
            tvSenderEmaillone = itemView.findViewById(R.id.tvSenderEmaillone);
            rlMain = itemView.findViewById(R.id.rlMain);
            ivMap = itemView.findViewById(R.id.ivMap);
        }
    }
   /* public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            context.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack(tag)
                    .commit();
            return true;
        }
        return false;
    }*/

}
