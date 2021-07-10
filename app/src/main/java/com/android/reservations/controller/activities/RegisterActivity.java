package com.android.reservations.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.reservations.R;
import com.android.reservations.helper.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends BaseActivity {

    private TextInputEditText registerEtUserName, registerEtEmail,
            registerEtPhone, registerEtPassword, registerEtLocation, registerEtOpen, registerEtClose;
    private TextInputLayout register_tv_gender;
    private ProgressBar progressBar;
    private AutoCompleteTextView registerEtGender;
    private LinearLayout socialMedia_login, register_linear_time;
    private Button registerBtnLogin;
    private ImageView img_back;
    private int user_type;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.includeAppbar_tv_welcome1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

    }

    private void initView() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        user_type = intent.getIntExtra("user_type", -1);

        registerEtLocation = findViewById(R.id.register_et_location);
        registerEtOpen = findViewById(R.id.register_et_open);
        registerEtClose = findViewById(R.id.register_et_close);

        registerEtUserName = findViewById(R.id.register_et_userName);
        registerEtEmail = findViewById(R.id.register_et_email);
        registerEtPhone = findViewById(R.id.register_et_phone);
        registerEtPassword = findViewById(R.id.register_et_password);
        registerEtGender = findViewById(R.id.register_et_gender);
        register_tv_gender = findViewById(R.id.register_tv_gender);
        registerBtnLogin = findViewById(R.id.register_btn_login);
        img_back = findViewById(R.id.register_img_back);
        progressBar = findViewById(R.id.register_progressBar);
        progressBar.setVisibility(View.GONE);
        socialMedia_login = findViewById(R.id.socialMedia_login);
        register_linear_time = findViewById(R.id.register_linear_time);

        switch (user_type) {
            case 1:
                register_tv_gender.setVisibility(View.VISIBLE);
                register_linear_time.setVisibility(View.GONE);
                registerEtLocation.setVisibility(View.GONE);
                getDataGender();
                break;
            case 2:
                register_tv_gender.setVisibility(View.GONE);
                register_linear_time.setVisibility(View.VISIBLE);
                registerEtLocation.setVisibility(View.VISIBLE);
                break;
        }

        img_back.setOnClickListener(v -> finish());

        registerBtnLogin.setOnClickListener(v -> {
            switch (user_type) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    registerCompany();
                    break;
            }
        });

    }

    private void registerUser() {
        if (isNotEmpty(registerEtUserName)
                && isNotEmpty(registerEtEmail)
                && isNotEmpty(registerEtPhone)
                && isNotEmpty(registerEtPassword)
                && isNotEmpty2(registerEtGender)
                && isValidEmail(registerEtEmail)
        ) {
            if (registerEtPassword.getText().toString().trim().length() >= 6) {
                enableElements(false);
                mAuth.createUserWithEmailAndPassword(
                        registerEtEmail.getText().toString().trim(),
                        registerEtPassword.getText().toString().trim()
                ).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final String Uid = mAuth.getUid() + "";
                        Map<String, Object> map = new HashMap<>();
                        Hawk.put("isLogin", true);
                        map.put("id", Uid);
                        map.put("name", getText(registerEtUserName));
                        map.put("email", getText(registerEtEmail));
                        map.put("phone", getText(registerEtPhone));
                        map.put("password", getText(registerEtPassword));
                        map.put("gender", registerEtGender.getText().toString());
                        db.collection("User").document(Uid).set(map);
                        Hawk.put("loginType", 1);
                        Hawk.put("id", Uid);
                        Hawk.put("name", getText(registerEtUserName));
                        Hawk.put("email", getText(registerEtEmail));
                        Hawk.put("phone", getText(registerEtPhone));
                        Hawk.put("password", getText(registerEtPassword));
                        Hawk.put("gender", registerEtGender.getText().toString());
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(RegisterActivity.this,
                                getResources().getString(R.string.successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    enableElements(true);
                    Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                enableElements(true);
                Toast.makeText(RegisterActivity.this,
                        getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerCompany() {
        if (isNotEmpty(registerEtUserName)
                && isNotEmpty(registerEtEmail)
                && isNotEmpty(registerEtPhone)
                && isNotEmpty(registerEtPassword)
                && isNotEmpty(registerEtLocation)
                && isNotEmpty(registerEtOpen)
                && isNotEmpty(registerEtClose)
                && isValidEmail(registerEtEmail)
        ) {
            if (registerEtPassword.getText().toString().trim().length() >= 6) {
                enableElements(false);
                mAuth.createUserWithEmailAndPassword(
                        registerEtEmail.getText().toString().trim(),
                        registerEtPassword.getText().toString().trim()
                ).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final String Uid = mAuth.getUid() + "";
                        Map<String, Object> map = new HashMap<>();
                        Hawk.put("isLogin", true);
                        map.put("id", Uid);
                        map.put("name", getText(registerEtUserName));
                        map.put("email", getText(registerEtEmail));
                        map.put("phone", getText(registerEtPhone));
                        map.put("password", getText(registerEtPassword));
                        map.put("open", getText(registerEtOpen));
                        map.put("close", getText(registerEtClose));
                        map.put("location", getText(registerEtLocation));
                        db.collection("Company").document(Uid).set(map);
                        Hawk.put("loginType", 2);
                        Hawk.put("id", Uid);
                        Hawk.put("name", getText(registerEtUserName));
                        Hawk.put("email", getText(registerEtEmail));
                        Hawk.put("phone", getText(registerEtPhone));
                        Hawk.put("password", getText(registerEtPassword));
                        Hawk.put("open", getText(registerEtOpen));
                        Hawk.put("close", getText(registerEtClose));
                        Hawk.put("location", getText(registerEtLocation));
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(RegisterActivity.this,
                                getResources().getString(R.string.successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    enableElements(true);
                    Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                enableElements(true);
                Toast.makeText(RegisterActivity.this,
                        getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableElements(boolean enable) {
        registerBtnLogin.setEnabled(enable);
        if (!enable) {
            registerBtnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_disabled));
            progressBar.setVisibility(View.VISIBLE);
        } else {
            registerBtnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_buy));
            progressBar.setVisibility(View.INVISIBLE);
        }
        img_back.setEnabled(enable);
        registerEtUserName.setEnabled(enable);
        registerEtEmail.setEnabled(enable);
        registerEtPhone.setEnabled(enable);
        registerEtPassword.setEnabled(enable);
        registerEtGender.setEnabled(enable);
        socialMedia_login.setEnabled(enable);
    }

    private void getDataGender() {
        ArrayList<String> listGender = new ArrayList<>();
        listGender.add(getResources().getString(R.string.male));
        listGender.add(getResources().getString(R.string.female));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listGender);
        registerEtGender.setAdapter(adapter);
        registerEtGender.setInputType(InputType.TYPE_NULL);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private String getText(TextInputEditText text) {
        return Objects.requireNonNull(text.getText()).toString().trim();
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