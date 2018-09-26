package com.example.tuanfpt.companymanager.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.CompanyAdapter;
import com.example.tuanfpt.companymanager.adapter.OnCompanyItemSelected;
import com.example.tuanfpt.companymanager.models.Company;
import com.example.tuanfpt.companymanager.models.Department;
import com.example.tuanfpt.companymanager.network.RetrofitContext;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnCompanyItemSelected {

    @BindView(R.id.edtCompany)
    EditText edtCompany;
    @BindView(R.id.rvCompanies)
    RecyclerView rvCompanies;

    private CompanyAdapter companyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCompany();
    }

    private void init() {
        ButterKnife.bind(this);
        rvCompanies.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addListener() {
        edtCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                companyAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void updateRecyclerView(ArrayList<Company> companies){
        companyAdapter = new CompanyAdapter(companies, this);
        rvCompanies.setAdapter(companyAdapter);
    }

    private void getAllCompany(){
        RetrofitContext.getAllCompany().enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call, @NonNull Response<ArrayList<Company>> response) {
                if (response.code() == 200) {
                    ArrayList<Company> companies = response.body();
                    if(companies == null) return;
                    updateRecyclerView(companies);
                } else {
                     showToast(getString(R.string.internet_error));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                showToast(getString(R.string.internet_error));
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(Company company) {

        edtCompany.setText("");
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constant.COMPANY_ID, company.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
