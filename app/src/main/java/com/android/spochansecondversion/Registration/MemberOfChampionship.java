package com.android.spochansecondversion.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.Group;
import com.android.spochansecondversion.Rating.GroupAdapter;
import com.android.spochansecondversion.Rating.RatingAdapter;

import java.util.ArrayList;

public class MemberOfChampionship extends AppCompatActivity {

    private TextView userFirstNameTextView, userSecondNameTextView, userBornDateTextView, userSexTextView, userClubTextView;
    private String bornDate;
    private Button payButton, comeOnButton;

    private RecyclerView.Adapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_of_championship);

        userFirstNameTextView = findViewById(R.id.userFirstNameTextView);
        userSecondNameTextView = findViewById(R.id.userSecondNameTextView);
        userBornDateTextView = findViewById(R.id.userBornDateTextView);
        userSexTextView = findViewById(R.id.userSexTextView);
        userClubTextView = findViewById(R.id.userClubTextView);

        payButton = findViewById(R.id.payButton);
        comeOnButton = findViewById(R.id.comeOnButton);

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
        groupListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        groupListRecycleView.setAdapter(groupAdapter);
    }
}