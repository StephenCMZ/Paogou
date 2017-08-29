package com.stephen.paogou.mvp.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stephen.paogou.R;
import com.stephen.paogou.mvp.model.ImageModel;

import java.util.List;

/**
 * Created by StephenChen on 2017/8/28.
 */

public class ImageAdapter extends BaseQuickAdapter<ImageModel, BaseViewHolder> {

    public ImageAdapter(@Nullable List<ImageModel> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageModel item) {
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.image_view);
        simpleDraweeView.setImageURI(Uri.parse(item.getImageUrl()));
    }
}
