package com.android.spochansecondversion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class TabCompetitions extends Fragment {

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_competitions, container, false);
        return root;//ОБЯЗАТЕЛЬНО ИМЕННО ТАК, ЭТО СВЯЗАНО С ФАЙЛОМ SectionPagerAdapter строка там с 38 и далее
    }
}
