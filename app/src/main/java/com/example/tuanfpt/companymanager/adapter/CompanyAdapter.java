package com.example.tuanfpt.companymanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.models.Company;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private ArrayList<Company> companies;
    private ArrayList<Company> cacheCompanies;
    private OnCompanyItemSelected listener;

    public CompanyAdapter(ArrayList<Company> companies, OnCompanyItemSelected listener) {
        this.companies = companies;
        this.listener = listener;
        this.cacheCompanies = companies;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, final int position) {
        holder.bindData(companies.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemSelected(companies.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public void filter(String text) {
        ArrayList<Company> temp = new ArrayList<>();
        for (Company company : cacheCompanies) {
            if (company.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(company);
            }
        }
        updateRecyclerView(temp);
    }

    private void updateRecyclerView(ArrayList<Company> temp) {
        companies = temp;
        notifyDataSetChanged();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {

        CardView itemCompany;
        TextView tvCompany;
        TextView tvMotherCompany;
        TextView tvPhone;
        TextView tvAddress;

        public CompanyViewHolder(View itemView) {
            super(itemView);

            itemCompany = itemView.findViewById(R.id.item_company);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvMotherCompany = itemView.findViewById(R.id.tvMotherCompany);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
        }

        void bindData(Company company) {
            tvCompany.setText(company.getName());
            if (company.getType().equals(Constant.COMPANY_MOTHER)) {
                itemCompany.setCardBackgroundColor(itemView.getResources().getColor(R.color.orange_300));
                tvMotherCompany.setVisibility(View.GONE);
            } else {
                itemCompany.setCardBackgroundColor(itemView.getResources().getColor(R.color.orange_100));
                tvMotherCompany.setText(Html.fromHtml("<u>" + company.getMother() + "</u>"));
            }
            tvPhone.setText(company.getPhone());
            tvAddress.setText(company.getAddress());
        }
    }
}
