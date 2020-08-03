package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CompetitionsActivity extends AppCompatActivity {

    private DatabaseReference competitionsDataBaseReference;
    private RecyclerView competitionRecycleView;

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


        //Все что выше это настройки для меню, которое снизу находится, а ниже этой записи остальные настройки


        competitionsDataBaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.app_country)).child("Competitions");
        competitionsDataBaseReference.keepSynced(true);//автоматическая синхронизация с базой данных, то есть даже не придется обновлять активити

        competitionRecycleView = findViewById(R.id.competitionsListRecycleView);
        competitionRecycleView.setHasFixedSize(true);
        competitionRecycleView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompetitionsActivity.this, AddCompetitionsActivity.class));
            }
        });


    }

    @Override
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
    }

    /*private void attachCompetitionDatabaseReferenceListener() {
        competitionsDataBaseReference = FirebaseDatabase.getInstance().getReference().child("Competitions");

        if (competitionsChildEventListener == null) {//чтоб не создавать миллиард event listener, а создаем только тогда, когда он не существует
            competitionsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Competition competition = new Competition();
                    //Competition competition = snapshot.getValue(Competition.class);
                    competition.setCompetitionName("Название соревнований");
                    competition.setCompetitionData("Дата соревнований");
                    competition.setCompetitionLocation("Место проведения соревнований");
                    competition.setCompetitionDescription("Описание соревнований");
                    competitionArrayList.add(competition);
                    competitionAdapter.notifyDataSetChanged();

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

            competitionsDataBaseReference.addChildEventListener(competitionsChildEventListener);
        }
    }

    private void buildRecycleView() {

        competitionRecycleView = findViewById(R.id.competitionsListRecycleView);
        competitionRecycleView.setHasFixedSize(true);
        competitionLayoutManager = new LinearLayoutManager(this);
        competitionAdapter = new CompetitionAdapter(competitionArrayList);

        competitionRecycleView.setLayoutManager(competitionLayoutManager);
        competitionRecycleView.setAdapter(competitionAdapter);

        competitionAdapter.setOnCompetitionClick(new CompetitionAdapter.OnCompetitionClickListener() {//устанавливаем адаптер, а в адаптере прописан метод onClick(setOnCompetitionClick) для каждого объекта наших соревнований, все это делается для того, чтоб если нажимаешь на соревнование, то открывалось активити, где будет более развернутая информация по чемпионату
            @Override
            public void onCompetitionClick(int position) {
                goToCompetitionItem(position);//создали метод ниже
            }
        });
    }

    private void goToCompetitionItem(int position) {//нужен для того, чтоб при нажатии на конкретные соревы открывалась отдельная активити с этим соревнованием
        /*Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
        intent.putExtra("recipientUserId", userArrayList.get(position).getId());//получаем айди юзера, на которого кликнули
        intent.putExtra("userName", userName);
        intent.putExtra("recipientUserName", userArrayList.get(position).getName());//это имя будет отображаться на верхней полосе, чтоб мы знали с кем ведется переписка, остальная работа с этой переменнойв в чат активити
        startActivity(intent);*/
    //}*/
}