package com.android.reservations.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.reservations.R;
import com.android.reservations.controller.activities.LoginActivity;
import com.android.reservations.controller.activities.MainActivity;
import com.android.reservations.controller.activities.RegisterActivity;
import com.android.reservations.helper.BaseFragment;
import com.facebook.login.Login;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends BaseFragment {

    private TextView fAccountTvLogout;
    private TextInputEditText profileEtUserName;
    private TextInputEditText profileEtEmail;
    private TextInputEditText profileEtPhone;
    private TextInputEditText profileEtPassword;
    private AutoCompleteTextView profileEtGender;
    private TextInputLayout profile_tv_gender;
    private LinearLayout profileLinearTime;
    private TextInputEditText profileEtOpen;
    private TextInputEditText profileEtClose;
    private TextInputEditText profileEtLocation;
    private ProgressBar profileProgressBar;
    private Button profileBtnUpdate;
    private FirebaseFirestore db;
    private String uId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        db = FirebaseFirestore.getInstance();

        initView(root);

        return root;
    }

    private void initView(View root) {
        fAccountTvLogout = root.findViewById(R.id.fAccount_tv_logout);
        profileEtUserName = root.findViewById(R.id.profile_et_userName);
        profileEtEmail = root.findViewById(R.id.profile_et_email);
        profileEtPhone = root.findViewById(R.id.profile_et_phone);
        profileEtPassword = root.findViewById(R.id.profile_et_password);
        profileEtGender = root.findViewById(R.id.profile_et_gender);
        profile_tv_gender = root.findViewById(R.id.profile_tv_gender);
        profileLinearTime = root.findViewById(R.id.profile_linear_time);
        profileEtOpen = root.findViewById(R.id.profile_et_open);
        profileEtClose = root.findViewById(R.id.profile_et_close);
        profileEtLocation = root.findViewById(R.id.profile_et_location);
        profileProgressBar = root.findViewById(R.id.profile_progressBar);
        profileBtnUpdate = root.findViewById(R.id.profile_btn_update);

        fAccountTvLogout.setOnClickListener(v -> {
            Hawk.deleteAll();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        int loginType = Hawk.get("loginType");
        profileEtUserName.setText(Hawk.get("name"));
        profileEtEmail.setText(Hawk.get("email"));
        profileEtPhone.setText(Hawk.get("phone"));
        profileEtPassword.setText(Hawk.get("password"));
        uId = Hawk.get("id");

        if (loginType == 1) {
            profileLinearTime.setVisibility(View.GONE);
            profileEtLocation.setVisibility(View.GONE);
            profile_tv_gender.setVisibility(View.VISIBLE);
            profileEtGender.setText(Hawk.get("gender"));
            getDataGender();
        }
        if (loginType == 2) {
            profileLinearTime.setVisibility(View.VISIBLE);
            profileEtLocation.setVisibility(View.VISIBLE);
            profile_tv_gender.setVisibility(View.GONE);
            profileEtOpen.setText(Hawk.get("open"));
            profileEtClose.setText(Hawk.get("close"));
            profileEtLocation.setText(Hawk.get("location"));
        }

        profileBtnUpdate.setOnClickListener(v -> {
            if (loginType == 1) {
                updateUser();
            }
            if (loginType == 2) {
                updateCompany();
            }
        });
    }

    private void updateUser() {
        if (isNotEmpty(profileEtUserName)
                && isNotEmpty(profileEtEmail)
                && isNotEmpty(profileEtPhone)
                && isNotEmpty(profileEtPassword)
                && isNotEmpty2(profileEtGender)
                && isValidEmail(profileEtEmail)
        ) {
            if (profileEtPassword.getText().toString().trim().length() >= 6) {
                enableElements(false);
                final DocumentReference noteRef = db.collection("User").document(uId);
                Map<String, Object> map = new HashMap<>();
                map.put("name", getText(profileEtUserName));
                map.put("email", getText(profileEtEmail));
                map.put("phone", getText(profileEtPhone));
                map.put("password", getText(profileEtPassword));
                map.put("gender", profileEtGender.getText().toString());
                noteRef.update(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                getActivity().getResources().getString(R.string.successful),
                                Toast.LENGTH_SHORT).show();
                        Hawk.put("name", getText(profileEtUserName));
                        Hawk.put("email", getText(profileEtEmail));
                        Hawk.put("phone", getText(profileEtPhone));
                        Hawk.put("password", getText(profileEtPassword));
                        Hawk.put("gender", profileEtGender.getText().toString());
                        MainActivity.tv_welcome.setText(Hawk.get("name", "Mr/Mrs"));
                        enableElements(true);
                    }
                });
            } else {
                enableElements(true);
                Toast.makeText(getContext(),
                        getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateCompany() {
        if (isNotEmpty(profileEtUserName)
                && isNotEmpty(profileEtEmail)
                && isNotEmpty(profileEtPhone)
                && isNotEmpty(profileEtPassword)
                && isNotEmpty(profileEtClose)
                && isNotEmpty(profileEtOpen)
                && isNotEmpty(profileEtLocation)
                && isValidEmail(profileEtEmail)
        ) {
            if (profileEtPassword.getText().toString().trim().length() >= 6) {
                enableElements(false);
                final DocumentReference noteRef = db.collection("Company").document(uId);
                Map<String, Object> map = new HashMap<>();
                map.put("name", getText(profileEtUserName));
                map.put("email", getText(profileEtEmail));
                map.put("phone", getText(profileEtPhone));
                map.put("password", getText(profileEtPassword));
                map.put("close", getText(profileEtClose));
                map.put("open", getText(profileEtOpen));
                map.put("location", getText(profileEtLocation));
                noteRef.update(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                getActivity().getResources().getString(R.string.successful),
                                Toast.LENGTH_SHORT).show();
                        Hawk.put("name", getText(profileEtUserName));
                        Hawk.put("email", getText(profileEtEmail));
                        Hawk.put("phone", getText(profileEtPhone));
                        Hawk.put("password", getText(profileEtPassword));
                        Hawk.put("close", getText(profileEtClose));
                        Hawk.put("open", getText(profileEtOpen));
                        Hawk.put("location", getText(profileEtLocation));
                        MainActivity.tv_welcome.setText(Hawk.get("name", "Mr/Mrs"));
                        enableElements(true);
                    }
                });
            } else {
                enableElements(true);
                Toast.makeText(getContext(),
                        getResources().getString(R.string.short_password), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void enableElements(boolean enable) {
        profileBtnUpdate.setEnabled(enable);
        if (!enable) {
            profileBtnUpdate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_btn_disabled));
            profileProgressBar.setVisibility(View.VISIBLE);
        } else {
            profileBtnUpdate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shape_buy));
            profileProgressBar.setVisibility(View.INVISIBLE);
        }
        profileEtUserName.setEnabled(enable);
//        profileEtEmail.setEnabled(enable);
        profileEtPhone.setEnabled(enable);
//        profileEtPassword.setEnabled(enable);
        profileEtGender.setEnabled(enable);
        profileEtLocation.setEnabled(enable);
        profileEtClose.setEnabled(enable);
        profileEtOpen.setEnabled(enable);
    }

    private void getDataGender() {
        ArrayList<String> listGender = new ArrayList<>();
        listGender.add(getResources().getString(R.string.male));
        listGender.add(getResources().getString(R.string.female));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listGender);
        profileEtGender.setAdapter(adapter);
        profileEtGender.setInputType(InputType.TYPE_NULL);
    }

    private String getText(TextInputEditText text) {
        return Objects.requireNonNull(text.getText()).toString().trim();
    }
}