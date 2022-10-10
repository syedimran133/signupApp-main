package com.diamond.future.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diamond.future.LoginActivity;
import com.diamond.future.MapsMarkerActivity;
import com.diamond.future.R;
import com.diamond.future.adapter.ItemsAdapter;
import com.diamond.future.adapter.ReceiverItemAdapter;
import com.diamond.future.model.ItemsSender;
import com.diamond.future.model.UploadingModel;
import com.diamond.future.utility.Constant;
import com.diamond.future.utility.PreManager;
import com.diamond.future.utility.RequestApi;
import com.diamond.future.utility.RetroConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {
    View v;
    Context context;
    RecyclerView rvListitem,rvListitemReceive;
    ArrayList<String> itemList=new ArrayList<>();
    PreManager preManager;
    Button  btnSendItems,btnReceiveItems;
    TextView tvNoItems;
    public static ItemsSender.Items videoItem=new ItemsSender.Items();
    public static boolean selecctedtype=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.history_fragment, container, false);
        context=getContext();
        initView();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(selecctedtype){
            if(btnReceiveItems!=null)
                btnReceiveItems.performClick();
        }else {
            if (btnSendItems != null)
                btnSendItems.performClick();
        }

    }

    private void initView() {
        preManager=new PreManager(getContext());
        btnSendItems=v.findViewById(R.id.btnSendItems);
        rvListitemReceive=v.findViewById(R.id.rvListitemReceive);
        tvNoItems=v.findViewById(R.id.tvNoItems);
        btnReceiveItems=v.findViewById(R.id.btnReceiveItems);
        btnReceiveItems.setOnClickListener(V->{
            btnReceiveItems.setBackgroundResource(R.drawable.rounded_button_editback);
            btnSendItems.setBackgroundResource(R.drawable.rounded_button_editback_blue);
            getDataFromServerReceiveItems();
            selecctedtype=true;
        });
        btnSendItems.setOnClickListener(V->{
            btnSendItems.setBackgroundResource(R.drawable.rounded_button_editback);
            btnReceiveItems.setBackgroundResource(R.drawable.rounded_button_editback_blue);
            getDataFromServer();
            selecctedtype=false;
        });
        getDataFromServer();
        rvListitem=v.findViewById(R.id.rvListitem);
        itemList.add("");
        itemList.add("");
        itemList.add("");
        itemList.add("");
        itemList.add("");
        itemList.add("");
        itemList.add("");



    }
    private void getDataFromServerReceiveItems() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(getContext());
        requestAPI.getReceiveItems(preManager.getAuthToken()).enqueue(new Callback<ItemsSender>() {
            @Override
            public void onResponse(Call<ItemsSender> call, Response<ItemsSender> response) {
                if (response.code() == 200) {
                    if(response.body().getStatus()==1) {
                     //   Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                        if(response.body().getItems()!=null) {
                            tvNoItems.setVisibility(View.GONE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.VISIBLE);
                            setAdapterforreceive(response.body().getItems());
                        }else {
                            tvNoItems.setVisibility(View.VISIBLE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.GONE);
                            ArrayList<ItemsSender.Items> items=new ArrayList<>();
                            setAdapterforreceive(items);
                        }
                    }else if(response.body().getStatus()==0){
                        if(response.body().getData().equals("token Id not vaild")){
                            Intent intent=new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            tvNoItems.setVisibility(View.VISIBLE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.GONE);
                            ArrayList<ItemsSender.Items> items=new ArrayList<>();
                            setAdapterforreceive(items);
                            Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        tvNoItems.setVisibility(View.VISIBLE);
                        rvListitem.setVisibility(View.GONE);
                        rvListitemReceive.setVisibility(View.GONE);
                        ArrayList<ItemsSender.Items> items=new ArrayList<>();
                        setAdapterforreceive(items);
                        Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                    }

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<ItemsSender> call, Throwable t) {
                tvNoItems.setVisibility(View.VISIBLE);
                rvListitem.setVisibility(View.GONE);
                rvListitemReceive.setVisibility(View.GONE);
                ArrayList<ItemsSender.Items> items=new ArrayList<>();
                setAdapterforreceive(items);
                Toast.makeText(getContext(), "Data not Found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }
    private void getDataFromServer() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();

        RequestApi requestAPI = RetroConfig.getClient(getContext());
        requestAPI.getSentItems(preManager.getAuthToken()).enqueue(new Callback<ItemsSender>() {
            @Override
            public void onResponse(Call<ItemsSender> call, Response<ItemsSender> response) {
                if (response.code() == 200) {
                    if(response.body().getStatus()==1) {
                   //     Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                        if(response.body().getItems()!=null) {
                            tvNoItems.setVisibility(View.GONE);
                            rvListitem.setVisibility(View.VISIBLE);
                            rvListitemReceive.setVisibility(View.GONE);
                            setAdapter(response.body().getItems());
                        }else {
                            tvNoItems.setVisibility(View.VISIBLE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.GONE);
                            ArrayList<ItemsSender.Items> items=new ArrayList<>();
                            setAdapter(items);
                        }
                    }else if(response.body().getStatus()==0){
                        if(response.body().getData().equals("token Id not vaild")){
                            tvNoItems.setVisibility(View.VISIBLE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.GONE);
                            Intent intent=new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            tvNoItems.setVisibility(View.VISIBLE);
                            rvListitem.setVisibility(View.GONE);
                            rvListitemReceive.setVisibility(View.GONE);
                            ArrayList<ItemsSender.Items> items=new ArrayList<>();
                            setAdapter(items);
                            Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        ArrayList<ItemsSender.Items> items=new ArrayList<>();
                        setAdapter(items);
                        Toast.makeText(getContext(), response.body().getData(), Toast.LENGTH_LONG).show();
                    }

                }
                progressDialog.dismiss();
                Log.e("Login Failed ", " " + response.code());

            }

            @Override
            public void onFailure(Call<ItemsSender> call, Throwable t) {
                tvNoItems.setVisibility(View.VISIBLE);
                rvListitem.setVisibility(View.GONE);
                rvListitemReceive.setVisibility(View.GONE);
                ArrayList<ItemsSender.Items> items=new ArrayList<>();
                setAdapter(items);
                Toast.makeText(getContext(), "Data not Found ", Toast.LENGTH_SHORT).show();
                Log.e("Login Api ERROR ", t.getMessage());
                t.printStackTrace();
                progressDialog.dismiss();
            }
        });
    }

    private void setAdapter(ArrayList<ItemsSender.Items> items) {
        ItemsAdapter itemsAdapter=new ItemsAdapter(getContext(),items,"");
        rvListitem.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListitem.setAdapter(itemsAdapter);

       itemsAdapter.notifyDataSetChanged();
    }
    public boolean pushFragment(Fragment fragment, String tag) {
        //   FrameLayout viewPager_old = (FrameLayout)findViewById(R.id.viewPager_old);
        //    viewPager_old.setVisibility(View.VISIBLE);
//        viewPager.setVisibility(View.GONE);


        if (fragment != null) {
            getActivity().getSupportFragmentManager()
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
    private void setAdapterforreceive(ArrayList<ItemsSender.Items> items) {
        ReceiverItemAdapter itemsAdapter=new ReceiverItemAdapter(getContext(),items,"");
        rvListitemReceive.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListitemReceive.setAdapter(itemsAdapter);
        itemsAdapter.registerOnItemClickListener(new ReceiverItemAdapter.IonItemSelect() {
            @Override
            public void onItemSelect(int position) {
                //pushFragment(new MapsMarkerActivity(),"teg");
                Intent intent = new Intent(context,MapsMarkerActivity.class);
                intent.putExtra(Constant.LAT, items.get(position).getLatitude());
                intent.putExtra(Constant.LOG, items.get(position).getLongitude());
                startActivity(intent);
            }
        });
        itemsAdapter.notifyDataSetChanged();
    }

}
