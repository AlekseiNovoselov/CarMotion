package com.example.aleksei.carmotion.view

import com.example.aleksei.carmotion.model.Point
import com.example.aleksei.carmotion.presenter.CarPresenter

interface CarView : BaseView<CarPresenter> {

    fun getScreenWidth(): Float

    fun getScreenHeight(): Float

    fun getCarRotation(): Float

    fun showCar(point: Point)

    fun rotateCarOnPointDirection(destinationPoint: Point, startAngle: Float, finishAngle: Float)

    fun moveStraightToPoint(destinationPoint: Point)

    fun updateCarPosition(deltaX: Float, deltaY: Float)

    fun updateCarRotation(rotation: Float)

    fun cancelRotation()
}
