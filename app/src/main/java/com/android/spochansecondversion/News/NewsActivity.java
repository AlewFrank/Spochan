package com.android.spochansecondversion.News;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.spochansecondversion.Competition.CompetitionsActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.Rating.RatingActivity;
import com.android.spochansecondversion.User;
import com.android.spochansecondversion.logInSignUp.LogInActivity;
import com.firebase.ui.firestore.SnapshotParser;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.OnListItemClick { //СМОТРИ CompetitionAdapter, ТАМ ВСЕ НОРМАЛЬНО ОБЪЯСНЯЕТСЯ

    private RecyclerView newsRecycleView;

    private FirebaseFirestore firebaseFirestore;

    private NewsAdapter adapter;

    private ProgressBar progressBar;

    private TextView newsTimeTextView, newsDataTextView, newsTitleTextView, newsDescriptionTextView;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private boolean isDirectorModeActivated;

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference newsImagesStorageReference1, newsImagesStorageReference2, newsImagesStorageReference3, newsImagesStorageReference4, newsImagesStorageReference5;

    String[] addresses = {"26bas@mail.ru"};
    String subject_help = "Help"; //тема письма для помощи
    String subject_developer = "Hello developer"; //тема письма для связи с разработчиком
    String emailtext;

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
//                    case R.id.navigation_myProfile:
//                        startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
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
                startActivity(new Intent(NewsActivity.this, AddNewsActivity.class));
            }
        });

        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем(тут сверху включать не надо, так как она у нас в разметке поставлена android:visibility="visible")

    }


    @Override
    public void onItemClick(DocumentSnapshot snapshot, int position) {//этот метод мы создали в классе адаптера + position это не какая-то переменная программы, это мы ее такой создали, а получаем мы этот position в методе public void onClick(View v) в классе CompetitionAdapter
        //int position = getAdapterPosition(); можно еще такой способ использовать, как в PizzaRecipes делали

        if (isDirectorModeActivated) {//чтоб только администратор мог совершать эти действия
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
                        newsImagesStorageReference1 = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId).child("1");
                        newsImagesStorageReference2 = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId).child("2");
                        newsImagesStorageReference3 = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId).child("3");
                        newsImagesStorageReference3 = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId).child("4");
                        newsImagesStorageReference3 = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images").child(onItemClickId).child("5");


                        newsImagesStorageReference1.delete();
                        newsImagesStorageReference2.delete();
                        newsImagesStorageReference3.delete();
                        newsImagesStorageReference4.delete();
                        newsImagesStorageReference5.delete();

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
                startActivity(new Intent(NewsActivity.this, LogInActivity.class));
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