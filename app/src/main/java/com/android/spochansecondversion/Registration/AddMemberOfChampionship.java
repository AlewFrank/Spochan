package com.android.spochansecondversion.Registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.AddCompetitionsActivity;
import com.android.spochansecondversion.Competition.CompetitionsActivity;
import com.android.spochansecondversion.EditMyProfileActivity;
import com.android.spochansecondversion.MyProfileActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.Group;
import com.android.spochansecondversion.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class AddMemberOfChampionship extends AppCompatActivity {

    private EditText firstNameEditText, secondNameEditText, userSexEditText, userClubEditText, userCertificationEditView;
    private EditText daysBornDateEditText, monthBornDateEditText, yearBornDateEditText;
    private RadioButton group_1, group_2, group_3, group_4, group_5, group_6, group_7, group_8, group_9;
    private boolean isFull = false;

    private String competitionTitle, competitionId, groups, userId, сoachId;

    private ArrayList<Integer> buttons;

    private DatabaseReference usersDataBaseReference;

    private boolean payButtonCounter = false;
    private boolean comeOnButtonCounter = false;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_of_championship);

        Intent userIntent = getIntent(); //получаем интент из FullCompetitionItem, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        competitionTitle = userIntent.getStringExtra("competitionTitle");
        competitionId = userIntent.getStringExtra("competitionId");

        firstNameEditText = findViewById(R.id.userNameEditText);
        secondNameEditText = findViewById(R.id.userSurnameEditText);
        userSexEditText = findViewById(R.id.userSexEditText);
        userClubEditText = findViewById(R.id.userClubEditText);
        userCertificationEditView = findViewById(R.id.userCertificationEditView);

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

        //создаем массив с кнопками, чтоб потом использовать это в переключении кнопок
        buttons = new ArrayList<>();
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);
        buttons.add(0);


        if (userIntent.getStringExtra("isFull") != null) { //получаем из MemberOfChampionship
            isFull = true;

            userId = userIntent.getStringExtra("userId");

            firstNameEditText.setText(userIntent.getStringExtra("firstName"));
            secondNameEditText.setText(userIntent.getStringExtra("secondName"));
            userSexEditText.setText(userIntent.getStringExtra("sex"));
            userClubEditText.setText(userIntent.getStringExtra("userClub"));
            userCertificationEditView.setText(userIntent.getStringExtra("userCertification"));

            daysBornDateEditText.setText(userIntent.getStringExtra("daysBornDate"));
            monthBornDateEditText.setText(userIntent.getStringExtra("monthBornDate"));
            yearBornDateEditText.setText(userIntent.getStringExtra("yearBornDate"));

            сoachId = userIntent.getStringExtra("сoachId");

            if (userIntent.getBooleanExtra("hasComeOn", false)) {
                comeOnButtonCounter = true;
            }

            if (userIntent.getBooleanExtra("hasPayed", false)) {
                payButtonCounter = true;
            }

            groups = userIntent.getStringExtra("userGroup");

            chooseRadioButtonIfEdit(groups);

        }

        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    public void cancelButton(View view) {
        Intent competitionTitleIntent = new Intent(AddMemberOfChampionship.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        competitionTitleIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
        competitionTitleIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
        startActivity(competitionTitleIntent);
    }

    public void addButton(View view) {

        User user = new User();

        usersDataBaseReference = FirebaseDatabase.getInstance().getReference().child(competitionTitle);

        String userName = firstNameEditText.getText().toString().trim();
        String userSurname = secondNameEditText.getText().toString().trim();
        String userSex = userSexEditText.getText().toString().trim();
        String userClub = userClubEditText.getText().toString().trim();
        String userCertification = userCertificationEditView.getText().toString().trim();

        String userDaysBorn = daysBornDateEditText.getText().toString().trim();
        String userMonthsBorn = monthBornDateEditText.getText().toString().trim();
        String userYearsBorn = yearBornDateEditText.getText().toString().trim();

        String userGroup = createGroupString();



        //если все поля у нас заполнены
        if (!userName.equals("") & !userSurname.equals("") & !userSex.equals("") & !userClub.equals("") & !userDaysBorn.equals("") & !userMonthsBorn.equals("") & !userYearsBorn.equals("") & !userCertification.equals("") & isGroupChoose()) {


            if (!isFull) {//создаем нового человека
                userId = secondNameEditText.getText().toString().trim() + firstNameEditText.getText().toString().trim() + daysBornDateEditText.getText().toString().trim() + monthBornDateEditText.getText().toString().trim() + yearBornDateEditText.getText().toString().trim();

                firebaseFirestore = FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                currentUser = auth.getCurrentUser();
                сoachId = currentUser.getUid(); //это для определения айди тренера или админа, который сейчас создает нового человека
            }


            user.setFirstName(userName);
            user.setSecondName(userSurname);
            user.setSex(userSex);
            user.setUserClub(userClub);
            user.setDaysBornDate(userDaysBorn);
            user.setMonthBornDate(userMonthsBorn);
            user.setYearBornDate(userYearsBorn);
            user.setUserGroup(userGroup);
            user.setUserCertification(userCertification);
            user.setHasComeOn(comeOnButtonCounter);
            user.setHasPayed(payButtonCounter);
            user.setUserId(userId);
            user.setCoachId(сoachId);

            usersDataBaseReference.child(userId).setValue(user);


            //firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection(competitionTitle).document(secondNameEditText.getText().toString().trim() + "." + firstNameEditText.getText().toString().trim() + "." + daysBornDateEditText.getText().toString().trim() + "." + monthBornDateEditText.getText().toString().trim() + "." + yearBornDateEditText.getText().toString().trim()).set(user); //daysBornDateEditText.getText().toString().trim() и следующее  нужно для того, что если однофамильцы, то чтоб они не удаляли друг друга, а ставились рядом
            Toast.makeText(AddMemberOfChampionship.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
            //startActivity(new Intent(AddMemberOfChampionship.this, RegListActivity.class));
            Intent competitionTitleIntent = new Intent(AddMemberOfChampionship.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
            competitionTitleIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
            competitionTitleIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
            startActivity(competitionTitleIntent);
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
            userGroup = userGroup + getResources().getString(R.string.group_1) + "\n";
        }
        if (group_2.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_2) + "\n";
        }
        if (group_3.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_3) + "\n";
        }
        if (group_4.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_4) + "\n";
        }
        if (group_5.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_5) + "\n";
        }
        if (group_6.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_6) + "\n";
        }
        if (group_7.isChecked()) {
            userGroup = userGroup +  getResources().getString(R.string.group_7) + "\n";
        }
        if (group_8.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_8) + "\n";
        }
        if (group_9.isChecked()) {
            userGroup = userGroup + getResources().getString(R.string.group_9) + "\n";
        }

        return userGroup.trim();
    }

    //смысл этих функции в том, чтобы при повторном нажатии на кнопку, выбор категории отменялся
    public void onClickForRadioButtons1(View view) {
        unCheckButton(group_1, 0);
    }

    public void onClickForRadioButtons2(View view) {
        unCheckButton(group_2, 1);
    }

    public void onClickForRadioButtons3(View view) {
        unCheckButton(group_3, 2);
    }

    public void onClickForRadioButtons4(View view) {
        unCheckButton(group_4, 3);
    }

    public void onClickForRadioButtons5(View view) {
        unCheckButton(group_5, 4);
    }

    public void onClickForRadioButtons6(View view) {
        unCheckButton(group_6, 5);
    }

    public void onClickForRadioButtons7(View view) {
        unCheckButton(group_7, 6);
    }

    public void onClickForRadioButtons8(View view) {
        unCheckButton(group_8, 7);
    }

    public void onClickForRadioButtons9(View view) {
        unCheckButton(group_9, 8);
    }


    private void unCheckButton(RadioButton button, int index) {

        Integer buttonIndex = buttons.get(index);

        if(buttonIndex == 0) {
            button.setChecked(true);
            buttons.set(index, 1);
        } else {
            button.setChecked(false);
            buttons.set(index, 0);
        }
    }

    private void chooseRadioButtonIfEdit(String groups) {

        if (groups.contains(getResources().getString(R.string.group_1))) {
            group_1.setChecked(true);
            unCheckButton(group_1, 0);
        } if (groups.contains(getResources().getString(R.string.group_2))) {
            group_2.setChecked(true);
            unCheckButton(group_2, 1);
        } if (groups.contains(getResources().getString(R.string.group_3))) {
            group_3.setChecked(true);
            unCheckButton(group_3, 2);
        } if (groups.contains(getResources().getString(R.string.group_4))) {
            group_4.setChecked(true);
            unCheckButton(group_4, 3);
        } if (groups.contains(getResources().getString(R.string.group_5))) {
            group_5.setChecked(true);
            unCheckButton(group_5, 4);
        } if (groups.contains(getResources().getString(R.string.group_6))) {
            group_6.setChecked(true);
            unCheckButton(group_6, 5);
        } if (groups.contains(getResources().getString(R.string.group_7))) {
            group_7.setChecked(true);
            unCheckButton(group_7, 6);
        } if (groups.contains(getResources().getString(R.string.group_8))) {
            group_8.setChecked(true);
            unCheckButton(group_8, 7);
        } if (groups.contains(getResources().getString(R.string.group_9))) {
            group_9.setChecked(true);
            unCheckButton(group_9, 8);
        }
    }
}