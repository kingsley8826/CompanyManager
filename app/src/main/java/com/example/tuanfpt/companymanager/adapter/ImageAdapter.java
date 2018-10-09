package com.example.tuanfpt.companymanager.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.models.Image;
import com.example.tuanfpt.companymanager.utilities.Constant;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private ArrayList<Image> images;
    private OnImageItemSelected listener;
    private String type;

    public ImageAdapter(ArrayList<Image> images, OnImageItemSelected listener, String type) {
        this.type = type;
        this.listener = listener;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, final int position) {
        holder.bindData(images.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (images.get(position).getType().equals(Constant.ADD_BUTTON)) {
                    if (type.equals(Constant.BEFORE)) {
                        listener.onItemBeforeSelected(images.get(position), position);
                    } else {
                        listener.onItemAfterSelected(images.get(position), position);
                    }
                } else {
                    ArrayList<String> slideImages = new ArrayList<>();
                    for (Image image : images) {
                        if (image.getType().equals(Constant.DEVICE_PATH)) {
                            slideImages.add("file://" + image.getPathInDevice());
                        } else if (image.getType().equals(Constant.URL_PATH)) {
                            slideImages.add(image.getUrl());
                        }
                    }
                    listener.onViewImage(position, slideImages);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void updateRecyclerView(ArrayList<Image> temp) {
        images.clear();
        images.addAll(temp);
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void bindData(Image image) {
            switch (image.getType()) {
                case Constant.URL_PATH:
                    Glide.with(itemView.getContext()).load(image.getUrl()).into(imageView);
                    break;
                case Constant.DEVICE_PATH:
                    Glide.with(itemView.getContext()).load(image.getPathInDevice()).into(imageView);
                    break;
                case Constant.ADD_BUTTON:
                    Glide.with(itemView.getContext()).load(R.drawable.capture).into(imageView);
                    break;
                default:
                    break;
            }
        }
    }
}
