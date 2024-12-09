package com.divine.yang.main.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.divine.yang.base.base.BaseFragment;
import com.divine.yang.main.R;
import com.divine.yang.main.adapter.SettingAdapter;
import com.divine.yang.main.bean.SettingBean;
import com.divine.yang.main.bean.UserBean;
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
        RecyclerView recyclerView = view.findViewById(R.id.recycler_in_mine);
        headerUserTitleView.setText(userBean.getUserName());
        headerOrgTitleView.setText(userBean.getUserOrg());
        String content = "你有多久没有发自内心的笑过了？你上一次笑着看别人的时候，是发自内心的开心吗？你有多久在笑的时候心里没有悲伤，没有压力过了？\r\n我们都知道20多岁自己想干啥，却不知道30多岁想干啥，因为周围30多岁的都在养孩子。";
        centerContentView.setText(content);

        ArrayList<SettingBean> settingBeans = new ArrayList<>();
        SettingBean settingBean = new SettingBean();
        settingBean.setImage("");
        settingBean.setTitle("title");
        for (int i = 0; i < 10; i++) {
            settingBeans.add(settingBean);
        }
        SettingAdapter homeAdapter = new SettingAdapter(getActivity());
        homeAdapter.setData(settingBeans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(homeAdapter);
    }

    private void setListener() {

    }

}