package com.example.suganraj.wbgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Suganraj on 29-12-2017.
 */

public class UserView extends AppCompatActivity{
String aadhar = "";
ImageView profile,invoice,logout;

@Override
    protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.userview);
    profile = (ImageView) findViewById(R.id.profile);
    invoice = (ImageView) findViewById(R.id.invoice);
    logout = (ImageView) findViewById(R.id.user_logout_btn);
    Bundle extra = getIntent().getExtras();
    aadhar = extra.getString("aadhar");
    if(aadhar.equals("")){

    }else{
        Firebase uRef = new Firebase("https://wb-generator.firebaseio.com/storage/"+aadhar+"/name");
        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    logout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),login.class);
            startActivity(intent);
            finish();
        }
    });
    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),UserProfile.class);
            intent.putExtra("aadhar",aadhar);
            intent.putExtra("from","user");
            startActivity(intent);
            finish();
        }
    });
}
}
