package com.example.tuanfpt.companymanager.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private String[] objects;
    private String type;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull String[] objects, String type) {
        super(context, resource, objects);
        this.objects = objects;
        this.type = type;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_spinner, parent, false);
        TextView label = row.findViewById(R.id.txt_spinner);
        label.setText(objects[position]);
        if(type.equals(Constant.DEPARTMENT)){
            ImageView imageView = row.findViewById(R.id.imv_spinner);
            Glide.with(parent).load(R.drawable.department).into(imageView);
        }
        return row;
    }
}
