package com.example.aleksei.carmotion.view

import com.example.aleksei.carmotion.model.Point
import com.example.aleksei.carmotion.presenter.CarPresenter

interface CarView : BaseView<CarPresenter> {

    fun getScreenWidth(): Float

    fun getScreenHeight(): Float

    fun getCarRotation(): Float

    fun showCar(point: Point)

    fun rotateCarOnPointDirection(point: Point, startAngle: Float, finishAngle: Float)

    fun moveStraightToPoint(point: Point)

    fun updateCarPosition(deltaX: Float, deltaY: Float, deltaCourse: Float)

    fun cancelRotation()
}
