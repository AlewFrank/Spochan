package com.android.spochansecondversion.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.spochansecondversion.Competition.AddCompetitionsActivity;
import com.android.spochansecondversion.Competition.CompetitionsActivity;
import com.android.spochansecondversion.Competition.FullCompetitionItem;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.Group;
import com.android.spochansecondversion.Rating.GroupAdapter;
import com.android.spochansecondversion.Rating.RatingAdapter;

import java.util.ArrayList;

public class FiltrationActivity extends Activity  { //после extends заменил слово на Activity, чтоб можно было поменять стиль этой активити и при этом не менять стиль остального приложения

    private RecyclerView.Adapter groupAdapter;
    private  String group;
    private  String groupTitle;

    private EditText firstNameEditText, secondNameEditText, daysEditText, monthsEditText, yearsEditText;
    private String firstName, secondName, days, months, years;

    private String competitionTitle;









    //шрифт roboto установить на все надписи, включая текста кнопок и hint у EditText
    
    
    
    
    
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtration);


        RecyclerView groupListRecycleView = findViewById(R.id.groupListRecycleView);
        groupListRecycleView.setVisibility(View.VISIBLE);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        secondNameEditText = findViewById(R.id.secondNameEditText);
        daysEditText = findViewById(R.id.daysEditText);
        monthsEditText = findViewById(R.id.monthsEditText);
        yearsEditText = findViewById(R.id.yearsEditText);

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
        groupAdapter = new FiltrationAdapter(groups, this);
        groupListRecycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        groupListRecycleView.setAdapter(groupAdapter);






        Intent intent = getIntent(); //отправляем его из FiltrationAdapter
        if (intent != null) {//так обозначается "не равен"
            group = intent.getStringExtra("index");
            groupTitle = intent.getStringExtra("title");
        }

//        if (group != null) {//так обозначается "не равен"
//
//        } else {
//
//        }

        if (intent != null) {
            competitionTitle = intent.getStringExtra("competitionTitle");
        }
    }


    public void cancelButton(View view) {
        startActivity(new Intent(FiltrationActivity.this, RegListActivity.class));
    }

    public void okButton(View view) {
        Intent filtrationIntent = new Intent(FiltrationActivity.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу

        firstName = firstNameEditText.getText().toString().trim();
        secondName = secondNameEditText.getText().toString().trim();
        days = daysEditText.getText().toString().trim();
        months = monthsEditText.getText().toString().trim();
        years = yearsEditText.getText().toString().trim();

        if (firstName!=null) {
            filtrationIntent.putExtra("firstName", firstName);
        }

        if (secondName!=null) {
            filtrationIntent.putExtra("secondName", secondName);
        }

        if (days!=null) {
            filtrationIntent.putExtra("days", days);
        }

        if (months!=null) {
            filtrationIntent.putExtra("months", months);
        }

        if (years!=null) {
            filtrationIntent.putExtra("years", years);
        }

        filtrationIntent.putExtra("competitionTitle", competitionTitle);

        Log.d("Fuck", firstName);

        startActivity(filtrationIntent);
    }
}