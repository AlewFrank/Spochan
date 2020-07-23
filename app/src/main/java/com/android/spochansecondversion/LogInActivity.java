package com.android.spochansecondversion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LogInActivity extends AppCompatActivity {

    private Button logInButton;
    TextView helloTextView, toggleLoginSingUpTextView;
    TextInputLayout textInputEmail;
    TextInputLayout textInputFirstName;
    TextInputLayout textInputSecondName;
    TextInputLayout textInputPassword;
    TextInputLayout textInputConfirmPassword;
    TextInputLayout textInputPasswordForDirector;

    public boolean isDirectorModeActivated = false;

    private boolean loginModeActive = false;

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

        /*auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(DriverSignInActivity.this, DriverMapsActivity.class));
        }*/
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
        } else  if (directorPasswordInput.equals(991842)) {
            textInputPasswordForDirector.setError("");
            isDirectorModeActivated = true;
            return true;
        } else  {
            textInputPasswordForDirector.setError(getResources().getString(R.string.wrong_director_password));
            return false;
        }
    }




    public void toggleLoginMode(View view) {

        if (loginModeActive){//пользователь пытается войти
            loginModeActive = false;
            //logInButton.setText("Войти");
            toggleLoginSingUpTextView.setText("Еще нет аккаунта?");
            helloTextView.setText("Рады снова вас видеть");
            textInputFirstName.setVisibility(View.GONE);
            textInputSecondName.setVisibility(View.GONE);
            textInputConfirmPassword.setVisibility(View.GONE);
        } else {//пользователь пытается зарегистрироваться
            loginModeActive = true;
            //logInButton.setText("  Зарегистрироваться  ");
            toggleLoginSingUpTextView.setText("Уже есть аккаунт?");
            helloTextView.setText("Добро Пожаловать");
            textInputFirstName.setVisibility(View.VISIBLE);
            textInputSecondName.setVisibility(View.VISIBLE);
            textInputConfirmPassword.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sign_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sign_in_as_director:
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.you_are_director), Toast.LENGTH_LONG).show();
                textInputPasswordForDirector.setVisibility(View.VISIBLE);
                return true;
            case R.id.sign_in_as_sportsman:
                Toast.makeText(LogInActivity.this, getResources().getString(R.string.you_are_sportsman), Toast.LENGTH_LONG).show();
                textInputPasswordForDirector.setVisibility(View.GONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dalee(View view) {//использую для проверок
        startActivity(new Intent(LogInActivity.this, NewsAndCompetitions.class));
    }
}