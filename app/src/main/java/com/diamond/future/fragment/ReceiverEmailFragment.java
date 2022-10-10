package com.diamond.future.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.diamond.future.LoginActivity;
import com.diamond.future.MainActivity;
import com.diamond.future.R;

import java.util.Calendar;


public class ReceiverEmailFragment extends Fragment {
    View v;
    Calendar myCalendar;
    TextView title;
    private TimePicker timePicker1;
    LinearLayout llNext;
    EditText etRecvEmail,etRecvEmail2,etRecvEmail3;
    ImageView ivBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.receiver_email_fragment, container, false);
        initView();
        return v;
    }

    private void initView() {

        etRecvEmail=v.findViewById(R.id.etRecvEmail);
        etRecvEmail2=v.findViewById(R.id.etRecvEmail2);
        etRecvEmail3=v.findViewById(R.id.etRecvEmail3);
        title=v.findViewById(R.id.title);
        timePicker1 = v. findViewById(R.id.timePicker1);
        myCalendar = Calendar.getInstance();
        title.setText("Receiver Email");
        llNext=v.findViewById(R.id.llNext);
        llNext.setOnClickListener(V->{
            if(etRecvEmail.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"At least Select One Email",Toast.LENGTH_LONG).show();
            }else  if (!isValidEmail(etRecvEmail.getText().toString())) {
                Toast.makeText(getContext(), "Enter valid email ", Toast.LENGTH_SHORT).show();
            } else {
                DateFragment.uploadingModel.setReciver_mail1(etRecvEmail.getText().toString());
                DateFragment.uploadingModel.setReciver_mail2(etRecvEmail2.getText().toString());
                DateFragment.uploadingModel.setReciver_mail3(etRecvEmail3.getText().toString());
                //((MainActivity) getContext()).setFrag("paysummery");
                ((MainActivity) getContext()).setFrag("paysummery");
            }
        });
        ivBack = v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(V->{
            ((MainActivity) getContext()).setFrag("timeFrag");
        });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
