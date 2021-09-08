package com.divine.dy.lib_camera2.select;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.dy.lib_camera2.R;
import com.divine.dy.lib_camera2.interfaces.OnPicSelectFragmentRvItemClickListener;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectFragmentRvAdapter extends RecyclerView.Adapter<PicSelectFragmentRvViewHolder> {
    private Context mContext;
    private List<PicBean> data;
    private PicSelectConfig config;
    private boolean showCamera, multiSelect;
    private OnPicSelectFragmentRvItemClickListener listener;

    public PicSelectFragmentRvAdapter(Context mContext, List<PicBean> data, PicSelectConfig config) {
        this.mContext = mContext;
        this.data = data;
        this.config = config;
    }

    @NonNull
    @Override
    public PicSelectFragmentRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_pic_select_fragment_rv_item, parent, false);
        PicSelectFragmentRvViewHolder mPicSelectFragmentRvViewHolder = new PicSelectFragmentRvViewHolder(rootView);
        return mPicSelectFragmentRvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicSelectFragmentRvViewHolder holder, int position) {
        PicBean itemData = data.get(position);
        if (position == 0 && showCamera) {
            Glide.with(mContext).load(R.mipmap.ic_camera_grey).into(holder.mAdapterPicSelectFragmentRvItemImg);
            return;
        }
        setItemClick(holder, position, itemData);
        if (multiSelect) {
            holder.mAdapterPicSelectFragmentRvItemCheck.setVisibility(View.VISIBLE);
            if (PicSelectStaticVariable.mPicSelectImageList.contains(itemData.path)) {
                Glide.with(mContext).load(R.mipmap.ic_check_true_blue_white).into(holder.mAdapterPicSelectFragmentRvItemCheck);
            } else {
                Glide.with(mContext).load(R.mipmap.ic_check_false_blue_alpha_grey).into(holder.mAdapterPicSelectFragmentRvItemCheck);
            }
            setCheckButtonClick(holder, position, itemData);
        } else {
            holder.mAdapterPicSelectFragmentRvItemCheck.setVisibility(View.GONE);
        }
        Glide.with(mContext)
                .asBitmap()
                .placeholder(R.mipmap.ic_default_img_grey_alpha)
                .load(new File(itemData.path))
                .into(holder.mAdapterPicSelectFragmentRvItemImg);
    }

    @Override
    public int getItemCount() {
        return null != this.data ? this.data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && showCamera) {
            return 1;
        }
        return 0;
    }

    private void setItemClick(PicSelectFragmentRvViewHolder holder, int position, PicBean item) {
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(view, position, item);
            }
        });
    }

    private void setCheckButtonClick(PicSelectFragmentRvViewHolder holder, int position, PicBean item) {
        holder.mAdapterPicSelectFragmentRvItemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int ret = listener.onItemCheckClick(view, position, item);
                    if (ret == 1) { // 局部刷新
                        if (PicSelectStaticVariable.mPicSelectImageList.contains(item.path)) {
                            holder.mAdapterPicSelectFragmentRvItemCheck.setImageResource(R.mipmap.ic_check_true_blue_white);
                        } else {
                            holder.mAdapterPicSelectFragmentRvItemCheck.setImageResource(R.mipmap.ic_check_false_blue_alpha_grey);
                        }
                    }
                }
            }
        });
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public void setListener(OnPicSelectFragmentRvItemClickListener listener) {
        this.listener = listener;
    }
}

