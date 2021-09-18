package com.divine.dy.lib_camera2.edit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import com.divine.dy.lib_camera2.edit.core.IMGMode;
import com.divine.dy.lib_camera2.edit.core.IMGText;
import com.divine.dy.lib_camera2.edit.core.file.IMGAssetFileDecoder;
import com.divine.dy.lib_camera2.edit.core.file.IMGContentDecoder;
import com.divine.dy.lib_camera2.edit.core.file.IMGDecoder;
import com.divine.dy.lib_camera2.edit.core.file.IMGFileDecoder;
import com.divine.dy.lib_camera2.edit.core.util.IMGUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.divine.dy.lib_camera2.edit.IMGEditBaseActivity.OP_CLIP;
import static com.divine.dy.lib_camera2.edit.IMGEditBaseActivity.OP_NORMAL;

/**
 * Author: Divine
 * CreateDate: 2020/9/22
 * Describe:
 */
//@Route(path = RouterManager.router_img_edit)
public class MyIMGEditActivity extends MyIMGEditBaseActivity {

    private static final int MAX_WIDTH = 1024;

    private static final int MAX_HEIGHT = 1024;

    public static final String EXTRA_IMAGE_URI = "IMAGE_URI";

    public static final String EXTRA_IMAGE_SAVE_PATH = "IMAGE_SAVE_PATH";

    //    @Autowired
    String params = "";

    JSONObject objParams;

    @Override
    public Bitmap getBitmap() {
        params = getParams();
        if (TextUtils.isEmpty(params)) {
            return null;
        }
        try {
            objParams = new JSONObject(params);
            String uriStr = objParams.optString(EXTRA_IMAGE_URI);
            Uri uri = Uri.parse(uriStr);
            if (uri == null) {
                return null;
            }
            IMGDecoder decoder = null;
            String path = uri.getPath();
            if (!TextUtils.isEmpty(path)) {
                switch (uri.getScheme()) {
                    case "asset":
                        decoder = new IMGAssetFileDecoder(this, uri);
                        break;
                    case "file":
                        decoder = new IMGFileDecoder(uri);
                        break;
                    case "content":
                        decoder = new IMGContentDecoder(this, uri);
                        break;
                }
            }
            if (decoder == null) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inJustDecodeBounds = true;
            decoder.decode(options);
            if (options.outWidth > MAX_WIDTH) {
                options.inSampleSize = IMGUtils.inSampleSize(Math.round(1f * options.outWidth / MAX_WIDTH));
            }
            if (options.outHeight > MAX_HEIGHT) {
                options.inSampleSize = Math.max(options.inSampleSize,
                                                IMGUtils.inSampleSize(Math.round(1f * options.outHeight / MAX_HEIGHT)));
            }
            options.inJustDecodeBounds = false;

            Bitmap bitmap = decoder.decode(options);
            return bitmap;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onText(IMGText text) {
        mImgView.addStickerText(text);
    }

    @Override
    public void onModeClick(IMGMode mode) {
        IMGMode cm = mImgView.getMode();
        if (cm == mode) {
            mode = IMGMode.NONE;
        }
        mImgView.setMode(mode);
        updateModeUI();

        if (mode == IMGMode.CLIP) {
            setOpDisplay(OP_CLIP);
        }
    }

    @Override
    public void onUndoClick() {
        IMGMode mode = mImgView.getMode();
        if (mode == IMGMode.DOODLE) {
            mImgView.undoDoodle();
        } else if (mode == IMGMode.MOSAIC) {
            mImgView.undoMosaic();
        }
    }

    @Override
    public void onCancelClick() {
        finish();
    }

    @Override
    public void onDoneClick() {
        try {
            objParams = new JSONObject(params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String path = objParams.optString(EXTRA_IMAGE_SAVE_PATH);
        if (!TextUtils.isEmpty(path)) {
            Bitmap bitmap = mImgView.saveBitmap();
            if (bitmap != null) {
                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                setResult(RESULT_OK);
                finish();
                return;
            }
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onCancelClipClick() {
        //        onCancelClick();
        mImgView.cancelClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onDoneClipClick() {
        //        onDoneClick();
        mImgView.doClip();
        setOpDisplay(mImgView.getMode() == IMGMode.CLIP ? OP_CLIP : OP_NORMAL);
    }

    @Override
    public void onResetClipClick() {
        mImgView.resetClip();
    }

    @Override
    public void onRotateClipClick() {
        mImgView.doRotate();
    }

    @Override
    public void onColorChanged(int checkedColor) {
        mImgView.setPenColor(checkedColor);
    }
}
