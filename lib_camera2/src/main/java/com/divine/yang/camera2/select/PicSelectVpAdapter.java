package com.divine.yang.camera2.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.divine.yang.camera2.R;
import com.divine.yang.camera2.interfaces.OnPicSelectImageClickListener;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectVpAdapter extends PagerAdapter {
    private Context mContext;
    private List<PicBean> images;
    private PicSelectConfig config;
    private OnPicSelectImageClickListener listener;

    public PicSelectVpAdapter(Context mContext, List<PicBean> images, PicSelectConfig config) {
        this.mContext = mContext;
        this.images = images;
        this.config = config;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pic_select_vp, container, false);
        ImageView mPicSelectVpItemImg = rootView.findViewById(R.id.pic_select_vp_item_img);
        ImageView mPicSelectVpItemCheck = rootView.findViewById(R.id.pic_select_vp_item_check);

        PicBean image = images.get(config.needCamera ? position + 1 : position);

        if (config.multiSelect) {
            mPicSelectVpItemCheck.setVisibility(View.VISIBLE);
            setImageChecked(mPicSelectVpItemCheck, image);
        } else {
            mPicSelectVpItemCheck.setVisibility(View.GONE);
        }
        setItemClick(mPicSelectVpItemImg, position, image);
        setCheckButtonClick(mPicSelectVpItemCheck, position, image);

        Glide.with(mContext)
                .asBitmap()
                // .placeholder(R.mipmap.ic_default_app)
                .load(new File(image.path))
                .into(mPicSelectVpItemImg);

        container.addView(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return rootView;
    }

    @Override
    public int getCount() {
        if (config.needCamera)
            return images.size() - 1;
        else
            return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void setItemClick(ImageView imageView, int position, PicBean item) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view, position, item);
            }
        });
    }

    private void setCheckButtonClick(ImageView imageView, int position, PicBean item) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemCheck(view, position, item);
                    setImageChecked(imageView, item);
                }
            }
        });
    }

    private void setImageChecked(ImageView view, PicBean item) {
        if (PicSelectBase.mPicSelectList.contains(item.path)) {
            // view.setImageResource(R.mipmap.ic_check_solid);
            view.setColorFilter(mContext.getResources().getColor(R.color.barGreen));
        } else {
            // view.setImageResource(R.mipmap.ic_check_not);
            view.setColorFilter(mContext.getResources().getColor(R.color.white));
        }
    }

    public void setListener(OnPicSelectImageClickListener listener) {
        this.listener = listener;
    }
}
