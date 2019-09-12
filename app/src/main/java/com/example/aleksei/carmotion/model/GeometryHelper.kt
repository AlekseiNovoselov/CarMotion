package com.example.aleksei.carmotion.model

class GeometryHelper {

    private val directionHelper = DirectionHelper()
    private val distanceHelper = DistanceHelper()

    fun getFinishAngles(
        carPoint: Point,
        carRotation: Float,
        radius: Float,
        destinationPoint: Point
    ): Pair<Float, Float> {
        val direction = directionHelper.calculateDirection(
            startPoint = carPoint,
            rotation = carRotation,
            finishPoint = destinationPoint
        )
        val isInRadius = distanceHelper.isDestinationPointInCarRadius(carPoint, radius, destinationPoint)
        val carAngle = carRotation * Math.PI.toFloat() / 180
        val (startAngle, diffAngle) = when {
            direction == Direction.LEFT && isInRadius -> carAngle - Math.PI.toFloat() to 1 * Math.PI.toFloat()
            direction == Direction.LEFT && !isInRadius -> carAngle to + 2 * Math.PI.toFloat()
            direction == Direction.RIGHT && !isInRadius -> carAngle to + 2 * Math.PI.toFloat()
            direction == Direction.RIGHT && isInRadius -> carAngle - Math.PI.toFloat() to 1 * Math.PI.toFloat()
            else -> 0f to 0f
        }
        return startAngle to (carAngle + diffAngle)
    }
}
