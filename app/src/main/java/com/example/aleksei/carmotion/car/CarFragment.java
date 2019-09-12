package com.example.aleksei.carmotion.car;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.aleksei.carmotion.R;

import androidx.fragment.app.Fragment;

public class CarFragment extends Fragment implements CarContract.View, View.OnTouchListener {

    private static final int CAR_HEIGHT = 100;
    private static final int CAR_WIDTH = 50;

    private static final long STRAIGHT_MOVE_SHOW_DURATION = 1000;
    private static final long ROTATION_SHOW_DURATION = 5000;

    private RelativeLayout rootView;
    private CarContract.Presenter mPresenter;

    private ImageView carView;

    private ScreenHelper screenHelper;
    private ValueAnimator rotationAnimator;

    @Override
    public void setPresenter(CarContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public static CarFragment newInstance() {
        return new CarFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHelper = new ScreenHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = (RelativeLayout) inflater.inflate(R.layout.car_fragment, container, false);
        rootView.setOnTouchListener(this);

        return rootView;
    }

    @Override
    public float getScreenWidth() {
        return screenHelper.getWidth();
    }

    @Override
    public float getScreenHeight() {
        return screenHelper.getHeight();
    }

    @Override
    public float getRotation() {
        return carView.getRotation();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                v.performClick();
                float x = event.getX();
                float y = event.getY();
                mPresenter.startMoveCarToPoint(new Point(x, y));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showCar(Point carPosition) {

        if (rootView.getChildCount() > 1) {
            return;
        }

        RelativeLayout.LayoutParams lpView = new RelativeLayout.LayoutParams(CAR_WIDTH, CAR_HEIGHT);
        carView = new ImageView(getContext());
        carView.setBackgroundColor(Color.BLUE);
        carView.setLayoutParams(lpView);
        carView.setTranslationX(carPosition.getX());
        carView.setTranslationY(carPosition.getY());
        rootView.addView(carView);
    }

    @Override
    public void moveStraightToPoint(Point destinationPoint) {
        if (carView != null) {
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(showAnimatorSet(carView, destinationPoint));
            set.addListener(getMoveListener(carView));
            set.start();
        }
    }

    private Animator showAnimatorSet(ImageView carView, Point point) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(STRAIGHT_MOVE_SHOW_DURATION).playTogether(
                ObjectAnimator.ofFloat(carView, View.TRANSLATION_X, point.getX()),
                ObjectAnimator.ofFloat(carView, View.TRANSLATION_Y, point.getY())
        );
        return set;
    }

    private AnimatorListenerAdapter getMoveListener(final ImageView carView) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mPresenter.onAnimationEnd(new Point(carView.getX(), carView.getY()));
            }
        };
    }

    @Override
    public void cancelRotate() {
        carView.animate().cancel();
        rotationAnimator.cancel();
    }

    @Override
    public void animateRotateDelta(float deltaX, float deltaY, float deltaCourse) {
        if (!Float.isNaN(deltaCourse)) {
            carView.setRotation(deltaCourse);
        }
        carView.setTranslationX(deltaX);
        carView.setTranslationY(deltaY);
    }

    public void rotateCarOnPointDirection(final Point point) {

        final float startAngle = mPresenter.getStartAngle();
        final float finishAngle = mPresenter.getFinishAngle();

        rotationAnimator = ValueAnimator.ofFloat(startAngle, finishAngle);
        rotationAnimator.setDuration(ROTATION_SHOW_DURATION);

        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) (animation.getAnimatedValue());
                mPresenter.onRotateAnimationUpdate(value, point, startAngle);
                carView.animate().setDuration(ROTATION_SHOW_DURATION).start();
            }
        });

        rotationAnimator.start();

        rotationAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPresenter.onRotationAnimationEnd(new Point(carView.getX(), carView.getY()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mPresenter.onRotationCancel(point);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        rotationAnimator.start();
    }

    class ScreenHelper {

        private int height;
        private int width;

        ScreenHelper() {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }

        int getHeight() {
            return height;
        }

        int getWidth() {
            return width;
        }
    }
}
