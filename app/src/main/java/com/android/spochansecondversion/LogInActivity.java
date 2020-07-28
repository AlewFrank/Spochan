package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";

    private Button logInButton;
    TextView helloTextView, toggleLoginSingUpTextView;
    TextInputLayout textInputEmail;
    TextInputLayout textInputFirstName;
    TextInputLayout textInputSecondName;
    TextInputLayout textInputPassword;
    TextInputLayout textInputConfirmPassword;
    TextInputLayout textInputPasswordForDirector;

    public boolean isDirectorModeActivated = false;
    private boolean isLoginModeActive = true;//по умолчанию все boolean переменные false

    private FirebaseAuth auth;

    private DatabaseReference usersDataBaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logInButton = findViewById(R.id.logInButton);
        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSingUpTextView);
        helloTextView = findViewById(R.id.helloTextView);
        textInputEmail = findViewById(R.id.textInputEmail);
        textInputFirstName = findViewById(R.id.textInputFirstName);
        textInputSecondName = findViewById(R.id.textInputSecondName);
        textInputPassword = findViewById(R.id.textInputPassword);
        textInputConfirmPassword = findViewById(R.id.textInputConfirmPassword);
        textInputPasswordForDirector = findViewById(R.id.textInputPasswordForDirector);

        textInputFirstName.setVisibility(View.GONE);
        textInputSecondName.setVisibility(View.GONE);
        textInputConfirmPassword.setVisibility(View.GONE);
        textInputPasswordForDirector.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();//получаем доступ к корневой папке нашей базы данных
        usersDataBaseReference = database.getReference().child("Users");//инициализируем, то есть говорим, что usersDataBaseReference это переменная связанная с папкой Users

       if (auth.getCurrentUser() != null) {//если пользователь уже авторизован
            startActivity(new Intent(LogInActivity.this, NewsActivity.class));
        }
    }

    private boolean validateEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().trim();//получаем то, что человек ввел

        if (emailInput.isEmpty()) {
            textInputEmail.setError(getResources().getString(R.string.set_email));
            return false;
        } else {
            textInputEmail.setError("");//убираем сообщение об ошибке, если человек с первого раза ошибся и ему высветилась ошибка
            return true;
        }
    }

    private boolean validateFirstName() {

        String firstNameInput = textInputFirstName.getEditText().getText().toString()
                .trim();

        if (firstNameInput.isEmpty()) {
            textInputFirstName.setError(getResources().getString(R.string.set_first_name));
            return false;
        }  else {
            textInputFirstName.setError("");
            return true;
        }
    }

    private boolean validateSecondName() {

        String secondNameInput = textInputSecondName.getEditText().getText().toString()
                .trim();

        if (secondNameInput.isEmpty()) {
            textInputSecondName.setError(getResources().getString(R.string.set_second_name));
            return false;
        } else {
            textInputSecondName.setError("");
            return true;
        }
    }

    private boolean validatePassword() {

        String passwordInput = textInputPassword.getEditText().getText()
                .toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError(getResources().getString(R.string.set_password));
            return false;
        } else if (passwordInput.length() < 6) {
            textInputPassword.setError(getResources().getString(R.string.normal_password_length));
            return false;
        } else {
            textInputPassword.setError("");
            return true;
        }
    }

    private boolean validateConfirmPassword() {

        String passwordInput = textInputPassword.getEditText().getText()
                .toString().trim();
        String confirmPasswordInput = textInputConfirmPassword.getEditText().getText()
                .toString().trim();

        if (!passwordInput.equals(confirmPasswordInput)) {
            textInputConfirmPassword.setError(getResources().getString(R.string.password_match));
            return false;
        } else {
            textInputConfirmPassword.setError("");
            return true;
        }
    }

    private boolean validateDirectorPassword() {

        String directorPasswordInput = textInputPasswordForDirector.getEditText().getText()
                .toString().trim();

        if (directorPasswordInput.isEmpty()) {
            textInputPasswordForDirector.setError(getResources().getString(R.string.set_director_password));
            return false;
        } else  if (Integer.parseInt(directorPasswordInput) == 991842) {
            textInputPasswordForDirector.setError("");
            isDirectorModeActivated = true;
            return true;
        } else  {
            textInputPasswordForDirector.setError(getResources().getString(R.string.wrong_director_password));
            return false;
        }
    }




    public void toggleLoginMode(View view) {//онклик метод синей надписи под кнопкой

        if (!isLoginModeActive){
            isLoginModeActive = true;
            logInButton.setText("Войти");
            toggleLoginSingUpTextView.setText("Еще нет аккаунта?");
            helloTextView.setText("Рады снова вас видеть");
            textInputFirstName.setVisibility(View.GONE);
            textInputSecondName.setVisibility(View.GONE);
            textInputConfirmPassword.setVisibility(View.GONE);
        } else {
            isLoginModeActive = false;
            logInButton.setText("  Зарегистрироваться  ");
            toggleLoginSingUpTextView.setText("Уже есть аккаунт?");
            helloTextView.setText("Добро Пожаловать");
            textInputFirstName.setVisibility(View.VISIBLE);
            textInputSecondName.setVisibility(View.VISIBLE);
            textInputConfirmPassword.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//добавляем меню, которое троеточие
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//задаем поведение при нажатии на пунктах меню

        switch (item.getItemId()) {
            case R.id.sign_in_as_director:
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.you_are_director), Toast.LENGTH_LONG).show();
                textInputPasswordForDirector.setVisibility(View.VISIBLE);
                isDirectorModeActivated = true;
                return true;
            case R.id.sign_in_as_sportsman:
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.you_are_sportsman), Toast.LENGTH_LONG).show();
                textInputPasswordForDirector.setVisibility(View.GONE);
                isDirectorModeActivated = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loginSignUpUser(View view) {//онклик метод из нашей разметки

        if (isDirectorModeActivated) {
            if (!validateEmail() | !validateDirectorPassword() | !validatePassword()) {// такая штука | обозначает почти как "или" (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                return;
            }
        } else {
            if (!validateEmail() | !validatePassword()) {// такая штука | обозначает почти как "или" (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                return;
            }
        }

        if (isLoginModeActive) {
            auth.signInWithEmailAndPassword(textInputEmail.getEditText().getText().toString().trim(), textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(LogInActivity.this, NewsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                // ...
                            }
                            // ...
                        }
                    });

        } else {
            if (isDirectorModeActivated) {
                if (!validateEmail() | !validateFirstName() | !validateDirectorPassword() | !validateSecondName() | !validatePassword() | !validateConfirmPassword()) {// такая штука | обозначает почти как или (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                    return;
                }
            } else {
                if (!validateEmail() | !validateFirstName() | !validateSecondName() | !validatePassword() | !validateConfirmPassword()) {// такая штука | обозначает почти как или (две вертикальных), но при этом проверяет все условия, а не ищет первое, которое совпадает и на остальные забивает. С двумя палками у нас бы выводилось только одно сообщение об ошибке для того поле ввода, которое первое попадет на проверку, а с двумя палками выводится сразу все сообщения об ошибке, в тех полях, где они есть
                    return;
                }
            }

            auth.createUserWithEmailAndPassword(textInputEmail.getEditText().getText().toString().trim(), textInputPassword.getEditText().getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                createUser(user);
                                startActivity(new Intent(LogInActivity.this, NewsActivity.class));
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }

    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());//это поле и ниже получаем из облачной базы данных(вводим эти значения при аутентификации)
        user.setEmail(firebaseUser.getEmail());
        user.setFirstName(textInputFirstName.getEditText().getText().toString().trim());//это поле берем из edit text, в которое записывали имя при регистрации и после отправляем в баз данных
        user.setSecondName(textInputSecondName.getEditText().getText().toString().trim());

        usersDataBaseReference.push().setValue(user);
    }
}