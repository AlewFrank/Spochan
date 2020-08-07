package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.LogDescriptor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewsActivity extends AppCompatActivity {

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference newsImagesStorageReference;

    private static final int RC_IMAGE_PICKER = 456;//константа, которую используем в методе loadNewImage, рандомное число, которое ни на что не влияет

    private Button editButton;

    private TextView loadComplete, mediumMark, loadNewImage;

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private String currentNewsImageUrl;

    private String onItemClickId;

    private EditText newsDescriptionEditText;

    private String itemId;

    private String currentDateScale, currentTimeScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_competitions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                    case R.id.navigation_news:
                    case R.id.navigation_myProfile:
                    case R.id.navigation_competitions:
                        Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.edit_notification), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });


        editButton = findViewById(R.id.editButton);
        loadComplete = findViewById(R.id.loadComplete);
        mediumMark = findViewById(R.id.mediumMark);

        newsDescriptionEditText = findViewById(R.id.newsDescriptionEditText);
        loadNewImage = findViewById(R.id.loadNewImage);




        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем

        Intent newsIntent = getIntent(); //получаем интент из NewsActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        onItemClickId = newsIntent.getStringExtra("onItemClickId");

        if (onItemClickId != null) {

            loadNewImage.setText(getResources().getString(R.string.change_image));

            firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference newsItemDocumentReference = firebaseFirestore.collection("News" + getResources().getString(R.string.app_country)).document(onItemClickId);

            newsItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    News news = documentSnapshot.toObject(News.class);
                    currentTimeScale = news.getNewsTime();
                    currentDateScale = news.getNewsData();
                    newsDescriptionEditText.setText(news.getNewsDescription());
                    currentNewsImageUrl = news.getNewsImageUrl();//если человек редактирует, но не создает новую фотку, то это ссылка на старую
                    //то есть мы сейчас устанавливаем в эту ячейку старое значение, а если будет происходить загрузка нового, то эта переменная перезапишется
                }
            });
        }



        storage = FirebaseStorage.getInstance();
        newsImagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("News_images");//в скобках это название папки в Firebase в Storage, которую мы создали вручную на сайте ранее,в которую будут помещаться изображения



        // Текущее время + есть вариант работы с датой addButton
        Date currentDate = new Date();

        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        DateFormat daysFormat = new SimpleDateFormat("dd", Locale.getDefault());
        DateFormat monthsFormat = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat yearsFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        DateFormat secondsFormat = new SimpleDateFormat("ss", Locale.getDefault());
        DateFormat minutesFormat = new SimpleDateFormat("mm", Locale.getDefault());
        DateFormat hoursFormat = new SimpleDateFormat("HH", Locale.getDefault());

        //Создаем из трех чисел единое, но так, чтобы сохранялось старшинство
        int seconds = Integer.parseInt(secondsFormat.format(currentDate));
        int minutes = Integer.parseInt(minutesFormat.format(currentDate)) * 60;
        int hours = Integer.parseInt(hoursFormat.format(currentDate)) * 3600;

        //Опять же создаем из трех чисел одно, но чтоб в любом случае у нас и в формате даты и в формате единого числа зависимость сохранялась(одно больше другого)
        int days = Integer.parseInt(daysFormat.format(currentDate));
        int months = Integer.parseInt(monthsFormat.format(currentDate)) * 32;
        int years = Integer.parseInt(yearsFormat.format(currentDate)) * 13 * 32;

        //Складываем в одно число
        int sum1 = seconds + minutes + hours;
        int sum2 = days + months + years;

        //sum1 и sum2 размещают в порядке возрастания, то есть самые первые сверху, а нам надо наоборот(сверху самые поздние), соответственно просто из большого числа вычитаем, чтоб создать обратную зависимость
        int sum1Invert = 1000000 - sum1;
        int sum2Invert = 1000000000 - sum2;

        //именно в таком порядке, так как сначала нам надо сравнить дни и только потом часы и минуты
        itemId = sum2Invert + "." + sum1Invert;


        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
    }

    public void cancelButton(View view) {
        startActivity(new Intent(AddNewsActivity.this, NewsActivity.class));
    }

    public void addButton(View view) {
        if (!newsDescriptionEditText.getText().toString().trim().equals("") & currentNewsImageUrl != null) {

            Date currentDate = new Date();

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            DateFormat demoTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            String dateText = dateFormat.format(currentDate);
            String demoTimeText = demoTimeFormat.format(currentDate);



            FirebaseFirestore db = FirebaseFirestore.getInstance();

            News news = new News();

            if (onItemClickId != null) {//если мы перешли в эту активити по нажатию на какую-то табличку, то вся новая информация сохраняется по тому же айди, то есть происходит редактирование, а не создание новой новости
                itemId = onItemClickId;

                news.setNewsDescription(newsDescriptionEditText.getText().toString().trim());
                news.setNewsData(currentDateScale);
                news.setNewsTime(currentTimeScale);
                news.setNewsImageUrl(currentNewsImageUrl);
                news.setNewsId(itemId);
            } else {//чтоб при редактировании время оставалось изначальным
                news.setNewsDescription(newsDescriptionEditText.getText().toString().trim());
                news.setNewsData(dateText);
                news.setNewsTime(demoTimeText);
                news.setNewsImageUrl(currentNewsImageUrl);
                news.setNewsId(itemId);
            }

            db.collection("News" + getResources().getString(R.string.app_country)).document(itemId).set(news);//устанавливаем айди по моменту, когда было начато создание надписи
            Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddNewsActivity.this, NewsActivity.class));
        } else {
            Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.fields_notification) + " " + getResources().getString(R.string.please_load_your_image), Toast.LENGTH_LONG).show();
        }

    }

    public void loadNewImage(View view) {//онклик метод для нашей кнопки Загрузить фото
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//означает что мы создаем интент для получения контента
            intent.setType("image/*");//вместо * мог быть любой тип файлов jpeg, png и тд, но чтоб не ограничиваться только одним, то поставили звезду
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//указываем что нам нужны только локальные изображения, то есть те, которые существуют на телефоне
            //начинаем активити с целью получения результата(изображения) + активити сама закрывается после выбора изображения
            startActivityForResult(Intent.createChooser(intent, "Choose an image"), RC_IMAGE_PICKER);//первое в скобках это для того, что активити, с помощью которой можем выбирать фотку, второе это переменная(задали ее в самом верху), который будет показывать нам, куда сохраняется выбранное изображение
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//этот метод нужен для работы с изображениями
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            //final StorageReference imageReference  = competitionImagesStorageReference.child(selectedImageUri.getLastPathSegment()); Хороший вариант, но потом фиг ссылку на это изображение нормально сделаешь в другой активити
            //допустим uri нашего изображения //content://images/some_folder/3 и с помощью строки выше мы считываем как раз последнюю тройку благодаря getLastPathSegment() в строке выше


            if (onItemClickId != null) {//в случае редактирования, чтоб новое изображение загрузилось по ссыдке старого
                itemId = onItemClickId;
            }
            //устанавливаем для изображения такое же id, как и у новости, чтобы потом было легче усоздать ссылку и удалить, когда будем удалять соревновательный элемент в FullCompetitionActivity
            final StorageReference imageReference  = newsImagesStorageReference.child(itemId);

            Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
            editButton.setEnabled(false);//чтоб человек не мог нажать, пока у нас загружается фотография
            UploadTask uploadTask = imageReference.putFile(selectedImageUri);

            uploadTask = imageReference.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {//то есть все хорошо работает, нет проблем ни с изображениями, ни с какими-то другими вещами
                        Uri downloadUri = task.getResult();

                        //единственная проблема тут это если вдруг человек загрузил изображение, но потом вышел и не сохранил новость, то фотка все равно будет в базе, но это пофиг, так как она никуда не установится, никуда не запишется, просто будет фотка в storage ни с чем не связанная
                        currentNewsImageUrl = downloadUri.toString();
                        editButton.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
                        loadComplete.setVisibility(View.VISIBLE);
                        mediumMark.setVisibility(View.VISIBLE);
                        Toast.makeText(AddNewsActivity.this, "Изображение успешно загружено", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddNewsActivity.this, "Ошибка: изображение не загружено", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}