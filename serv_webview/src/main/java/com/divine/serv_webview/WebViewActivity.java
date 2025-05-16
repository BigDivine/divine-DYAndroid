package com.divine.serv_webview;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.divine.serv_webview.client.WebViewInterface;
import com.divine.serv_webview.util.PermissionUtils;
import com.divine.serv_webview.util.WebViewUtils;
import com.divine.yang.base.AppBase;
import com.divine.yang.base.AppConstants;
import com.divine.yang.base.base.BaseActivity;
import com.divine.yang.base.utils.ActivityUtils;
import com.divine.yang.base.utils.DialogUtils;
import com.divine.yang.theme.ThemeSource;
import com.divine.yang.util.network.NetworkUtil;
import com.divine.yang.util.old.sys.StatusBarUtils;
import com.divine.yang.util.sys.FileUtils;

import java.io.File;


/**
 * @author Divine.Yang
 * @date 2019-06-21
 * @describe webView页
 */
public abstract class WebViewActivity extends BaseActivity implements WebViewInterface, View.OnClickListener, View.OnLongClickListener {
    private final String TAG = "WebViewActivity";
    private RelativeLayout container;
    private WebView webView;
    private ProgressBar progressBar, netLoadingProgress;
    private View netErrorLayout;
    private View netLoadingLayout;
    private Button btnToSetting;
    private ImageView btnBack;
    private ValueCallback<Uri> mInputValue;
    private ValueCallback<Uri[]> mInputValues;
    private PopupWindow selectPhotoOrCamera;
    private String imgName;
    private String imgPath;
    private String contentProviderPackageName;
    private boolean isClickMask = true;
    // 兼容input标签需要的变量
    private ValueCallback<Uri[]> filePathCallback;
    private WebChromeClient.FileChooserParams fileChooserParams;
    private ValueCallback uploadMsg;

    // 拍照时，新建的图片文件名
    public abstract String getImgName();

    // 获取上一页面传入的参数
    public abstract String getRouterParams();

    @Override
    public int getContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    public boolean showToolbar() {
        return false;
    }

    @Override
    public void initView() {
        imgPath = AppBase.appDir + "/camera/";
        contentProviderPackageName = getPackageName() + ".provider";
        container = findViewById(R.id.layout_container);
        // 页面加载进度条，顶部横向的，0-100%
        progressBar = findViewById(R.id.progressBar);

        webView = findViewById(R.id.web_view);
        netErrorLayout = findViewById(R.id.net_error);
        netLoadingLayout = findViewById(R.id.net_loading);
        netLoadingProgress = netLoadingLayout.findViewById(R.id.net_loading_progress);
        btnBack = netErrorLayout.findViewById(R.id.back_btn);
        btnToSetting = netErrorLayout.findViewById(R.id.to_setting_btn);
        btnBack.setOnClickListener(this);
        btnToSetting.setOnClickListener(this);
        netErrorLayout.setOnLongClickListener(this);
        netLoadingLayout.setOnLongClickListener(this);
        webView.setOnLongClickListener(this);
        // 初始化webview
        WebViewUtils.initWebView(this, this, webView);

        if (TextUtils.isEmpty(GmtWebView.webServerUrl)) {
            Log.e(TAG, "服务地址异常！");
        } else {
            String url = GmtWebView.isDevelop ? GmtWebView.webServerUrl : (GmtWebView.webServerUrl + GmtWebView.webServerPath);
            loadUrl(url);
        }
    }

    @Override
    public void setTheme() {
        int startColor = getColor(com.divine.yang.theme.R.color.BaseThemeColor);
        int endColor = getColor(com.divine.yang.theme.R.color.GradientColor);
        ThemeSource.setViewBackground(progressBar, new int[]{startColor, endColor});
        ThemeSource.setProgressCircleRotateAnim(this, netLoadingProgress, new int[]{startColor, endColor});
    }

    public void loadUrl(String url) {
        new NetworkUtil.UrlJudgeTask(url, success -> {
            if (success) {
                netLoadingLayout.setVisibility(View.VISIBLE);
                netErrorLayout.setVisibility(View.GONE);
                webView.loadUrl(url);
            } else {
                netLoadingLayout.setVisibility(View.GONE);
                netErrorLayout.setVisibility(View.VISIBLE);
                TextView errorMessage = netErrorLayout.findViewById(R.id.net_error_message);
                errorMessage.setText(getResources().getString(R.string.load_url_error_text));
            }
        }).execute();
    }

    @Override
    public void getData() {

    }

    @Override
    public String[] requestPermissions() {
        return PermissionUtils.requestPermissionsArray();
    }

    /**
     * 照片选择完成后回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == AppConstants.REQUEST_CODE_SYSTEM_CAMERA) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        File img = new File(imgPath, imgName);
                        Uri imgUri = FileProvider.getUriForFile(WebViewActivity.this, contentProviderPackageName, img);
                        Uri[] uris = new Uri[]{imgUri};
                        setInputValues(uris);
                    } else {
                        // 5.0以下拍照
                        Uri imgUri = Uri.fromFile(new File(imgPath, imgName));
                        setInputValue(imgUri);
                    }
                } else if (requestCode == AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE) {
                    // 选择文件
                    Uri imgUri = data == null ? null : data.getData();
                    if (imgUri == null) {
                        ClipData mClipData = data.getClipData();
                        Uri[] fileUris = new Uri[mClipData.getItemCount()];
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            fileUris[i] = mClipData.getItemAt(i).getUri();
                        }
                        setInputValues(fileUris);
                    } else {
                        // 不用这个会导致input标签不能重复使用
                        setInputValues(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                    }
                } else if (requestCode == AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE_LOW) {
                    // 5.0以下选择文件
                    Uri imgUri = data == null ? null : data.getData();
                    if (imgUri == null) {
                        ClipData mClipData = data.getClipData();
                        Uri[] fileUris = new Uri[mClipData.getItemCount()];
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            fileUris[i] = mClipData.getItemAt(i).getUri();
                        }
                        setInputValues(fileUris);
                    } else {
                        setInputValue(imgUri);
                    }

                }
                break;
            case RESULT_CANCELED:
                if (requestCode == AppConstants.REQUEST_CODE_SYSTEM_CAMERA || requestCode == AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE || requestCode == AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE_LOW) {
                    setInputValueNull(null, null);
                }
                break;
        }
    }

    private void setInputValue(Uri uri) {
        if (null == mInputValue) {
            return;
        }
        mInputValue.onReceiveValue(uri);
        mInputValue = null;
    }

    private void setInputValues(Uri[] uris) {
        if (null == mInputValues) {
            return;
        }
        mInputValues.onReceiveValue(uris);
        mInputValues = null;
    }

    private void setInputValueNull(ValueCallback<Uri[]> filePathCallback, ValueCallback uploadMsg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (filePathCallback != null) mInputValues = filePathCallback;
            setInputValues(null);
        } else {
            if (uploadMsg != null) mInputValues = uploadMsg;
            setInputValue(null);
        }
    }

    /**
     * 物理按键返回键拦截
     */
    @Override
    public void onBackPressed() {
        if (null != webView) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                super.onBackPressed();
            }
            // if (webView.getUrl().contains("app-execute-web")) {
            //     String webUrl = webView.getUrl();
            //     if (webUrl.contains("JJDAAppExecute")) {
            //         super.onBackPressed();
            //     } else {
            //         webView.loadUrl("javascript:PageBack()");
            //     }
            // } else if (webView.canGoBack()) {
            //     webView.goBack();
            // } else {
            //     int nerErrorVisible = netErrorLayout.getVisibility();
            //     if (nerErrorVisible == View.VISIBLE) {
            //         // SPUtils.getInstance(this).put(SPKeys.SP_KEY_IS_LOGIN, false);
            //         // toLogin();
            //     } else {
            //         super.onBackPressed();
            //     }
            // }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.back_btn) {
            // SPUtils.getInstance(this).put(SPKeys.SP_KEY_IS_LOGIN, false);
            // toLogin();
        } else if (viewId == R.id.to_setting_btn) {
            Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            startActivity(intent);
        } else if (viewId == R.id.popup_select_file_task) {
            isClickMask = false;
            toFile(filePathCallback, fileChooserParams, uploadMsg);
            selectPhotoOrCamera.dismiss();
        } else if (viewId == R.id.popup_select_photo_task) {
            isClickMask = false;
            toAlbum(filePathCallback, fileChooserParams, uploadMsg);
            selectPhotoOrCamera.dismiss();
        } else if (viewId == R.id.popup_select_camera_task) {
            isClickMask = false;
            toCamera(filePathCallback, uploadMsg);
            selectPhotoOrCamera.dismiss();
        } else if (viewId == R.id.popup_select_cancel) {
            isClickMask = false;
            selectPhotoOrCamera.dismiss();
            // 解决点击选择图片按钮无法重复回调的问题。
            setInputValueNull(filePathCallback, uploadMsg);
            ActivityUtils.setBackgroundAlpha(WebViewActivity.this, 1f);
        } else if (viewId == R.id.change_to_c) {
            loadUrl("http://10.97.10.76:9999");
            // webView.loadUrl("http://10.97.10.76:9999");
            // webView.reload();
            DialogUtils.dismissDialog();
        } else if (viewId == R.id.change_to_p) {
            loadUrl("http://192.168.1.32:9999");
            // webView.loadUrl("http://192.168.1.32:9999");
            // webView.reload();
            DialogUtils.dismissDialog();
        }
    }

    /**
     * webview长按监听
     *
     * @param view
     * @return true表示消费掉事件
     */
    @Override
    public boolean onLongClick(View view) {
        LinearLayout changeServerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_change_server, null, false);
        TextView changeServerC = changeServerView.findViewById(R.id.change_to_c);
        TextView changeServerP = changeServerView.findViewById(R.id.change_to_p);
        changeServerC.setOnClickListener(this);
        changeServerP.setOnClickListener(this);
        DialogUtils.showCustomDialog(this, changeServerView, "bottom");
        return true;
    }

    /**
     * 弹出选择拍照还是选择照片的popwindow
     *
     * @param fileChooserParams
     * @param filePathCallback
     * @param uploadMsg
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showSelectPhotoOrCamera(final ValueCallback<Uri[]> filePathCallback, final WebChromeClient.FileChooserParams fileChooserParams, final ValueCallback uploadMsg) {
        String[] types = fileChooserParams.getAcceptTypes();
        if (fileChooserParams.isCaptureEnabled()) {
            toCamera(filePathCallback, uploadMsg);
            return;
        }
        this.filePathCallback = filePathCallback;
        this.fileChooserParams = fileChooserParams;
        this.uploadMsg = uploadMsg;
        View contentView = LayoutInflater.from(this).inflate(R.layout.popup_select_photo_carema, null);
        View file = contentView.findViewById(R.id.popup_select_file_task);
        View album = contentView.findViewById(R.id.popup_select_photo_task);
        View camera = contentView.findViewById(R.id.popup_select_camera_task);
        TextView cancel = contentView.findViewById(R.id.popup_select_cancel);
        if (types[0] == "") {
            file.setVisibility(View.VISIBLE);
        } else {
            file.setVisibility(View.GONE);
        }
        file.setOnClickListener(this);
        album.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);

        selectPhotoOrCamera = new PopupWindow(this);
        selectPhotoOrCamera.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        selectPhotoOrCamera.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        selectPhotoOrCamera.setTouchable(true);
        selectPhotoOrCamera.setFocusable(true);
        selectPhotoOrCamera.setContentView(contentView);
        selectPhotoOrCamera.setBackgroundDrawable(new ColorDrawable(Color.rgb(255, 255, 255)));// new ColorDrawable(0)即为透明背景
        selectPhotoOrCamera.setOnDismissListener(() -> {
            ActivityUtils.setBackgroundAlpha(WebViewActivity.this, 1f);
            // 解决点击选择图片按钮无法重复回调的问题。
            if (isClickMask) {
                setInputValueNull(filePathCallback, uploadMsg);
            }
            isClickMask = true;
        });

        selectPhotoOrCamera.showAtLocation(contentView.getRootView(), Gravity.BOTTOM, 0, 0);
        ActivityUtils.setBackgroundAlpha(this, 0.5f);
    }

    public static final String DOC = "application/msword";
    public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String XLS = "application/vnd.ms-excel application/x-excel";
    public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PPT = "application/vnd.ms-powerpoint";
    public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String PDF = "application/pdf";
    public static final String IMG = "image/*";
    public static final String AUD = "audio/*";
    public static final String VOD = "video/*";
    public static final String TXT = "text/*";

    private void toFile(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams, ValueCallback uploadMsg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setInputValues(null);
            mInputValues = filePathCallback;
        } else {
            mInputValue = uploadMsg;
        }
        StringBuffer types = new StringBuffer();
        types.append(DOC).append("|").append(DOCX).append("|").append(XLS).append("|").append(XLSX).append("|").append(PPT).append("|").append(PPTX).append("|").append(PDF).append("|").append(IMG).append("|").append(AUD).append("|").append(VOD).append("|").append(TXT);
        int mode = fileChooserParams.getMode();
        boolean isMultiple = mode == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = fileChooserParams.createIntent();
            intent.setType(types.toString());

            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple);
            startActivityForResult(Intent.createChooser(intent, "File Browser"), AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(types.toString());
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple);
            startActivityForResult(Intent.createChooser(i, "File Browser"), AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE_LOW);
        }
    }

    private void toCamera(ValueCallback<Uri[]> filePathCallback, ValueCallback uploadMsg) {
        imgName = getImgName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setInputValues(null);
            mInputValues = filePathCallback;
        } else {
            mInputValue = uploadMsg;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imgUri;
        File img = new File(imgPath, imgName);
        FileUtils.createFile(img);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imgUri = FileProvider.getUriForFile(WebViewActivity.this, contentProviderPackageName, img);
        } else {
            imgUri = Uri.fromFile(img);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, AppConstants.REQUEST_CODE_SYSTEM_CAMERA);
    }

    private void toAlbum(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams, ValueCallback uploadMsg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setInputValues(null);
            mInputValues = filePathCallback;
        } else {
            mInputValue = uploadMsg;
        }
        int mode = fileChooserParams.getMode();
        boolean isMultiple = mode == WebChromeClient.FileChooserParams.MODE_OPEN_MULTIPLE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = fileChooserParams.createIntent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple);
            startActivityForResult(intent, AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isMultiple);
            startActivityForResult(Intent.createChooser(i, "Select Picture"), AppConstants.REQUEST_CODE_SYSTEM_SELECT_FILE_LOW);
        }
    }

    // private void ocrCallback(Intent data) {
    //     String res = data.getStringExtra("res");
    //     //        webView.loadUrl("javascript:OCcallJSClick(" + res + ")");
    //     webView.loadUrl("javascript:OCcallJSClick('" + res + "')");
    // }

    // protected void toLogin() {
    //     navigationTo(RouterManager.router_login);
    //     this.finish();
    // }

    public WebView getWebView() {
        return webView;
    }

    public View newWeb;

    @Override
    public View getNewWebPage() {
        newWeb = LayoutInflater.from(this).inflate(R.layout.layout_new_web_view, null, false);
        newWeb.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return newWeb;
    }

    private String newWebTitleContent;

    public void setWebTitle(String title) {
        newWebTitleContent = title;
    }

    @Override
    public String getNewWebPageTitle() {
        return newWebTitleContent;
    }

    @Override
    public View getNetErrorLayout() {
        return netErrorLayout;
    }

    @Override
    public View getNetLoadingLayout() {
        return netLoadingLayout;
    }

    @Override
    public RelativeLayout getWebViewContainer() {
        return container;
    }

    @Override
    public ProgressBar getWebViewProgressBar() {
        return progressBar;
    }
}
