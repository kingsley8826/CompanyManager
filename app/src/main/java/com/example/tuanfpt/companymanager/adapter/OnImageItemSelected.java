package com.example.tuanfpt.companymanager.adapter;

import com.example.tuanfpt.companymanager.models.Image;

import java.util.ArrayList;

public interface OnImageItemSelected {
    void onItemBeforeSelected(Image image, int position);
    void onItemAfterSelected(Image image, int position);
    void onViewImage(int position, ArrayList<String> images);
}
