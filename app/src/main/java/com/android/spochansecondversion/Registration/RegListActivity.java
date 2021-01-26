package com.android.spochansecondversion.Registration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.CompetitionsActivity;
import com.android.spochansecondversion.Competition.FullCompetitionItem;
import com.android.spochansecondversion.FiltrationActivity;
import com.android.spochansecondversion.News.AddNewsActivity;
import com.android.spochansecondversion.News.NewsActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.RatingAdapter;
import com.android.spochansecondversion.User;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class RegListActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private RecyclerView sportsmenListRecycleView;

    private RegListAdapter adapter;

    private String competitionTitle, competitionId;
    private String condition;

    private Toolbar mToolbar;

    private int userCounter = 0;

    private TextView numberOfUsers;

    private Button allMembersButton, comeMembersButton, payMembersButton;

    private Query query;

    private User user;

    private User specialUser;

    private DatabaseReference usersDataBaseReference;//если что-то не понятно то смотри в ChatActivity или в SignInActivity, там уже это разбиралось и не раз, это если что из приложения AwesomeChat
    private ChildEventListener usersChildEventListener;
    private RecyclerView.LayoutManager userLayoutManager;
    private ArrayList<User> userArrayList;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated = false;
    private boolean isCoachModeActivated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_list);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //случай, когда переходим из AddMemberOfChampionship и когда из FullCompetitionActivity, то есть для двух случаев юзаем один и тот же интент
        Intent competitionTitleIntent = getIntent(); //получаем интент из FullCompetitionItem, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        competitionTitle = competitionTitleIntent.getStringExtra("competitionTitle");
        competitionId = competitionTitleIntent.getStringExtra("competitionId");//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
        condition = "All";

        if (competitionTitleIntent.getStringExtra("Condition") != null) {
            condition = competitionTitleIntent.getStringExtra("Condition"); //это значение изымаем, если выбрали какую-то фильтрацию людей
        }
        numberOfUsers = findViewById(R.id.numberOfUsers);


        //createRecycleView("PayOnly");


        final FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка открывающая доступ к добавлению человека
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent competitionTitleIntent = new Intent(RegListActivity.this, AddMemberOfChampionship.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionTitleIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
                competitionTitleIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
                startActivity(competitionTitleIntent);
            }
        });

        userArrayList = new ArrayList<>();

        attachUserDatabaseReferenceListener(condition);//прикрепить листенер к датабейс референс

        buildRecycleView();

    }


    private void attachUserDatabaseReferenceListener(final String condition) {
        usersDataBaseReference = FirebaseDatabase.getInstance().getReference().child(competitionTitle);
        if (usersChildEventListener == null) {//чтоб не создавать миллиард event listener, а создаем только тогда, когда он не существует
            usersChildEventListener = new ChildEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    User user = snapshot.getValue(User.class);
                    //userArrayList.clear();//очищаем ArrayList, чтоб при изменении параметров настроек у нас люди заново выводились
                    //userCounter = 0;

                    if (condition.equals("All")) {
                        userArrayList.add(user);
                        userCounter ++; //счетчик людей
                        numberOfUsers.setText("Всего: " + userCounter);
                    } else if (condition.equals("PayOnly")) {
                        if (user != null && user.isHasPayed()) {
                            userArrayList.add(user);
                            userCounter ++; //счетчик людей
                            numberOfUsers.setText("Оплатили: " + userCounter);
                        }
                    } else if (condition.equals("ComeOnly")) {
                        if (user != null && user.isHasComeOn()) {
                            userArrayList.add(user);
                            userCounter ++; //счетчик людей
                            numberOfUsers.setText("Пришли: " + userCounter);
                        }
                    }

                    //возможно тут надо также вот эту строку вставить buildRecycleView();


                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            usersDataBaseReference.addChildEventListener(usersChildEventListener);
        }
    }

    private void buildRecycleView() {

        sportsmenListRecycleView = findViewById(R.id.sportsmenListRecycleView);
        sportsmenListRecycleView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(this);
        adapter = new RegListAdapter(userArrayList);

        sportsmenListRecycleView.setLayoutManager(userLayoutManager);
        sportsmenListRecycleView.setAdapter(adapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        final String currentUserUid = currentUser.getUid();
        final DocumentReference userItemDocumentReference = firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid);

        userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                isDirectorModeActivated = user.isDirector();
                isCoachModeActivated = user.isCoach();
            }
        });

        adapter.setOnUserClickListener(new RegListAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int position) { //при нажатии на карточку человека

                //String userOnItemClickId = userArrayList.get(position).getSecondName() + userArrayList.get(position).getFirstName() + userArrayList.get(position).getDaysBornDate() + userArrayList.get(position).getMonthBornDate() + userArrayList.get(position).getYearBornDate();
                Intent userIntent = new Intent(RegListActivity.this, MemberOfChampionship.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                userIntent.putExtra("UserId", userArrayList.get(position).getUserId()); //связываем строку со значение
                userIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
                userIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена

                userIntent.putExtra("firstName", userArrayList.get(position).getFirstName());
                userIntent.putExtra("secondName", userArrayList.get(position).getSecondName());
                userIntent.putExtra("bornDate", userArrayList.get(position).getDaysBornDate() + "." + userArrayList.get(position).getMonthBornDate() + "."  + userArrayList.get(position).getYearBornDate());
                userIntent.putExtra("sex", userArrayList.get(position).getSex());
                userIntent.putExtra("daysBornDate", userArrayList.get(position).getDaysBornDate());
                userIntent.putExtra("monthBornDate", userArrayList.get(position).getMonthBornDate());
                userIntent.putExtra("yearBornDate", userArrayList.get(position).getYearBornDate());
                userIntent.putExtra("userClub", userArrayList.get(position).getUserClub());
                userIntent.putExtra("userGroup", userArrayList.get(position).getUserGroup());
                userIntent.putExtra("userCertification", userArrayList.get(position).getUserCertification());

                if ((userArrayList.get(position).getCoachId())!=null) {
                    userIntent.putExtra("сoachId", userArrayList.get(position).getCoachId());
                } else {
                    userIntent.putExtra("сoachId", ""); //если каким-то хреномв базе данных нет этой информации
                }


                userIntent.putExtra("isDirector", isDirectorModeActivated);
                userIntent.putExtra("isCoach", isCoachModeActivated);

                userIntent.putExtra("hasPayed", userArrayList.get(position).isHasPayed());
                userIntent.putExtra("hasComeOn", userArrayList.get(position).isHasComeOn());

                startActivity(userIntent);

                }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //фильтрация будет доступна и тренерам, и спортсменам
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtration_in_reg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                Intent competitionTitleIntent = new Intent(RegListActivity.this, FullCompetitionItem.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionTitleIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
                competitionTitleIntent.putExtra("isDirectorModeActivated", isDirectorModeActivated);//это надо, чтоб при возвращении на пред страницу, приложение знало создавать ли кнопку редактирования для сорев или нет
                startActivity(competitionTitleIntent);
                return true;
            case R.id.menu_filtration:
                Intent filtrationIntent = new Intent(RegListActivity.this, FiltrationActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                //filtrationIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
                //filtrationIntent.putExtra("isDirectorModeActivated", isDirectorModeActivated);//это надо, чтоб при возвращении на пред страницу, приложение знало создавать ли кнопку редактирования для сорев или нет
                startActivity(filtrationIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void allMembersButtonPressed(View view) {
        //attachUserDatabaseReferenceListener("All");
        //buildRecycleView();
        //Toast.makeText(RegListActivity.this, "Все работают", Toast.LENGTH_SHORT).show();

        Intent randomIntent = new Intent(RegListActivity.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        randomIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
        randomIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
        randomIntent.putExtra("Condition", "All"); //отправляем параметр, с каким надо открывать данную страницу
        startActivity(randomIntent);
    }

    public void comeMembersButtonPressed(View view) {
        //attachUserDatabaseReferenceListener("ComeOnly");
        //buildRecycleView();
        //Toast.makeText(RegListActivity.this, "Пришедшие работают", Toast.LENGTH_SHORT).show();

        Intent randomIntent = new Intent(RegListActivity.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        randomIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
        randomIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
        randomIntent.putExtra("Condition", "ComeOnly"); //отправляем параметр, с каким надо открывать данную страницу
        startActivity(randomIntent);
    }

    public void payMembersButtonPressed(View view) {
        //attachUserDatabaseReferenceListener("PayOnly");
        //buildRecycleView();
        //Toast.makeText(RegListActivity.this, "Оплаченные работают", Toast.LENGTH_SHORT).show();

        Intent randomIntent = new Intent(RegListActivity.this, RegListActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        randomIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение
        randomIntent.putExtra("competitionId", competitionId);//это значение нужно для того, чтоб правильно работала кнопка назад в рег лист активити после возвращения из активити редактирования или создания спортсмена
        randomIntent.putExtra("Condition", "PayOnly"); //отправляем параметр, с каким надо открывать данную страницу
        startActivity(randomIntent);
    }


}