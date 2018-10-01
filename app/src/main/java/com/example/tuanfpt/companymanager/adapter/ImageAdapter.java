package com.example.tuanfpt.companymanager.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.tuanfpt.companymanager.R;
import com.example.tuanfpt.companymanager.models.Image;
import com.example.tuanfpt.companymanager.utilities.Constant;

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
                if (type.equals(Constant.BEFORE)) {
                    listener.onItemBeforeSelected(position);
                } else {
                    listener.onItemAfterSelected(position);
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
                    setPicture(image.getPathInDevice());
                    break;
                case Constant.ADD_BUTTON:
                    Glide.with(itemView.getContext()).load(R.drawable.capture).into(imageView);
                    break;
                default:
                    break;
            }
        }

        private void setPicture(String photoPath) {
            // Get the dimensions of the View
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(photoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
            imageView.setImageBitmap(bitmap);
        }
    }
}
