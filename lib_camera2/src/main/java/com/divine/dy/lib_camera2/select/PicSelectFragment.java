package com.divine.dy.lib_camera2.select;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.divine.dy.lib_base.AppBase;
import com.divine.dy.lib_base.AppConstants;
import com.divine.dy.lib_base.base.BaseFragment;
import com.divine.dy.lib_camera2.CameraUtils;
import com.divine.dy.lib_camera2.R;
import com.divine.dy.lib_camera2.interfaces.OnFolderChangeListener;
import com.divine.dy.lib_camera2.interfaces.OnPicSelectFragmentRvItemClickListener;
import com.divine.dy.lib_camera2.interfaces.PicSelectListener;
import com.divine.dy.lib_utils.sys.DensityUtils;
import com.divine.dy.lib_utils.sys.FileUtils;
import com.divine.dy.lib_widget.widget.ItemDecorationGrid;
import com.divine.dy.lib_widget.widget.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe:
 */
public class PicSelectFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, OnPicSelectFragmentRvItemClickListener, OnFolderChangeListener {
    private static final String TAG = "DY-PicSelectFrag";
    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID};

    private PicSelectFragmentRvAdapter mPicSelectFragmentRvAdapter;
    private PicSelectFragmentVpAdapter mPicSelectFragmentVpAdapter;
    private PicSelectConfig mPicSelectConfig;
    private PicSelectListener mPicSelectListener;
    private List<PicFolderBean> mFolderList = new ArrayList<>();
    private List<PicBean> mImageList = new ArrayList<>();

    private PopupWindow popupWindow;

    private boolean hasFolderGened = false;

    private File tempFile;

    public static PicSelectFragment instance() {
        PicSelectFragment fragment = new PicSelectFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        mPicSelectConfig = (PicSelectConfig) getArguments().getSerializable("config");
        if (mPicSelectConfig == null) {
            Log.e(TAG, "config 参数不能为空");
            return;
        }
        findView(view);
        setListener();

        mCvpPicSelectFragmentPicPreview.setOffscreenPageLimit(1);
//        mBtnPicSelectFragmentBottomAlbumSelect.setText(mPicSelectConfig.allImagesText);

        mRvPicSelectFragmentImgList.setLayoutManager(new GridLayoutManager(mContext, 4));
        ItemDecorationGrid itemDecorationGrid = new ItemDecorationGrid(mContext);
        itemDecorationGrid.setWidth(10);
        mRvPicSelectFragmentImgList.addItemDecoration(itemDecorationGrid);
        mPicSelectFragmentRvAdapter = new PicSelectFragmentRvAdapter(mContext, mImageList, mPicSelectConfig);
        mPicSelectFragmentRvAdapter.setShowCamera(mPicSelectConfig.needCamera);
        mPicSelectFragmentRvAdapter.setMultiSelect(mPicSelectConfig.multiSelect);
        mPicSelectFragmentRvAdapter.setListener(this);
        mRvPicSelectFragmentImgList.setAdapter(mPicSelectFragmentRvAdapter);

        PicSelectFragmentPopRvAdapter mPicSelectFragmentPopRvAdapter = new PicSelectFragmentPopRvAdapter(getActivity(), mFolderList, mPicSelectConfig);
        mBtnPicSelectFragmentBottomAlbumSelect.setAdapter(mPicSelectFragmentPopRvAdapter);
        mPicSelectFragmentPopRvAdapter.setOnFolderChangeListener(this);
//        mBtnPicSelectFragmentBottomAlbumSelect.setAdapter();

        mPicSelectFragmentVpAdapter = new PicSelectFragmentVpAdapter(mContext, mImageList, mPicSelectConfig);
        mCvpPicSelectFragmentPicPreview.setAdapter(mPicSelectFragmentVpAdapter);
        mPicSelectFragmentVpAdapter.setListener(new OnPicSelectFragmentRvItemClickListener() {
            @Override
            public void onItemClick(View view, int position, PicBean item) {
                hidePreview();
            }

            @Override
            public int onItemCheckClick(View view, int position, PicBean item) {
                return checkedImage(position, item);
            }
        });
    }

    @Override
    protected void getData() {
        Cursor cursor = mContext.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                       null,
                       null,
                       MediaStore.Images.Media.DATE_ADDED + " DESC");
        ArrayList<PicBean> tempImageList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
            PicBean image = new PicBean(path, name);
            tempImageList.add(image);
            File imageFile = new File(path);
            File folderFile = imageFile.getParentFile();
            if (folderFile == null || !imageFile.exists() || imageFile.length() < 10) {
                continue;
            }
            PicFolderBean parent = null;
            for (PicFolderBean folder : mFolderList) {
                if (TextUtils.equals(folder.path, folderFile.getAbsolutePath())) {
                    parent = folder;
                }
            }
            if (parent != null) {
                parent.images.add(image);
            } else {
                parent = new PicFolderBean();
                parent.name = folderFile.getName();
                parent.path = folderFile.getAbsolutePath();
                parent.cover = image;
                List<PicBean> imageList = new ArrayList<>();
                imageList.add(image);
                parent.images = imageList;
                mFolderList.add(parent);
            }
        }
        mImageList.clear();
        if (mPicSelectConfig.needCamera) {
            mImageList.add(new PicBean());
        }
        mImageList.addAll(tempImageList);

        mPicSelectFragmentRvAdapter.notifyDataSetChanged();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_pic_select_layout;
    }

    @Override
    public String getTitle() {
        return "图片选择";
    }

    @Override
    public int getIconResId() {
        return 0;
    }

    private int checkedImage(int position, PicBean image) {
        if (image != null) {
            if (PicSelectStaticVariable.mPicSelectImageList.contains(image.path)) {
                PicSelectStaticVariable.mPicSelectImageList.remove(image.path);
                if (mPicSelectListener != null) {
                    mPicSelectListener.onMultiImageUnselected(image.path);
                }
            } else {
                if (mPicSelectConfig.maxNum <= PicSelectStaticVariable.mPicSelectImageList.size()) {
                    Toast.makeText(getActivity(), String.format("最多选择%1$d张图片", mPicSelectConfig.maxNum), Toast.LENGTH_SHORT).show();
                    return 0;
                }

                PicSelectStaticVariable.mPicSelectImageList.add(image.path);
                if (mPicSelectListener != null) {
                    mPicSelectListener.onMultiImageSelected(image.path);
                }
            }
            return 1;
        }
        return 0;
    }

    private void createPopupFolderList(int width, int height) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_layout_with_rv, null, false);
         popupWindow = new PopupWindow(getActivity());
        popupWindow.setContentView(rootView);
        //点击外部弹出不消失
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        RecyclerView commonRecyclerView = rootView.findViewById(R.id.common_layout_rv);
        commonRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        PicSelectFragmentPopRvAdapter mPicSelectFragmentPopRvAdapter = new PicSelectFragmentPopRvAdapter(getActivity(), mFolderList, mPicSelectConfig);
        commonRecyclerView.setAdapter(mPicSelectFragmentPopRvAdapter);
        mPicSelectFragmentPopRvAdapter.setOnFolderChangeListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        WindowManager wm = getActivity().getWindowManager();
        final int size = wm.getDefaultDisplay().getWidth() / 3 * 2;
        if (v.getId() == R.id.pic_select_fragment_bottom_album_select) {
            if (popupWindow == null) {
                createPopupFolderList(size, size);
            }
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                int popUtils = DensityUtils.getScreenHeight(mContext) - mRlPicSelectFragmentBottomLayout.getHeight();
                popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, mRlPicSelectFragmentBottomLayout.getHeight());
                 setBackgroundAlpha(0.6f);
            }
        }
    }

    private void showCameraAction() {
        if (mPicSelectConfig.maxNum <= PicSelectStaticVariable.mPicSelectImageList.size()) {
            Toast.makeText(getActivity(), String.format("最多选择%1$d张图片", mPicSelectConfig.maxNum), Toast.LENGTH_SHORT).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort(getActivity(), "未获取拍照权限");
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            CameraUtils.setCamera2PicSourceName();
            tempFile = new File(AppBase.appDir + CameraUtils.getCamera2PicSourceName());
            FileUtils.createFile(tempFile);
            Uri uri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".image_provider", tempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //Uri.fromFile(tempFile)
            startActivityForResult(cameraIntent, AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC_TO_CAMERA);
        } else {
            Toast.makeText(getActivity(), "打开相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC_TO_CAMERA) {
                if (tempFile != null) {
                    if (mPicSelectListener != null) {
                        mPicSelectListener.onCameraShot(tempFile);
                    }
                }
            }
        } else {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(View view, int position, PicBean item) {
        if (mPicSelectConfig.needCamera && position == 0) {
            showCameraAction();
        } else {
            if (mPicSelectConfig.multiSelect) {
                mCvpPicSelectFragmentPicPreview.setVisibility(View.VISIBLE);
                if (mPicSelectConfig.needCamera) {
                    mPicSelectListener.onPreviewChanged(position, mImageList.size() - 1, true);
                } else {
                    mPicSelectListener.onPreviewChanged(position + 1, mImageList.size(), true);
                }
                mCvpPicSelectFragmentPicPreview.setCurrentItem(mPicSelectConfig.needCamera ? position - 1 : position);
            } else {
                if (mPicSelectListener != null) {
                    mPicSelectListener.onSingleImageSelected(item.path);
                }
            }
        }
    }

    @Override
    public int onItemCheckClick(View view, int position, PicBean item) {
        return checkedImage(position, item);
    }

    @Override
    public void onChange(int position, PicFolderBean folder) {
        popupWindow.dismiss();
        if (position == 0) {
//            mBtnPicSelectFragmentBottomAlbumSelect.setText(mPicSelectConfig.allImagesText);
            getData();
        } else {
            mImageList.clear();
            if (mPicSelectConfig.needCamera)
                mImageList.add(new PicBean());
            mImageList.addAll(folder.images);
            mPicSelectFragmentRvAdapter.notifyDataSetChanged();
//            mBtnPicSelectFragmentBottomAlbumSelect.setText(folder.name);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mPicSelectConfig.needCamera) {
            mPicSelectListener.onPreviewChanged(position + 1, mImageList.size() - 1, true);
        } else {
            mPicSelectListener.onPreviewChanged(position + 1, mImageList.size(), true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public boolean hidePreview() {
        if (mCvpPicSelectFragmentPicPreview.getVisibility() == View.VISIBLE) {
            mCvpPicSelectFragmentPicPreview.setVisibility(View.GONE);
            mPicSelectListener.onPreviewChanged(0, 0, false);
            mPicSelectFragmentRvAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

    private RecyclerView mRvPicSelectFragmentImgList;
//    private Button mBtnPicSelectFragmentBottomAlbumSelect;
    private Spinner mBtnPicSelectFragmentBottomAlbumSelect;
    private View mRlPicSelectFragmentBottomLayout;
    private ViewPager mCvpPicSelectFragmentPicPreview;

    private void findView(View view) {
        mRvPicSelectFragmentImgList = view.findViewById(R.id.pic_select_fragment_img_list);
        mBtnPicSelectFragmentBottomAlbumSelect = view.findViewById(R.id.pic_select_fragment_bottom_album_select);
        mRlPicSelectFragmentBottomLayout = view.findViewById(R.id.pic_select_fragment_bottom_layout);
        mCvpPicSelectFragmentPicPreview = view.findViewById(R.id.pic_select_fragment_pic_preview);
    }

    private void setListener() {
//        mBtnPicSelectFragmentBottomAlbumSelect.setOnClickListener(this);
        mCvpPicSelectFragmentPicPreview.addOnPageChangeListener(this);
    }

    /**
     * 设置图片选择时的监听，用于改变页面的内容
     *
     * @param mPicSelectListener
     */
    public void setPicSelectListener(PicSelectListener mPicSelectListener) {
        this.mPicSelectListener = mPicSelectListener;
    }


}
