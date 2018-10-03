package com.example.tuanfpt.companymanager.adapter;

import com.example.tuanfpt.companymanager.models.Image;

public interface OnImageItemSelected {
    void onItemBeforeSelected(Image image, int position);
    void onItemAfterSelected(Image image, int position);
}
