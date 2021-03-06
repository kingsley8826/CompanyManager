package com.example.tuanfpt.companymanager.network;

import com.example.tuanfpt.companymanager.models.Account;
import com.example.tuanfpt.companymanager.models.Company;
import com.example.tuanfpt.companymanager.models.Department;
import com.example.tuanfpt.companymanager.models.Maintain;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetService {
    @POST("account/login")
    Call<Account> callLogin(@Body RequestBody requestBody);

    @POST("account/department")
    Call<ArrayList<String>> getUsernameByDepartment(@Body RequestBody requestBody);

    @GET("department/all")
    Call<ArrayList<Department>> getAllDepartment();

    @GET("company/all")
    Call<ArrayList<Company>> getAllCompany();

    @POST("company/detail")
    Call<Company> getCompanyById(@Body RequestBody requestBody);

    @POST("company/maintain/add")
    Call<Company> postMaintain(@Body RequestBody requestBody);

    @GET("company/maintain/all")
    Call<ArrayList<Maintain>> getAllMaintain();
}
