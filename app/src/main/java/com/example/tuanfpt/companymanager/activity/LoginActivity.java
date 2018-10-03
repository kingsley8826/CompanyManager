package com.example.tuanfpt.companymanager.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.example.tuanfpt.companymanager.Manifest;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.SpinnerAdapter;
import com.example.tuanfpt.companymanager.models.Account;
import com.example.tuanfpt.companymanager.models.Department;
import com.example.tuanfpt.companymanager.network.RetrofitContext;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.ArrayList;

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

    ArrayList<String> departmentItems = new ArrayList<>();
    ArrayList<String> usernameItems = new ArrayList<>();

    private String[] listPermissions;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ensurePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = sharedPreferences.getString(Constant.USERNAME, "");
        if (!username.equals("")) {
            gotoHome();
        }
    }

    private void init() {
        ButterKnife.bind(this);
        sharedPreferences = this.getSharedPreferences(Constant.COMPANY_KEY, MODE_PRIVATE);
        filterSpinner(departmentSpinner, departmentItems, Constant.DEPARTMENT);
        filterSpinner(usernameSpinner, usernameItems, Constant.USERNAME);
        getAllDepartment();
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

    private void filterSpinner(Spinner spinner, ArrayList<String> data, String type) {
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, R.layout.custom_spinner, data, type);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String department = departmentItems.get(position);
        getUsernameByDepartment(department);
    }

    private void getAllDepartment() {

        RetrofitContext.getAllDepartment().enqueue(new Callback<ArrayList<Department>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Department>> call, @NonNull Response<ArrayList<Department>> response) {
                if (response.code() == 200) {
                    ArrayList<Department> departments = response.body();
                    if (departments == null) return;
                    for (int i = 0; i < departments.size(); i++) {
                        departmentItems.add(departments.get(i).getName());
                    }
                    getUsernameByDepartment(departmentItems.get(0));
                    filterSpinner(departmentSpinner, departmentItems, Constant.DEPARTMENT);
                } else {
                    showToast("Lỗi đường truyền");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Department>> call, @NonNull Throwable t) {
                showToast("Lỗi đường truyền");
            }
        });
    }

    private void getUsernameByDepartment(final String department) {

        RetrofitContext.getUsernameByDepartment(department).enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if (response.code() == 200) {
                    usernameItems = response.body();
                    filterSpinner(usernameSpinner, usernameItems, Constant.USERNAME);
                } else {
                    showToast("Lỗi đường truyền");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                showToast("Lỗi đường truyền");
            }
        });
    }

    private void login(String username, String password) {
        RetrofitContext.callLoginApi(new Account(username, password)).enqueue(new Callback<Account>() {
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
        Intent intent = new Intent(LoginActivity.this, MotherActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void saveToPreference(String key, String text) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, text);
        editor.apply();
    }

    private void ensurePermission() {
        listPermissions = new String[]{android.Manifest.permission.CAMERA};
        if (!hasPermissions(this, listPermissions)) {
            ActivityCompat.requestPermissions(this, listPermissions, 404);
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 404:
                if (!hasPermissions(LoginActivity.this, listPermissions)) {
                    finish();
                }
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String username = usernameItems.get(usernameSpinner.getSelectedItemPosition());
                String password = edtPassword.getText().toString();
                login(username, password);
                break;
        }
    }
}
