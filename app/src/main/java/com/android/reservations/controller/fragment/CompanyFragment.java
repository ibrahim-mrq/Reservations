package com.android.reservations.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.reservations.R;
import com.android.reservations.controller.adapter.CompanyAdapter;
import com.android.reservations.model.Company;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CompanyFragment extends Fragment {

    private FirebaseFirestore db;
    private RecyclerView rv;
    private CompanyAdapter adapter;
    private TextView tv_empty;
    private TextView tv_name, tv_email, tv_phone, tv_open, tv_close, tv_location;
    private String phone, email;
    private LinearLayout layout;
    private BottomSheetBehavior bottomSheetBehavior;
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_company, container, false);

        initView(root);
        getCompanyData();

        return root;

    }

    private void initView(View root) {
        db = FirebaseFirestore.getInstance();

        searchView = root.findViewById(R.id.search_searchView);
        rv = root.findViewById(R.id.company_rv);
        tv_empty = root.findViewById(R.id.company_tv_empty);
        layout = root.findViewById(R.id.layout_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        tv_name = root.findViewById(R.id.sheet_header_name);
        tv_email = root.findViewById(R.id.sheet_tv_email);
        tv_phone = root.findViewById(R.id.sheet_tv_phone);
        tv_open = root.findViewById(R.id.sheet_tv_open);
        tv_close = root.findViewById(R.id.sheet_tv_close);
        tv_location = root.findViewById(R.id.sheet_tv_location);

        tv_phone.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            Uri uri = Uri.parse("tel:" + phone);
            intent.setData(uri);
            startActivity(intent);
        });
        tv_email.setOnClickListener(v -> {
            String[] r = email.split(",");
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, r);
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "Hi,\n");
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "choose email"));
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void getCompanyData() {
        db.collection("Company").addSnapshotListener((query, e) -> {
            ArrayList<Company> list = new ArrayList<>();
            if (query != null && query.size() > 0) {
                for (QueryDocumentSnapshot document : query) {
                    Company company = document.toObject(Company.class);
                    list.add(company);
                }
                if (list.isEmpty()) {
                    tv_empty.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                } else {
                    tv_empty.setVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                    adapter = new CompanyAdapter(list, getContext(), model -> {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        phone = model.getPhone();
                        email = model.getEmail();
                        tv_name.setText(model.getName());
                        tv_email.setText(getResources().getString(R.string.email) + " : " + model.getEmail());
                        tv_open.setText(getResources().getString(R.string.open) + " : " + model.getOpen());
                        tv_close.setText(getResources().getString(R.string.close) + " : " + model.getClose());
                        tv_phone.setText(getResources().getString(R.string.phone) + " : " + model.getPhone());
                        tv_location.setText(getResources().getString(R.string.location) + " : " + model.getLocation());
                    });
                    rv.setAdapter(adapter);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                }

            }
        });
    }
}