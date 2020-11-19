package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spochansecondversion.News.NewsActivity;
import com.android.spochansecondversion.Rating.RatingActivity;
import com.android.spochansecondversion.logInSignUp.LogInActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FullCompetitionItem extends AppCompatActivity {

    private TextView competitionTitleTextView, competitionLocationTextView, competitionDataTextView, competitionAddressTextView, competitionDescription;
    private TextView addressText;
    private TextView openRegListTextView;
    private ImageView competitionImageView;

    private String yearCompetitionDate, monthCompetitionDate, daysCompetitionDate;//это нужно для того, что айди удаляемого(если захотим удалить соревнование) изображения знать
    private String competitionDate;

    private FirebaseFirestore firebaseFirestore;

    private ProgressBar progressBar;

    private String onItemClickId;

    private String competitionTitle;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    private Button registerOnCompetitionButton;

    private String whichRegGroupActive;

    private LinearLayout radioButtonsLinearLayout;
    private RadioButton firstGroupRadioButton, secondGroupRadioButton, thirdGroupRadioButton, forthGroupRadioButton, fifthGroupRadioButton, sixGroupRadioButton, sevenGroupRadioButton, eightGroupRadioButton, nineGroupRadioButton;

    String[] addresses = {"26bas@mail.ru"};
    String subject_help = "Help"; //тема письма для помощи
    String subject_developer = "Hello developer"; //тема письма для связи с разработчиком
    String emailtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_competition_item);












        //сделать, чтоб при прокрутке изображение главное убиралось точно так же, как и в приложении про toolbar, которое я из интернета скачал




















        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

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
//                    case R.id.navigation_myProfile:
//                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
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
        openRegListTextView = findViewById(R.id.openRegListTextView);
        registerOnCompetitionButton = findViewById(R.id.registerOnCompetitionButton);


        radioButtonsLinearLayout = findViewById(R.id.radioButtonsLinearLayout);

        firstGroupRadioButton = findViewById(R.id.firstGroupRadioButton);
        secondGroupRadioButton = findViewById(R.id.secondGroupRadioButton);
        thirdGroupRadioButton = findViewById(R.id.thirdGroupRadioButton);
        forthGroupRadioButton = findViewById(R.id.forthGroupRadioButton);
        fifthGroupRadioButton = findViewById(R.id.fifthGroupRadioButton);
        sixGroupRadioButton = findViewById(R.id.sixGroupRadioButton);
        sevenGroupRadioButton = findViewById(R.id.sevenGroupRadioButton);
        eightGroupRadioButton = findViewById(R.id.eightGroupRadioButton);
        nineGroupRadioButton = findViewById(R.id.nineGroupRadioButton);


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем


        radioButtonsLinearLayout.setVisibility(View.GONE);//в разметке и так выключена, но на всякий случай и здесь еще раз при создании выключаем
        openRegListTextView.setVisibility(View.GONE);//на всякий случай тоже изначально выключим, хоть это и есть в разметке


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
                    openRegListTextView.setVisibility(View.VISIBLE);

                } else {
                    registerOnCompetitionButton.setVisibility(View.GONE);
                    openRegListTextView.setVisibility(View.GONE);
                }


                Glide.with(competitionImageView.getContext())//таким образом мы загружаем изображения в наш image View
                        .load(competition.getCompetitionImageUrl())
                        .into(competitionImageView);

                if (competition.getCompetitionAddress().equals("")) {//если точного адреса нет, то чтоб не висела просто надпись "Адрес: "
                    addressText.setVisibility(View.GONE);
                    competitionAddressTextView.setVisibility(View.GONE);//раз текста нет, то зачем тогда вообще этот текствью
                }
            }
        });



        //строчек 20 вниз это настройки в зависимости от того администратор ты или нет
        final FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись

        floatingActionButton.setVisibility(View.GONE);

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


        registerOnCompetitionButton.setOnClickListener(new View.OnClickListener() {//выполняется, если мы нажали на кнопку с регистрацией на чемпионат
            @Override
            public void onClick(View v) {


                openRegListTextView.setVisibility(View.GONE);//просто чтоб не мешалась

                if (whichRegGroupActive != null) {//то есть группа уже выбрана
                    userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {//получаем текущего пользователя, чтоб отправить его данные в список зарегестрированных пользователей
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            User user = documentSnapshot.toObject(User.class);


                            //чтоб у человека были заполнены все поля, а не только имя и фамилия
                            if (!user.getSex().equals("") & !user.getDaysBornDate().equals("") & !user.getMonthBornDate().equals("") & !user.getYearBornDate().equals("")) {

                                //таким образом сохраняем пользователей в коллекцию с названием соревнований и документ по их группам + сохраняем их еще и по списку в зависимсоти от имени и фамилии, но также и добавляем айди, чтоб если есть полные тески, то все тоже работало
                                firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle + "." + competitionDate).collection(whichRegGroupActive).document(user.getSecondName() + "." + user.getFirstName() + "." + user.getId()).set(user);

                                Toast.makeText(FullCompetitionItem.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(FullCompetitionItem.this, CompetitionsActivity.class));//ни в коем случае не делай здесь переход в FullCompetitionItem,так как при переходе сюда не из CompetitionActivity переменная onItemClickId будет равна нулю и соответственно будет все вылетать, так как информация по соревнованию не прогрузится по пустой ссылке

                            } else {
                                Toast.makeText(FullCompetitionItem.this, getResources().getString(R.string.fill_your_profile), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {//если человек только намеревается выбрать группу
                    radioButtonsLinearLayout.setVisibility(View.VISIBLE);
                    registerOnCompetitionButton.setText(getResources().getString(R.string.push));

                    competitionAddressTextView.setVisibility(View.GONE);//чтоб не мешалось
                    addressText.setVisibility(View.GONE);
                    competitionDescription.setVisibility(View.GONE);
                }



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
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FullCompetitionItem.this, LogInActivity.class));
                return true;
            case R.id.menu_ask_developer:
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, addresses); //вводим сверху переменные addresses и subject
                intent.putExtra(Intent.EXTRA_SUBJECT, subject_developer);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            case R.id.menu_help:
                emailtext = getResources().getString(R.string.help_email);
                Intent intent_help = new Intent(Intent.ACTION_SENDTO);
                intent_help.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent_help.putExtra(Intent.EXTRA_EMAIL, addresses); //вводим сверху переменные addresses и subject
                intent_help.putExtra(Intent.EXTRA_SUBJECT, subject_help);
                intent_help.putExtra(Intent.EXTRA_TEXT, emailtext);//текст сообщения
                if (intent_help.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent_help);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openRegList(View view) { //онклик метод для нашего текста Открыть список зарегестрировавшихся
        Intent competitionTitleIntent = new Intent(FullCompetitionItem.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        competitionTitleIntent.putExtra("competitionTitleAndCompetitionDate", competitionTitle + "." + competitionDate); //связываем строку со значение

        startActivity(competitionTitleIntent);
    }


    public void onRadioButtonClick(View view) {//оклик метод для наших кнопок по выбору группы для регистрации
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();


        // Check which radio button was clicked
        if (checked) {
            switch(view.getId()) {
                case R.id.firstGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_1";
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.secondGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_2";
                    firstGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.thirdGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_3";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.forthGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_4";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.fifthGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_5";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.sixGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_6";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.sevenGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_7";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.eightGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_8";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    nineGroupRadioButton.setChecked(false);
                    break;
                case R.id.nineGroupRadioButton:
                    if (checked)
                        whichRegGroupActive = "group_9";
                    firstGroupRadioButton.setChecked(false);
                    secondGroupRadioButton.setChecked(false);
                    thirdGroupRadioButton.setChecked(false);
                    forthGroupRadioButton.setChecked(false);
                    fifthGroupRadioButton.setChecked(false);
                    sixGroupRadioButton.setChecked(false);
                    sevenGroupRadioButton.setChecked(false);
                    eightGroupRadioButton.setChecked(false);
                    break;
            }
        }
    }
}
