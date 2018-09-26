package com.example.tuanfpt.companymanager.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.tuanfpt.companymanager.utilities.Constant;

public class ChildrenActivity extends MotherActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isChildrenActivity = true;
        Intent intent = getIntent();
        motherName = intent.getStringExtra(Constant.MOTHER_NAME);
    }
}
