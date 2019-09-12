package com.example.aleksei.carmotion.model

class DistanceHelper {

    fun isDestinationPointInCarRadius(
        carPoint: Point,
        radius: Float,
        destinationPoint: Point
    ): Boolean {
        val xDiff = (carPoint.x - destinationPoint.x)
        val yDiff = (carPoint.y - destinationPoint.y)
        return 2 * radius * radius > xDiff * xDiff + yDiff * yDiff
    }
}
