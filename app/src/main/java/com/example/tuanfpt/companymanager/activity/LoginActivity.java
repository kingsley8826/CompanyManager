package com.example.tuanfpt.companymanager.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.SpinnerAdapter;
import com.example.tuanfpt.companymanager.models.Account;
import com.example.tuanfpt.companymanager.network.RetrofitContext;
import com.example.tuanfpt.companymanager.utilities.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    @BindView(R.id.spinner_department)
    Spinner departmentSpinner;
    @BindView(R.id.spinner_username)
    Spinner usernameSpinner;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.txt_error)
    TextView txtError;

    String[] departmentItems = new String[]{"P.Điều Hành", "P.Bảo Dưỡng"};
    String[] usernameItems = new String[]{"admin_dh1", "admin_dh2"};

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = sharedPreferences.getString(Constant.USERNAME, "");
        if(!username.equals("")){
            gotoHome();
        }
    }

    private void init() {
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(Constant.COMPANY_KEY, MODE_PRIVATE);
        filterSpinner(departmentSpinner, departmentItems, Constant.DEPARTMENT);
        filterSpinner(usernameSpinner, usernameItems,Constant.USERNAME);
        getUsernameByDepartment(departmentItems[0]);
    }

    private void addListener() {
        departmentSpinner.setOnItemSelectedListener(this);
        btnLogin.setOnClickListener(this);
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtError.getVisibility() == View.VISIBLE) {
                    txtError.setVisibility(View.GONE);
                }
            }
        });
    }

    private void filterSpinner(Spinner spinner, String[] data, String type) {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.custom_spinner, data, type);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String department = departmentItems[position];
        getUsernameByDepartment(department);
    }

    private void getUsernameByDepartment(final String department) {

        RetrofitContext.getUsernameByDepartment(department).enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                if (response.code() == 200) {
                    usernameItems = response.body();
                    filterSpinner(usernameSpinner, usernameItems, Constant.USERNAME);
                } else {
                    getUsernameByDepartment(department);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
                getUsernameByDepartment(department);
            }
        });
    }

    private void login(String username,String password){
        RetrofitContext.callLoginApi(new Account(username,password)).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(@NonNull Call<Account> call, @NonNull Response<Account> response) {
                if (response.code() == 200) {
                    Account account = response.body();
                    saveToPreference(Constant.USERNAME, account.getUserName());
                    gotoHome();
                } else {
                    txtError.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
                    txtError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Account> call, @NonNull Throwable t) {
                txtError.setText("Lỗi mạng, vui lòng kiểm tra lại đường truyền!");
                txtError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gotoHome() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void saveToPreference(String key, String text){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, text);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String username = usernameItems[usernameSpinner.getSelectedItemPosition()];
                String password = edtPassword.getText().toString();
                login(username, password);
                break;
        }
    }
}
