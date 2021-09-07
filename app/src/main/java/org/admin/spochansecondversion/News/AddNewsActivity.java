package org.admin.spochansecondversion.News;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.admin.spochansecondversion.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
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

    private Button editButton, cancelButton;

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private String currentNewsImageUrl1 = "", currentNewsImageUrl2 = "", currentNewsImageUrl3 = "", currentNewsImageUrl4 = "", currentNewsImageUrl5 = "";
    private String currentNewsImageUrl;

    private ImageButton imageButton1, imageButton2, imageButton3, imageButton4, imageButton5;
    private ImageView backgroundForImage1, backgroundForImage2, backgroundForImage3, backgroundForImage4, backgroundForImage5;

    private String onItemClickId;

    private TextInputEditText newsDescriptionEditText, newsTitleEditText;

    private String itemId;

    private int imageCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_news);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                    case R.id.navigation_news:
                    case R.id.navigation_competitions:
                        Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.edit_notification), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });




        editButton = findViewById(R.id.editButton);
        cancelButton = findViewById(R.id.cancelButton);
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-LightItalic.ttf");
        editButton.setTypeface(roboto);
        cancelButton.setTypeface(roboto);

        newsTitleEditText = findViewById(R.id.newsTitleEditText);
        newsDescriptionEditText = findViewById(R.id.newsDescriptionEditText);

        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        imageButton4 = findViewById(R.id.imageButton4);
        imageButton5 = findViewById(R.id.imageButton5);

        backgroundForImage1 = findViewById(R.id.backgroundForImage1);
        backgroundForImage2 = findViewById(R.id.backgroundForImage2);
        backgroundForImage3 = findViewById(R.id.backgroundForImage3);
        backgroundForImage4 = findViewById(R.id.backgroundForImage4);
        backgroundForImage5 = findViewById(R.id.backgroundForImage5);

        imageButton2.setVisibility(View.GONE);
        imageButton3.setVisibility(View.GONE);
        imageButton4.setVisibility(View.GONE);
        imageButton5.setVisibility(View.GONE);

        backgroundForImage2.setVisibility(View.GONE);
        backgroundForImage3.setVisibility(View.GONE);
        backgroundForImage4.setVisibility(View.GONE);
        backgroundForImage5.setVisibility(View.GONE);




        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем

        Intent newsIntent = getIntent(); //получаем интент из NewsActivity, который вызвал эту активити, извлекаем его и помещаем в новую переменную, которая будет активна на этой странице
        onItemClickId = newsIntent.getStringExtra("onItemClickId");

        firebaseFirestore = FirebaseFirestore.getInstance();

        if (onItemClickId != null) {

            DocumentReference newsItemDocumentReference = firebaseFirestore.collection("News" + getResources().getString(R.string.app_country)).document(onItemClickId);

            newsItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    News news = documentSnapshot.toObject(News.class);
                    newsTitleEditText.setText(news.getNewsTitle());
                    newsDescriptionEditText.setText(news.getNewsDescription());

                    currentNewsImageUrl1 = news.getNewsImageUrl_1();
                    currentNewsImageUrl2 = news.getNewsImageUrl_2();
                    currentNewsImageUrl3 = news.getNewsImageUrl_3();
                    currentNewsImageUrl4 = news.getNewsImageUrl_4();
                    currentNewsImageUrl5 = news.getNewsImageUrl_5();

                    //так как нельзя будет редактировать изображения, то сделаем так, чтобы они впринципе не отображались

                    imageButton1.setVisibility(View.GONE);
                    imageButton2.setVisibility(View.GONE);
                    imageButton3.setVisibility(View.GONE);
                    imageButton4.setVisibility(View.GONE);
                    imageButton5.setVisibility(View.GONE);

                    backgroundForImage1.setVisibility(View.GONE);
                    backgroundForImage2.setVisibility(View.GONE);
                    backgroundForImage3.setVisibility(View.GONE);
                    backgroundForImage4.setVisibility(View.GONE);
                    backgroundForImage5.setVisibility(View.GONE);


                    //currentNewsImageUrl = news.getNewsImageUrl();//если человек редактирует, но не создает новую фотку, то это ссылка на старую
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
        if (!newsTitleEditText.getText().toString().trim().equals("") & !newsDescriptionEditText.getText().toString().trim().equals("") & currentNewsImageUrl1 != null & !currentNewsImageUrl1.equals("")) {

            Date currentDate = new Date();

            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            DateFormat demoTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            String dateText = dateFormat.format(currentDate);
            String demoTimeText = demoTimeFormat.format(currentDate);



            FirebaseFirestore db = FirebaseFirestore.getInstance();

            News news = new News();

            if (onItemClickId != null) {//если мы перешли в эту активити по нажатию на какую-то табличку, то вся новая информация сохраняется по тому же айди, то есть происходит редактирование, а не создание новой новости
                itemId = onItemClickId;

                db.collection("users").document("frank")
                        .update(
                                "age", 13,
                                "favorites.color", "Red"
                        );

                firebaseFirestore.collection("News" + getResources().getString(R.string.app_country)).document(onItemClickId).update("newsDescription", newsDescriptionEditText.getText().toString().trim());
                firebaseFirestore.collection("News" + getResources().getString(R.string.app_country)).document(onItemClickId).update("newsTitle", newsTitleEditText.getText().toString().trim());
            } else {//чтоб при редактировании время оставалось изначальным
                news.setNewsDescription(newsDescriptionEditText.getText().toString().trim());
                news.setNewsTitle(newsTitleEditText.getText().toString().trim());
                news.setNewsData(dateText);
                news.setNewsTime(demoTimeText);
                //news.setNewsImageUrl(currentNewsImageUrl);
                if (currentNewsImageUrl1 != null)  /*& !currentNewsImageUrl1.equals(""))*/ {
                    news.setNewsImageUrl_1(currentNewsImageUrl1);
                }

                if (currentNewsImageUrl2 != null)/* & !currentNewsImageUrl2.equals(""))*/ {
                    news.setNewsImageUrl_2(currentNewsImageUrl2);
                }

                if (currentNewsImageUrl3 != null)  /* & !currentNewsImageUrl3.equals(""))*/ {
                    news.setNewsImageUrl_3(currentNewsImageUrl3);
                }

                if (currentNewsImageUrl4 != null)  /* & !currentNewsImageUrl4.equals(""))*/ {
                    news.setNewsImageUrl_4(currentNewsImageUrl4);
                }

                if (currentNewsImageUrl5 != null)  /* & !currentNewsImageUrl5.equals("")) */{
                    news.setNewsImageUrl_5(currentNewsImageUrl5);
                }

                news.setNewsId(itemId);
                db.collection("News" + getResources().getString(R.string.app_country)).document(itemId).set(news);//устанавливаем айди по моменту, когда было начато создание надписи
            }

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
            final StorageReference imageReference  = newsImagesStorageReference.child(itemId).child(String.valueOf(imageCounter));

            Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
            editButton.setEnabled(false);//чтоб человек не мог нажать, пока у нас загружается фотография
            UploadTask uploadTask = imageReference.putFile(selectedImageUri);

            //uploadTask = imageReference.putFile(selectedImageUri);

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
                        currentNewsImageUrl = downloadUri.toString();//это строка нужна чтоб нельзя было загрузить новость, если фотка еще не подгружена
                        editButton.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
                        Toast.makeText(AddNewsActivity.this,  getResources().getString(R.string.load_successful), Toast.LENGTH_LONG).show();

                        imageCounter ++;

                        if (imageCounter == 2) {
                            currentNewsImageUrl1 = currentNewsImageUrl;
                            Glide.with(backgroundForImage1.getContext()).load(currentNewsImageUrl1).into(backgroundForImage1);//таким образом мы загружаем изображение, которое только что отправили в базу даннных
                            imageButton2.setVisibility(View.VISIBLE);//делаем видимыми следующее окошко для добавления фоток
                            backgroundForImage2.setVisibility(View.VISIBLE);//делаем видимыми следующее окошко для добавления фоток
                            imageButton1.setVisibility(View.GONE);//убираем значок камеры с пред фотки, чтоб там наложения не было
                        } else if (imageCounter == 3) {
                            currentNewsImageUrl2 = currentNewsImageUrl;
                            Glide.with(backgroundForImage2.getContext()).load(currentNewsImageUrl2).into(backgroundForImage2);
                            imageButton3.setVisibility(View.VISIBLE);
                            backgroundForImage3.setVisibility(View.VISIBLE);
                            imageButton2.setVisibility(View.GONE);
                        } else if (imageCounter == 4) {
                            currentNewsImageUrl3 = currentNewsImageUrl;
                            Glide.with(backgroundForImage3.getContext()).load(currentNewsImageUrl3).into(backgroundForImage3);
                            imageButton4.setVisibility(View.VISIBLE);
                            backgroundForImage4.setVisibility(View.VISIBLE);
                            imageButton3.setVisibility(View.GONE);
                        } else if (imageCounter == 5) {
                            currentNewsImageUrl4 = currentNewsImageUrl;
                            Glide.with(backgroundForImage4.getContext()).load(currentNewsImageUrl4).into(backgroundForImage4);
                            imageButton5.setVisibility(View.VISIBLE);
                            backgroundForImage5.setVisibility(View.VISIBLE);
                            imageButton4.setVisibility(View.GONE);
                        } else if (imageCounter == 6) {
                            currentNewsImageUrl5 = currentNewsImageUrl;
                            Glide.with(backgroundForImage5.getContext()).load(currentNewsImageUrl5).into(backgroundForImage5);
                            imageButton5.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(AddNewsActivity.this, getResources().getString(R.string.load_fail), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
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
}