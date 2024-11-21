package com.divine.yang.camera2.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.yang.camera2.R;
import com.divine.yang.camera2.interfaces.OnPicSelectImageClickListener;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectRvAdapter extends RecyclerView.Adapter<PicSelectRvViewHolder> {
    private Context mContext;
    private List<PicBean> data;
    private PicSelectConfig config;
    private OnPicSelectImageClickListener listener;

    public PicSelectRvAdapter(Context mContext, List<PicBean> data, PicSelectConfig config) {
        this.mContext = mContext;
        this.data = data;
        this.config = config;
    }

    @NonNull
    @Override
    public PicSelectRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pic_select_image, parent, false);
        PicSelectRvViewHolder mPicSelectFragmentRvViewHolder = new PicSelectRvViewHolder(rootView);
        return mPicSelectFragmentRvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicSelectRvViewHolder holder, int position) {
        PicBean itemData = data.get(position);
        if (position == 0 && config.needCamera) {
            holder.mItemPicSelectRvCheck.setVisibility(View.GONE);
            // holder.mItemPicSelectRvImg.setImageResource(R.mipmap.ic_camera_app);
            holder.mItemPicSelectRvImg.setColorFilter(mContext.getResources().getColor(R.color.back_black));
            setCameraClick(holder, itemData);
            return;
        }
        if (config.multiSelect) {
            holder.mItemPicSelectRvCheck.setVisibility(View.VISIBLE);
            setImageChecked(holder, itemData);
        } else {
            holder.mItemPicSelectRvCheck.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .asBitmap()
                // .placeholder(R.mipmap.ic_default_app)
                .load(new File(itemData.path))
                .into(holder.mItemPicSelectRvImg);

        setItemClick(holder, position, itemData);
        setCheckButtonClick(holder, position, itemData);
    }

    @Override
    public int getItemCount() {
        return null != this.data ? this.data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && config.needCamera) {
            return 1;
        }
        return 0;
    }

    private void setCameraClick(PicSelectRvViewHolder holder, PicBean item) {
        holder.mItemPicSelectRvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onCameraClick(item);
            }
        });
    }

    private void setItemClick(PicSelectRvViewHolder holder, int position, PicBean item) {
        holder.mItemPicSelectRvImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view, position, item);
            }
        });
    }

    private void setCheckButtonClick(PicSelectRvViewHolder holder, int position, PicBean item) {
        holder.mItemPicSelectRvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemCheck(view, position, item);
                    setImageChecked(holder, item);
                }
            }
        });
    }

    private void setImageChecked(PicSelectRvViewHolder holder, PicBean item) {
        if (PicSelectBase.mPicSelectList.contains(item.path)) {
            // holder.mItemPicSelectRvCheck.setImageResource(R.mipmap.ic_check_solid);
            holder.mItemPicSelectRvCheck.setColorFilter(mContext.getResources().getColor(R.color.barGreen));
        } else {
            // holder.mItemPicSelectRvCheck.setImageResource(R.mipmap.ic_check_not);
            holder.mItemPicSelectRvCheck.setColorFilter(mContext.getResources().getColor(R.color.white));
        }
    }

    public void setListener(OnPicSelectImageClickListener listener) {
        this.listener = listener;
    }
}

