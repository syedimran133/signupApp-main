package com.diamond.future.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
//import android.widget.Toast;
import static android.app.Activity.RESULT_OK;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.diamond.future.LoginActivity;
import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.diamond.future.utility.UtilNew;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ReceiverEmailFragment extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int CONTACT_PICKER_RESULT = 2;

    View v;
    Calendar myCalendar;
    TextView title,btnCal;
    private TimePicker timePicker1;
    LinearLayout llNext;
    EditText etRecvEmail,etRecvEmail2,etRecvEmail3;
    ImageView ivBack;
    String phoneNumber="";
    String contactName = "";
    public static Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.receiver_email_fragment, container, false);
        context=getContext();
        initView();
        return v;
    }
    //etRecvEmail3 is in use only for email
    private void initView() {

        etRecvEmail=v.findViewById(R.id.etRecvEmail);
        etRecvEmail2=v.findViewById(R.id.etRecvEmail2);
        etRecvEmail3=v.findViewById(R.id.etRecvEmail3);
        btnCal=v.findViewById(R.id.btnCal);
        title=v.findViewById(R.id.title);
        timePicker1 = v. findViewById(R.id.timePicker1);
        myCalendar = Calendar.getInstance();
        title.setText("Receiver");
        llNext=v.findViewById(R.id.llNext);
        llNext.setOnClickListener(V->{
            if(TextUtils.isEmpty(contactName)&&TextUtils.isEmpty(phoneNumber)){
                //Toast.makeText(getContext(),"At least Select One",Toast.LENGTH_LONG).show();
            }
            /*else  if (!isValidEmail(etRecvEmail.getText().toString())) {
                Toast.makeText(getContext(), "Enter valid email ", Toast.LENGTH_SHORT).show();
            } */
            else {
                DateFragment.uploadingModel.setReciver_email1(etRecvEmail3.getText().toString());
                DateFragment.uploadingModel.setReciver_name2(contactName);
                DateFragment.uploadingModel.setReciver_number3(phoneNumber);
                //((MainActivity) getContext()).setFrag("paysummery");
                ((MainActivity) getContext()).setFrag("paysummery");
            }
        });
        ivBack = v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(V->{
            ((MainActivity) getContext()).setFrag("timeFrag");
        });
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Request contacts permission if not granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    // Permission is already granted, fetch contacts
                    phoneNumber ="";
                    contactName = "";
                    btnCal.setText("Whatsapp");
                    fetchContacts();
                }
            }
        });
    }
    private void fetchContacts() {
        // Start an activity to pick a contact
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch contacts
                fetchContacts();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Log.d("ContactFetcher", "Contacts permission denied");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    // Handle the picked contact
                    handleContactPicked(data);
                    break;
            }
        }
    }

    @SuppressLint("Range")
    private void handleContactPicked(Intent data) {

        if (data == null) {
            return;
        }
        ContentResolver contentResolver = requireContext().getContentResolver();
        Cursor cursor = contentResolver.query(data.getData(), null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            boolean hasEmail = hasEmail(contentResolver, contactID);
            boolean hasPhoneNumber = hasPhoneNumber(contentResolver, contactID);


                //retrieveEmails(contentResolver, contactID);
                retrievePhoneNumber(contentResolver, contactID);
                retrieveContactName(contentResolver, contactID);
                //.makeText(getContext(), "Phone Number: " + phoneNumber , Toast.LENGTH_SHORT).show();

            cursor.close();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @SuppressLint("Range")
    private void retrievePhoneNumber(ContentResolver contentResolver, String contactID) {
        Cursor phones = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null
        );
        while (phones != null && phones.moveToNext()) {
            phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        if(UtilNew.hasCountryCode(phoneNumber)){
            //Toast.makeText(getContext(), "Phone Number with country code", Toast.LENGTH_SHORT).show();
            btnCal.setText(phoneNumber);
        }else{
            //Toast.makeText(getContext(), "Phone Number without country code", Toast.LENGTH_SHORT).show();
            phoneNumber=formatPhoneNumber(phoneNumber);
            btnCal.setText(phoneNumber);
        }
        if (phones != null) {
            phones.close();
        }
    }

    @SuppressLint("Range")
    private String retrieveContactName(ContentResolver contentResolver, String contactID) {

        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        }

        if (cursor != null) {
            cursor.close();
        }

        return contactName;
    }

    private static String formatPhoneNumber(String phone) {
        // Ensure 'phone' is not null and is a string
        if (phone != null && phone instanceof String) {
            // Remove unwanted characters from the phone number
            String[] charactersToRemove = {"(", ")", "-", " "};
            for (String character : charactersToRemove) {
                phone = phone.replace(character, "");
            }

            // Check if the phone number does not contain "+" and does not start with "00"
            if (!phone.contains("+") && !phone.startsWith("00")) {
                // Get the country code based on the current locale
                TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                String countryCode = UtilNew.getCountryCodeDictionary().get(tm.getNetworkCountryIso().toUpperCase());
                // Add the country code to the phone number
                if (countryCode != null) {
                    phone = "+" + countryCode + phone;
                    return phone;  // Return the modified phone number
                }
            }
        }

        return phone;  // Return the original phone number if no modifications were made
    }

    private boolean hasEmail(ContentResolver contentResolver, String contactID) {
        Cursor emails = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactID},
                null
        );
        boolean hasEmail = emails != null && emails.moveToFirst();
        if (emails != null) {
            emails.close();
        }
        return hasEmail;
    }

    private boolean hasPhoneNumber(ContentResolver contentResolver, String contactID) {
        Cursor phones = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactID},
                null
        );
        boolean hasPhoneNumber = phones != null && phones.moveToFirst();
        if (phones != null) {
            phones.close();
        }
        return hasPhoneNumber;
    }

}
