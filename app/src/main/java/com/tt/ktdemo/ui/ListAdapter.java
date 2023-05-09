package com.tt.ktdemo.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tt.ktdemo.R;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ListAdapter(List<String> data) {
        super(R.layout.item_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_info_tv, item);
        String picUrl = "https://fanpaixiu.com/data/attachment/forum/202106/12/093218rcs4wrr5swwx44sg.jpg";

        Glide.with(getContext())
                .load(picUrl)
                .error(R.mipmap.ic_launcher_round)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        helper.setImageDrawable(R.id.item_info_img, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Glide.with(getContext()).clear(this);
                    }
                });
    }
}