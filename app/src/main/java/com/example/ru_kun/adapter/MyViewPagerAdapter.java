package com.example.ru_kun.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.ru_kun.fragmentRiwayat.DitolakFragment;
import com.example.ru_kun.fragmentRiwayat.SedangDiprosesFragment;
import com.example.ru_kun.fragmentRiwayat.SelesaiFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new SedangDiprosesFragment();
            case 1:
                return new SelesaiFragment();
            case 2:
                return new DitolakFragment();
            default:
                return new SedangDiprosesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}