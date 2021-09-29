package org.admin.spochansecondversion.Competition;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.admin.spochansecondversion.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddCompetitionsActivity extends AppCompatActivity {

    private DatabaseReference competitionsDataBaseReference;

    private TextInputEditText competitionTitleEditText, competitionLocationEditText, competitionAddressEditText, competitionDescriptionEditText;
    private EditText daysCompetitionDateEditText, monthCompetitionDateEditText, yearCompetitionDateEditText;

    private Button editButton, cancelButton;

    private ImageButton loadNewImage;
    private ImageView backgroundForImage;

    private String ImageUrlValue;

    private String daysCompetitionDate, monthCompetitionDate, yearCompetitionDate;//используем для сравнения предыдущей даты и новой при сохранении

    private static final int RC_IMAGE_PICKER = 321;//константа, которую используем в методе loadNewImage, рандомное число, которое ни на что не влияет

    private FirebaseStorage storage;//это надо для хранения фотографий, так как фотки хранятся в папке Storage не рядом с изображениями
    private StorageReference competitionImagesStorageReference;
    private StorageReference imagesStorageReference;

    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;
    private String currentCompetitionImageUrl;

    private  String onItemClickId;

    private boolean isImage = false;//переменная, показывающая есть у нас изображение или нет

    private String competitionTitle;

    private Toolbar mToolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_competitions);

        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        //четыре строки ниже это чтобы установить кнопку назад в левом верхнем углу, а поведение имплементируем в методе onOptionsItemSelected
        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_competitions);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_rating:
                    case R.id.navigation_news:
                    case R.id.navigation_competitions:
                        Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.edit_notification), Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        //competitionsDataBaseReference = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.app_country)).child("Competitions");

        storage = FirebaseStorage.getInstance();
        competitionImagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Competitions_images");//в скобках это название папки в Firebase в Storage, которую мы создали вручную на сайте ранее,в которую будут помещаться изображения


        competitionTitleEditText = findViewById(R.id.competitionTitleEditText);
        competitionLocationEditText = findViewById(R.id.competitionLocationEditText);
        competitionAddressEditText = findViewById(R.id.competitionAddressEditText);
        competitionDescriptionEditText = findViewById(R.id.competitionDescriptionEditText);

        daysCompetitionDateEditText = findViewById(R.id.daysCompetitionDateEditText);
        monthCompetitionDateEditText = findViewById(R.id.monthCompetitionDateEditText);
        yearCompetitionDateEditText = findViewById(R.id.yearCompetitionDateEditText);

        loadNewImage = findViewById(R.id.loadNewImage);

        editButton = findViewById(R.id.editButton);
        cancelButton = findViewById(R.id.cancelButton);
        Typeface roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-LightItalic.ttf");
        editButton.setTypeface(roboto);
        cancelButton.setTypeface(roboto);

        backgroundForImage = findViewById(R.id.backgroundForImage);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем



        //Чтобы можно было скроллить editText, при том что он находится в ScrollView
        competitionTitleEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (competitionTitleEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        competitionLocationEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (competitionLocationEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        competitionAddressEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (competitionAddressEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        competitionDescriptionEditText.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (competitionDescriptionEditText.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });




        Intent competitionItemIntent = getIntent(); //получаем интент из FullCompetitionActivity, чтоб если мы редактируем класс, а не создаем новый, то тогда чтоб в EditTextы уже заносились текущие значения
        onItemClickId = competitionItemIntent.getStringExtra("competitionId");

        if (onItemClickId != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(onItemClickId);

            competitionItemDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Competition competition = documentSnapshot.toObject(Competition.class);
                    competitionTitleEditText.setText(competition.getCompetitionTitle());
                    competitionLocationEditText.setText(competition.getCompetitionLocation());

                    competitionTitle = competition.getCompetitionTitle();//используем, чтоб сравнивать изначальное имя и новое при сохранении

                    daysCompetitionDateEditText.setText(competition.getDaysCompetitionDate());
                    monthCompetitionDateEditText.setText(competition.getMonthCompetitionDate());
                    yearCompetitionDateEditText.setText(competition.getYearCompetitionDate());

                    if (competition.getCompetitionImageUrl() != null) {
                        ImageUrlValue = competition.getCompetitionImageUrl();
                        loadNewImage.setVisibility(View.INVISIBLE);
                        Glide.with(backgroundForImage.getContext()).load(ImageUrlValue).into(backgroundForImage);
                    }

                    daysCompetitionDate = competition.getDaysCompetitionDate();
                    monthCompetitionDate = competition.getMonthCompetitionDate();
                    yearCompetitionDate = competition.getYearCompetitionDate();

                    competitionAddressEditText.setText(competition.getCompetitionAddress());
                    competitionDescriptionEditText.setText(competition.getCompetitionDescription());





                }
            });
        }

        progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
    }

    public void cancelButton(View view) {//онклик метод для кнопки Отмены
        startActivity(new Intent(AddCompetitionsActivity.this, CompetitionsActivity.class));
    }

    public void addButton(View view) {
        /*Map<String, Object> competition = new HashMap<>();
        competition.put("name", "Tokyo");
        competition.put("country", "Japan");*///Хороший вариант, то есть устанавливаешь сразу и поля и их значения

                if (validateCompetitionValues()) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Competition competition = new Competition();
                    competition.setCompetitionTitle(competitionTitleEditText.getText().toString().trim());
                    competition.setDaysCompetitionDate(daysCompetitionDateEditText.getText().toString().trim());
                    competition.setMonthCompetitionDate(monthCompetitionDateEditText.getText().toString().trim());
                    competition.setYearCompetitionDate(yearCompetitionDateEditText.getText().toString().trim());
                    competition.setCompetitionLocation(competitionLocationEditText.getText().toString().trim());
                    competition.setCompetitionAddress(competitionAddressEditText.getText().toString().trim());
                    competition.setCompetitionDescription(competitionDescriptionEditText.getText().toString().trim());
                    competition.setCompetitionId(yearCompetitionDateEditText.getText().toString().trim() + monthCompetitionDateEditText.getText().toString().trim() + daysCompetitionDateEditText.getText().toString().trim());//это не прям айди документа, который задется ниже, здесь же просто поле на всякий с имененм айди сделали, типо пусть будет дублироваться

                    if (isImage) {//чтоб при редактировании нам пустая строка сюда не ставилась, если мы новое изображение не загружаем
                        competition.setCompetitionImageUrl(currentCompetitionImageUrl);
                    } else {//перезаписываем старое значение, чтоб null в базу данных не отправлялся
                        competition.setCompetitionImageUrl(ImageUrlValue);
                    }


                    if (onItemClickId != null) {//то есть если мы редактируем запись, а не создаем новую
                        //если хоть одна составляющая даты не равна предыдущему значению, то тогда надо удалить прошлую запись
                        //правда фотка тоже удалиться, но она удалиться только из нашей Storage, но в системе она останется и будет показываться людям это сто процентов или вариант, что фотка не удаляется, но тогда пользователь при попытке загрузить другую запись по старой дате перезапишет изображение и у него будет все хреново, короче сейчас все работает четко
                        if (!daysCompetitionDate.equals(daysCompetitionDateEditText.getText().toString().trim()) || !monthCompetitionDate.equals(monthCompetitionDateEditText.getText().toString().trim()) || !yearCompetitionDate.equals(yearCompetitionDateEditText.getText().toString().trim())) {
                            //если дату изменили, то соревнования с прошлой датой удаляем
                            deleteCompetitionIfNewCreated();//создал внизу, удаляет запись по старой дате, чтоб после изменения записи не было одинаковых записей по старой и новой дате()
                        }
                    }

                    //Благодаря такой записи .document() соревнования будут располагаться по дате, при этом самые ближайшие в самом верху
                    db.collection("Competitions" + getResources().getString(R.string.app_country)).document(yearCompetitionDateEditText.getText().toString().trim() + monthCompetitionDateEditText.getText().toString().trim() + daysCompetitionDateEditText.getText().toString().trim()).set(competition);
                    Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.load_complete), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddCompetitionsActivity.this, CompetitionsActivity.class));
                }
    }

    public void loadNewImage(View view) {//онклик метод для нашей кнопки Загрузить фото

        //такая схема, чтоб нельзя было загрузить изображение до того, как вводим дату, так как изображению устанавливается айди(дата в обратном порядке), как у соревнования, чтоб потом было легче удалять

            if (validateCompetitionValues()) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//означает что мы создаем интент для получения контента
                intent.setType("image/*");//вместо * мог быть любой тип файлов jpeg, png и тд, но чтоб не ограничиваться только одним, то поставили звезду
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);//указываем что нам нужны только локальные изображения, то есть те, которые существуют на телефоне
                //начинаем активити с целью получения результата(изображения) + активити сама закрывается после выбора изображения
                startActivityForResult(Intent.createChooser(intent, "Choose an image"), RC_IMAGE_PICKER);//первое в скобках это для того, что активити, с помощью которой можем выбирать фотку, второе это переменная(задали ее в самом верху), который будет показывать нам, куда сохраняется выбранное изображение
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {//этот метод нужен для работы с изображениями
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            //final StorageReference imageReference  = competitionImagesStorageReference.child(selectedImageUri.getLastPathSegment()); Хороший вариант, но потом фиг ссылку на это изображение нормально сделаешь в другой активити
            //допустим uri нашего изображения //content://images/some_folder/3 и с помощью строки выше мы считываем как раз последнюю тройку благодаря getLastPathSegment() в строке выше


                //устанавливаем для изображения такое же id, как и у соревнования, чтобы потом было легче усоздать ссылку и удалить, когда будем удалять соревновательный элемент в FullCompetitionActivity
                final StorageReference imageReference  = competitionImagesStorageReference.child(yearCompetitionDateEditText.getText().toString().trim() + monthCompetitionDateEditText.getText().toString().trim() + daysCompetitionDateEditText.getText().toString().trim());

                Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.wait), Toast.LENGTH_LONG).show();
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

                            //единственная проблема тут это если вдруг человек загрузил изображение, но потом вышел и не сохранил соревнование, то фотка все равно будет в базе, но это пофиг, так как она никуда не установится, никуда не запишется, просто будет фотка в dstorage ни с чем не связанная
                            currentCompetitionImageUrl = downloadUri.toString();
                            editButton.setEnabled(true);
                            isImage = true;
                            progressBar.setVisibility(View.INVISIBLE);//смысл в том, что мы как бы сверху и снизу трудоемкого и энергозатратного кода ставим progressBar и типа сверху включаем, снизу выключаем
                            Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.load_successful), Toast.LENGTH_LONG).show();

                            loadNewImage.setVisibility(View.INVISIBLE);
                            Glide.with(backgroundForImage.getContext()).load(currentCompetitionImageUrl).into(backgroundForImage);//таким образом мы загружаем изображение, которое только что отправили в базу даннных
                        } else {
                            Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.load_fail), Toast.LENGTH_LONG).show();
                        }
                    }
                });


        }
    }

    private boolean validateCompetitionValues() {//запихнули всю проверку на вводимые данные, чтоб все было здесь и не приходилось захломлять кодв других местах

        try {//для того, чтобы люди не вводили буквенные выражения для даты

            if (competitionTitleEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.title_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (daysCompetitionDateEditText.getText().toString().trim().length() != 2) {//проверяем чтоб дата имела только два знака
                Toast.makeText(this, getResources().getString(R.string.days_exception), Toast.LENGTH_SHORT).show();
                return false;
            }else if (monthCompetitionDateEditText.getText().toString().trim().length() != 2) {//проверяем чтоб дата имела только два знака
                Toast.makeText(this, getResources().getString(R.string.months_exception), Toast.LENGTH_SHORT).show();
                return false;
            }else if (yearCompetitionDateEditText.getText().toString().trim().length() != 4) {//проверяем чтоб дата имела только четыре знака
                Toast.makeText(this, getResources().getString(R.string.years_exception), Toast.LENGTH_SHORT).show();
                return false;
            }else if (Integer.parseInt(daysCompetitionDateEditText.getText().toString().trim()) >31) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            }else if (Integer.parseInt(daysCompetitionDateEditText.getText().toString().trim()) <= 0) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(monthCompetitionDateEditText.getText().toString().trim()) <= 0) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(monthCompetitionDateEditText.getText().toString().trim()) >12) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(monthCompetitionDateEditText.getText().toString().trim()) >12) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (Integer.parseInt(yearCompetitionDateEditText.getText().toString().trim()) >2099) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            }else if (Integer.parseInt(yearCompetitionDateEditText.getText().toString().trim()) <1900) {
                Toast.makeText(this, getResources().getString(R.string.date_format_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else if (competitionLocationEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.city_exception), Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException nef) {
            Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.chislo_exception), Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    private void deleteCompetitionIfNewCreated() {

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(yearCompetitionDate + monthCompetitionDate + daysCompetitionDate);

        competitionItemDocumentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //удаляем наше изображение, чтоб не засорять storage
                        if (ImageUrlValue != null) {//если у соревнования впринципе нет фотки, то чтоб не вылетало приложение из-за ссылки на нулевой объект
                            storage = FirebaseStorage.getInstance();
                            imagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Competitions_images").child(yearCompetitionDate + monthCompetitionDate + daysCompetitionDate);

                            imagesStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //ничего не делаем
                    }
                });
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {//добавляем меню, которое справа сверху

        if (onItemClickId != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_delete_competition_or_news_item, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//задаем поведение при нажатии на пунктах меню

        switch (item.getItemId()) {
            case android.R.id.home: //поведение кнопки слева-сверху
                this.finish();
                return true;
            case R.id.menu_delete:
                showDeleteCompetitionDialog();//создали этот метод внизу
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDeleteCompetitionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//в скобках активити в которой будет появляться этот диалог
        builder.setMessage(getResources().getString(R.string.confirm_delete));
        builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCompetition();//имплементируем этот метод в самом низу
                Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.delete_successful), Toast.LENGTH_LONG).show();
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

    private void deleteCompetition() {

        firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference competitionItemDocumentReference = firebaseFirestore.collection("Competitions" + getResources().getString(R.string.app_country)).document(onItemClickId);

        competitionItemDocumentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {//удаление прошло успешно, следователь переходим в новую активити и удаляем изображение удаленного соревнования из базы данных, чтоб не захломлять
                        //Toast.makeText(FullCompetitionItem.this, getResources().getString(R.string.delete_successful), Toast.LENGTH_LONG).show(); мы уже в showDeleteCompetitionDialog выводим, что элемент удален

                        //удаляем наше изображение, чтоб не засорять storage
                        if (ImageUrlValue != null) {//если у соревнования впринципе нет фотки, то чтоб не вылетало приложение из-за ссылки на нулевой объект
                            storage = FirebaseStorage.getInstance();
                            imagesStorageReference = storage.getReference().child(getResources().getString(R.string.app_country)).child("Competitions_images").child(yearCompetitionDate + monthCompetitionDate + daysCompetitionDate);

                            imagesStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        }

                        /*//удаляем список с зарегестрировавшимися, чтоб опять же не засорять базу данных
                        //DocumentReference competitionRegListDocumentReference = firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle);
                        firebaseFirestore.collection("CompetitionUserList" + getResources().getString(R.string.app_country)).document(competitionTitle).delete();*/


                        startActivity(new Intent(AddCompetitionsActivity.this, CompetitionsActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCompetitionsActivity.this, getResources().getString(R.string.delete_fail), Toast.LENGTH_LONG).show();
                    }
                });
    }
}