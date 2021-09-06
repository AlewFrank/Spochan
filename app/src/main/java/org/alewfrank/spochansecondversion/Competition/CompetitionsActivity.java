package org.alewfrank.spochansecondversion.Competition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.alewfrank.spochansecondversion.logInSignUp.LogInActivity;
import org.alewfrank.spochansecondversion.News.NewsActivity;
import org.alewfrank.spochansecondversion.R;
import org.alewfrank.spochansecondversion.Rating.RatingActivity;
import org.alewfrank.spochansecondversion.User;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CompetitionsActivity extends AppCompatActivity implements CompetitionAdapter.OnListItemClick{

    private RecyclerView competitionRecycleView;

    private FirebaseFirestore firebaseFirestore;

    private CompetitionAdapter adapter;

    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    String[] addresses = {"26bas@mail.ru"};
    String subject_help = "Help"; //тема письма для помощи
    String subject_developer = "Hello developer"; //тема письма для связи с разработчиком
    String emailtext;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

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

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем



        //Все что выше это настройки для меню, которое снизу(снизу экрана) находится, а ниже этой записи остальные настройки

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Query
        Query query = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country));  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

        PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных

        //RecyclerOptions
        //раньше вместо FirestorePagingOptions тут было FirestoreRecyclerAdapter, но тогда мы бы не смогли работать с paging(содержится в слове FirestorePagingOptions) и соответственно не могли бы использовать snaphot и следовательно узнавать айди текущего элемента
        //FirestorePagingOptions<Competition> options = new FirestorePagingOptions.Builder<Competition>().setLifecycleOwner(this).setQuery(query, config, Competition.class).build(); ниже то же самое, только теперь мы еще и айди получаем
        FirestorePagingOptions<Competition> options = new FirestorePagingOptions.Builder<Competition>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<Competition>() {
            @NonNull
            @Override
            public Competition parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
                Competition competition = snapshot.toObject(Competition.class);
                String itemId = snapshot.getId();
                competition.setCompetitionId(itemId);
                return competition;
            }
        }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь

        adapter = new CompetitionAdapter(options, this);//по сути мы просто взяли и перенесли все методы, которые относятся к адаптеру в класс CompetitionAdapter, чтоб здесь не мешались, так что когда будешь там код смотреть, то представляй, что это все в этой активити находится

        competitionRecycleView = findViewById(R.id.competitionsListRecycleView);

        competitionRecycleView.setHasFixedSize(true);
        competitionRecycleView.setLayoutManager(new LinearLayoutManager(this));
        competitionRecycleView.setAdapter(adapter);



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
                startActivity(new Intent(CompetitionsActivity.this, AddCompetitionsActivity.class));
            }
        });

        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")

    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {//этот метод мы создали в классе адаптера + position это не какая-то переменная программы, это мы ее такой создали, а получаем мы этот position в методе public void onClick(View v) в классе CompetitionAdapter
        //int position = getAdapterPosition(); можно еще такой способ использовать, как в PizzaRecipes делали
        String onItemClickId = snapshot.getId();

        Intent competitionIntent = new Intent(CompetitionsActivity.this, FullCompetitionItem.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        competitionIntent.putExtra("competitionId", onItemClickId); //связываем строку со значение
        competitionIntent.putExtra("isDirectorModeActivated", isDirectorModeActivated); //чтоб понимать создавать ли меню с возможностью редактирования сорев или нет

        startActivity(competitionIntent);
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
                startActivity(new Intent(CompetitionsActivity.this, LogInActivity.class));
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
}