// package com.divine.yang.main.old.home;
//
// import android.content.Intent;
// import android.view.MenuItem;
//
// import androidx.annotation.NonNull;
// import androidx.core.view.MenuProvider;
// import androidx.fragment.app.Fragment;
// import androidx.fragment.app.FragmentManager;
// import androidx.fragment.app.FragmentPagerAdapter;
// import androidx.lifecycle.Lifecycle;
// import androidx.lifecycle.LifecycleOwner;
// import androidx.viewpager.widget.ViewPager;
//
// import com.divine.yang.base.base.BaseActivity;
// import com.divine.yang.main.old.common.FragmentManagerForMain;
// import com.google.android.material.bottomnavigation.BottomNavigationView;
//
// import java.util.ArrayList;
//
// public class NavigatorPagerFragmentActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
//     private static final String TAG = "DY-NPActivity";
//
//     private ArrayList<Fragment> fragments;
//     private FragmentManagerForMain fmForMain;
//
//     @Override
//     public int getContentViewId() {
//         return 0;
//         // return R.old.activity_navigator_pager_fragment;
//     }
//
//     @Override
//     public boolean showToolbar() {
//         return false;
//     }
//
//     @Override
//     public void initView() {
//         getIntentData();
//         findView();
//         setListener();
//         fmForMain = new FragmentManagerForMain(this);
//         fragments = fmForMain.getFragments();
//
//         FragmentManager mFragmentManager = getSupportFragmentManager();
//         // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
//         ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(mFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments);
//         vpNavigatorPager.setAdapter(mViewPagerAdapter);
//         vpNavigatorPager.setCurrentItem(0);
//     }
//
//     private ViewPager vpNavigatorPager;
//     private BottomNavigationView bnvNavigatorPager;
//
//     private void getIntentData() {
//         Intent intent = getIntent();
//     }
//
//     private void findView() {
//         // vpNavigatorPager = findViewById(R.id.navigator_pager_view_pager);
//         // bnvNavigatorPager = findViewById(R.id.navigator_pager_bottom_navigator);
//     }
//
//     private void setListener() {
//         bnvNavigatorPager.setOnNavigationItemSelectedListener(this);
//         vpNavigatorPager.addOnPageChangeListener(this);
//     }
//
//     @Override
//     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//     }
//
//     @Override
//     public void onPageSelected(int position) {
//         MenuItem miCheck = bnvNavigatorPager.getMenu().getItem(position);
//         miCheck.setChecked(true);
//     }
//
//     @Override
//     public void onPageScrollStateChanged(int state) {
//
//     }
//
//     @Override
//     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//         int viewId = item.getItemId();
//         // if (viewId == R.id.bottom_menu_home) {
//         //     vpNavigatorPager.setCurrentItem(0);
//         // } else if (viewId == R.id.bottom_menu_user) {
//         //     vpNavigatorPager.setCurrentItem(1);
//         // }
//         return true;
//     }
//
//     @Override
//     public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {
//
//     }
// }