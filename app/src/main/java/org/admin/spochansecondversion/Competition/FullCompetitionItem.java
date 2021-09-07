package org.admin.spochansecondversion.Competition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.admin.spochansecondversion.R;
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

    private FirebaseFirestore firebaseFirestore;

    private String onItemClickId;

    private String competitionTitle;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    private Toolbar mToolbar;

    private Button editCompetitionButton;
    private ImageView editCompetitionBackground;

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


        competitionTitleTextView = findViewById(R.id.competitionTitleTextView);
        competitionLocationTextView = findViewById(R.id.competitionLocationTextView);
        competitionDataTextView = findViewById(R.id.competitionDataTextView);
        competitionAddressTextView = findViewById(R.id.competitionAddressTextView);
        competitionDescription = findViewById(R.id.competitionDescription);
        competitionImageView = findViewById(R.id.competitionImageView);

        editCompetitionButton = findViewById(R.id.editCompetitionButton);
        editCompetitionBackground = findViewById(R.id.editCompetitionBackground);
        editCompetitionButton.setVisibility(View.GONE);
        editCompetitionBackground.setVisibility(View.GONE);

        //устанавливаем специальный шрифт, который находится при выборе сверху слева Project, далее app/src/main/assets/fonts
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Italic.ttf");
        Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        competitionTitleTextView.setTypeface(roboto);
        competitionDataTextView.setTypeface(roboto_bold);
        competitionAddressTextView.setTypeface(roboto_bold);
        competitionDescription.setTypeface(roboto);
        competitionLocationTextView.setTypeface(roboto);



        final Intent competitionIntent = getIntent(); //получаем интент из CompetitionActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        onItemClickId = competitionIntent.getStringExtra("competitionId");

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(onItemClickId);

        if (competitionIntent.getBooleanExtra("isDirectorModeActivated", false)) {
            editCompetitionButton.setVisibility(View.VISIBLE);
            editCompetitionBackground.setVisibility(View.VISIBLE);
        }

        competitionItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Competition competition = documentSnapshot.toObject(Competition.class);
                competitionTitleTextView.setText(competition.getCompetitionTitle());
                competitionTitle = competition.getCompetitionTitle();//используем для регистрации пользователей на соревнования по его названию

                competitionLocationTextView.setText(competition.getCompetitionLocation());
                competitionDataTextView.setText(competition.getDaysCompetitionDate() + "." + competition.getMonthCompetitionDate() + "." + competition.getYearCompetitionDate());


                daysCompetitionDate = competition.getDaysCompetitionDate();//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
                monthCompetitionDate = competition.getMonthCompetitionDate();
                yearCompetitionDate = competition.getYearCompetitionDate();

                competitionAddressTextView.setText(competition.getCompetitionAddress());
                competitionDescription.setText(competition.getCompetitionDescription());



                Glide.with(competitionImageView.getContext())//таким образом мы загружаем изображения в наш image View
                        .load(competition.getCompetitionImageUrl())
                        .into(competitionImageView);

                if (competition.getCompetitionAddress().equals("")) {//если точного адреса нет, то чтоб не висела просто надпись "Адрес: "
                    competitionAddressTextView.setVisibility(View.GONE);//раз текста нет, то зачем тогда вообще этот текствью
                }
            }
        });
    }


    public void editCompetitionButtonPressed(View view) {
        //отправляем intent в AddCompetitionsActivity, чтоб там отредактировать значения
        Intent competitionItemIntent = new Intent(FullCompetitionItem.this, AddCompetitionsActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        competitionItemIntent.putExtra("competitionId", onItemClickId); //связываем строку со значение
        startActivity(competitionItemIntent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                startActivity(new Intent(FullCompetitionItem.this, CompetitionsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
