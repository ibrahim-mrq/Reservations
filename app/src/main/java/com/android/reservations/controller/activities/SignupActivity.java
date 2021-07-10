package com.android.reservations.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.reservations.R;

public class SignupActivity extends AppCompatActivity {

    private LinearLayout layout_company, layout_customer;
    private ImageView img_back;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        layout_company = findViewById(R.id.chooseLogin_linear_pioneer);
        layout_customer = findViewById(R.id.chooseLogin_linear_customer);
        img_back = findViewById(R.id.chooseLogin_img_back);

        toolbar = findViewById(R.id.includeAppbar_tv_welcome1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_customer.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, RegisterActivity.class)
                        .putExtra("user_type", 1)
                ));

        layout_company.setOnClickListener(v ->
                startActivity(new Intent(SignupActivity.this, RegisterActivity.class)
                        .putExtra("user_type", 2)
                ));

        img_back.setOnClickListener(v -> onBackPressed());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}