package com.android.spochansecondversion.Rating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.spochansecondversion.ChooseGroupForRating;
import com.android.spochansecondversion.CompetitionsActivity;
import com.android.spochansecondversion.MyProfileActivity;
import com.android.spochansecondversion.NewsActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.User;
import com.android.spochansecondversion.UserAdapter;
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

public class RatingActivity extends AppCompatActivity {







    //НАДО ПОМЕНЯТЬ ШРИФТЫ НА ТОТ, КОТОРЫЙ ДИЗАЙНЕР ДАЛ








    private FirebaseFirestore firebaseFirestore;

    private UserAdapter adapter;

    private ProgressBar progressBar;

    private boolean isClose1 = true;
    private boolean isClose2 = true;
    private boolean isClose3 = true;
    private boolean isClose4 = true;
    private boolean isClose5 = true;
    private boolean isClose6 = true;
    private boolean isClose7 = true;
    private boolean isClose8 = true;
    private boolean isClose9 = true;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    String[] addresses = {"26bas@mail.ru"};
    String subject_help = "Help"; //тема письма для помощи
    String subject_developer = "Hello developer"; //тема письма для связи с разработчиком
    String emailtext;

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
                    case R.id.navigation_myProfile:
                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
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
                startActivity(new Intent(RatingActivity.this, ChooseGroupForRating.class));
            }
        });



        //Query
        Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_1").collection("group_1");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

        adapter = new UserAdapter(options);

        RecyclerView sportsmenListRecycleView = findViewById(R.id.sportsmenListRecycleView);

        sportsmenListRecycleView.setHasFixedSize(true);
        sportsmenListRecycleView.setLayoutManager(new LinearLayoutManager(this));
        sportsmenListRecycleView.setAdapter(adapter);


    }







//    public void firstGroup(View view) {//онклик метод на нашу первую группу
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView firstGroupImageView = findViewById(R.id.firstGroupImageView);
//        RecyclerView firstGroupListRecycleView = findViewById(R.id.firstGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose1) {//isClose изначально тру, то есть изначально все скрыто
//            isClose1 = false;
//
//            firstGroupListRecycleView.setVisibility(View.VISIBLE);
//            firstGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_1").collection("group_1");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            firstGroupListRecycleView.setHasFixedSize(true);
//            firstGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            firstGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose1 = true;
//
//            firstGroupListRecycleView.setVisibility(View.GONE);
//            firstGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void secondGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView secondGroupImageView = findViewById(R.id.secondGroupImageView);
//        RecyclerView secondGroupListRecycleView = findViewById(R.id.secondGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose2) {//isClose изначально тру, то есть изначально все скрыто
//            isClose2 = false;
//
//            secondGroupListRecycleView.setVisibility(View.VISIBLE);
//            secondGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_2").collection("group_2");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            secondGroupListRecycleView.setHasFixedSize(true);
//            secondGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            secondGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose2 = true;
//
//            secondGroupListRecycleView.setVisibility(View.GONE);
//            secondGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void thirdGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView thirdGroupImageView = findViewById(R.id.thirdGroupImageView);
//        RecyclerView thirdGroupListRecycleView = findViewById(R.id.thirdGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose3) {//isClose изначально тру, то есть изначально все скрыто
//            isClose3 = false;
//
//            thirdGroupListRecycleView.setVisibility(View.VISIBLE);
//            thirdGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_3").collection("group_3");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            thirdGroupListRecycleView.setHasFixedSize(true);
//            thirdGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            thirdGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose3 = true;
//
//            thirdGroupListRecycleView.setVisibility(View.GONE);
//            thirdGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void forthGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView forthGroupImageView = findViewById(R.id.forthGroupImageView);
//        RecyclerView fourthGroupListRecycleView = findViewById(R.id.fourthGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose4) {//isClose изначально тру, то есть изначально все скрыто
//            isClose4 = false;
//
//            fourthGroupListRecycleView.setVisibility(View.VISIBLE);
//            forthGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_4").collection("group_4");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            fourthGroupListRecycleView.setHasFixedSize(true);
//            fourthGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            fourthGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose4 = true;
//
//            fourthGroupListRecycleView.setVisibility(View.GONE);
//            forthGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void fifthGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView fifthGroupImageView = findViewById(R.id.fifthGroupImageView);
//        RecyclerView fifthGroupListRecycleView = findViewById(R.id.fifthGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose5) {//isClose изначально тру, то есть изначально все скрыто
//            isClose5 = false;
//
//            fifthGroupListRecycleView.setVisibility(View.VISIBLE);
//            fifthGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_5").collection("group_5");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            fifthGroupListRecycleView.setHasFixedSize(true);
//            fifthGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            fifthGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose5 = true;
//
//            fifthGroupListRecycleView.setVisibility(View.GONE);
//            fifthGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void sixGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView sixGroupImageView = findViewById(R.id.sixGroupImageView);
//        RecyclerView sixGroupListRecycleView = findViewById(R.id.sixGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose6) {//isClose изначально тру, то есть изначально все скрыто
//            isClose6 = false;
//
//            sixGroupListRecycleView.setVisibility(View.VISIBLE);
//            sixGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_6").collection("group_6");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            sixGroupListRecycleView.setHasFixedSize(true);
//            sixGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            sixGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose6 = true;
//
//            sixGroupListRecycleView.setVisibility(View.GONE);
//            sixGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void sevenGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView sevenGroupImageView = findViewById(R.id.sevenGroupImageView);
//        RecyclerView sevenGroupListRecycleView = findViewById(R.id.sevenGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose7) {//isClose изначально тру, то есть изначально все скрыто
//            isClose7 = false;
//
//            sevenGroupListRecycleView.setVisibility(View.VISIBLE);
//            sevenGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_7").collection("group_7");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            sevenGroupListRecycleView.setHasFixedSize(true);
//            sevenGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            sevenGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose7 = true;
//
//            sevenGroupListRecycleView.setVisibility(View.GONE);
//            sevenGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void eightGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView eightGroupImageView = findViewById(R.id.eightGroupImageView);
//        RecyclerView eightGroupListRecycleView = findViewById(R.id.eightGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose8) {//isClose изначально тру, то есть изначально все скрыто
//            isClose8 = false;
//
//            eightGroupListRecycleView.setVisibility(View.VISIBLE);
//            eightGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_8").collection("group_8");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            eightGroupListRecycleView.setHasFixedSize(true);
//            eightGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            eightGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose8 = true;
//
//            eightGroupListRecycleView.setVisibility(View.GONE);
//            eightGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }
//
//    public void nineGroup(View view) {
//
//        progressBar = findViewById(R.id.progressBar);
//        ImageView nineGroupImageView = findViewById(R.id.nineGroupImageView);
//        RecyclerView nineGroupListRecycleView = findViewById(R.id.nineGroupListRecycleView);
//
//        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
//
//
//        if (isClose9) {//isClose изначально тру, то есть изначально все скрыто
//            isClose9 = false;
//
//            nineGroupListRecycleView.setVisibility(View.VISIBLE);
//            nineGroupImageView.setImageResource(R.drawable.ic_active_arrow);
//
//
//            //Query
//            Query query = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document("group_9").collection("group_9");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database
//
//            PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных
//
//            //RecyclerOptions
//            FirestorePagingOptions<User> options = new FirestorePagingOptions.Builder<User>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<User>() {
//                @NonNull
//                @Override
//                public User parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
//                    User user = snapshot.toObject(User.class);
//                    String itemId = snapshot.getId();
//                    user.setUserId(itemId);
//                    return user;
//                }
//            }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь
//
//            adapter = new UserAdapter(options);
//
//            nineGroupListRecycleView.setHasFixedSize(true);
//            nineGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
//            nineGroupListRecycleView.setAdapter(adapter);
//
//        } else {//если нажимаем, когда у нас все открыто
//            isClose9 = true;
//
//            nineGroupListRecycleView.setVisibility(View.GONE);
//            nineGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
//        }
//
//
//        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")
//
//    }

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