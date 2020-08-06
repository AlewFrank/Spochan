package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CompetitionsActivity extends AppCompatActivity implements CompetitionAdapter.OnListItemClick{

    private RecyclerView competitionRecycleView;

    private FirebaseFirestore firebaseFirestore;

    private CompetitionAdapter adapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competitions);

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



        FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись
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
        competitionIntent.putExtra("onItemClickId", onItemClickId); //связываем строку со значение

        startActivity(competitionIntent);
    }




    /* @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Competition, CompetitionViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Competition, CompetitionViewHolder>
                (Competition.class, R.layout.competitions_item, CompetitionViewHolder.class, competitionsDataBaseReference) {
            @Override
            protected void populateViewHolder(CompetitionViewHolder viewHolder, Competition competition, int i) {
                viewHolder.setTitle(competition.getCompetitionTitle());
                viewHolder.setLocation(competition.getCompetitionLocation());
                viewHolder.setData(competition.getCompetitionData());
                viewHolder.setImage(getApplicationContext(),competition.getCompetitionImageUrl());
            }
        };
        competitionRecycleView.setAdapter(firebaseRecyclerAdapter);
    }



    public static class CompetitionViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public CompetitionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title) {
            TextView competition_title = mView.findViewById(R.id.competitionNameTextView);
            competition_title.setText(title);
        }

        public void setLocation (String location){
            TextView competition_location = mView.findViewById(R.id.competitionLocationTextView);
            competition_location.setText(location);
        }

        public void setData(String data) {
            TextView competition_data = mView.findViewById(R.id.competitionDataTextView);
            competition_data.setText(data);
        }

        public void setImage(Context context, String image) {
            ImageView competition_image = mView.findViewById(R.id.competitionImageView);
            Glide.with(context).load(image).into(competition_image);
        }
    }*/ //в этом случае не нужен класс адаптер, но здесь также нет функциональности, что при нажатии открывается соответствующая активити
}