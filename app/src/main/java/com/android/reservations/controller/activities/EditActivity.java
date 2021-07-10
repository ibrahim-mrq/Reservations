package com.android.reservations.controller.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.android.reservations.R;
import com.android.reservations.helper.BaseActivity;
import com.android.reservations.model.Appointment;
import com.android.reservations.model.Company;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class EditActivity extends BaseActivity {

    private Toolbar toolbar;
    private CompactCalendarView calender;
    private TextInputEditText editEtCompany;
    private TextInputEditText editEtTime;
    private TextInputEditText editEtDesc;
    private AppCompatButton editBtnLogin;
    private TextView tv_month;
    private ProgressBar progressBar;

    private ArrayList<Company> list = new ArrayList<>();
    private ArrayList<String> company = new ArrayList<>();
    private ArrayList<String> companyId = new ArrayList<>();
    private FirebaseFirestore db;
    private long date = 0;
    private Appointment model;
    private String id;
    private String cId;
    private String type;
    private String location;
    private String cPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        initView();
        getCompanyData();

    }

    private void initView() {
        db = FirebaseFirestore.getInstance();
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        toolbar = findViewById(R.id.edit_toolbar);
        toolbar.setTitle(getResources().getString(R.string.addBooking));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.progressBar);
        tv_month = findViewById(R.id.edit_tv_month);
        calender = findViewById(R.id.calender);
        editEtCompany = findViewById(R.id.edit_et_company);
        editEtTime = findViewById(R.id.edit_et_time);
        editEtDesc = findViewById(R.id.edit_et_desc);
        editBtnLogin = findViewById(R.id.edit_btn_login);

        editEtTime.setOnClickListener(v -> dialogTime());
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.ENGLISH);

//        calender.setMinimumDate();


        calender.setUseThreeLetterAbbreviation(false);
        calender.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        calender.setFirstDayOfWeek(Calendar.SATURDAY);
        calender.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                date = dateClicked.getTime();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tv_month.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        if (type.equals("edit")) {
            model = ((Appointment) getIntent().getSerializableExtra("Appointment"));
            date = model.getDate();
            editEtTime.setText(model.getTime());
            editEtCompany.setText(model.getcName());
            editEtDesc.setText(model.getcDesc());
            location = model.getcLocation();
            cPhone = model.getcPhone();
            cId = model.getcId();
            editBtnLogin.setText("Edit");
        }

        editBtnLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (type.equals("edit")) {
                editData();
            }
            if (type.equals("add")) {
                pushData();
            }
        });
    }

    private void pushData() {
        if (isNotEmpty(editEtCompany)
                && isNotEmpty(editEtTime)
                && isNotEmpty(editEtDesc)
                && date != 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", "id");
            map.put("uName", Hawk.get("name"));
            map.put("uId", Hawk.get("id"));
            map.put("uPhone", Hawk.get("phone"));
            map.put("uLocation", "location");
            map.put("cName", editEtCompany.getText().toString());
            map.put("cDesc", editEtDesc.getText().toString());
            map.put("cLocation", location);
            map.put("cPhone", cPhone);
            map.put("cId", cId);
            map.put("date", date);
            map.put("time", editEtTime.getText().toString());

            db.collection("Appointment")
                    .add(map)
                    .addOnSuccessListener(documentReference -> {
                        DocumentReference noteRef = db.collection("Appointment")
                                .document(documentReference.getId());
                        noteRef.update("id", documentReference.getId() + "");
                        Toast.makeText(this, ""
                                        + getResources().getString(R.string.successful),
                                Toast.LENGTH_SHORT).show();
                        editEtCompany.setText("");
                        editEtDesc.setText("");
                        editEtTime.setText("");
                        date = 0;
                        cId = "";
                        progressBar.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    });
        } else progressBar.setVisibility(View.GONE);

    }

    private void editData() {
        if (isNotEmpty(editEtCompany)
                && isNotEmpty(editEtTime)
                && isNotEmpty(editEtDesc)
                && date != 0) {
            DocumentReference noteRef = db.collection("Appointment")
                    .document(id);
            noteRef.update("cName", editEtCompany.getText().toString());
            noteRef.update("cDesc", editEtDesc.getText().toString());
            noteRef.update("cLocation", location);
            noteRef.update("cPhone", cPhone);
            noteRef.update("cId", cId);
            noteRef.update("date", date);
            noteRef.update("time", editEtTime.getText().toString());

            Toast.makeText(this, ""
                            + getResources().getString(R.string.successful),
                    Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        } else progressBar.setVisibility(View.VISIBLE);
    }

    private void getCompanyData() {
        db.collection("Company").addSnapshotListener((queryDocumentSnapshots, e) -> {
            list = new ArrayList<>();
            if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() > 0) {
                for (QueryDocumentSnapshot snapshots : queryDocumentSnapshots) {
                    Company companies = snapshots.toObject(Company.class);
                    list.add(companies);
                }
                for (int i = 0; i < list.size(); i++) {
                    company.add(list.get(i).getName());
                    companyId.add(list.get(i).getId());
                }
//                Collections.sort(company);
                editEtCompany.setOnClickListener(v -> {
                    dialog(getResources().getString(R.string.selectCompanyName), company);
                });
            }
        });
    }

    private void dialog(String title, ArrayList<String> lists) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_item_dialog);
        TextView tv = dialog.findViewById(R.id.customDialog_tv);
        tv.setText(title);
        ListView lv = dialog.findViewById(R.id.customDialog_listView);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                R.layout.custom_tv_dialog, R.id.customTvDialog_tv, lists);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            editEtCompany.setText(lists.get(position));
            cId = companyId.get(position);
            location = list.get(position).getLocation();
            cPhone = list.get(position).getPhone();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void dialogTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(EditActivity.this,
                (timePicker, selectedHour, selectedMinute) -> {
                    editEtTime.setText("" + selectedHour + ":" + selectedMinute);
                }, hour, minute, false);
        mTimePicker.show();
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