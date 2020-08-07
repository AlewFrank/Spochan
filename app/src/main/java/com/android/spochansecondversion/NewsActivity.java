package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.OnListItemClick{ //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    private RecyclerView newsRecycleView;

    private FirebaseFirestore firebaseFirestore;

    private NewsAdapter adapter;

    private ProgressBar progressBar;

    private TextView newsTimeTextView, newsDataTextView;

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference newsImagesStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //Подробнее про настройку BottonNavigationView можно прочитать по ссылке https://javadevblog.com/primer-raboty-s-bottomnavigationview-nizhnee-menyu-v-android.html
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_news);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                        startActivity(new Intent(getApplicationContext(), RatingActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_news:
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

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем


        newsTimeTextView = findViewById(R.id.newsTimeTextView);
        newsDataTextView = findViewById(R.id.newsDataTextView);



        firebaseFirestore = FirebaseFirestore.getInstance();

        //Query
        Query query = firebaseFirestore.collection("News" + getResources().getString(R.string.app_country));  //нужно убедиться(начни набирать слово Query и там справо рядом с вариантами будет блеклым шрифтом написано), что ты выбрал именно Query, который относится к firestore, а не к database

        PagedList.Config config = new PagedList.Config.Builder().setInitialLoadSizeHint(5).setPageSize(3).build();//setInitialLoadSizeHint(5) это сколько изначально загружается объектов, setPageSize(3) это сколько загружается после того, как долистали до последнего из уже загруженных

        //RecyclerOptions
        FirestorePagingOptions<News> options = new FirestorePagingOptions.Builder<News>().setLifecycleOwner(this).setQuery(query, config, new SnapshotParser<News>() {
            @NonNull
            @Override
            public News parseSnapshot(@NonNull DocumentSnapshot snapshot) {//snapshot это как мгновенный снимок, то есть можем использовать его для получения айди и всякой другой фигни о конкретной карточке
                News news = snapshot.toObject(News.class);
                String itemId = snapshot.getId();
                news.setNewsId(itemId);
                return news;
            }
        }).build();//setLifecycleOwner(this) автоматически останавливает и возобновляет обновление информации при переносе приложения в фоновый режим и обратно, короче классная вещь

        adapter = new NewsAdapter(options, this);

        newsRecycleView = findViewById(R.id.newsListRecycleView);

        newsRecycleView.setHasFixedSize(true);
        newsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        newsRecycleView.setAdapter(adapter);






        FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);//кнопка добавляющая нам новую запись
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsActivity.this, AddNewsActivity.class));
            }
        });

        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")

    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {//этот метод мы создали в классе адаптера + position это не какая-то переменная программы, это мы ее такой создали, а получаем мы этот position в методе public void onClick(View v) в классе CompetitionAdapter
        //int position = getAdapterPosition(); можно еще такой способ использовать, как в PizzaRecipes делали
        final String onItemClickId = snapshot.getId();

        final Intent newsIntent = new Intent(NewsActivity.this, AddNewsActivity.class); //для перехода на др страницу, в скобках начально и конечное положение при переходе судя по всему + Intent нужен для передачи данных со страницы на страницу
        newsIntent.putExtra("onItemClickId", onItemClickId); //связываем строку со значение

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//в скобках активити в которой будет появляться этот диалог
        builder.setMessage(getResources().getString(R.string.choose_action));
        builder.setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(newsIntent);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if (dialog != null){dialog.dismiss();} эта запись просто отменяет и возвращает к тому состоянию, которое было до нажатия
                showDeleteCompetitionDialog(onItemClickId);//создали метод ниже
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteCompetitionDialog(final String onItemClickId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);//в скобках активити в которой будет появляться этот диалог
        builder.setMessage(getResources().getString(R.string.confirm_delete));
        builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNewsItem(onItemClickId);//имплементируем этот метод в самом низу
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null){dialog.dismiss();}
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteNewsItem(final String onItemClickId) {
        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference newsItemDocumentReference = firebaseFirestore.collection("News" + getResources().getString(R.string.app_country)).document(onItemClickId);

        newsItemDocumentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//удаление прошло успешно, следователь переходим в новую активити и удаляем изображение удаленного соревнования из базы данных, чтоб не захломлять
                        Toast.makeText(NewsActivity.this, getResources().getString(R.string.delete_successful), Toast.LENGTH_LONG).show();

                        //удаляем наше изображение, чтоб не засорять storage
                        //так как айди записи и фотографии совпадает, то можно в ссылке на изображение указывать onItemClickId
                        storage = FirebaseStorage.getInstance();
                        newsImagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId);

                        newsImagesStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // File deleted successfully
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                            }
                        });


                        Toast.makeText(NewsActivity.this, getResources().getString(R.string.delete_successful), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewsActivity.this, NewsActivity.class));//чтоб страница обновилась
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewsActivity.this, getResources().getString(R.string.delete_fail), Toast.LENGTH_LONG).show();
                    }
                });
    }


}