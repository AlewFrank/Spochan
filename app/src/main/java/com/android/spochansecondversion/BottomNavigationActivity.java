package com.android.spochansecondversion;

import android.os.Bundle;
import android.view.MenuItem;

import com.android.spochansecondversion.BottonNavigation.CompetitionsFragment;
import com.android.spochansecondversion.BottonNavigation.MyProfileFragment;
import com.android.spochansecondversion.BottonNavigation.NewsFragment;
import com.android.spochansecondversion.BottonNavigation.RatingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BottomNavigationActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_rating:
                    loadFragment(RatingFragment.newInstance());
                    return true;
                case R.id.navigation_news:
                    loadFragment(NewsFragment.newInstance());
                    return true;
                case R.id.navigation_myProfile:
                    loadFragment(MyProfileFragment.newInstance());
                    return true;
                case R.id.navigation_competitions:
                    loadFragment(CompetitionsFragment.newInstance());
                    return true;
            }
            return false;
        }


    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        loadFragment(NewsFragment.newInstance());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}