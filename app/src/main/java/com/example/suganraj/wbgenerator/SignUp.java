package com.example.suganraj.wbgenerator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Suganraj on 27-12-2017.
 */

public class SignUp extends AppCompatActivity{
    String uName = "";
    String uAadhar = "";
    String uaddress = "";
    String uAge = "";
    String uMobile = "";
    String uMailID = "";
    String uPassword = "";
    String uVerifyPassword = "";
    String uGender = "";
    Button signUP;
    ProgressDialog progressDialog;
    EditText name,aadhar,address,age,phone,email,password,rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        signUP = (Button) findViewById(R.id.buttonSignUp);
        name = (EditText) findViewById(R.id.sname);
        aadhar = (EditText) findViewById(R.id.saadhar);
        address = (EditText) findViewById(R.id.saddress);
        age = (EditText) findViewById(R.id.sage);
        phone = (EditText) findViewById(R.id.sphone);
        email = (EditText) findViewById(R.id.semail);
        password = (EditText) findViewById(R.id.spassword);
        rePassword = (EditText) findViewById(R.id.sRePassword);

        signUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(view.getContext());
                progressDialog.setMessage("Please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                uName = name.getText().toString();
                uAadhar = aadhar.getText().toString();
                uaddress = address.getText().toString();
                uAge = age.getText().toString();
                uMailID = email.getText().toString();
                uMobile = phone.getText().toString();
                uPassword = password.getText().toString();
                uVerifyPassword = rePassword.getText().toString();

                if (uAadhar.length() == 12) {

                    if (password.length() > 7) {

                        if (uName.equals("") || uAadhar.equals("") || uaddress.equals("") || uAge.equals("") || uMailID.equals("") || uMobile.equals("") || uPassword.equals("") || uVerifyPassword.equals("")|| uGender.equals("")) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please Enter All the Details", Toast.LENGTH_SHORT).show();
                        } else {
                            if (uPassword.equals(uVerifyPassword)) {
                                if (uMailID.contains("@")) {
                                    if (uMobile.length() == 10) {
                                        Firebase uRefAadhar = new Firebase("https://wb-generator.firebaseio.com/aadhar");
                                        uRefAadhar.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Map<String,String> map = dataSnapshot.getValue(Map.class);
                                                if(map.containsValue(uAadhar)){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"You are Already Registered",Toast.LENGTH_SHORT).show();
                                                }else
                                                {
                                                    Firebase uReference = new Firebase("https://wb-generator.firebaseio.com/");
                                                    Firebase uRefAccounts = uReference.child("accounts");
                                                    uRefAccounts.child(uAadhar).setValue(uPassword);
                                                    Firebase uRefStorage = uReference.child("storage");
                                                    Firebase uRefUserFolder = uRefStorage.child(uAadhar);
                                                    uRefUserFolder.child("aadhar").setValue(uAadhar);
                                                    uRefUserFolder.child("name").setValue(uName);
                                                    uRefUserFolder.child("address").setValue(uaddress);
                                                    uRefUserFolder.child("age").setValue(uAge);
                                                    uRefUserFolder.child("phone").setValue(uMobile);
                                                    uRefUserFolder.child("email").setValue(uMailID);
                                                    uRefUserFolder.child("gender").setValue(uGender);
                                                    uRefUserFolder.child("current").setValue("0.0");
                                                    uRefUserFolder.child("previous").setValue("0.0");
                                                    uRefUserFolder.child("paid").setValue("Yes");
                                                    uRefUserFolder.child("password").setValue(uPassword);
                                                    uRefUserFolder.child("maintanence").setValue("0.0");
                                                    uRefUserFolder.child("costPerLiter").setValue("0.0");

                                                    uReference.child("aadhar").push().setValue(uAadhar);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(),UserView.class);
                                                    intent.putExtra("aadhar",uAadhar);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Mobile number must contain 10 Digits", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "MailID is not a valid one", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Password Not Same", Toast.LENGTH_SHORT).show();
                                password.setText("");
                                rePassword.setText("");
                            }
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Password must contain atleast 8 letters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid Aadhar Number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
        public void onRadioButtonClicked(View view){

        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()){
            case R.id.male:
                if(checked)
                    uGender = "Male";
                break;
            case R.id.female:
                if(checked)
                    uGender = "Female";
                break;
        }
    }

}
