package com.android.spochansecondversion.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.AddCompetitionsActivity;
import com.android.spochansecondversion.Competition.CompetitionsActivity;
import com.android.spochansecondversion.EditMyProfileActivity;
import com.android.spochansecondversion.MyProfileActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMemberOfChampionship extends AppCompatActivity {

    private EditText firstNameEditText, secondNameEditText, userSexEditText, userClubEditText;
    private EditText daysBornDateEditText, monthBornDateEditText, yearBornDateEditText;
    private RadioButton group_1, group_2, group_3, group_4, group_5, group_6, group_7, group_8, group_9;

    private FirebaseFirestore firebaseFirestore;

    private String competitionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_of_championship);

        Intent competitionTitleIntent = getIntent(); //получаем интент из FullCompetitionItem, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        competitionTitle = competitionTitleIntent.getStringExtra("competitionTitle");

        firstNameEditText = findViewById(R.id.firstNameEditText);
        secondNameEditText = findViewById(R.id.secondNameEditText);
        userSexEditText = findViewById(R.id.userSexEditText);
        userClubEditText = findViewById(R.id.userClubEditText);

        daysBornDateEditText = findViewById(R.id.daysBornDateEditText);
        monthBornDateEditText = findViewById(R.id.monthBornDateEditText);
        yearBornDateEditText = findViewById(R.id.yearBornDateEditText);

        group_1 = findViewById(R.id.group_1);
        group_2 = findViewById(R.id.group_2);
        group_3 = findViewById(R.id.group_3);
        group_4 = findViewById(R.id.group_4);
        group_5 = findViewById(R.id.group_5);
        group_6 = findViewById(R.id.group_6);
        group_7 = findViewById(R.id.group_7);
        group_8 = findViewById(R.id.group_8);
        group_9 = findViewById(R.id.group_9);


        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void cancelButton(View view) {
        startActivity(new Intent(AddMemberOfChampionship.this, RegListActivity.class));
    }

    public void addButton(View view) {

        User user = new User();

        String userName = firstNameEditText.getText().toString().trim();
        String userSurname = secondNameEditText.getText().toString().trim();
        String userSex = userSexEditText.getText().toString().trim();
        String userClub = userClubEditText.getText().toString().trim();

        String userDaysBorn = daysBornDateEditText.getText().toString().trim();
        String userMonthsBorn = monthBornDateEditText.getText().toString().trim();
        String userYearsBorn = yearBornDateEditText.getText().toString().trim();

        String userGroup = createGroupString();



        //если все поля у нас заполнены
        if (!userName.equals("") & !userSurname.equals("") & !userSex.equals("") & !userClub.equals("") & !userDaysBorn.equals("") & !userMonthsBorn.equals("") & !userYearsBorn.equals("") & isGroupChoose()) {

            user.setFirstName(userName);
            user.setSecondName(userSurname);
            user.setSex(userSex);
            user.setUserClub(userClub);
            user.setDaysBornDate(userDaysBorn);
            user.setMonthBornDate(userMonthsBorn);
            user.setYearBornDate(userYearsBorn);
            user.setUserGroup(userGroup);

            firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection(competitionTitle).document(secondNameEditText.getText().toString().trim() + "." + firstNameEditText.getText().toString().trim() + "." + daysBornDateEditText.getText().toString().trim() + "." + monthBornDateEditText.getText().toString().trim() + "." + yearBornDateEditText.getText().toString().trim()).set(user); //daysBornDateEditText.getText().toString().trim() и следующее  нужно для того, что если однофамильцы, то чтоб они не удаляли друг друга, а ставились рядом
            Toast.makeText(AddMemberOfChampionship.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddMemberOfChampionship.this, getResources().getString(R.string.fields_notification), Toast.LENGTH_LONG).show();
        }




      }

    private boolean isGroupChoose() {

        if (group_1.isChecked() || group_2.isChecked() || group_3.isChecked() || group_4.isChecked() || group_5.isChecked() || group_6.isChecked() || group_7.isChecked() || group_8.isChecked() || group_9.isChecked()) {
            return true;
        } else {
            return false;
        }
    }

    private String createGroupString() {

        String userGroup = "";

        if (group_1.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_1);
        } else if (group_2.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_2);
        } else if (group_3.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_3);
        } else if (group_4.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_4);
        } else if (group_5.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_5);
        } else if (group_6.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_6);
        } else if (group_7.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_7);
        } else if (group_8.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_8);
        } else if (group_9.isChecked()) {
            userGroup = userGroup + "\n" + getResources().getString(R.string.group_9);
        }

        return userGroup;
    }
}