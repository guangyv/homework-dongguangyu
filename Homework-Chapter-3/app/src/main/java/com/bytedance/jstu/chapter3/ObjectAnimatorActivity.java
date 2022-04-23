package com.bytedance.jstu.chapter3;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class ObjectAnimatorActivity extends AppCompatActivity {

    private ImageView likeView;
    private ImageView coinView;
    private ImageView collectView;
    private CircleProgressBar circleProgressBarCoin;
    private CircleProgressBar circleProgressBarCollect;
    private final int totalProgress = 100;
    private int currentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_animator);

        likeView = findViewById(R.id.img_like);
        coinView = findViewById(R.id.img_coin);
        collectView = findViewById(R.id.img_collect);
        circleProgressBarCoin = (CircleProgressBar) findViewById(R.id.circleProgressbar_coin);
        circleProgressBarCollect = (CircleProgressBar) findViewById(R.id.circleProgressbar_collect);

        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(likeView,
                "translationX", -5, 5);
        translationXAnimator.setRepeatCount(10);
        translationXAnimator.setInterpolator(new LinearInterpolator());
        translationXAnimator.setDuration(100);
        translationXAnimator.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator translationYAnimator = ObjectAnimator.ofFloat(likeView,
                "translationY", 5, -5);
        translationYAnimator.setRepeatCount(5);
        translationYAnimator.setInterpolator(new LinearInterpolator());
        translationYAnimator.setDuration(200);
        translationYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationXAnimator, translationYAnimator);
        animatorSet.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (currentProgress < totalProgress) {
                    currentProgress += 1;
                    circleProgressBarCoin.setProgress(currentProgress);
                    circleProgressBarCollect.setProgress(currentProgress);
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        circleProgressBarCoin.setVisibility(View.INVISIBLE);
                        circleProgressBarCollect.setVisibility(View.INVISIBLE);
                        likeView.setColorFilter(0xFF00BCD4);
                        coinView.setColorFilter(0xFF00BCD4);
                        collectView.setColorFilter(0xFF00BCD4);

                        ObjectAnimator likeViewScaleXAnimator = scaleXAnimator(likeView, 1.2f);
                        ObjectAnimator likeViewScaleYAnimator = scaleYAnimator(likeView, 1.2f);
                        ObjectAnimator likeViewRotateAnimator = ObjectAnimator.ofFloat(likeView, "rotation", 0, -30, 0);
                        likeViewRotateAnimator.setRepeatCount(0);
                        likeViewRotateAnimator.setInterpolator(new LinearInterpolator());
                        likeViewRotateAnimator.setDuration(800);
                        likeViewRotateAnimator.setRepeatMode(ValueAnimator.REVERSE);

                        ObjectAnimator coinViewScaleXAnimator = scaleXAnimator(coinView, 1.2f);
                        ObjectAnimator coinViewScaleYAnimator = scaleYAnimator(coinView, 1.2f);

                        ObjectAnimator collectViewScaleXAnimator = scaleXAnimator(collectView, 1.2f);
                        ObjectAnimator collectViewScaleYAnimator = scaleYAnimator(collectView, 1.2f);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(likeViewScaleXAnimator, likeViewScaleYAnimator, likeViewRotateAnimator, coinViewScaleXAnimator, coinViewScaleYAnimator, collectViewScaleXAnimator, collectViewScaleYAnimator);
                        animatorSet.start();
                    }
                });
            }
        }).start();
    }

    private ObjectAnimator scaleXAnimator(ImageView imageView, float size) {
        ObjectAnimator viewScaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, size);
        viewScaleXAnimator.setRepeatCount(0);
        viewScaleXAnimator.setInterpolator(new LinearInterpolator());
        viewScaleXAnimator.setDuration(1000);
        viewScaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        return viewScaleXAnimator;
    }

    private ObjectAnimator scaleYAnimator(ImageView imageView, float size) {
        ObjectAnimator viewScaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, size);
        viewScaleYAnimator.setRepeatCount(0);
        viewScaleYAnimator.setInterpolator(new LinearInterpolator());
        viewScaleYAnimator.setDuration(1000);
        viewScaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        return viewScaleYAnimator;
    }

}