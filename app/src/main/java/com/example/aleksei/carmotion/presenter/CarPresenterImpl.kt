package com.example.aleksei.carmotion.presenter

import com.example.aleksei.carmotion.model.Point
import com.example.aleksei.carmotion.view.CarView
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

class CarPresenterImpl(carView: CarView) : CarPresenter {

    private var initXPosition = 0f
    private var initYPosition = 0f

    private var prevDeltaX = 0f
    private var prevDeltaY = 0f

    private var isAnimation = false

    private val mCarView: CarView = carView

    init {
        mCarView.setPresenter(this)
    }

    override fun start() {
        initParams()
        mCarView.showCar(Point(initXPosition, initYPosition))
    }

    private fun initParams() {
        initXPosition = mCarView.getScreenWidth() / 2
        initYPosition = mCarView.getScreenHeight() / 2

        ROTATION_RADIUS = mCarView.getScreenWidth() / 5

        prevDeltaX = initXPosition
        prevDeltaY = initYPosition
    }

    override fun startMoveCarToPoint(point: Point) {
        if (!isAnimation) {
            mCarView.rotateCarOnPointDirection(point, currentAngle(), finishAngle())
            isAnimation = true
        }
    }

    private fun currentAngle() = mCarView.getCarRotation() * Math.PI.toFloat() / 180

    private fun finishAngle() = currentAngle() + 2 * Math.PI.toFloat()


    override fun onRotationCancel(point: Point) {
        mCarView.moveStraightToPoint(point)
    }

    override fun onAnimationEnd(point: Point) {
        initXPosition = point.x
        initYPosition = point.y
        prevDeltaX = point.x
        prevDeltaY = point.y
        isAnimation = false
    }

    override fun onRotateAnimationUpdate(
        curRotationAngle: Float,
        destinationPoint: Point,
        startAngle: Float
    ) {
        val deltaX = getDeltaX(curRotationAngle, startAngle)
        val deltaY = getDeltaY(curRotationAngle, startAngle)

        val deltaCourse = getDeltaCourse(deltaX, deltaY)
        val destinationCourse = getDestinationCourse(
            destinationPoint.x,
            destinationPoint.y, deltaX, deltaY
        )

        mCarView.updateCarPosition(deltaX, deltaY, deltaCourse)

        if (isDirectionToDestination(deltaCourse, destinationCourse)) {
            mCarView.cancelRotation()
            return
        }

        prevDeltaX = deltaX
        prevDeltaY = deltaY
    }

    override fun onRotationAnimationEnd(point: Point) {
        initXPosition = point.x
        initYPosition = point.y
    }

    private fun isDirectionToDestination(deltaCourse: Float, destinationCourse: Float): Boolean {
        return abs(deltaCourse - destinationCourse) < COURSE_EPSILON
    }

    private fun getDeltaX(value: Float, startAngle: Float): Float {
        return (cos(value.toDouble()) - cos(startAngle.toDouble())).toFloat() * ROTATION_RADIUS + initXPosition
    }

    private fun getDeltaY(value: Float, startAngle: Float): Float {
        return (sin(value.toDouble()) - sin(startAngle.toDouble())).toFloat() * ROTATION_RADIUS + initYPosition
    }

    private fun getDeltaCourse(deltaX: Float, deltaY: Float): Float {
        return Math.toDegrees(atan(((prevDeltaX - deltaX) / (deltaY - prevDeltaY)).toDouble())).toFloat()
    }

    private fun getDestinationCourse(x: Float, y: Float, deltaX: Float, deltaY: Float): Float {
        return Math.toDegrees(atan(((x - deltaX) / (deltaY - y)).toDouble())).toFloat()
    }

    companion object {

        private var ROTATION_RADIUS: Float = 0f

        private const val COURSE_EPSILON = 1.0f
    }
}
