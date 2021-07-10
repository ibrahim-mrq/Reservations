package com.android.reservations.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.reservations.R;
import com.android.reservations.controller.activities.EditActivity;
import com.android.reservations.controller.interfaces.CompanyInterface;
import com.android.reservations.model.Appointment;
import com.android.reservations.model.Company;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> implements Filterable {

    private ArrayList<Company> list;
    private List<Company> exampleListFull;
    private Context mContext;
    private CompanyInterface anInterface;

    public CompanyAdapter(ArrayList<Company> list, Context context) {
        this.list = list;
        this.mContext = context;
        exampleListFull = new ArrayList<>(list);
    }

    public CompanyAdapter(ArrayList<Company> list, Context context, CompanyInterface anInterface) {
        this.list = list;
        this.mContext = context;
        this.anInterface = anInterface;
        exampleListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        final Company model = list.get(position);
        holder.Bind(model);

        holder.imgAdd.setOnClickListener(v ->
                mContext.startActivity(new Intent(mContext, EditActivity.class)
                        .putExtra("type", "add"))
        );

        holder.imgInfo.setOnClickListener(v -> {
            anInterface.onClick(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CompanyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private ImageView imgInfo, imgAdd;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.customCompany_tv_name);
            imgAdd = itemView.findViewById(R.id.customCompany_img_add);
            imgInfo = itemView.findViewById(R.id.customCompany_img_info);
        }

        public void Bind(Company model) {
            tv_name.setText(model.getName());
        }

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Company> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Company item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
