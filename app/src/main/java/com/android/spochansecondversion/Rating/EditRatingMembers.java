package com.android.spochansecondversion.Rating;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.spochansecondversion.News.AddNewsActivity;
import com.android.spochansecondversion.R;
import com.android.spochansecondversion.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditRatingMembers extends AppCompatActivity {

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference ratingImagesStorageReference;
    private String ratingReference;
    private String imageUrl = "";

    private ImageView userPhoto;

    private ProgressBar photoProgressBar;

    private TextInputEditText firstName;
    private TextInputEditText secondName;
    private TextInputEditText points;
    private TextInputEditText userCity;

    private EditText daysBornDateEditText, monthBornDateEditText, yearBornDateEditText;

    private String onGroupClick, groupIndex;

    private FirebaseFirestore firebaseFirestore;

    private static final int RC_IMAGE_PICKER = 921;//константа, которую используем в методе loadNewImage, рандомное число, которое ни на что не влияет

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rating_members);


        //три строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
//        ActionBar actionBar =getSupportActionBar();
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent ratingIntent = getIntent();
        onGroupClick = ratingIntent.getStringExtra("onItemClickId");
        groupIndex = ratingIntent.getStringExtra("groupIndex");


        firstName = findViewById(R.id.firstName);
        secondName = findViewById(R.id.secondName);
        points = findViewById(R.id.points);
        userCity = findViewById(R.id.userCity);
        userPhoto = findViewById(R.id.userPhoto);
        daysBornDateEditText = findViewById(R.id.daysBornDateEditText);
        monthBornDateEditText = findViewById(R.id.monthBornDateEditText);
        yearBornDateEditText = findViewById(R.id.yearBornDateEditText);
        photoProgressBar = findViewById(R.id.photoProgressBar);


        photoProgressBar.setVisibility(View.GONE);

        storage = FirebaseStorage.getInstance();
        ratingImagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Rating_images");//в скобках это название папки в Firebase в Storage, которую мы создали вручную на сайте ранее,в которую будут помещаться изображения




        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference groupItemDocumentReference = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document(groupIndex).collection(groupIndex).document(onGroupClick);
        groupItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                if (user.getFirstName() != null) {
                    firstName.setText(user.getFirstName());
                }

                if (user.getSecondName() != null) {
                    secondName.setText(user.getSecondName());
                }

                if (user.getUserPoints() != null) {
                    points.setText(user.getUserPoints());
                }

                if (user.getUserPoints() != null) {
                    userCity.setText(user.getUserCity());
                }

                if (user.getDaysBornDate() != null &  user.getMonthBornDate() != null & user.getYearBornDate() != null) {
                    daysBornDateEditText.setText(user.getDaysBornDate());
                    monthBornDateEditText.setText(user.getMonthBornDate());
                    yearBornDateEditText.setText(user.getYearBornDate());
                }

                if (user.getAvatarUrl() != null) {
                    Glide.with(userPhoto.getContext())//таким образом мы загружаем изображения в наш image View
                            .load(user.getAvatarUrl())
                            .into(userPhoto);

                    imageUrl = user.getAvatarUrl(); //делаем так, чтоб если новой фотки нет, то старая не удалялась
                }


            }
        });


        FloatingActionButton floatingActionButton = findViewById(R.id.addFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();

                if (!firstName.getText().toString().trim().equals("") & !secondName.getText().toString().trim().equals("")) {
                user.setFirstName(firstName.getText().toString().trim());
                user.setSecondName(secondName.getText().toString().trim());

                if (!points.getText().toString().trim().equals("")) {
                    user.setUserPoints(points.getText().toString().trim());
                }

                if (!userCity.getText().toString().trim().equals("")) {
                    user.setUserCity(userCity.getText().toString().trim());
                }

                if (!daysBornDateEditText.getText().toString().trim().equals("") || !monthBornDateEditText.getText().toString().trim().equals("") || !yearBornDateEditText.getText().toString().trim().equals("")) {
                    user.setDaysBornDate(daysBornDateEditText.getText().toString().trim());
                    user.setMonthBornDate(monthBornDateEditText.getText().toString().trim());
                    user.setYearBornDate(yearBornDateEditText.getText().toString().trim());
                }

                if (!imageUrl.equals("")) {
                    user.setAvatarUrl(imageUrl);
                }


                DocumentReference groupItemDocumentReference = firebaseFirestore.collection("Rating" + getResources().getString(R.string.app_country)).document(groupIndex).collection(groupIndex).document(onGroupClick);

                groupItemDocumentReference.set(user);

            }

                Toast.makeText(EditRatingMembers.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditRatingMembers.this, RatingActivity.class));
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

    public void loadImage(View view) { //онклик метод
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

            ratingReference = groupIndex + onGroupClick;

            photoProgressBar.setVisibility(View.VISIBLE);

            //устанавливаем для изображения id, которое связано с группой и местом в рейтинге в конкретной группе
            final StorageReference imageReference  = ratingImagesStorageReference.child(ratingReference);

            Toast.makeText(EditRatingMembers.this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
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

                        imageUrl = downloadUri.toString();

                        Glide.with(userPhoto.getContext())//таким образом мы загружаем новое изображения в наш image View
                                .load(imageUrl)
                                .into(userPhoto);

                        Toast.makeText(EditRatingMembers.this,  getResources().getString(R.string.load_successful), Toast.LENGTH_LONG).show();
                        photoProgressBar.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(EditRatingMembers.this, getResources().getString(R.string.load_fail), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}