package com.example.suganraj.wbgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Suganraj on 04-01-2018.
 */

public class AdminBill extends AppCompatActivity{
    ProgressDialog progressDialog;
    String aadhar_str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminbill);
        final EditText aadhar = (EditText) findViewById(R.id.admin_aadhar_number);
        Button generate = (Button) findViewById(R.id.admin_generate_bill);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Please wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                aadhar_str = aadhar.getText().toString();
                Firebase mRef = new Firebase("https://wb-generator.firebaseio.com/aadhar/");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,String> map = dataSnapshot.getValue(Map.class);
                        if(map.containsValue(aadhar_str)){
                            Firebase mRef = new Firebase("https://wb-generator.firebaseio.com/accounts/"+aadhar_str);
                            mRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String pass = dataSnapshot.getValue(String.class);
                                    if(pass.length()>5){
                                        Intent intent = new Intent(getApplicationContext(),BillGenerate.class);
                                        intent.putExtra("aadhar",aadhar_str);
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Check your aadhar number...",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),AdminView.class);
        startActivity(intent);
        finish();
    }
}
