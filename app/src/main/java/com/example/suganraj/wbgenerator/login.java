package com.example.suganraj.wbgenerator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

/**
 * Created by Suganraj on 04-01-2018.
 */

public class login extends AppCompatActivity {
    Button login;
    TextView newReg;
    EditText aadhar, password;
    String aadhar_str = "";
    String password_str = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        aadhar = (EditText) findViewById(R.id.aadhar);
        password = (EditText) findViewById(R.id.password);

        login = (Button) findViewById(R.id.login);
        newReg = (TextView) findViewById(R.id.new_registration);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }

                if (connected) {
                    progressDialog = new ProgressDialog(view.getContext());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    aadhar_str = aadhar.getText().toString();
                    password_str = password.getText().toString();

                    if (aadhar_str.equals("admin")) {

                        if (password_str.equals("1234")) {
                            Toast.makeText(getApplicationContext(), "Welcome admin....:)", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent i = new Intent(getApplicationContext(), AdminView.class);
                            i.putExtra("from", aadhar_str);
                            startActivity(i);
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid secret code", Toast.LENGTH_SHORT).show();
                        }

                    } else {


                        Firebase mRef = new Firebase("https://waterbillgenerator.firebaseio.com/aadhar/");

                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Map<String, String> map = dataSnapshot.getValue(Map.class);

                                if (map.containsValue(aadhar_str)) {

                                    Firebase mRef = new Firebase("https://waterbillgenerator.firebaseio.com/accounts/" + aadhar_str);

                                    mRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String pass = dataSnapshot.getValue(String.class);
                                            if (pass.equals(password_str)) {
                                                Toast.makeText(getApplicationContext(), "Welcome......:)", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(getApplicationContext(), UserView.class);
                                                i.putExtra("aadhar", aadhar_str);

                                                progressDialog.dismiss();
                                                startActivity(i);
                                                finish();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError firebaseError) {

                                        }
                                    });

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Check your Aadhar number...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your internet connection..", Toast.LENGTH_SHORT).show();
                }


            }
        });

        newReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
                finish();

            }
        });

    }
}
