package com.example.trivia;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.trivia.controller.AppController;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String SCORE = "SCORE";
    private static final String CURRENT_SCORE = "CURRENT_SCORE";
    private static final String HIGHEST_SCORE = "HIGHEST_SCORE";
    public ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedDate;
    SharedPreferences.Editor sharedPreferencesEditor;
    List<Question> questionList;
    private int currentScore;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        new AppController(this);

        sharedPreferences = getSharedPreferences(SCORE, MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        sharedDate = getSharedPreferences(SCORE, MODE_PRIVATE);
        currentScore = sharedDate.getInt(CURRENT_SCORE, 0);

        questionList = new Repository().getQuestions(questionArrayList -> {
            updateCounter(questionArrayList.size());
            updateQuestion();
        });


        binding.btnNext.setOnClickListener(this::onNextButtonClick);
        binding.btnPrev.setOnClickListener(this::onPrevButtonClick);
        binding.btnTrue.setOnClickListener(view -> checkAnswer(true));
        binding.btnFalse.setOnClickListener(view -> checkAnswer(false));
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestionText();
        binding.textViewQuestion.setText(question);
        updateCounter(questionList.size());
        updateScore();
    }

    @SuppressLint("StringFormatMatches")
    private void updateScore() {
        currentScore = sharedDate.getInt(CURRENT_SCORE, 0);
        binding.textViewScore.setText(String.format(getString(R.string.score), currentScore));
    }

    @SuppressLint("StringFormatMatches")
    private void updateCounter(int arraySize) {
        binding.textViewOutOf.setText(String.format(getString(R.string.questions_out_of_text),
                (currentQuestionIndex + 1), arraySize));
    }

    private void checkAnswer(boolean userChoice) {
        boolean correctAnswer = questionList.get(currentQuestionIndex).getAnswer();
        int message;

        if (userChoice == correctAnswer) {
            sharedPreferencesEditor.putInt(CURRENT_SCORE, currentScore + 4);
            message = R.string.correct_ans;
            fadeAnimation();
        } else {
            if (currentScore > 0) sharedPreferencesEditor.putInt(CURRENT_SCORE, currentScore - 1);
            message = R.string.wrong_ans;
            shakeAnimation();
        }
        sharedPreferencesEditor.apply();
        updateQuestion();
        Snackbar.make(MainActivity.this, binding.cardViewQuestion,
                getText(message), Snackbar.LENGTH_SHORT).show();
    }

    private void onNextButtonClick(View view) {
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    private void onPrevButtonClick(View view) {
        if (currentQuestionIndex == 0) return;
        currentQuestionIndex--;
        updateQuestion();
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(150);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardViewQuestion.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.animation_shake);
        binding.cardViewQuestion.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textViewQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}