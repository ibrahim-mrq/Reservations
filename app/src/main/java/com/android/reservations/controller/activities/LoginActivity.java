package com.android.reservations.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.reservations.R;
import com.android.reservations.helper.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends BaseActivity {

    private TextInputEditText loginEtEmail, loginEtPassword;
    private AppCompatButton loginBtnLogin;
    private ProgressBar progressBar;
    private TextView loginTvRest, loginTvRegister;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PopupWindow window1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Hawk.contains("isLogin")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        initView();

    }

    private void initView() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginEtEmail = findViewById(R.id.login_et_email);
        loginEtPassword = findViewById(R.id.login_et_password);
        loginBtnLogin = findViewById(R.id.login_btn_login);
        loginTvRest = findViewById(R.id.login_tv_rest);
        loginTvRegister = findViewById(R.id.login_tv_register);
        progressBar = findViewById(R.id.login_progressBar);

        loginTvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class))
        );

        loginBtnLogin.setOnClickListener(v -> {
            if (isNotEmpty(loginEtEmail)
                    && isNotEmpty(loginEtPassword)
                    && isValidEmail(loginEtEmail)
            ) {
                if (loginEtPassword.getText().toString().trim().length() >= 6) {
                    ShowUserTypeMessage();
                } else {
                    enableElements(true);
                    Toast.makeText(LoginActivity.this,
                            getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void login(int type) {
        enableElements(false);
        mAuth.signInWithEmailAndPassword(
                loginEtEmail.getText().toString().trim(),
                loginEtPassword.getText().toString().trim()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (type == 1)
                    getData("User", mAuth.getUid());
                else if (type == 2)
                    getData("Company", mAuth.getUid());
            }
        }).addOnFailureListener(e -> {
            enableElements(true);
            Toast.makeText(this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void enableElements(boolean enable) {
        loginBtnLogin.setEnabled(enable);
        if (!enable) {
            loginBtnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_disabled));
            progressBar.setVisibility(View.VISIBLE);
        } else {
            loginBtnLogin.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_buy));
            progressBar.setVisibility(View.GONE);
        }
        loginEtEmail.setEnabled(enable);
        loginEtPassword.setEnabled(enable);
        loginTvRest.setEnabled(enable);
        loginTvRegister.setEnabled(enable);
    }

    public void ShowUserTypeMessage() {
        try {
            final ConstraintLayout customer, producer;
            LayoutInflater inflater = (LayoutInflater)
                    LoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_signup_type, null);
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            window1 = new PopupWindow(layout, width, height, true);
            window1.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window1.setOutsideTouchable(true);
            window1.showAtLocation(layout, Gravity.CENTER, 0, 0);
            customer = layout.findViewById(R.id.customer_register);
            customer.setOnClickListener(v -> {
                login(1);
                window1.dismiss();
            });
            producer = layout.findViewById(R.id.producer_register);
            producer.setOnClickListener(v -> {
                login(2);
                window1.dismiss();
            });
        } catch (Exception e) {
            Log.e("ERROR1", e.getMessage());
        }
    }

    private void getData(String type, String uId) {
        db.collection(type).document(uId).addSnapshotListener((value, error) -> {
            Hawk.put("isLogin", true);
            Hawk.put("id", value.getString("id"));
            Hawk.put("name", value.getString("name"));
            Hawk.put("email", value.getString("email"));
            Hawk.put("phone", value.getString("phone"));
            Hawk.put("password", value.getString("password"));
            if (type.equals("User")) {
                Hawk.put("gender", value.getString("gender"));
                Hawk.put("loginType", 1);
            } else if (type.equals("Company")) {
                Hawk.put("loginType", 2);
                Hawk.put("open", value.getString("open"));
                Hawk.put("close", value.getString("close"));
                Hawk.put("location", value.getString("location"));
            }
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}