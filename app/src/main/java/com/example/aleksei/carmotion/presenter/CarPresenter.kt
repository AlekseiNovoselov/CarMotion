package com.example.aleksei.carmotion.presenter

import com.example.aleksei.carmotion.model.Point

interface CarPresenter : BasePresenter {

    fun startMoveCarToPoint(point: Point)

    fun onRotationCancel(point: Point)

    fun onAnimationEnd(point: Point)

    fun onRotateAnimationUpdate(curRotationAngle: Float, destinationPoint: Point, startAngle: Float)

    fun onRotationAnimationEnd(point: Point)
}
