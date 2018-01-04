package com.example.suganraj.wbgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Suganraj on 04-01-2018.
 */

public class UserBill extends AppCompatActivity{
Button payment;
ImageView download;
String stringForTextFile;
    TextView name,aadhar,address,phone,current_reading,previous_reading,consumption,total_bill,paid,maintenance,costPerLiter;
    Firebase mRefCostPerLiter;
    Firebase mRefPrevious;
    Firebase mRefMaintenance;
    String aadhar_str = "";
    String current_str = "";
    String previous_str = "";
    String maintenance_str = "";
    float tot_bal = 0.0f;
    String paid_check = "";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userbill);
        Bundle extra = getIntent().getExtras();
        aadhar_str = extra.getString("aadhar");
        String aadharID = aadhar_str;
        download = (ImageView)findViewById(R.id.download_txt);
        name = (TextView)findViewById(R.id.user_bill_name);
        aadhar = (TextView)findViewById(R.id.user_bill_aadhar);
        address = (TextView)findViewById(R.id.user_bill_address);
        phone = (TextView)findViewById(R.id.user_bill_phone);
        current_reading = (TextView)findViewById(R.id.user_bill_current_reading);
        previous_reading = (TextView)findViewById(R.id.user_bill_previous_reading);
        consumption = (TextView)findViewById(R.id.user_bill_consumption);
        maintenance = (TextView)findViewById(R.id.user_bill_maintenance);
        total_bill = (TextView)findViewById(R.id.user_bill_total_charge);
        paid = (TextView)findViewById(R.id.user_bill_paid);
        costPerLiter = (TextView)findViewById(R.id.user_bill_cost_liter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Firebase mRefName = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/name");
        Firebase mRefAddress = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/address");
        Firebase mRefPhone = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/phone");

        Firebase mRefCurrent = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/current");
        mRefPrevious = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/previous");
        Firebase mRefPaid = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/paid");
        mRefMaintenance = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/maintenance");
        mRefCostPerLiter = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar_str+"/costPerLiter");

        aadhar.setText("Aadhar Number     : "+aadharID);

        mRefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name_str = dataSnapshot.getValue(String.class);
                name.setText("Name      : "+name_str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String address_str = dataSnapshot.getValue(String.class);
                address.setText("Address     :"+address_str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mRefPhone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String phone_str = dataSnapshot.getValue(String.class);
                phone.setText("Phone     :"+phone_str);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefCurrent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                current_str = dataSnapshot.getValue(String.class);
                current_reading.setText("Current Reading    :"+current_str);
                mRefPrevious.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        previous_str = dataSnapshot.getValue(String.class);
                        previous_reading.setText("Previous Reading     : "+previous_str);

                        mRefMaintenance.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                maintenance_str = dataSnapshot.getValue(String.class);

                                mRefCostPerLiter.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        String liter = dataSnapshot.getValue(String.class);

                                        Float cur = Float.parseFloat(current_str);
                                        Float pre = Float.parseFloat(previous_str);
                                        Float main = Float.parseFloat(maintenance_str);
                                        Float lit = Float.parseFloat(liter);
                                        Float consumpt = cur - pre;
                                        Float tot = (consumpt * lit) + main;
                                        tot_bal = tot;

                                        consumption.setText("Consumption     : "+consumpt+" liters");
                                        maintenance.setText("Maintenance Charge     : " +main);
                                        total_bill.setText("Total Charge     : Rs."+tot+"/-");
                                        costPerLiter.setText("Cost Per Liter     : Rs."+lit+"/-");



                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        mRefPaid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();
                String paid_str = dataSnapshot.getValue(String.class);
                paid_check = paid_str;
                paid.setText("Paid     : "+paid_str);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        payment = (Button) findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paid_check.equals("no")) {
                    Intent i = new Intent(getApplicationContext(), Payment.class);
                    i.putExtra("aadhar", aadhar_str);
                    i.putExtra("totalcharge",tot_bal);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Already paid..",Toast.LENGTH_SHORT).show();
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download.setVisibility(View.INVISIBLE);
                stringForTextFile = "------- Service Information-------\n"+name.getText().toString()+"\n"+aadhar.getText().toString()
                        +"\n"+address.getText().toString()+"\n"+phone.getText().toString()+"\n-------Meter Information-------"+"\n"+current_reading.getText().toString()+"\n"+
                        previous_reading.getText().toString()+"\n"+consumption.getText().toString()+"\n-------Payment Information-------"+"\n"+maintenance.getText().toString()+"\n"+costPerLiter.getText().toString()
                        +"\n"+total_bill.getText().toString()+"\n"+paid.getText().toString();

                try {
                    File root = new File(Environment.getExternalStorageDirectory(), "Waterbill");
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File gpxfile = new File(root, "bill.txt");
                    FileWriter writer = new FileWriter(gpxfile);
                    writer.append(stringForTextFile);
                    writer.flush();
                    writer.close();
                    Toast.makeText(getApplicationContext(),""+root, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(),UserView.class);
        i.putExtra("aadhar",aadhar_str);
        startActivity(i);
        finish();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){

        }
    }
}
