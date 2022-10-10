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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.diamond.future.MainActivity;
import com.diamond.future.ProfileActivity;
import com.diamond.future.R;
import com.diamond.future.SendingVideoActivity;
import com.diamond.future.model.ItemsSender;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.diamond.future.fragment.HistoryFragment.videoItem;

public class ReceiverItemAdapter extends RecyclerView.Adapter<ReceiverItemAdapter.ViewHolder> {
    Context context;
    ArrayList<ItemsSender.Items> data;
    String baseUrl;
    private IonItemSelect ionItemSelect;
    long receiveditem = 0;

    public ReceiverItemAdapter(Context context, ArrayList<ItemsSender.Items> bannerModules, String baseUrl) {
        this.context = context;
        this.data = bannerModules;
        this.baseUrl = baseUrl;
    }

    public void registerOnItemClickListener(IonItemSelect ionItemSelect) {
        this.ionItemSelect = ionItemSelect;
    }

    int currentPostion = 0;

    int getCurrentPostion() {
        return currentPostion;
    }

    public ReceiverItemAdapter() {
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_item_receiver_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //Glide.with(context).load(data.get(i).getImagePath()).placeholder(R.drawable.sendinglogo).into(viewHolder.ivimage);
        //  viewHolder.tvId.setText(data.get(i).getId()+".");
        viewHolder.tvId.setText((i + 1) + ".");
        viewHolder.tvName.setText(data.get(i).getSenderName());
        viewHolder.tvDate.setText(data.get(i).getReciveDate());
        viewHolder.tvTime.setText(data.get(i).getModified());
        if (data.get(i).getLatitude() != null) {
            if (data.get(i).getLatitude().equalsIgnoreCase("")) {
                viewHolder.imgProfile.setVisibility(View.GONE);
                viewHolder.ivMap.setVisibility(View.GONE);
            } else {
                viewHolder.imgProfile.setVisibility(View.VISIBLE);
                viewHolder.ivMap.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.imgProfile.setVisibility(View.GONE);
            viewHolder.ivMap.setVisibility(View.GONE);
        }
        Glide.with(context).load(data.get(i).getUserImage())
                .into(viewHolder.imgProfile);
        viewHolder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, MapsMarkerActivity.class));
                if (ionItemSelect != null)
                    ionItemSelect.onItemSelect(i);
            }
        });
        if (data.get(i).getAdd_datetime() != null && !data.get(i).getAdd_datetime().isEmpty()) {
            String serverTime = data.get(i).getAdd_datetime(); // GMT
            ZonedDateTime gmtTime = LocalDateTime.parse(serverTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
            LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String newDate = localTime.format(f2);
            viewHolder.tvDate.setText("" + newDate);
        }
        if (data.get(i).getReciveDate() != null && !data.get(i).getReciveDate().isEmpty()) {
            try {
                String serverTime = data.get(i).getReciveDate() + " " + data.get(i).getReciveTime(); // GMT
                ZonedDateTime gmtTime = LocalDateTime.parse(serverTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String newDate = localTime.format(f2);
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


                viewHolder.tvTime.setText("" + newDate);
            } catch (Exception e) {
            }
        }

        viewHolder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.tvStatus.getText().toString().equals("Video Sent")) {

                    videoItem = data.get(i);
                    Intent intent = new Intent(context, SendingVideoActivity.class);
                    intent.putExtra("type", "receiveItem");
                    context.startActivity(intent);
                    //  ((MainActivity) context).setFrag("sendingvideo");
                } else {
                    Toast.makeText(context, "You can view video after " + viewHolder.tvStatus.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivimage, ivMap;
        CircleImageView imgProfile;
        TextView tvId, tvName, tvDate, tvTime, tvPrice, tvSenderEmaillone, tvSenderEmailltwo, tvSenderEmaillthree, tvSize, tvStatus;
        RelativeLayout rlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            ivimage = itemView.findViewById(R.id.ivimage);
            ivMap = itemView.findViewById(R.id.ivMap);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvSenderEmaillthree = itemView.findViewById(R.id.tvSenderEmaillthree);
            tvSenderEmailltwo = itemView.findViewById(R.id.tvSenderEmailltwo);
            tvSenderEmaillone = itemView.findViewById(R.id.tvSenderEmaillone);
            rlMain = itemView.findViewById(R.id.rlMain);
        }
    }

    public interface IonItemSelect {
        void onItemSelect(int position);
    }
}
