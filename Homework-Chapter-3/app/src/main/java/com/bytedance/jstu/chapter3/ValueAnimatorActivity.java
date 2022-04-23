package com.bytedance.jstu.chapter3;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ValueAnimatorActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_value_animator);

        likeView = findViewById(R.id.img_like);
        coinView = findViewById(R.id.img_coin);
        collectView = findViewById(R.id.img_collect);
        circleProgressBarCoin = (CircleProgressBar) findViewById(R.id.circleProgressbar_coin);
        circleProgressBarCollect = (CircleProgressBar) findViewById(R.id.circleProgressbar_collect);

        Log.d("TAG", "");

        final ValueAnimator animator = ValueAnimator.ofInt(-10, 10);
        animator.setDuration(100);
        animator.setRepeatCount(10);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int value = (int) animation.getAnimatedValue();
                likeView.setTranslationX(value);
                likeView.setTranslationY(value*0.5f);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
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
                    }
                }).start();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                circleProgressBarCoin.setVisibility(View.INVISIBLE);
                circleProgressBarCollect.setVisibility(View.INVISIBLE);
                likeView.setColorFilter(0xFF00BCD4);
                coinView.setColorFilter(0xFF00BCD4);
                collectView.setColorFilter(0xFF00BCD4);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

}