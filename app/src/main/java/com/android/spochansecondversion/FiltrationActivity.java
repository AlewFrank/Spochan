package com.android.spochansecondversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.android.spochansecondversion.Rating.Group;
import com.android.spochansecondversion.Rating.GroupAdapter;
import com.android.spochansecondversion.Rating.RatingAdapter;

import java.util.ArrayList;

public class FiltrationActivity extends Activity /*implements GroupAdapter.OnListItemClick*/ { //после extends заменил слово на Activity, чтоб можно было поменять стиль этой активити и при этом не менять стиль остального приложения

    private RecyclerView.Adapter groupAdapter;
    private  String group;
    private  String groupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtration);

        RecyclerView groupListRecycleView = findViewById(R.id.groupListRecycleView);

        ArrayList<Group> groups = new ArrayList<>();

        groups.add(new Group(getResources().getString(R.string.group_1), "group_1"));
        groups.add(new Group(getResources().getString(R.string.group_2), "group_2"));
        groups.add(new Group(getResources().getString(R.string.group_3), "group_3"));
        groups.add(new Group(getResources().getString(R.string.group_4), "group_4"));
        groups.add(new Group(getResources().getString(R.string.group_5), "group_5"));
        groups.add(new Group(getResources().getString(R.string.group_6), "group_6"));
        groups.add(new Group(getResources().getString(R.string.group_7), "group_7"));
        groups.add(new Group(getResources().getString(R.string.group_8), "group_8"));
        groups.add(new Group(getResources().getString(R.string.group_9), "group_9"));

        groupListRecycleView.setHasFixedSize(true);
        groupAdapter = new GroupAdapter(groups, this);
        groupListRecycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        groupListRecycleView.setAdapter(groupAdapter);
    }

    //шрифт roboto установить на все надписи, включая текста кнопок и hint у EditText



}