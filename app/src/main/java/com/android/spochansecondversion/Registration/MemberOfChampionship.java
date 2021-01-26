package com.android.spochansecondversion.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.AddCompetitionsActivity;
import com.android.spochansecondversion.Competition.Competition;
import com.android.spochansecondversion.Competition.FullCompetitionItem;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.Group;
import com.android.spochansecondversion.Rating.GroupAdapter;
import com.android.spochansecondversion.Rating.RatingAdapter;
import com.android.spochansecondversion.User;
import com.android.spochansecondversion.logInSignUp.LogInActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MemberOfChampionship extends AppCompatActivity {

    private TextView userFirstNameTextView, userSecondNameTextView, userBornDateTextView, userSexTextView, userClubTextView, userGroupTextView, userCertificationTextView;
    private String daysBornDate, monthBornDate, yearBornDate, сoachId, currentUserUid;
    private Button payButton, comeOnButton;
    private boolean payButtonCounter = false;
    private boolean comeOnButtonCounter = false;

    private String userId, competitionTitle, competitionId;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private Toolbar mToolbar;

    private DatabaseReference usersDataBaseReference;
    private ChildEventListener usersChildEventListener;

    private boolean isDirectorModeActivated = false;
    private boolean isCoachModeActivated = false;



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_of_championship);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent userIntent = getIntent(); //получаем интент из RegListActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        userId = userIntent.getStringExtra("UserId");
        competitionTitle = userIntent.getStringExtra("competitionTitle");
        competitionId = userIntent.getStringExtra("competitionId");//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена

        userFirstNameTextView = findViewById(R.id.userFirstNameTextView);
        userFirstNameTextView.setText(userIntent.getStringExtra("firstName"));

        userSecondNameTextView = findViewById(R.id.userSecondNameTextView);
        userSecondNameTextView.setText(userIntent.getStringExtra("secondName"));

        userBornDateTextView = findViewById(R.id.userBornDateTextView);
        userBornDateTextView.setText(userIntent.getStringExtra("bornDate"));
        daysBornDate = userIntent.getStringExtra("daysBornDate");
        monthBornDate = userIntent.getStringExtra("monthBornDate");
        yearBornDate = userIntent.getStringExtra("yearBornDate");

        userSexTextView = findViewById(R.id.userSexTextView);
        userSexTextView.setText(userIntent.getStringExtra("sex"));

        userClubTextView = findViewById(R.id.userClubTextView);
        userClubTextView.setText(userIntent.getStringExtra("userClub"));

        userGroupTextView = findViewById(R.id.userGroupTextView);
        userGroupTextView.setText(userIntent.getStringExtra("userGroup"));

        userCertificationTextView = findViewById(R.id.userCertificationTextView);
        userCertificationTextView.setText(userIntent.getStringExtra("userCertification"));

        сoachId = userIntent.getStringExtra("сoachId");

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        currentUserUid = currentUser.getUid(); //это для определения айди тренера или админа, который сейчас работает

        isDirectorModeActivated = userIntent.getBooleanExtra("isDirector", false);

        payButton = findViewById(R.id.payButton);
        comeOnButton = findViewById(R.id.comeOnButton);

        if (userIntent.getBooleanExtra("hasComeOn", false)) {
            comeOnButton.setBackground(getResources().getDrawable(R.drawable.button_main));
            comeOnButtonCounter = true;
        }

        if (userIntent.getBooleanExtra("hasPayed", false)) {
            payButton.setBackground(getResources().getDrawable(R.drawable.button_main));
            payButtonCounter = true;
        }

        usersDataBaseReference = FirebaseDatabase.getInstance().getReference().child(competitionTitle).child(userId);
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public void payButtonPressed(View view) {

        if (isDirectorModeActivated) { //чтобы только админ мог нажимать на эти кнопки
            if(!payButtonCounter) {
                payButton.setBackground(getResources().getDrawable(R.drawable.button_main));
                payButtonCounter = true;
            } else {
                payButton.setBackground(getResources().getDrawable(R.drawable.button_main_enabled));
                payButtonCounter = false;
            }

            //usersDataBaseReference.update("hasPayed", payButtonCounter);  это было раньше, когда с файерстор работали
            usersDataBaseReference.child("hasPayed").setValue(payButtonCounter);
        } else {
            Toast.makeText(MemberOfChampionship.this, getResources().getString(R.string.notAllowed), Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void comeOnButtonPressed(View view) {

        if (isDirectorModeActivated) { //чтобы только админ мог нажимать на эти кнопки
            if(!comeOnButtonCounter) {
                comeOnButton.setBackground(getResources().getDrawable(R.drawable.button_main));
                comeOnButtonCounter = true;
            } else {
                comeOnButton.setBackground(getResources().getDrawable(R.drawable.button_main_enabled));
                comeOnButtonCounter = false;
            }

            usersDataBaseReference.child("hasComeOn").setValue(comeOnButtonCounter);
        } else {
            Toast.makeText(MemberOfChampionship.this, getResources().getString(R.string.notAllowed), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_competition_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                Intent competitionTitleIntent = new Intent(MemberOfChampionship.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionTitleIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
                competitionTitleIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
                startActivity(competitionTitleIntent);
                return true;
            case R.id.menu_edit:

                if (isDirectorModeActivated || сoachId.equals(currentUserUid)) {

                    Intent userIntent = new Intent(MemberOfChampionship.this, AddMemberOfChampionship.class);

                    userIntent.putExtra("competitionId", competitionId);
                    userIntent.putExtra("competitionTitle", competitionTitle);

                    userIntent.putExtra("isFull", "true");
                    userIntent.putExtra("userId", userId);
                    userIntent.putExtra("firstName", userFirstNameTextView.getText() + "");

                    userIntent.putExtra("secondName", userSecondNameTextView.getText() + "");
                    userIntent.putExtra("sex", userSexTextView.getText() + "");
                    userIntent.putExtra("userClub", userClubTextView.getText() + "");
                    userIntent.putExtra("userGroup", userGroupTextView.getText() + "");
                    userIntent.putExtra("userCertification", userCertificationTextView.getText() + "");

                    userIntent.putExtra("daysBornDate", daysBornDate);
                    userIntent.putExtra("monthBornDate", monthBornDate);
                    userIntent.putExtra("yearBornDate", yearBornDate);

                    userIntent.putExtra("hasComeOn", comeOnButtonCounter);
                    userIntent.putExtra("hasPayed", payButtonCounter);

                    userIntent.putExtra("сoachId", сoachId);

                    startActivity(userIntent);

//                Intent competitionItemIntent = new Intent(MemberOfChampionship.this, AddMemberOfChampionship.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
//                competitionItemIntent.putExtra("competitionId", onItemClickId); //связываем строку со значение
//                startActivity(competitionItemIntent);
                } else {
                    Toast.makeText(MemberOfChampionship.this, getResources().getString(R.string.wrongSportsman), Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}