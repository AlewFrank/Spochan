package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FullCompetitionItem extends AppCompatActivity {

    private TextView competitionTitleTextView, competitionLocationTextView, competitionDataTextView, competitionAddressTextView, competitionDescription;
    private TextView addressText;
    private ImageView competitionImageView;

    private String yearCompetitionDate, monthCompetitionDate, daysCompetitionDate;//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
    private String competitionImageUrl;

    private FirebaseFirestore firebaseFirestore;

    private ProgressBar progressBar;

    private String onItemClickId;

    private FirebaseStorage storage;//это надо для удаления фотографий, так как фотки хранятся в папке Storage
    private StorageReference imagesStorageReference;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    private boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_competition_item);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_competitions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_news:
                        startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_myProfile:
                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_competitions:
                        return true;
                }
                return false;
            }
        });

        competitionTitleTextView = findViewById(R.id.competitionTitleTextView);
        competitionLocationTextView = findViewById(R.id.competitionLocationTextView);
        competitionDataTextView = findViewById(R.id.competitionDataTextView);
        competitionAddressTextView = findViewById(R.id.competitionAddressTextView);
        competitionDescription = findViewById(R.id.competitionDescription);
        competitionImageView = findViewById(R.id.competitionImageView);
        addressText = findViewById(R.id.addressText);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем


        Intent competitionIntent = getIntent(); //получаем интент из CompetitionActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        onItemClickId = competitionIntent.getStringExtra("onItemClickId");

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(onItemClickId);

        competitionItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Competition competition = documentSnapshot.toObject(Competition.class);
                competitionTitleTextView.setText(competition.getCompetitionTitle());
                competitionLocationTextView.setText(competition.getCompetitionLocation());
                competitionDataTextView.setText(competition.getDaysCompetitionDate() + "." + competition.getMonthCompetitionDate() + "." + competition.getYearCompetitionDate());

                daysCompetitionDate = competition.getDaysCompetitionDate();//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
                monthCompetitionDate = competition.getMonthCompetitionDate();
                yearCompetitionDate = competition.getYearCompetitionDate();

                competitionAddressTextView.setText(competition.getCompetitionAddress());
                competitionDescription.setText(competition.getCompetitionDescription());

                competitionImageUrl = competition.getCompetitionImageUrl();//используем для удаления фотки

                Glide.with(competitionImageView.getContext())//таким образом мы загружаем изображения в наш image View
                        .load(competition.getCompetitionImageUrl())
                        .into(competitionImageView);

                if (competition.getCompetitionAddress().equals("")) {//если точного адреса нет, то чтоб не висела просто надпись "Адрес: "
                    addressText.setVisibility(View.INVISIBLE);
                }
            }
        });

        //строчек 20 вниз это настройки в зависимости от того администратор ты или нет
        final FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись

        floatingActionButton.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        String currentUserUid = currentUser.getUid();
        DocumentReference userItemDocumentReference = firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid);

        userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                isDirectorModeActivated = user.isDirector();

                if (isDirectorModeActivated) {//это должно стоять именно так, а не снаружи, так как на обработку запроса в firebase требуется какое-то время и из-за этого по-другому неправильно все работает
                    floatingActionButton.setVisibility(View.VISIBLE);
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //отправляем intent в AddCompetitionsActivity, чтоб там отредактировать значения
                Intent competitionItemIntent = new Intent(FullCompetitionItem.this, AddCompetitionsActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionItemIntent.putExtra("onItemClickId", onItemClickId); //связываем строку со значение

                startActivity(competitionItemIntent);
            }
        });


        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FullCompetitionItem.this, LogInActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
