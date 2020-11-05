package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class ChooseGroupForRating extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group_for_rating);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
//        ActionBar actionBar = this.getSupportActionBar();
//        if(actionBar!=null){
//            actionBar.setHomeButtonEnabled(true);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
    }

    public void firstGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_1"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void secondGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_2"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void thirdGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_3"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void forthGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_4"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void fifthGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_5"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void sixGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_6"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void sevenGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_7"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void eightGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_8"); //связываем строку со значение
        startActivity(forEditActivityIntent);
    }

    public void nineGroup(View view) {
        Intent forEditActivityIntent = new Intent(ChooseGroupForRating.this, EditRatingMembers.class);
        forEditActivityIntent.putExtra("onGroupClick", "group_9"); //связываем строку со значение
        startActivity(forEditActivityIntent);
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