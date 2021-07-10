package com.android.reservations.controller.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.reservations.R;
import com.android.reservations.controller.fragment.CompanyFragment;
import com.android.reservations.controller.fragment.HomeFragment;
import com.android.reservations.controller.fragment.ProfileFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.hawk.Hawk;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;

    public static TextView tv_welcome;
    public static BadgeDrawable badge;
    public static Context context;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        initView();
        bottomNavigation();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tv_welcome = findViewById(R.id.includeMainAppbar_tv_welcome);
        tv_welcome.setText(Hawk.get("name", "Mr/Mrs"));
        fab = findViewById(R.id.main_float);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, EditActivity.class)
                    .putExtra("type", "add"));
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void bottomNavigation() {

        bottomNavigationView.getMenu().clear(); //clear old inflated items.
        if (Hawk.get("loginType", 1) == 2) {
            bottomNavigationView.inflateMenu(R.menu.bottom_menu_company);
        } else {
            bottomNavigationView.inflateMenu(R.menu.bottom_menu);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nav_account:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.nav_company:
                    replaceFragment(new CompanyFragment());
                    break;
            }
            return true;
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}