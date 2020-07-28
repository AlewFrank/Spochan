package com.android.spochansecondversion.BottonNavigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.spochansecondversion.R;

public class CompetitionsFragment extends Fragment {

    public CompetitionsFragment() {
    }

    public static CompetitionsFragment newInstance() {
        return new CompetitionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competitions, container, false);

    }
}