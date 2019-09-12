package com.example.aleksei.carmotion.presenter

import com.example.aleksei.carmotion.model.Point

interface CarPresenter : BasePresenter {

    fun startMoveCarToPoint(carPoint: Point, destinationPoint: Point)

    fun onRotateAnimationUpdate(startAngle: Float, destinationPoint: Point, currentAngle: Float)

    fun handleRotationFinish(carPoint: Point, destinationPoint: Point)

    fun onAnimationEnd(destinationPoint: Point)
}
