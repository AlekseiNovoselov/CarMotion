package com.example.aleksei.carmotion.car;

import com.example.aleksei.carmotion.BasePresenter;
import com.example.aleksei.carmotion.BaseView;

public interface CarContract {

    interface View extends BaseView<Presenter> {

        void showCar(Point point);

        void rotateCarOnPointDirection(Point point);

        void moveStraightToPoint(Point point);

        float getScreenWidth();

        float getScreenHeight();

        float getRotation();

        void cancelRotate();

        void animateRotateDelta(float deltaX, float deltaY, float deltaCourse);
    }

    interface Presenter extends BasePresenter {

        void startMoveCarToPoint(Point point);

        void onRotationCancel(Point point);

        void onAnimationEnd(Point point);

        float getStartAngle();

        float getFinishAngle();

        void onRotateAnimationUpdate(float value, Point point, float startAngle);

        void onRotationAnimationEnd(Point point);
    }
}
