package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RegListActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;

    private RegListAdapter adapter;

    private boolean isClose1 = true;
    private boolean isClose2 = true;
    private boolean isClose3 = true;
    private boolean isClose4 = true;
    private boolean isClose5 = true;
    private boolean isClose6 = true;
    private boolean isClose7 = true;
    private boolean isClose8 = true;
    private boolean isClose9 = true;

    private String competitionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_list);

        Intent competitionTitleIntent = getIntent(); //получаем интент из CompetitionActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        competitionTitle = competitionTitleIntent.getStringExtra("competitionTitle");
    }



    public void firstGroup(View view) {//онклик метод на нашу первую группу

        ImageView firstGroupImageView = findViewById(R.id.firstGroupImageView);
        RecyclerView firstGroupListRecycleView = findViewById(R.id.firstGroupListRecycleView);


        if (isClose1) {//isClose изначально тру, то есть изначально все скрыто
            isClose1 = false;

            firstGroupListRecycleView.setVisibility(View.VISIBLE);
            firstGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_1");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            firstGroupListRecycleView.setHasFixedSize(true);
            firstGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            firstGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose1 = true;

            firstGroupListRecycleView.setVisibility(View.GONE);
            firstGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }

    }

    public void secondGroup(View view) {

        ImageView secondGroupImageView = findViewById(R.id.secondGroupImageView);
        RecyclerView secondGroupListRecycleView = findViewById(R.id.secondGroupListRecycleView);


        if (isClose2) {//isClose изначально тру, то есть изначально все скрыто
            isClose2 = false;

            secondGroupListRecycleView.setVisibility(View.VISIBLE);
            secondGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_2");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            secondGroupListRecycleView.setHasFixedSize(true);
            secondGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            secondGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose2 = true;

            secondGroupListRecycleView.setVisibility(View.GONE);
            secondGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }

    }

    public void thirdGroup(View view) {

        ImageView thirdGroupImageView = findViewById(R.id.thirdGroupImageView);
        RecyclerView thirdGroupListRecycleView = findViewById(R.id.thirdGroupListRecycleView);


        if (isClose3) {//isClose изначально тру, то есть изначально все скрыто
            isClose3 = false;

            thirdGroupListRecycleView.setVisibility(View.VISIBLE);
            thirdGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_3");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            thirdGroupListRecycleView.setHasFixedSize(true);
            thirdGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            thirdGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose3 = true;

            thirdGroupListRecycleView.setVisibility(View.GONE);
            thirdGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void forthGroup(View view) {

        ImageView forthGroupImageView = findViewById(R.id.forthGroupImageView);
        RecyclerView fourthGroupListRecycleView = findViewById(R.id.fourthGroupListRecycleView);


        if (isClose4) {//isClose изначально тру, то есть изначально все скрыто
            isClose4 = false;

            fourthGroupListRecycleView.setVisibility(View.VISIBLE);
            forthGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_4");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            fourthGroupListRecycleView.setHasFixedSize(true);
            fourthGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            fourthGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose4 = true;

            fourthGroupListRecycleView.setVisibility(View.GONE);
            forthGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void fifthGroup(View view) {

        ImageView fifthGroupImageView = findViewById(R.id.fifthGroupImageView);
        RecyclerView fifthGroupListRecycleView = findViewById(R.id.fifthGroupListRecycleView);



        if (isClose5) {//isClose изначально тру, то есть изначально все скрыто
            isClose5 = false;

            fifthGroupListRecycleView.setVisibility(View.VISIBLE);
            fifthGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_5");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            fifthGroupListRecycleView.setHasFixedSize(true);
            fifthGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            fifthGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose5 = true;

            fifthGroupListRecycleView.setVisibility(View.GONE);
            fifthGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void sixGroup(View view) {

        ImageView sixGroupImageView = findViewById(R.id.sixGroupImageView);
        RecyclerView sixGroupListRecycleView = findViewById(R.id.sixGroupListRecycleView);


        if (isClose6) {//isClose изначально тру, то есть изначально все скрыто
            isClose6 = false;

            sixGroupListRecycleView.setVisibility(View.VISIBLE);
            sixGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_6");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            sixGroupListRecycleView.setHasFixedSize(true);
            sixGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            sixGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose6 = true;

            sixGroupListRecycleView.setVisibility(View.GONE);
            sixGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void sevenGroup(View view) {

        ImageView sevenGroupImageView = findViewById(R.id.sevenGroupImageView);
        RecyclerView sevenGroupListRecycleView = findViewById(R.id.sevenGroupListRecycleView);



        if (isClose7) {//isClose изначально тру, то есть изначально все скрыто
            isClose7 = false;

            sevenGroupListRecycleView.setVisibility(View.VISIBLE);
            sevenGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_7");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            sevenGroupListRecycleView.setHasFixedSize(true);
            sevenGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            sevenGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose7 = true;

            sevenGroupListRecycleView.setVisibility(View.GONE);
            sevenGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void eightGroup(View view) {

        ImageView eightGroupImageView = findViewById(R.id.eightGroupImageView);
        RecyclerView eightGroupListRecycleView = findViewById(R.id.eightGroupListRecycleView);


        if (isClose8) {//isClose изначально тру, то есть изначально все скрыто
            isClose8 = false;

            eightGroupListRecycleView.setVisibility(View.VISIBLE);
            eightGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_8");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            eightGroupListRecycleView.setHasFixedSize(true);
            eightGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            eightGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose8 = true;

            eightGroupListRecycleView.setVisibility(View.GONE);
            eightGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }

    public void nineGroup(View view) {

        ImageView nineGroupImageView = findViewById(R.id.nineGroupImageView);
        RecyclerView nineGroupListRecycleView = findViewById(R.id.nineGroupListRecycleView);


        if (isClose9) {//isClose изначально тру, то есть изначально все скрыто
            isClose9 = false;

            nineGroupListRecycleView.setVisibility(View.VISIBLE);
            nineGroupImageView.setImageResource(R.drawable.ic_active_arrow);

            firebaseFirestore = FirebaseFirestore.getInstance();

            //Query
            Query query = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).collection("group_9");  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

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

            adapter = new RegListAdapter(options);

            nineGroupListRecycleView.setHasFixedSize(true);
            nineGroupListRecycleView.setLayoutManager(new LinearLayoutManager(this));
            nineGroupListRecycleView.setAdapter(adapter);

        } else {//если нажимаем, когда у нас все открыто
            isClose9 = true;

            nineGroupListRecycleView.setVisibility(View.GONE);
            nineGroupImageView.setImageResource(R.drawable.ic_disactive_arrow);
        }
    }
}