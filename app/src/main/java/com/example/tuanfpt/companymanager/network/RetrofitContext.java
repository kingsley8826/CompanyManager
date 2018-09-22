package com.example.tuanfpt.companymanager.network;

import com.example.tuanfpt.companymanager.models.Account;
import com.example.tuanfpt.companymanager.models.JSONDepartmentSendForm;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitContext {

    private static final Retrofit RETROFIT =  new Retrofit.Builder()
            .baseUrl("https://company-8826.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Call<Account> callLoginApi(Account account) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                (new Gson()).toJson(account));
        return RETROFIT.create(GetService.class).callLogin(requestBody);
    }

    public static Call<String[]> getUsernameByDepartment(String departmentName) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                (new Gson()).toJson(new JSONDepartmentSendForm(departmentName)));
        return RETROFIT.create(GetService.class).getUsernameByDepartment(requestBody);
    }
}