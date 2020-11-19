package com.android.spochansecondversion.Rating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.spochansecondversion.CompetitionsActivity;
import com.android.spochansecondversion.MyProfileActivity;
import com.android.spochansecondversion.News.AddNewsActivity;
import com.android.spochansecondversion.News.NewsActivity;
import com.android.spochansecondversion.News.NewsAdapter;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.User;
import com.android.spochansecondversion.logInSignUp.LogInActivity;
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

import java.util.ArrayList;


//НАДО ПОМЕНЯТЬ ШРИФТЫ НА ТОТ, КОТОРЫЙ ДИЗАЙНЕР ДАЛ f








//сделай elevation в самом конце, когда закончишь со всем дизайном этого окна, пока что сделал фон не совсем белым



































    public class RatingActivity extends AppCompatActivity implements RatingAdapter.OnListItemClick{

        private FirebaseFirestore firebaseFirestore;

        private RatingAdapter adapter;

        private ProgressBar progressBar;

        private FirebaseAuth auth;
        private FirebaseUser currentUser;
        private boolean isDirectorModeActivated;

        private RecyclerView.Adapter groupAdapter;
        private GridLayoutManager layoutManager;

        private  String group;
        private String groupTitle;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rating);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            bottomNavigationView.setSelectedItemId(R.id.navigation_rating);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.navigation_rating:
                            return true;
                        case R.id.navigation_news:
                            startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                            overridePendingTransition(0,0);
                            return true;
//                        case R.id.navigation_myProfile:
//                            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
//                            overridePendingTransition(0,0);
//                            return true;
                        case R.id.navigation_competitions:
                            startActivity(new Intent(getApplicationContext(), CompetitionsActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                    }
                    return false;
                }
            });




            firebaseFirestore = FirebaseFirestore.getInstance();


            //строчек 20 вниз это настройки в зависимости от того администратор ты или нет

            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
            String currentUserUid = currentUser.getUid();
            DocumentReference userItemDocumentReference = firebaseFirestore.collection("Users" + getResources().getString(R.string.app_country)).document(currentUserUid);

            userItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);

                    isDirectorModeActivated = user.isDirector();

                }
            });




            RecyclerView sportsmenListRecycleView = findViewById(R.id.sportsmenListRecycleView);
            RecyclerView groupListRecycleView = findViewById(R.id.groupListRecycleView);
            sportsmenListRecycleView.setVisibility(View.VISIBLE);
            groupListRecycleView.setVisibility(View.VISIBLE);



            ArrayList<Group> groups = new ArrayList<>();

            groups.add(new Group(getResources().getString(R.string.group_1), "group_1"));
            groups.add(new Group(getResources().getString(R.string.group_2), "group_2"));
            groups.add(new Group(getResources().getString(R.string.group_3), "group_3"));
            groups.add(new Group(getResources().getString(R.string.group_4), "group_4"));
            groups.add(new Group(getResources().getString(R.string.group_5), "group_5"));
            groups.add(new Group(getResources().getString(R.string.group_6), "group_6"));
            groups.add(new Group(getResources().getString(R.string.group_7), "group_7"));
            groups.add(new Group(getResources().getString(R.string.group_8), "group_8"));
            groups.add(new Group(getResources().getString(R.string.group_9), "group_9"));

            groupListRecycleView.setHasFixedSize(true);
            groupAdapter = new GroupAdapter(groups, this);
            groupListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            groupListRecycleView.setAdapter(groupAdapter);


            TextView groupTitleTextView = findViewById(R.id.groupTitle);


            Intent intent = getIntent();
            if (intent != null) {//так обозначается "не равен"
                group = intent.getStringExtra("index");
                groupTitle = intent.getStringExtra("title");
            }

            if (group != null) {//так обозначается "не равен"
                Log.d("groupIndex", group);
                groupTitleTextView.setText("Категория " + groupTitle);
            } else {
                group = "group_8";//когда поменяешь цифру, то обязательно поменяй слова тремя строчками ниже
                Log.d("groupIndex", group);
                groupTitleTextView.setText("Категория Дан");
            }




            //Query
            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document(group).collection(group);  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RatingAdapter(options, this);

            sportsmenListRecycleView.setHasFixedSize(true);
            sportsmenListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            sportsmenListRecycleView.setAdapter(adapter);
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
                    startActivity(new Intent(RatingActivity.this, LogInActivity.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        @Override
        public void onItemClick(DocumentSnapshot snapshot, int position) {
            final String onItemClickId = snapshot.getId();

            if (isDirectorModeActivated) {//чтоб только администратор мог совершать эти действия


                final Intent ratingIntent = new Intent(RatingActivity.this, EditRatingMembers.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
                ratingIntent.putExtra("onItemClickId", onItemClickId); //связываем строку со значение
                ratingIntent.putExtra("groupIndex", group);


                AlertDialog.Builder builder = new AlertDialog.Builder(this);//в скобках активити в которой будет появляться этот диалог
                builder.setMessage(getResources().getString(R.string.choose_action));
                builder.setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(ratingIntent);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        } //эта запись просто отменяет и возвращает к тому состоянию, которое было до нажатия
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }
