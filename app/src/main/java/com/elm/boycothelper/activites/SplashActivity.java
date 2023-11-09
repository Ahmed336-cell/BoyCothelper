package com.elm.boycothelper.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.elm.boycothelper.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2200; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splashImage = findViewById(R.id.splashImage);
        TextView splashText = findViewById(R.id.textsplash);

        // Animate image from left to center
        ObjectAnimator imageAnimator = ObjectAnimator.ofFloat(splashImage, "translationX", -500f, 0f);
        imageAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        // Animate text from right to center
        ObjectAnimator textAnimator = ObjectAnimator.ofFloat(splashText, "translationX", 500f, 0f);
        textAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(imageAnimator, textAnimator);
        animatorSet.setDuration(1500); // Adjust the duration as needed
        animatorSet.start();



        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DELAY);

    }
}