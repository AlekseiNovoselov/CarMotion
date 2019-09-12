package com.example.aleksei.carmotion.model

import kotlin.math.cos
import kotlin.math.sin

class DirectionHelper {

    fun calculateDirection(startPoint: Point, rotation: Float, finishPoint: Point): Direction {
        val x1 = startPoint.x
        val y1 = startPoint.y
        val x2 = cos(rotation) + x1
        val y2 = sin(rotation) + y1
        val x3 = finishPoint.x
        val y3 = finishPoint.y
        val d = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1)
        return when {
            d > 0 -> Direction.RIGHT
            d < 0 -> Direction.LEFT
            else -> Direction.STRAIGHT
        }
    }
}
