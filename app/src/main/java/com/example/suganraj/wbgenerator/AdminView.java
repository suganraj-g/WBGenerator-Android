package com.example.suganraj.wbgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Suganraj on 04-01-2018.
 */

public class AdminView extends AppCompatActivity {
    ImageView profile, invoice, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview);

        profile = (ImageView) findViewById(R.id.profile);
        invoice = (ImageView) findViewById(R.id.invoice);
        logout = (ImageView) findViewById(R.id.user_logout_btn);

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminBill.class);
                startActivity(i);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AdminProfile.class);
                startActivity(i);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), login.class);
                startActivity(i);
                finish();
            }
        });
    }
}