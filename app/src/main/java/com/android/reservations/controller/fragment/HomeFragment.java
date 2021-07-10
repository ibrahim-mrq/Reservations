package com.android.reservations.controller.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.reservations.R;
import com.android.reservations.controller.adapter.AppointmentAdapter;
import com.android.reservations.model.Appointment;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private View root;
    private CompactCalendarView calendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM - yyyy", Locale.ENGLISH);
    private RecyclerView rv;
    private Event event;
    private ImageView img_hide;
    private TextView tv_month, tv_empty;
    private FirebaseFirestore db;
    private ArrayList<Appointment> list;
    private String token;
    private int loginType;
    private boolean shouldShow = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);

        return root;

    }

    private void initView(View root) {
        db = FirebaseFirestore.getInstance();
        token = Hawk.get("id");

        calendar = root.findViewById(R.id.calender);
        img_hide = root.findViewById(R.id.home_img_hide);
        tv_month = root.findViewById(R.id.home_tv_month);
        tv_empty = root.findViewById(R.id.home_tv_empty);
        rv = root.findViewById(R.id.home_rv);
        calendar.setUseThreeLetterAbbreviation(false);
        calendar.setLocale(TimeZone.getDefault(), Locale.ENGLISH);
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);

        tv_empty.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);

        Date currentTime = Calendar.getInstance().getTime();
        tv_month.setText(dateFormatMonth.format(currentTime));
        img_hide.setOnClickListener(v -> {
            if (!calendar.isAnimating()) {
                if (shouldShow) {
                    calendar.showCalendar();
                    img_hide.setImageResource(R.drawable.ic_arrow_up);
                } else {
                    calendar.hideCalendar();
                    img_hide.setImageResource(R.drawable.ic_arrow_down);
                }
                shouldShow = !shouldShow;
            }
        });
        if (Hawk.get("loginType", 1) == 1) {
            getDataCalendar("uId");
            loginType = 1;
        }
        if (Hawk.get("loginType", 1) == 2) {
            getDataCalendar("cId");
            loginType = 2;
        }


    }

    private void getDataCalendar(String type) {
        db.collection("Appointment")
                .whereEqualTo(type, token)
                .addSnapshotListener((document, e) -> {
                    list = new ArrayList<>();
                    if (document != null && document.size() > 0) {
                        for (QueryDocumentSnapshot snapshots : document) {
                            Appointment appointment = snapshots.toObject(Appointment.class);
                            list.add(appointment);
                        }
                        calendar.removeAllEvents();
                        for (int i = 0; i < list.size(); i++) {
                            event = new Event(Color.WHITE, list.get(i).getDate()
                                    , list.get(i).getId() + " # "
                                    + list.get(i).getuName() + " # "
                                    + list.get(i).getuId() + " # "
                                    + list.get(i).getuPhone() + " # "
                                    + list.get(i).getuLocation() + " # "
                                    + list.get(i).getcName() + " # "
                                    + list.get(i).getcDesc() + " # "
                                    + list.get(i).getcLocation() + " # "
                                    + list.get(i).getcPhone() + " # "
                                    + list.get(i).getcId() + " # "
                                    + list.get(i).getTime() + " # "
                                    + list.get(i).getDf()
                            );
                            calendar.addEvent(event);
                        }

                        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                            @Override
                            public void onDayClick(Date dateClicked) {
                                List<Event> events = calendar.getEvents(dateClicked);
                                if (calendar.getEvents(dateClicked).isEmpty()) {
                                    tv_empty.setVisibility(View.VISIBLE);
                                    rv.setVisibility(View.GONE);
                                } else {
                                    tv_empty.setVisibility(View.GONE);
                                    rv.setVisibility(View.VISIBLE);
                                    ArrayList<Appointment> reservations = new ArrayList<>();
                                    AppointmentAdapter adapter =
                                            new AppointmentAdapter(reservations, loginType, getContext());
                                    DateFormat df =
                                            new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
                                    for (int i = 0; i < events.size(); i++) {
                                        String[] x = events.get(i).getData().toString().split("#");
                                        reservations.add(new Appointment(
                                                x[0],
                                                x[1],
                                                x[2],
                                                x[3],
                                                x[4],
                                                x[5],
                                                x[6],
                                                x[7],
                                                x[8],
                                                x[9],
                                                x[10],
                                                df.format(dateClicked)
                                        ));
                                    }
                                    rv.setAdapter(adapter);
                                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    rv.setHasFixedSize(true);
                                }
                            }

                            @Override
                            public void onMonthScroll(Date firstDayOfNewMonth) {
                                tv_month.setText(dateFormatMonth.format(firstDayOfNewMonth));
                            }
                        });

                    }
                });
    }

}