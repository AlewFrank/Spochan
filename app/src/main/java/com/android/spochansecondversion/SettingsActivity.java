package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}