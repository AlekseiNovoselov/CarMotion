package com.example.aleksei.carmotion.presenter

import com.example.aleksei.carmotion.model.Point
import com.example.aleksei.carmotion.view.CarView
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

private const val COURSE_EPSILON = 1.0f

class CarPresenterImpl(private val carView: CarView) : CarPresenter {
    private var initXPosition = 0f
    private var initYPosition = 0f
    private var prevDeltaX = 0f
    private var prevDeltaY = 0f
    private var rotationRadius = 0f
    private var isAnimation = false

    init {
        carView.setPresenter(this)
    }

    override fun start() {
        initParams()
        carView.showCar(Point(initXPosition, initYPosition))
    }

    private fun initParams() {
        initXPosition = carView.getScreenWidth() / 2
        initYPosition = carView.getScreenHeight() / 2
        rotationRadius = carView.getScreenWidth() / 5
        prevDeltaX = initXPosition
        prevDeltaY = initYPosition
    }

    override fun startMoveCarToPoint(destinationPoint: Point) {
        if (!isAnimation) {
            carView.rotateCarOnPointDirection(destinationPoint, currentAngle(), finishAngle())
            isAnimation = true
        }
    }

    private fun currentAngle() = carView.getCarRotation() * Math.PI.toFloat() / 180

    private fun finishAngle() = currentAngle() + 2 * Math.PI.toFloat()

    override fun onRotateAnimationUpdate(
        startAngle: Float,
        destinationPoint: Point,
        currentAngle: Float
    ) {
        val deltaX = getDeltaX(currentAngle, startAngle)
        val deltaY = getDeltaY(currentAngle, startAngle)

        val deltaCourse = getDeltaCourse(deltaX, deltaY)
        val destinationCourse = getDestinationCourse(
            destinationPoint.x,
            destinationPoint.y, deltaX, deltaY
        )

        carView.updateCarPosition(deltaX, deltaY)
        if (!deltaCourse.isNaN()) {
            carView.updateCarRotation(deltaCourse)
        }

        if (isDirectionToDestination(deltaCourse, destinationCourse)) {
            carView.cancelRotation()
            return
        }

        prevDeltaX = deltaX
        prevDeltaY = deltaY
    }

    private fun getDeltaX(currentAngle: Float, startAngle: Float): Float {
        return (cos(currentAngle.toDouble()) - cos(startAngle.toDouble())).toFloat() * rotationRadius + initXPosition
    }

    private fun getDeltaY(currentAngle: Float, startAngle: Float): Float {
        return (sin(currentAngle.toDouble()) - sin(startAngle.toDouble())).toFloat() * rotationRadius + initYPosition
    }

    private fun getDeltaCourse(deltaX: Float, deltaY: Float): Float {
        return Math.toDegrees(atan(((prevDeltaX - deltaX) / (deltaY - prevDeltaY)).toDouble())).toFloat()
    }

    private fun getDestinationCourse(x: Float, y: Float, deltaX: Float, deltaY: Float): Float {
        return Math.toDegrees(atan(((x - deltaX) / (deltaY - y)).toDouble())).toFloat()
    }

    private fun isDirectionToDestination(deltaCourse: Float, destinationCourse: Float): Boolean {
        return abs(deltaCourse - destinationCourse) < COURSE_EPSILON
    }

    override fun handleRotationFinish(carPoint: Point, destinationPoint: Point) {
        val rotation = getDestinationCourse(carPoint.x, carPoint.y, destinationPoint.x, destinationPoint.y)
        carView.updateCarRotation(rotation)
        carView.moveStraightToPoint(destinationPoint)
    }

    override fun onAnimationEnd(destinationPoint: Point) {
        initXPosition = destinationPoint.x
        initYPosition = destinationPoint.y
        prevDeltaX = destinationPoint.x
        prevDeltaY = destinationPoint.y
        isAnimation = false
    }
}
