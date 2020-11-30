package com.android.spochansecondversion.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.FullCompetitionItem;
import com.android.spochansecondversion.News.AddNewsActivity;
import com.android.spochansecondversion.News.NewsActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.RatingAdapter;
import com.android.spochansecondversion.User;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RegListActivity extends AppCompatActivity implements RegListAdapter.OnListItemClick {

    private FirebaseFirestore firebaseFirestore;

    private RecyclerView sportsmenListRecycleView;

    private RegListAdapter adapter;

    private String competitionTitle;




    //при нажатии на фильтрацию, будет происходить просто фильтрация, никакого другого функционала, при нажатии на карточку будет открывать окно с информацией о спортсмене(в каких категориях учавствует и какие там аттестации) + две кнопки по поводу оплаты и пришел он или нет, если нет чего-то одного, то фон под группой красный, если все хорошо, то синий. Так же сверху три кнопки, которые дают переключаться между общим списком, кол-вом пришедших и не пришедших

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_list);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent competitionTitleIntent = getIntent(); //получаем интент из FullCompetitionItem, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        competitionTitle = competitionTitleIntent.getStringExtra("competitionTitle");


        firebaseFirestore = FirebaseFirestore.getInstance();

        //Query
        Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection(competitionTitle);  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

        PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных

        //RecyclerOptions
        FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
            @NonNull
            @Override
            public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
                User user = snapshot.toObject(User.class);
                String itemId = snapshot.getId();
                user.setUserId(itemId);
                return user;
            }
        }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь

        adapter = new RegListAdapter(options, this);

        sportsmenListRecycleView = findViewById(R.id.sportsmenListRecycleView);

        sportsmenListRecycleView.setHasFixedSize(true);
        sportsmenListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        sportsmenListRecycleView.setAdapter(adapter);


        final FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка открывающая доступ к добавлению человека
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent competitionTitleIntent = new Intent(RegListActivity.this, AddMemberOfChampionship.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                competitionTitleIntent.putExtra("competitionTitle", competitionTitle); //связываем строку со значение

                startActivity(new Intent(RegListActivity.this, AddMemberOfChampionship.class));
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override //при нажатии на карточку человека
    public void onItemClick(DocumentSnapshot snapshot, int position) {
        Toast.makeText(RegListActivity.this, "Нажатие работает", Toast.LENGTH_LONG).show();

        //здесь надо чтоб айди того, на кого нажали передавался


        //Intent competitionTitleIntent = new Intent(RegListActivity.this, AddMemberOfChampionship.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        //competitionTitleIntent.putExtra("competitionTitle", competitionTitle + " ! " + competitionDate); //связываем строку со значение
    }
}