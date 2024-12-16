package com.divine.yang.main.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.main.R;
import com.divine.yang.main.bean.SettingBean;
import com.divine.yang.main.bean.UserBean;
import com.divine.yang.util.sys.DensityUtils;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class UserFragment extends BaseFragment {
    private UserBean userBean;

    @Override
    protected void initView(View view) {
        findView(view);
        setListener();
    }

    @Override
    protected void getData() {
        userBean = new UserBean();
        userBean.setUserName("天音丽歌");
        userBean.setUserOrg("美妙集团A");
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_user;
    }

    @Override
    public String getTitle() {
        return "个人中心";
    }

    @Override
    public int getIconResId() {
        return R.mipmap.ic_main_user;
    }


    private void findView(View view) {
        MaterialTextView headerUserTitleView = view.findViewById(R.id.header_user_title_in_mine);
        MaterialTextView headerOrgTitleView = view.findViewById(R.id.header_org_title_in_mine);
        MaterialTextView centerContentView = view.findViewById(R.id.center_content_in_mine);
        // RecyclerView recyclerView = view.findViewById(R.id.recycler_in_mine);
        headerUserTitleView.setText(userBean.getUserName());
        headerOrgTitleView.setText(userBean.getUserOrg());
        String content = "你有多久没有发自内心的笑过了？你上一次笑着看别人的时候，是发自内心的开心吗？你有多久在笑的时候心里没有悲伤，没有压力过了？\r\n我们都知道20多岁自己想干啥，却不知道30多岁想干啥，因为周围30多岁的都在养孩子。";
        centerContentView.setText(content);

        ArrayList<SettingBean> settingBeans = new ArrayList<>();
        SettingBean settingBean = new SettingBean();
        for (int i = 0; i < 7; i++) {
            settingBeans.add(settingBean);
        }
        LinearLayout settingContainView = view.findViewById(R.id.setting_contain_in_mine);
        int[] settingImages = new int[]{R.mipmap.ic_backlog, R.mipmap.ic_tickling, R.mipmap.ic_announcement, R.mipmap.ic_earlywarning, R.mipmap.ic_system_notification, R.mipmap.ic_setting, R.mipmap.ic_version};
        String[] settingTitles = new String[]{"待办信息", "反馈提醒", "公告", "预警信息", "系统通知", "设置", "版本"};

        for (int i = 0; i < settingBeans.size(); i++) {
            View settingView = LayoutInflater.from(mContext).inflate(R.layout.item_user_setting, null);
            settingView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(40.0f,mContext)));
            AppCompatImageView settingImageView = settingView.findViewById(R.id.item_image_in_setting);
            MaterialTextView settingTitleView = settingView.findViewById(R.id.item_title_in_setting);
            MaterialTextView settingOtherView = settingView.findViewById(R.id.item_other_in_setting);
            AppCompatImageView itemArrowView = settingView.findViewById(R.id.item_arrow_in_setting);
            settingImageView.setImageResource(settingImages[i]);
            settingTitleView.setText(settingTitles[i]);
            if (i == settingBeans.size() - 1) {
                settingOtherView.setText("v1.0.0");
                settingOtherView.setVisibility(View.VISIBLE);
                itemArrowView.setVisibility(View.GONE);
            }else{
                itemArrowView.setVisibility(View.VISIBLE);
            }
            // settingView.setOnClickListener(v->{
            //     settingViewClick(settingTitles[i]);
            // });
            settingContainView.addView(settingView);
        }
    }

    private void setListener() {

    }

}