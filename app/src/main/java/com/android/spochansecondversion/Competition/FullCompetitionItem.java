package com.android.spochansecondversion.Competition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Registration.AddMemberOfChampionship;
import com.android.spochansecondversion.Registration.MemberOfChampionship;
import com.android.spochansecondversion.Registration.RegListActivity;
import com.android.spochansecondversion.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FullCompetitionItem extends AppCompatActivity {

    private TextView competitionTitleTextView, competitionLocationTextView, competitionDataTextView, competitionAddressTextView, competitionDescription;
    private ImageView competitionImageView;

    private String yearCompetitionDate, monthCompetitionDate, daysCompetitionDate;//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
    private String competitionDate;

    private FirebaseFirestore firebaseFirestore;

    private String onItemClickId;

    private String competitionTitle;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    private Button registerOnCompetitionButton;

    private String whichRegGroupActive;

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_competition);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        bottomNavigationView.setSelectedItemId(R.id.navigation_competitions);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.navigation_rating:
//                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.navigation_news:
//                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
////                    case R.id.navigation_myProfile:
////                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
////                        overridePendingTransition(0,0);
////                        return true;
//                    case R.id.navigation_competitions:
//                        return true;
//                }
//                return false;
//            }
//        });

        competitionTitleTextView = findViewById(R.id.competitionTitleTextView);
        competitionLocationTextView = findViewById(R.id.competitionLocationTextView);
        competitionDataTextView = findViewById(R.id.competitionDataTextView);
        competitionAddressTextView = findViewById(R.id.competitionAddressTextView);
        competitionDescription = findViewById(R.id.competitionDescription);
        competitionImageView = findViewById(R.id.competitionImageView);
        registerOnCompetitionButton = findViewById(R.id.registerOnCompetitionButton);


        //устанавливаем специальный шрифт, который находится при выборе сверху слева Project, далее app/src/main/assets/fonts
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
        Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        competitionTitleTextView.setTypeface(roboto);
        competitionDataTextView.setTypeface(roboto_bold);
        competitionAddressTextView.setTypeface(roboto_bold);
        competitionDescription.setTypeface(roboto);
        competitionLocationTextView.setTypeface(roboto);



        Intent competitionIntent = getIntent(); //получаем интент из CompetitionActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        onItemClickId = competitionIntent.getStringExtra("onItemClickId");

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(onItemClickId);

        competitionItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Competition competition = documentSnapshot.toObject(Competition.class);
                competitionTitleTextView.setText(competition.getCompetitionTitle());
                competitionTitle = competition.getCompetitionTitle();//используем для регистрации пользователей на соревнования по его названию

                competitionLocationTextView.setText(competition.getCompetitionLocation());
                competitionDataTextView.setText(competition.getDaysCompetitionDate() + "." + competition.getMonthCompetitionDate() + "." + competition.getYearCompetitionDate());

                competitionDate = competition.getDaysCompetitionDate() + "." + competition.getMonthCompetitionDate() + "." + competition.getYearCompetitionDate();

                daysCompetitionDate = competition.getDaysCompetitionDate();//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
                monthCompetitionDate = competition.getMonthCompetitionDate();
                yearCompetitionDate = competition.getYearCompetitionDate();

                competitionAddressTextView.setText(competition.getCompetitionAddress());
                competitionDescription.setText(competition.getCompetitionDescription());

                if (competition.isCompetitionRegistrationActive()) {
                    registerOnCompetitionButton.setVisibility(View.VISIBLE);

                } else {
                    registerOnCompetitionButton.setVisibility(View.GONE);
                }


                Glide.with(competitionImageView.getContext())//таким образом мы загружаем изображения в наш image View
                        .load(competition.getCompetitionImageUrl())
                        .into(competitionImageView);

                if (competition.getCompetitionAddress().equals("")) {//если точного адреса нет, то чтоб не висела просто надпись "Адрес: "
                    competitionAddressTextView.setVisibility(View.GONE);//раз текста нет, то зачем тогда вообще этот текствью
                }
            }
        });



        //строчек 20 вниз это настройки в зависимости от того администратор ты или нет
//        final FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись
//        floatingActionButton.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        final String currentUserUid = currentUser.getUid();
        final DocumentReference userItemDocumentReference = firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid);

        userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                isDirectorModeActivated = user.isDirector();

                if (isDirectorModeActivated) {//это должно стоять именно так, а не снаружи, так как на обработку запроса в firebase требуется какое-то время и из-за этого по-другому неправильно все работает
                    //floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });


        registerOnCompetitionButton.setOnClickListener(new View.OnClickListener() {//выполняется, если мы нажали на кнопку с регистрацией на чемпионат
            @Override
            public void onClick(View v) {

                Intent competitionTitleIntent = new Intent(FullCompetitionItem.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionTitleIntent.putExtra("competitionTitle", competitionTitle + " ! " + competitionDate); //связываем строку со значение
                startActivity(new Intent(FullCompetitionItem.this, RegListActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_competition_item, menu);
        return true;
    }



    //ДЛЯ ОБЫЧНЫХ ПОЛЬЗОВАТЕЛЕЙ ТОЛЬКО ПЕРВЫЙ ПУНКТ ОСТАЕТСЯ
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            case R.id.menu_edit:
                //отправляем intent в AddCompetitionsActivity, чтоб там отредактировать значения
                Intent competitionItemIntent = new Intent(FullCompetitionItem.this, AddCompetitionsActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionItemIntent.putExtra("onItemClickId", onItemClickId); //связываем строку со значение
                startActivity(competitionItemIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
