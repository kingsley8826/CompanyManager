package com.example.tuanfpt.companymanager.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.adapter.MaintainAdapter;
import com.example.tuanfpt.companymanager.models.Maintain;
import com.example.tuanfpt.companymanager.network.RetrofitContext;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalysisActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String DATE_FORMAT = "%02d/%02d/%d";

    @BindView(R.id.txt_date_from)
    TextView txtDateFrom;
    @BindView(R.id.txt_date_to)
    TextView txtDateTo;

    @BindView(R.id.spinner_mother)
    Spinner spinnerMother;
    @BindView(R.id.spinner_children)
    Spinner spinnerChildren;

    @BindView(R.id.rvMaintains)
    RecyclerView rvMaintains;

    private Calendar calendar;

    private ProgressDialog dialog;
    ArrayList<String> mothers;
    ArrayList<String> children;

    ArrayList<Maintain> maintains;
    MaintainAdapter maintainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        init();
        addListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllMaintain();
    }

    private void init() {
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Đang tìm kiếm...");
        dialog.setCancelable(false);
        dialog.show();

        maintains = new ArrayList<>();

        mothers = new ArrayList<>();
        mothers.add(Constant.ALL);
        children = new ArrayList<>();
        children.add(Constant.ALL);

        filterSpinner(spinnerMother, mothers);
        filterSpinner(spinnerChildren, children);

        calendar = Calendar.getInstance();
        txtDateFrom.setText(getDayOfToday());
        txtDateTo.setText(getDayOfToday());

        initRecyclerView();
    }

    private void initRecyclerView() {
        rvMaintains.setLayoutManager(new LinearLayoutManager(this));
        maintainAdapter = new MaintainAdapter(maintains);
        rvMaintains.setAdapter(maintainAdapter);
    }

    private void filterData() {
        if (maintains == null || maintains.size() == 0) return;
        maintainAdapter.update(txtDateFrom.getText().toString(), txtDateTo.getText().toString(),
                spinnerMother.getSelectedItem().toString(), spinnerChildren.getSelectedItem().toString());
    }

    private void addListener() {
        txtDateFrom.setOnClickListener(this);
        txtDateTo.setOnClickListener(this);

        spinnerMother.setOnItemSelectedListener(this);
        spinnerChildren.setOnItemSelectedListener(this);
    }

    private void filterSpinner(Spinner spinner, ArrayList<String> data) {
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void getAllMaintain() {
        RetrofitContext.getAllMaintain().enqueue(new Callback<ArrayList<Maintain>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Maintain>> call, @NonNull Response<ArrayList<Maintain>> response) {
                if (response.code() == 200) {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    maintains = response.body();
                    filterListCompany();
                    initRecyclerView();
                    filterData();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Maintain>> call, @NonNull Throwable t) {
                showToast("Lỗi đường truyền");
            }
        });
    }

    private void filterListCompany() {
        if (maintains != null && maintains.size() != 0) {
            for (Maintain maintain : maintains) {
                if (!checkExistCompany(mothers, maintain.getMother())) {
                    mothers.add(maintain.getMother());
                }
                if (!checkExistCompany(children, maintain.getChild())) {
                    children.add(maintain.getChild());
                }
            }
            filterSpinner(spinnerMother, mothers);
            filterSpinner(spinnerChildren, children);
        }
    }

    private boolean checkExistCompany(ArrayList<String> companyNames, String companyName) {
        for (String name : companyNames) {
            if (name.equals(companyName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_date_from:
                getDateFromDatePicker(txtDateFrom);
                break;
            case R.id.txt_date_to:
                getDateFromDatePicker(txtDateTo);
                break;
            default:
                break;
        }
    }

    private void getDateFromDatePicker(final TextView textView) {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(String.format(DATE_FORMAT, dayOfMonth, monthOfYear + 1, year));
                filterData();
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public String getDayOfToday() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(Constant.TIME_ZONE));
        return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                + calendar.get(Calendar.YEAR);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        filterData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
