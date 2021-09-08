package com.divine.dy.lib_camera2.select;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

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
public class PicSelectFragmentSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private Context mContext;
    private List<PicFolderBean> data;
    private PicSelectConfig config;
    private int selected = 0;

    private OnFolderChangeListener listener;

    public PicSelectFragmentSpinnerAdapter(Context mContext, List<PicFolderBean> data, PicSelectConfig config) {
        this.mContext = mContext;
        this.data = data;
        this.config = config;
    }


    @Override
    public void onBindViewHolder(@NonNull PicSelectFragmentPopRvViewHolder holder, int position) {
        PicFolderBean itemData = this.data.get(position);
        if (position == 0) {
            holder.mAdapterPicSelectFragmentPopRvItemTitle.setText("所有图片");
            holder.mAdapterPicSelectFragmentPopRvItemNum.setText("共" + getTotalImageSize() + "张");
            if (data.size() > 0) {
                Glide.with(mContext).load(Uri.fromFile(new File(itemData.cover.path))).into(holder.mAdapterPicSelectFragmentPopRvItemImg);
            }
        } else {
            holder.mAdapterPicSelectFragmentPopRvItemTitle.setText(itemData.name);
            holder.mAdapterPicSelectFragmentPopRvItemNum.setText("共" + itemData.images.size() + "张");
            if (data.size() > 0) {
                Glide.with(mContext).load(Uri.fromFile(new File(itemData.cover.path))).into(holder.mAdapterPicSelectFragmentPopRvItemImg);
            }
        }

        if (selected == position) {
            holder.mAdapterPicSelectFragmentPopRvItemSelect.setVisibility(View.VISIBLE);
        } else {
            holder.mAdapterPicSelectFragmentPopRvItemSelect.setVisibility(View.GONE);
        }


        holder.getItemView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectIndex(position);
            }
        });
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getCount() {
        return null != data ? data.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.adapter_pic_select_fragment_pop_rv_item, parent, false);

        return rootView;
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

