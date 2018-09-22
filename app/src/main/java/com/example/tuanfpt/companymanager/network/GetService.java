package com.example.tuanfpt.companymanager.network;

import com.example.tuanfpt.companymanager.models.Account;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GetService {
    @POST("account/login")
    Call<Account> callLogin(@Body RequestBody requestBody);

    @POST("account/department")
    Call<String[]> getUsernameByDepartment(@Body RequestBody requestBody);
}
