package com.divine.dy.lib_camera2.select;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.divine.dy.lib_camera2.R;
import com.divine.dy.lib_camera2.interfaces.OnFolderChangeListener;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Author: Divine
 * CreateDate: 2020/10/22
 * Describe:
 */
public class PicSelectPopRvAdapter extends RecyclerView.Adapter<PicSelectPopRvViewHolder> {
    private Context mContext;
    private List<PicFolderBean> data;
    private PicSelectConfig config;
    private int selected = 0;

    private OnFolderChangeListener listener;

    public PicSelectPopRvAdapter(Context mContext, List<PicFolderBean> data, PicSelectConfig config) {
        this.mContext = mContext;
        this.data = data;
        this.config = config;
    }

    @NonNull
    @Override
    public PicSelectPopRvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pic_select_folder, parent, false);
        PicSelectPopRvViewHolder mPicSelectPopRvViewHolder = new PicSelectPopRvViewHolder(rootView);
        return mPicSelectPopRvViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PicSelectPopRvViewHolder holder, int position) {
        PicFolderBean itemData = this.data.get(position);
        if (position == 0) {
            holder.mPicSelectRvItemName.setText("所有图片(" + getTotalImageSize() + ")");
        } else {
            holder.mPicSelectRvItemName.setText(itemData.name + "(" + itemData.images.size() + ")");
        }
        if (data.size() > 0) {
            Glide.with(mContext).load(Uri.fromFile(new File(itemData.cover.path))).into(holder.mPicSelectRvItemImg);
        }
        if (selected == position) {
            holder.mPicSelectRvItemChecked.setVisibility(View.VISIBLE);
        } else {
            holder.mPicSelectRvItemChecked.setVisibility(View.GONE);
        }
        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectIndex(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != this.data ? this.data.size() : 0;
    }


    public void setData(List<PicFolderBean> folders) {
        data.clear();
        if (folders != null && folders.size() > 0) {
            data.addAll(folders);
        }
        notifyDataSetChanged();
    }

    private int getTotalImageSize() {
        int result = 0;
        if (data != null && data.size() > 0) {
            for (PicFolderBean folder : data) {
                result += folder.images.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int position) {
        if (selected == position)
            return;
        if (listener != null)
            listener.onChange(position, data.get(position));
        selected = position;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return selected;
    }

    public void setOnFolderChangeListener(OnFolderChangeListener listener) {
        this.listener = listener;
    }
}

