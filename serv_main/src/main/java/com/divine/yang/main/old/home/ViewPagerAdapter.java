// package com.divine.yang.main.old.home;
//
// import com.divine.yang.base.base.BaseFragment;
//
// import java.util.ArrayList;
//
// import androidx.annotation.NonNull;
// import androidx.annotation.Nullable;
// import androidx.fragment.app.Fragment;
// import androidx.fragment.app.FragmentManager;
// import androidx.fragment.app.FragmentPagerAdapter;
//
// /**
//  * Author: Divine
//  * CreateDate: 2021/8/4
//  * Describe:
//  */
// public class ViewPagerAdapter extends FragmentPagerAdapter {
//     private ArrayList<Fragment> fragments;
//
//     public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, ArrayList<Fragment> fragments) {
//         super(fm, behavior);
//         this.fragments = fragments;
//     }
//
//     @NonNull
//     @Override
//     public Fragment getItem(int position) {
//         return fragments.get(position);
//     }
//
//     @Override
//     public int getCount() {
//         return fragments.size();
//     }
//
//     @Nullable
//     @Override
//     public CharSequence getPageTitle(int position) {
//         BaseFragment fragment= (BaseFragment) fragments.get(position);
//         return fragment.getTitle();
//     }
// }
