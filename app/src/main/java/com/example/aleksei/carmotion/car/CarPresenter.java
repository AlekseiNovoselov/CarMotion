package com.example.aleksei.carmotion.car;

import android.support.annotation.NonNull;

public class CarPresenter implements CarContract.Presenter {

    private static float ROTATION_RADIUS;

    private float initXPosition;
    private float initYPosition;

    private float prevDeltaX;
    private float prevDeltaY;

    private static final float COURSE_EPSILON = 1.0f;

    private boolean isAnimation = false;

    @NonNull
    private final CarContract.View mCarView;

    public CarPresenter(@NonNull CarFragment carView) {
        mCarView = carView;
        mCarView.setPresenter(this);
    }

    private void initParams() {
        initXPosition = mCarView.getScreenWidth() / 2;
        initYPosition = mCarView.getScreenHeight() /2;

        ROTATION_RADIUS = mCarView.getScreenWidth() / 5;

        prevDeltaX = initXPosition;
        prevDeltaY = initYPosition;
    }

    @Override
    public void start() {
        initParams();
        mCarView.showCar(new Point(initXPosition, initYPosition));
    }

    @Override
    public void startMoveCarToPoint(Point point) {
        if (!isAnimation) {
            mCarView.rotateCarOnPointDirection(point);
            isAnimation = true;
        }
    }

    @Override
    public void onRotationCancel(Point point) {
        mCarView.moveStraightToPoint(point);
    }

    @Override
    public void onAnimationEnd(Point point) {
        initXPosition = point.getX();
        initYPosition = point.getY();
        prevDeltaX = point.getX();
        prevDeltaY = point.getY();
        isAnimation = false;
    }

    @Override
    public float getStartAngle() {
        return getCurrentAngle();
    }

    @Override
    public float getFinishAngle() {
        return getCurrentAngle() + 2 * (float) Math.PI;
    }

    @Override
    public void onRotateAnimationUpdate(float curRotationAngle, Point destinationPoint, float startAngle) {
        float deltaX = getDeltaX(curRotationAngle, startAngle);
        float deltaY = getDeltaY(curRotationAngle, startAngle);

        float deltaCourse = getDeltaCourse(deltaX, deltaY);
        float destinationCourse = getDestinationCourse(destinationPoint.getX(),
                destinationPoint.getY(), deltaX, deltaY);

        if (isDirectionToDestination(deltaCourse, destinationCourse)) {
            mCarView.cancelRotate();
            return;
        }

        mCarView.animateRotateDelta(deltaX, deltaY, deltaCourse);

        prevDeltaX = deltaX;
        prevDeltaY = deltaY;
    }

    @Override
    public void onRotationAnimationEnd(Point point) {
        initXPosition = point.getX();
        initYPosition = point.getY();
    }

    private float getCurrentAngle() {
        return mCarView.getRotation() * (float) Math.PI / 180;
    }

    private boolean isDirectionToDestination(float deltaCourse, float destinationCourse) {
        return Math.abs(deltaCourse - destinationCourse) < COURSE_EPSILON;
    }

    private float getDeltaX(float value, float startAngle) {
        return (float) ((Math.cos(value) - Math.cos(startAngle))) * ROTATION_RADIUS + initXPosition;
    }

    private float getDeltaY(float value, float startAngle) {
        return (float) (Math.sin(value) - Math.sin(startAngle)) * ROTATION_RADIUS + initYPosition;
    }

    private float getDeltaCourse(float deltaX, float deltaY) {
        return (float) Math.toDegrees(Math.atan( (prevDeltaX - deltaX) / (deltaY - prevDeltaY)));
    }

    private float getDestinationCourse(float x, float y, float deltaX, float deltaY) {
        return (float) Math.toDegrees(Math.atan((x - deltaX) / (deltaY - y)));
    }
}
