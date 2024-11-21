package com.divine.yang.camera2.select;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divine.yang.base.AppConstants;
import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.base.getpermission.PermissionList;
import com.divine.yang.camera2.Camera2Base;
import com.divine.yang.camera2.CameraUtils;
import com.divine.yang.camera2.R;
import com.divine.yang.camera2.interfaces.OnFolderChangeListener;
import com.divine.yang.camera2.interfaces.OnPicSelectImageClickListener;
import com.divine.yang.util.sys.FileUtils;
import com.divine.yang.widget.ItemDecorationGrid;
import com.divine.yang.widget.ItemDecorationLine;
import com.divine.yang.widget.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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
public class PicSelectActivity extends BaseActivity implements View.OnClickListener, OnFolderChangeListener, OnPicSelectImageClickListener {
    private static final String TAG = "DY-PicSelect";

    private final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media._ID};

    private List<PicFolderBean> mFolderList = new ArrayList<>();
    private List<PicBean> mImageList = new ArrayList<>();
    private PicSelectRvAdapter mPicSelectRvAdapter;
    private PicSelectVpAdapter mPreviewAdapter;
    private PicSelectConfig mPicSelectConfig;
    private ArrayList<String> mPicSelectImagesResult = new ArrayList<>();

    private PopupWindow mPopupWindow;
    private PicSelectPopRvAdapter mPicSelectPopRvAdapter;

    private final int HANDLE_UPDATE_SELECTED_IMAGE_COUNT = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == HANDLE_UPDATE_SELECTED_IMAGE_COUNT) {
                btPicSelectSubmit.setText(String.format("%1$s(%2$d/%3$d)",
                                                        mPicSelectConfig.submitText,
                                                        PicSelectBase.mPicSelectList.size(),
                                                        mPicSelectConfig.maxNum));
                if (PicSelectBase.mPicSelectList.size() > 0) {
                    btPicSelectSubmit.setBackgroundColor(getResources().getColor(R.color.barGreen));
                } else {
                    btPicSelectSubmit.setBackgroundColor(getResources().getColor(R.color.gray_text));
                }
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_pic_select;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        findView();
        setListener();
        createPopupFolderList();
        mPicSelectConfig = (PicSelectConfig) getIntent().getSerializableExtra("config");
        if (mPicSelectConfig != null) {
            initConfig();
        }
        if (!FileUtils.isSdCardAvailable()) {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
        } else {
            rvPicSelectImages.setLayoutManager(new GridLayoutManager(this, 4));
            ItemDecorationGrid itemDecorationGrid = new ItemDecorationGrid(this, 10, true);
            rvPicSelectImages.addItemDecoration(itemDecorationGrid);
            mPicSelectRvAdapter = new PicSelectRvAdapter(this, mImageList, mPicSelectConfig);
            mPicSelectRvAdapter.setListener(this);
            rvPicSelectImages.setAdapter(mPicSelectRvAdapter);

            mPreviewAdapter = new PicSelectVpAdapter(this, mImageList, mPicSelectConfig);
            mPreviewAdapter.setListener(this);
            mVpPicSelectPicPreview.setAdapter(mPreviewAdapter);
        }
    }

    /**
     * 根据config配置页面
     */
    private void initConfig() {
        if (mPicSelectConfig.backResId != -1) {
            ivPicSelectClose.setImageResource(mPicSelectConfig.backResId);
        }
        tvPicSelectFolderName.setText(mPicSelectConfig.rootFolderName);
        if (mPicSelectConfig.multiSelect) {
            btPicSelectSubmit.setVisibility(View.VISIBLE);
            if (!mPicSelectConfig.rememberSelected) {
                PicSelectBase.mPicSelectList.clear();
            }
            mHandler.dispatchMessage(Message.obtain(mHandler, HANDLE_UPDATE_SELECTED_IMAGE_COUNT));
        } else {
            PicSelectBase.mPicSelectList.clear();
            btPicSelectSubmit.setVisibility(View.GONE);
        }
    }

    @Override
    public void getData() {
        getImages();
        getFolders();
    }

    @Override
    public String[] requestPermissions() {
        return new String[]{PermissionList.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("config", mPicSelectConfig);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPicSelectConfig = (PicSelectConfig) savedInstanceState.getSerializable("config");
    }

    @Override
    public void onBackPressed() {
        PicSelectBase.mPicSelectList.clear();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pic_select_close) {
            onBackPressed();
        } else if (id == R.id.pic_select_folder_select) {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            } else {
                mPopupWindow.showAsDropDown(rlPicSelectHeader);
            }
        } else if (id == R.id.pic_select_submit) {
            if (PicSelectBase.mPicSelectList != null && !PicSelectBase.mPicSelectList.isEmpty()) {
                exitPicSelectActivity();
            } else {
                ToastUtils.showShort(this, "最少选择一张图片");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC_TO_CAMERA) {
                File tempFile = new File(Camera2Base.camera2PicSourceDir + CameraUtils.getCamera2PicSourceName());
                if (tempFile != null) {
                    if (mPicSelectConfig.multiSelect) {
                        PicSelectBase.mPicSelectList.add(tempFile.getAbsolutePath());
                        getData();
                        mHandler.dispatchMessage(Message.obtain(mHandler, HANDLE_UPDATE_SELECTED_IMAGE_COUNT));
                    } else {
                        exitPicSelectActivity();
                    }
                }
            }
        } else {
            File tempFile = new File(Camera2Base.camera2PicSourceDir + CameraUtils.getCamera2PicSourceName());
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChange(int position, PicFolderBean folder) {
        mPopupWindow.dismiss();
        if (position == 0) {
            tvPicSelectFolderName.setText(mPicSelectConfig.rootFolderName);
            getImages();
        } else {
            tvPicSelectFolderName.setText(folder.name);
            mImageList.clear();
            if (mPicSelectConfig.needCamera)
                mImageList.add(new PicBean());
            mImageList.addAll(folder.images);
            mPicSelectRvAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCameraClick(PicBean item) {
        if (mPicSelectConfig.maxNum <= PicSelectBase.mPicSelectList.size()) {
            Toast.makeText(this, String.format("最多选择%1$d张图片", mPicSelectConfig.maxNum), Toast.LENGTH_SHORT).show();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            CameraUtils.setCamera2PicSourceName();
            File tempFile = new File(Camera2Base.camera2PicSourceDir + CameraUtils.getCamera2PicSourceName());
            FileUtils.createFile(tempFile);
            String provider = getPackageName() + ".file_provider";
            Uri uri = FileProvider.getUriForFile(this, provider, tempFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC_TO_CAMERA);
        } else {
            ToastUtils.showShort(this, "打开相机失败");
        }
    }

    @Override
    public void onItemClick(View view, int position, PicBean item) {
        if (mPicSelectConfig.multiSelect) {
            if (mVpPicSelectPicPreview.getVisibility() == View.VISIBLE) {
                mVpPicSelectPicPreview.setVisibility(View.GONE);
            } else {
                mVpPicSelectPicPreview.setVisibility(View.VISIBLE);
                mVpPicSelectPicPreview.setCurrentItem(mPicSelectConfig.needCamera ? position - 1 : position, false);
            }
        } else {
            PicSelectBase.mPicSelectList.add(item.path);
            exitPicSelectActivity();
        }
    }

    @Override
    public void onItemCheck(View view, int position, PicBean item) {
        if (item != null) {
            if (PicSelectBase.mPicSelectList.contains(item.path)) {
                PicSelectBase.mPicSelectList.remove(item.path);
            } else {
                if (mPicSelectConfig.maxNum <= PicSelectBase.mPicSelectList.size()) {
                    ToastUtils.showShort(this, String.format("最多选择%1$d张图片", mPicSelectConfig.maxNum));
                } else {
                    PicSelectBase.mPicSelectList.add(item.path);
                }
            }
            mHandler.dispatchMessage(Message.obtain(mHandler, HANDLE_UPDATE_SELECTED_IMAGE_COUNT));

        }
    }

    private void createPopupFolderList() {
        // View rootView = LayoutInflater.from(this).inflate(R.layout.common_layout_with_rv, null, false);
        // mPopupWindow = new PopupWindow(this);
        // mPopupWindow.setContentView(rootView);
        // mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // RecyclerView commonRecyclerView = rootView.findViewById(R.id.common_layout_rv);
        // commonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // ItemDecorationLine line = new ItemDecorationLine(this, R.color.white, 2);
        // commonRecyclerView.addItemDecoration(line);
        // mPicSelectPopRvAdapter = new PicSelectPopRvAdapter(this, mFolderList, mPicSelectConfig);
        // commonRecyclerView.setAdapter(mPicSelectPopRvAdapter);
        // mPicSelectPopRvAdapter.setOnFolderChangeListener(this);
    }

    private void getImages() {
        mImageList.clear();
        if (mPicSelectConfig.needCamera) {
            mImageList.add(new PicBean());
        }
        Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                       null,
                       null,
                       MediaStore.Images.Media.DATE_ADDED + " DESC");
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
            PicBean image = new PicBean(path, name);
            mImageList.add(image);
        }
        for (int i = 0; i < mImageList.size(); i++) {
            PicBean image = null;
            if (mPicSelectConfig.needCamera) {
                if (i > 0) {
                    image = mImageList.get(i);
                }
            } else {
                image = mImageList.get(i);
            }
            if (null != image) {
                File imageFile = new File(image.path);
                if (!imageFile.exists() || imageFile.length() < 10) {
                    mImageList.remove(i);
                }
            }
        }
        mPicSelectRvAdapter.notifyDataSetChanged();
        mPreviewAdapter.notifyDataSetChanged();
    }

    private void getFolders() {
        mFolderList.clear();
        for (int i = 0; i < mImageList.size(); i++) {
            PicBean image = null;
            if (mPicSelectConfig.needCamera) {
                if (i > 0) {
                    image = mImageList.get(i);
                }
            } else {
                image = mImageList.get(i);
            }
            if (null != image) {
                File imageFile = new File(image.path);
                File folderFile = imageFile.getParentFile();

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
                    parent.images = new ArrayList<>();
                    parent.name = folderFile.getName();
                    parent.path = folderFile.getAbsolutePath();
                    parent.cover = image;
                    parent.images.add(image);
                    mFolderList.add(parent);
                }
            }
        }
        mPicSelectPopRvAdapter.notifyDataSetChanged();
    }

    public void exitPicSelectActivity() {
        mPicSelectImagesResult.clear();
        mPicSelectImagesResult.addAll(PicSelectBase.mPicSelectList);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("select_pics", mPicSelectImagesResult);
        intent.putExtra("select_pics_bundle", bundle);
        setResult(RESULT_OK, intent);
        if (!mPicSelectConfig.multiSelect) {
            PicSelectBase.mPicSelectList.clear();
        }
        finish();
    }

    private RelativeLayout rlPicSelectHeader;
    private ImageView ivPicSelectClose;
    private LinearLayout rlPicSelectFolder;
    private TextView tvPicSelectFolderName;
    private Button btPicSelectSubmit;
    private RecyclerView rvPicSelectImages;
    private ViewPager mVpPicSelectPicPreview;

    private void findView() {
        rlPicSelectHeader = findViewById(R.id.pic_select_header);
        ivPicSelectClose = findViewById(R.id.pic_select_close);
        rlPicSelectFolder = findViewById(R.id.pic_select_folder_select);
        tvPicSelectFolderName = findViewById(R.id.pic_select_folder_name);
        btPicSelectSubmit = findViewById(R.id.pic_select_submit);
        rvPicSelectImages = findViewById(R.id.pic_select_images);
        mVpPicSelectPicPreview = findViewById(R.id.pic_select_pic_preview);
    }

    private void setListener() {
        ivPicSelectClose.setOnClickListener(this);
        rlPicSelectFolder.setOnClickListener(this);
        btPicSelectSubmit.setOnClickListener(this);
    }
}
