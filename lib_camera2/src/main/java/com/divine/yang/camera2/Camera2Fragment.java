package com.divine.yang.camera2;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.divine.yang.base.AppConstants;
import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.camera2.edit.MyIMGEditActivity;
import com.divine.yang.camera2.select.PicSelectActivity;
import com.divine.yang.camera2.select.PicSelectConfig;
import com.divine.yang.camera2.widget.AutoFixTextureView;
import com.divine.yang.util.sys.DensityUtils;
import com.divine.yang.util.sys.FileUtils;
import com.divine.yang.util.sys.ImageUtils;
import com.divine.yang.widget.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Divine
 * CreateDate: 2020/10/20
 * Describe: 相机取景比例为16：9
 */
public class Camera2Fragment extends BaseFragment implements TextureView.SurfaceTextureListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnTouchListener {
    private String TAG = "camera2Fragment";
    private Display activityDisplay;
    //图片裁剪requestCode
    private final int HIDE_GREY_MARK = 3;
    private final int SHOW_GREY_MARK = 4;

    //camera2相关
    //cameraId，自定义的一个值
    private String mCameraId;
    private CameraManager mCameraManager;
    private ImageReader mImageReader;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder mPreviewBuilder;
    private CameraCaptureSession mSession;
    private Handler mHandler;
    private Handler mainHandler;
    private CameraCharacteristics mCameraCharacteristics;

    private Camera2Callback mCamera2Callback;

    //图片路径集合，连拍是需要数量限制
    private ArrayList<String> mPicPathList;
    //是否开启闪光灯,判断是连拍，还是单拍（true:单拍；false:连拍）,isSingle=false:图片选择是否支持多选
    private boolean isOpenFlash = false, isSingle = true;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == HIDE_GREY_MARK) {
                if (!DialogUtils.isShowDialog())
                    mCamera2CameraGrayMark.setVisibility(View.GONE);
            } else if (msg.what == SHOW_GREY_MARK)
                mCamera2CameraGrayMark.setVisibility(View.VISIBLE);
        }
    };

    //相机camera2拍照图片处理
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            try {
                CameraUtils.setCamera2PicSourceName();
                //进行相片存储
                Camera2Utils.saveCamera2Pic(reader, Camera2Base.camera2PicSourceDir);
                File file = new File(Camera2Base.camera2PicSourceDir + CameraUtils.getCamera2PicSourceName());
                if (isSingle) {//单拍
                    showImageAlbum();
                    Uri sourceUri = Uri.fromFile(file);
                    CameraUtils.setCamera2PicCropName();
                    File cropFile = new File(Camera2Base.camera2PicCropDir + CameraUtils.getCamera2PicCropName());
                    FileUtils.createFile(cropFile);
                    toImageEdit(sourceUri, cropFile.getAbsolutePath());
                } else {
                    mPicPathList.add(Camera2Base.camera2PicSourceDir + CameraUtils.getCamera2PicSourceName());
                    showImagePreview(mPicPathList);
                    if (mPicPathList.size() > 0) {
                        Bitmap sourceBmp = BitmapFactory.decodeFile(mPicPathList.get(mPicPathList.size() - 1));
                        Matrix m = new Matrix();
                        m.postRotate(90);
                        Bitmap newBmp = Bitmap.createBitmap(sourceBmp, 0, 0, sourceBmp.getWidth(), sourceBmp.getHeight(), m, true);
                        mCamera2ButtonImagePreview.setImageBitmap(newBmp);
                    } else {
                        mCamera2ButtonImagePreview.setImageBitmap(null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
    //相机camera2设备状态callback
    private CameraDevice.StateCallback deviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            try {
                takePreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            closeCameraDevice();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(getActivity(), "打开摄像头失败", Toast.LENGTH_SHORT).show();
        }
    };
    //相机预览相关配置
    private CameraCaptureSession.StateCallback mSessionPreviewStateCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mSession = session;
            //配置完毕开始预览
            try {
                //无限次的重复获取图像
                mSession.setRepeatingRequest(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            Toast.makeText(getActivity(), "相机预览配置失败", Toast.LENGTH_SHORT).show();
        }
    };
    //相机拍照事项
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mSession = session;
            handler.sendEmptyMessage(HIDE_GREY_MARK);
        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            mSession = session;
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            handler.sendEmptyMessage(HIDE_GREY_MARK);
        }
    };

    @Override
    protected void initView(View view) {
        findView(view);
        //显示纯色覆盖层
        handler.sendEmptyMessage(SHOW_GREY_MARK);
        //显示拍照提示语
        mCamera2CameraWordMark.setVisibility(Camera2Base.showHint ? View.VISIBLE : View.GONE);
        mPicPathList = new ArrayList<>();
        activityDisplay = getActivity().getWindowManager().getDefaultDisplay();
        try {
            //子线程
            HandlerThread handlerThread = new HandlerThread("Camera2Thread");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
            mainHandler = new Handler(getActivity().getMainLooper());//用来处理ui线程的handler，即ui线程
            // CameraCharacteristics.LENS_FACING_FRONT-后置摄像头； LENS_FACING_BACK-前置摄像头
            mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;
            mCameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
            mCameraCharacteristics = mCameraManager.getCameraCharacteristics(mCameraId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void getData() { }

    @Override
    public int getContentView() {
        return R.layout.fragment_camera2;
    }

    @Override
    public String getTitle() {
        return "camera2";
    }

    @Override
    public int getIconResId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAutoFixTextureView.isAvailable()) {
            initCameraAndPreview();
        } else {
            mAutoFixTextureView.setSurfaceTextureListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeCameraDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCameraDevice();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        try {
            if (viewId == R.id.camera2_camera_header_close) {
                getActivity().finish();
            } else if (viewId == R.id.camera2_camera_header_open_flash_lamp) {
                changeFlashState();
            } else if (viewId == R.id.camera2_button_album) {
                selectImages();
            } else if (viewId == R.id.camera2_button_take_photo) {
                // 拍照按钮监听
                takePicture();
            } else if (viewId == R.id.camera2_button_submit) {
                mCamera2Callback.multiCallback(mPicPathList);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    //跳转到图片编辑页面
    private void toImageEdit(Uri sourceUri, String cropSavePath) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("IMAGE_URI", sourceUri);
            obj.put("IMAGE_SAVE_PATH", cropSavePath);
            String params = obj.toString();
            Intent intent = new Intent(getActivity(), MyIMGEditActivity.class);
            intent.putExtra("image_edit_params", params);
            getActivity().startActivityForResult(intent, AppConstants.REQUEST_CODE_IMAGE_EDIT);
            //                        navigationTo(RouterManager.router_img_edit, params, AppConstants.REQUEST_CODE_IMAGE_EDIT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void result(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConstants.REQUEST_CODE_IMAGE_EDIT) {
                mCamera2Callback.singleCallback(Camera2Base.camera2PicCropDir + CameraUtils.getCamera2PicCropName());
            } else if (requestCode == AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC) {
                Bundle bundle = data.getBundleExtra("select_pics_bundle");
                ArrayList<String> imageList = bundle.getStringArrayList("select_pics");
                if (imageList.size() > 0) {
                    if (isSingle) {
                        String mPicSavePath = imageList.get(0);
                        try {
                            Bitmap cpsbmp = ImageUtils.compressByQuality(mPicSavePath, 1024 * 1024);
                            File outputFile = new File(mPicSavePath);
                            FileOutputStream out = new FileOutputStream(outputFile);
                            cpsbmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.close();
                            Uri sourceUri = Uri.fromFile(new File(mPicSavePath));
                            CameraUtils.setCamera2PicCropName();
                            File cropFile = new File(Camera2Base.camera2PicCropDir + CameraUtils.getCamera2PicCropName());
                            mPicSavePath = cropFile.getAbsolutePath();
                            toImageEdit(sourceUri, mPicSavePath);
                            //                                                        Intent intent = new Intent(getContext(), MyIMGEditActivity.class);
                            //                                                        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI, sourceUri);
                            //                                                        intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH, mPicSavePath);
                            //                                                        getActivity().startActivityForResult(intent, AppConstants.REQUEST_CODE_IMAGE_EDIT);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //                        mCamera2Callback.multiCallback(imageList);
                    }
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.camera2_camera_header_single) {
            showImageAlbum();
            isSingle = true;
        } else if (checkedId == R.id.camera2_camera_header_continuous) {
            showImagePreview(mPicPathList);
            isSingle = false;
        }
    }

    // TextureView的SurfaceTextureListener---------------------------------------
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        initCameraAndPreview();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //释放camera
        closeCameraDevice();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    // ----------------------------------------------------------------------------
    //初始化相机和预览设置
    @TargetApi(19)
    public void initCameraAndPreview() {
        try {
            int mDisplayRotation = activityDisplay.getRotation();
            //获取摄像头方向
            int mCameraSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean exchange = Camera2Utils.exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation);
            //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
            StreamConfigurationMap configurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //手机支持的输出照片尺寸集合
            Size[] mPicSaveSize = configurationMap.getOutputSizes(ImageFormat.JPEG);
            //手机支持的预览尺寸集合
            Size[] mPicPreviewSize = configurationMap.getOutputSizes(SurfaceTexture.class);
            //最佳图片保存尺寸
            mPicBestSaveSize = CameraUtils.getBestSize(exchange, Arrays.asList(mPicSaveSize));
            //最佳图片预览尺寸
            mPicBestPreviewSize = CameraUtils.getBestSize(exchange, Arrays.asList(mPicPreviewSize));

            mAutoFixTextureView.setAspectRation(mPicBestPreviewSize.getWidth(), mPicBestPreviewSize.getHeight());
            mAutoFixTextureView.getSurfaceTexture().setDefaultBufferSize(mPicBestPreviewSize.getWidth(), mPicBestPreviewSize.getHeight());
            int screenWidth = DensityUtils.getScreenWidth(mContext);
            //            int textureHeight = screenWidth * 16 /9;
            //            int textureHeight = screenWidth * 4 / 3;
            //            mAutoFixTextureView.setAspectRation(screenWidth, textureHeight);
            //            mAutoFixTextureView.getSurfaceTexture().setDefaultBufferSize(screenWidth, textureHeight);
            // 设置ImageReader，用来读取拍摄图像的类

            mImageReader = ImageReader.newInstance(mPicBestSaveSize.getWidth(), mPicBestSaveSize.getHeight(), ImageFormat.JPEG,/*maxImages*/2);
            //            mImageReader = ImageReader.newInstance(screenWidth, textureHeight, ImageFormat.JPEG,/*maxImages*/2);
            //这里必须传入mainHandler，因为涉及到了Ui操作
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mainHandler);
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "未取得摄像头授权，请到手机设置中进行授权", Toast.LENGTH_SHORT).show();
                return;
            }
            //在子线程中打开了相机
            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler);
        } catch (CameraAccessException e) {
            Toast.makeText(getActivity(), "打开相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    Size mPicBestSaveSize, mPicBestPreviewSize;

    //打开相机预览
    public void takePreview() throws CameraAccessException {
        //创建预览请求
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        Surface surface = new Surface(mAutoFixTextureView.getSurfaceTexture());
        mPreviewBuilder.addTarget(surface);
        //自动对焦
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        //默认闪光灯是关闭的
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.CONTROL_AE_MODE_OFF);
        int rotation = activityDisplay.getRotation();
        //使图片做顺时针旋转
        mPreviewBuilder.set(CaptureRequest.JPEG_ORIENTATION, Camera2Utils.getJpegOrientation(mCameraCharacteristics, rotation));
        //创建预览session
        mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()), mSessionPreviewStateCallback, mHandler);
    }

    //拍照
    public void takePicture() throws CameraAccessException {
        //用来设置拍照请求的request
        CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
        captureBuilder.addTarget(mImageReader.getSurface());
        // 自动对焦
        captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        // 自动曝光
        if (isOpenFlash) {
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            captureBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
        }
        int rotation = activityDisplay.getRotation();
        //根据摄像头方向对保存的照片进行旋转
        captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, Camera2Utils.getJpegOrientation(mCameraCharacteristics, rotation));//使图片做顺时针旋转
        mSession.capture(captureBuilder.build(), mSessionCaptureCallback, mHandler);
    }

    //相机闪光灯开关
    private void changeFlashState() throws CameraAccessException {
        //        是否支持闪光灯
        boolean hasFlash = mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        if (hasFlash) {
            isOpenFlash = !isOpenFlash;
            if (isOpenFlash) {
                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CameraMetadata.FLASH_MODE_TORCH);
            } else {
                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            }
            mSession.setRepeatingRequest(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);
        }
    }

    //触摸手动对焦
    private float touchX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //        touchX = event.getX();
        //        try {
        //            touchToFocus();
        //        } catch (CameraAccessException e) {
        //            e.printStackTrace();
        //        }
        return false;
    }

    private void touchToFocus() throws CameraAccessException {
        float previewWidth = mAutoFixTextureView.getWidth();
        float previewHeight = mAutoFixTextureView.getHeight();
        int areaSize = 600;
        int weight = 1000;
        Rect rect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        Log.d(TAG, "active Rect:" + rect.toString());
        Rect newRect;
        int leftPos, topPos;
        // 坐标转换
        float newX = touchX;
        float newY = previewWidth - touchX;
        // 大小转换
        leftPos = (int) ((newX / previewHeight) * rect.right);
        topPos = (int) ((newY / previewWidth) * rect.bottom);
        // 以坐标点为中心生成一个矩形, 需要防止上下左右的值溢出
        int left = clamp(leftPos - areaSize, 0, rect.right);
        int top = clamp(topPos - areaSize, 0, rect.bottom);
        int right = clamp(leftPos + areaSize, leftPos, rect.right);
        int bottom = clamp(topPos + areaSize, topPos, rect.bottom);
        newRect = new Rect(left, top, right, bottom);
        Log.d(TAG, newRect.toString());
        // 构造MeteringRectangle
        MeteringRectangle mr = new MeteringRectangle(newRect, weight);

        MeteringRectangle[] rectangle = new MeteringRectangle[]{mr};
        // 对焦模式必须设置为AUTO
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
        //AE
        mPreviewBuilder.set(CaptureRequest.CONTROL_AE_REGIONS, rectangle);
        //AF 此处AF和AE用的同一个rect, 实际AE矩形面积比AF稍大, 这样测光效果更好
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_REGIONS, rectangle);
        // AE/AF区域设置通过setRepeatingRequest不断发请求
        mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
        //触发对焦
        mPreviewBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
        //触发对焦通过capture发送请求, 因为用户点击屏幕后只需触发一次对焦
        mSession.capture(mPreviewBuilder.build(), mSessionCaptureCallback, mHandler);
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    private void selectImages() {
        PicSelectConfig mPicSelectConfig = new PicSelectConfig.Builder()
                // 是否记住上次选中记录
                .rememberSelected(true)
                .needCamera(true)
                .multiSelect(!isSingle)
                // 使用沉浸式状态栏
                .build();
        Intent intent = new Intent(getContext(), PicSelectActivity.class);
        intent.putExtra("config", mPicSelectConfig);
        getActivity().startActivityForResult(intent, AppConstants.REQUEST_CODE_CUSTOM_SELECT_PIC);
    }

    public boolean isSingle() {
        return isSingle;
    }

    private void closeCameraDevice() {
        if (mCameraDevice != null) {
            //停止预览
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    // 相机预览布局，拍照界面头部布局，动作按钮布局
    private RelativeLayout mCamera2CameraHeader, mCamera2ButtonLayout;
    // 图像预览view
    private AutoFixTextureView mAutoFixTextureView;
    // 灰色覆盖
    private View mCamera2CameraGrayMark;
    // 相册预览
    private ImageView mCamera2ButtonImagePreview;
    // 单拍连拍单选
    private RadioGroup mCamera2CameraHeaderPicMode;
    // 单拍，连拍
    private RadioButton mCamera2CameraHeaderSingle, mCamera2CameraHeaderContinuous;
    // 文字覆盖，连拍照片张数，连拍完成按钮
    private TextView mCamera2CameraWordMark, mCamera2ButtonImageNum, mCamera2ButtonSubmit;
    // 打开闪光灯按钮，关闭按钮，进入相册按钮，拍照按钮
    private ImageButton mCamera2CameraHeaderOpenFlashLamp, mCamera2CameraHeaderClose, mCamera2ButtonAlbum, mCamera2ButtonTakePhoto;

    private void findView(View view) {
        mAutoFixTextureView = view.findViewById(R.id.camera2_camera_texture_view);
        mCamera2CameraGrayMark = view.findViewById(R.id.camera2_camera_gray_mark);
        mCamera2CameraHeader = view.findViewById(R.id.camera2_camera_header);
        mCamera2CameraHeaderOpenFlashLamp = view.findViewById(R.id.camera2_camera_header_open_flash_lamp);
        mCamera2CameraHeaderClose = view.findViewById(R.id.camera2_camera_header_close);
        mCamera2CameraHeaderPicMode = view.findViewById(R.id.camera2_camera_header_pic_mode);
        mCamera2CameraHeaderSingle = view.findViewById(R.id.camera2_camera_header_single);
        mCamera2CameraHeaderContinuous = view.findViewById(R.id.camera2_camera_header_continuous);
        mCamera2CameraWordMark = view.findViewById(R.id.camera2_camera_word_mark);
        mCamera2ButtonLayout = view.findViewById(R.id.camera2_button_layout);
        mCamera2ButtonImagePreview = view.findViewById(R.id.camera2_button_image_preview);
        mCamera2ButtonImageNum = view.findViewById(R.id.camera2_button_image_num);
        mCamera2ButtonAlbum = view.findViewById(R.id.camera2_button_album);
        mCamera2ButtonTakePhoto = view.findViewById(R.id.camera2_button_take_photo);
        mCamera2ButtonSubmit = view.findViewById(R.id.camera2_button_submit);

        camera2_camera_text_view = view.findViewById(R.id.camera2_camera_text_view);


        setTextureLayoutParams();
        setListener();
    }

    private TextView camera2_camera_text_view;

    private void setListener() {
        mCamera2ButtonAlbum.setOnClickListener(this);
        mCamera2ButtonTakePhoto.setOnClickListener(this);
        mCamera2ButtonSubmit.setOnClickListener(this);
        mCamera2CameraHeaderOpenFlashLamp.setOnClickListener(this);
        mCamera2CameraHeaderClose.setOnClickListener(this);
        mCamera2CameraHeaderPicMode.setOnCheckedChangeListener(this);
        mAutoFixTextureView.setSurfaceTextureListener(this);
        mAutoFixTextureView.setOnTouchListener(this);
    }

    private void setTextureLayoutParams() {
        int screenWidth = DensityUtils.getScreenWidth(mContext);
        int textureHeight = screenWidth * 16 / 9;
        //        int textureHeight = screenWidth * 4 / 3;
        ViewGroup.LayoutParams mAutoFixTextureViewLayoutParams = mAutoFixTextureView.getLayoutParams();
        mAutoFixTextureViewLayoutParams.width = screenWidth;
        mAutoFixTextureViewLayoutParams.height = textureHeight;
        mAutoFixTextureView.setLayoutParams(mAutoFixTextureViewLayoutParams);

        ViewGroup.LayoutParams camera2_camera_text_view1 = camera2_camera_text_view.getLayoutParams();
        camera2_camera_text_view1.width = screenWidth;
        camera2_camera_text_view1.height = textureHeight;
        camera2_camera_text_view.setLayoutParams(camera2_camera_text_view1);

    }

    private void showImageAlbum() {
        mCamera2ButtonSubmit.setVisibility(View.GONE);
        mCamera2ButtonImagePreview.setVisibility(View.GONE);
        mCamera2ButtonImageNum.setVisibility(View.GONE);
        mCamera2ButtonAlbum.setVisibility(View.VISIBLE);
    }

    private void showImagePreview(ArrayList<String> picList) {
        mCamera2ButtonSubmit.setVisibility(View.VISIBLE);
        mCamera2ButtonImagePreview.setVisibility(View.VISIBLE);
        if (null != picList && picList.size() > 0) {
            mCamera2ButtonImageNum.setVisibility(View.VISIBLE);
            mCamera2ButtonImageNum.setText(picList.size() + "");
            mCamera2ButtonAlbum.setVisibility(View.GONE);
        } else {
            mCamera2ButtonAlbum.setVisibility(View.VISIBLE);
        }
    }


    public void setCamera2Callback(Camera2Callback callback) {
        this.mCamera2Callback = callback;
    }
}
