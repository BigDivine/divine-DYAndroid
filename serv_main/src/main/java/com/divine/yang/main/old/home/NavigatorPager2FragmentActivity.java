// package com.divine.yang.main.old.home;
//
// import android.content.Intent;
// import android.view.MenuItem;
//
// import com.divine.yang.home.R;
// import com.divine.yang.main.old.common.FragmentManagerForMain;
// import com.divine.yang.base.base.BaseActivity;
// import com.google.android.material.bottomnavigation.BottomNavigationView;
//
// import java.util.ArrayList;
//
// import androidx.annotation.NonNull;
// import androidx.core.view.MenuProvider;
// import androidx.fragment.app.Fragment;
// import androidx.fragment.app.FragmentManager;
// import androidx.lifecycle.Lifecycle;
// import androidx.lifecycle.LifecycleOwner;
// import androidx.viewpager2.widget.ViewPager2;
//
// public class NavigatorPager2FragmentActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
//     private static final String TAG = "DY-NP2Activity";
//
//     private ArrayList<Fragment> fragments;
//     private FragmentManagerForMain fmForMain;
//
//      @Override
//     public int getContentViewId() {
//          return 0;
//          // return R.old.activity_navigator_pager2_fragment;
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
//         Lifecycle mLifeCycle = getLifecycle();
//         // FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT fragment懒加载
//         ViewPager2Adapter mViewPager2Adapter = new ViewPager2Adapter(mFragmentManager, mLifeCycle, fragments);
//         vp2NavigatorPager2.setAdapter(mViewPager2Adapter);
//
//         vp2NavigatorPager2.setCurrentItem(0);
//
//     }
//
//     private ViewPager2 vp2NavigatorPager2;
//     private BottomNavigationView bnvNavigatorPager2;
//     private void getIntentData() {
//         Intent intent = getIntent();
//
//     }
//     private void findView() {
//         // vp2NavigatorPager2 = findViewById(R.id.navigator_pager2_view_pager2);
//         // bnvNavigatorPager2 = findViewById(R.id.navigator_pager2_bottom_navigator);
//     }
//
//     private void setListener() {
//         bnvNavigatorPager2.setOnNavigationItemSelectedListener(this);
//         vp2NavigatorPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//             @Override
//             public void onPageSelected(int position) {
//                 MenuItem miCheck = bnvNavigatorPager2.getMenu().getItem(position);
//                 miCheck.setChecked(true);
//             }
//         });
//     }
//
//     @Override
//     public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//         int viewId = item.getItemId();
//         // if (viewId == R.id.bottom_menu_home) {
//         //     vp2NavigatorPager2.setCurrentItem(0);
//         // } else if (viewId == R.id.bottom_menu_user) {
//         //     vp2NavigatorPager2.setCurrentItem(1);
//         // }
//
//         return true;
//     }
//
//     @Override
//     public void addMenuProvider(@NonNull MenuProvider provider, @NonNull LifecycleOwner owner, @NonNull Lifecycle.State state) {
//
//     }
// }