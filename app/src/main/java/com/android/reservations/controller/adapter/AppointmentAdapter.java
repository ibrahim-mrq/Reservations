package com.android.reservations.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.reservations.R;
import com.android.reservations.controller.activities.EditActivity;
import com.android.reservations.model.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ReservationsViewHolder> {

    private ArrayList<Appointment> list;
    private Context context;
    private int num;

    public AppointmentAdapter(ArrayList<Appointment> list, int num, Context context) {
        this.list = list;
        this.num = num;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event, parent, false);
        return new ReservationsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationsViewHolder holder, int position) {
        final Appointment model = list.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ReservationsViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name, tv_time_start, tv_address, tv_desc, tv_phone;
        private TextView tv_edit;

        public ReservationsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address = itemView.findViewById(R.id.custom_event_tv_location);
            tv_name = itemView.findViewById(R.id.custom_event_tv_name);
            tv_time_start = itemView.findViewById(R.id.custom_event_tv_time_start);
            tv_desc = itemView.findViewById(R.id.custom_event_tv_desc);
            tv_phone = itemView.findViewById(R.id.custom_event_tv_phone);
            tv_edit = itemView.findViewById(R.id.custom_event_tv_edit);
        }

        public void bind(Appointment model) {
            if (num == 1) {
                tv_name.setText(model.getcName());
                tv_address.setText(model.getcLocation());
                tv_phone.setText(model.getcPhone());
                tv_phone.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    Uri uri = Uri.parse("tel:" + model.getcPhone());
                    intent.setData(uri);
                    context.startActivity(intent);
                });
                tv_edit.setVisibility(View.VISIBLE);
                tv_edit.setOnClickListener(v ->
                        context.startActivity(new Intent(context, EditActivity.class)
                                .putExtra("type", "edit")
                                .putExtra("Appointment", ((Serializable) model))
                        ));
            }
            if (num == 2) {
                tv_edit.setVisibility(View.GONE);
                tv_name.setText(model.getuName());
//                tv_address.setText(model.getuLocation());
                tv_address.setVisibility(View.GONE);
                tv_phone.setText(model.getuPhone());
                tv_phone.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    Uri uri = Uri.parse("tel:" + model.getuPhone());
                    intent.setData(uri);
                    context.startActivity(intent);
                });
            }
            tv_desc.setText(model.getcDesc());
            tv_time_start.setText(model.getTime());

        }
    }

}
