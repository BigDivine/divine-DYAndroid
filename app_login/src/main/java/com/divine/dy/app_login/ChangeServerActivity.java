package com.divine.dy.app_login;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.divine.dy.lib_base.GmtBase;
import com.divine.dy.lib_base.base.BaseActivity;
import com.divine.dy.lib_base.base.BaseToolbar;
import com.divine.dy.lib_base.base.ToolbarClickListener;
import com.divine.dy.lib_source.SPKeys;
import com.divine.dy.lib_utils.sys.SPUtils;
import com.divine.dy.lib_utils.ui.ToastUtils;

public class ChangeServerActivity extends BaseActivity implements ToolbarClickListener, View.OnClickListener {
    private TextView changeServerText, changeLoginServerText;
    private EditText changeServerEdit, changeLoginServerEdit;
    private Button changeServerBtn;
    private SPUtils mSPUtils;

    private String curServer, newServer, curLoginServer, newLoginServer;

    @Override
    public int getContentViewId() {
        return R.layout.activity_change_server;
    }

    @Override
    public boolean showToolbar() {
        return true;
    }

    @Override
    public void initView() {
        BaseToolbar toolbar = getBaseToolbar();
        toolbar.setTitle("切换服务器");
        toolbar.setRightVisible(false);
        toolbar.setLeftText("");
        toolbar.setToolbarClickListener(this);

        mSPUtils = SPUtils.getInstance(this);
        changeServerText = findViewById(R.id.change_server_label);
        changeServerEdit = findViewById(R.id.change_server_edit);
        changeLoginServerText = findViewById(R.id.change_login_server_label);
        changeLoginServerEdit = findViewById(R.id.change_login_server_edit);
        curServer = (String) mSPUtils.get(SPKeys.SP_KEY_WEB_SERVER, "");
        changeServerEdit.setText(curServer);
        curLoginServer = (String) mSPUtils.get(SPKeys.SP_KEY_SERVER, "");
        changeLoginServerEdit.setText(curLoginServer);
        if (GmtBase.isDevelop) {
            changeLoginServerText.setVisibility(View.VISIBLE);
            changeLoginServerEdit.setVisibility(View.VISIBLE);
        } else {
            changeLoginServerText.setVisibility(View.GONE);
            changeLoginServerEdit.setVisibility(View.GONE);
        }
        changeServerBtn = findViewById(R.id.change_server_button);
        changeServerBtn.setOnClickListener(this);
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void centerClick() {

    }

    @Override
    public void rightClick() {

    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.change_server_button) {
            newLoginServer = changeLoginServerEdit.getText().toString();
            newServer = changeServerEdit.getText().toString();
            if (!TextUtils.equals(curServer, newServer) || !TextUtils.equals(curLoginServer, newLoginServer)) {
                mSPUtils.put(SPKeys.SP_KEY_WEB_SERVER, newServer);
                if (GmtBase.isDevelop) {
                    mSPUtils.put(SPKeys.SP_KEY_SERVER, newLoginServer);
                } else {
                    mSPUtils.put(SPKeys.SP_KEY_SERVER, newServer);
                }
                setResult(RESULT_OK);
                finish();
            } else {
                if (!TextUtils.isEmpty(newServer) && !TextUtils.isEmpty(newLoginServer)) {
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    ToastUtils.showLong(this, "请检查是否已配置所有服务器地址");
                }
            }
        }
    }
}